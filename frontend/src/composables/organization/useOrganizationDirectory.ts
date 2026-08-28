import { computed, ref } from "vue";

import { useSession } from "@/composables/useSession";
import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { areaService, singularService } from "@/services/organization";
import type { AreaResponse } from "@/types/organization/area.types";
import type { SingularResponse } from "@/types/organization/singular.types";

/**
 * FT-SINGULAR/FT-AREA read-only directories for the sidebar's "Federação"
 * and "Singular" sections (production nav model — see
 * docs/discovery/frontend-production-discovery.md §"Menus", collaborator
 * menu). Both are lazy (load only on first expand — the sidebar is global,
 * eager-fetching would fan out requests on every authenticated page) and
 * scoped to the colaborador's own `activeContext.federationId` (FT-SESSION),
 * never a hardcoded or global federation.
 */

const DIRECTORY_PAGE_SIZE = 200;

function matchesQuery(query: string, ...values: string[]): boolean {
  if (!query) {
    return true;
  }
  return values.some(value => value.toLowerCase().includes(query));
}

export interface SingularDirectoryEntry {
  id: number;
  name: string;
  acronym: string;
}

/**
 * "Unimed Ceará" is the federation itself, not a real Singular — some
 * federations carry a self-referential Singular row for it (sede/matriz
 * modeling on the backend side), which would otherwise show up as if it
 * were just another Singular in this colaborador-facing directory. Explicit
 * product decision to exclude it here, not a backend/data fix.
 */
const EXCLUDED_SINGULAR_NAMES = new Set(["unimed ceará"]);

function isExcludedSingular(name: string): boolean {
  return EXCLUDED_SINGULAR_NAMES.has(name.trim().toLowerCase());
}

export function useSingularDirectory() {
  const { activeContext } = useSession();
  const { withErrorHandling } = useStandardErrorHandling();

  const items = ref<SingularDirectoryEntry[]>([]);
  const loading = ref(false);
  const loaded = ref(false);
  const search = ref("");

  const filtered = computed<SingularDirectoryEntry[]>(() => {
    const query = search.value.trim().toLowerCase();
    return items.value.filter(item =>
      matchesQuery(query, item.name, item.acronym)
    );
  });

  async function load(): Promise<void> {
    if (loaded.value || loading.value) {
      return;
    }

    const federationId = activeContext.value?.federationId;
    if (federationId == null) {
      loaded.value = true;
      return;
    }

    loading.value = true;
    const page = await withErrorHandling(
      () =>
        singularService.list({
          federationId,
          status: "ACTIVE",
          size: DIRECTORY_PAGE_SIZE,
          sort: "name"
        }),
      { silent: true }
    );
    loading.value = false;
    loaded.value = true;

    items.value = (page?.content ?? [])
      .filter(
        (singular: SingularResponse) => !isExcludedSingular(singular.name)
      )
      .map((singular: SingularResponse) => ({
        id: singular.id,
        name: singular.name,
        acronym: singular.acronym
      }));
  }

  return { items: filtered, loading, loaded, search, load };
}

export interface FederationAreaDirectoryEntry {
  id: number;
  name: string;
  acronym: string;
  singularName: string;
  /** Whether this is the colaborador's own vínculo área (FT-SESSION `activeContext.areaId`). */
  isOwnArea: boolean;
}

/**
 * "Áreas da Federação" — aggregates FT-AREA across every FT-SINGULAR of the
 * colaborador's federation (the Área API only filters by `singularId`, not
 * `federationId` — see backend `AreaController`/`AreaListParams`), so this
 * composes two existing, already-approved read endpoints instead of one.
 * Per-singular failures are swallowed (silent) so one bad singular doesn't
 * blank the whole directory.
 */
export function useFederationAreaDirectory() {
  const { activeContext } = useSession();
  const { withErrorHandling } = useStandardErrorHandling();

  const items = ref<FederationAreaDirectoryEntry[]>([]);
  const loading = ref(false);
  const loaded = ref(false);
  const search = ref("");

  const filtered = computed<FederationAreaDirectoryEntry[]>(() => {
    const query = search.value.trim().toLowerCase();
    const matches = items.value.filter(item =>
      matchesQuery(query, item.name, item.acronym, item.singularName)
    );
    // Colaborador's own vínculo área always leads the list — explicit
    // product decision so the área they actually belong to doesn't get
    // lost alphabetically among every other área in the federation.
    // `Array.prototype.sort` is stable (ES2019+), so this only reorders the
    // own-area entry; every other item keeps its existing (name-sorted,
    // from the API) relative order.
    return [...matches].sort(
      (a, b) => Number(b.isOwnArea) - Number(a.isOwnArea)
    );
  });

  async function load(): Promise<void> {
    if (loaded.value || loading.value) {
      return;
    }

    const federationId = activeContext.value?.federationId;
    if (federationId == null) {
      loaded.value = true;
      return;
    }

    loading.value = true;

    const singularPage = await withErrorHandling(
      () =>
        singularService.list({
          federationId,
          status: "ACTIVE",
          size: DIRECTORY_PAGE_SIZE,
          sort: "name"
        }),
      { silent: true }
    );
    const singulares = singularPage?.content ?? [];

    const ownAreaId = activeContext.value?.areaId ?? null;

    const perSingular = await Promise.all(
      singulares.map(async (singular: SingularResponse) => {
        const areaPage = await withErrorHandling(
          () =>
            areaService.list({
              singularId: singular.id,
              status: "ACTIVE",
              size: DIRECTORY_PAGE_SIZE,
              sort: "name"
            }),
          { silent: true }
        );
        return (areaPage?.content ?? []).map(
          (area: AreaResponse): FederationAreaDirectoryEntry => ({
            id: area.id,
            name: area.name,
            acronym: area.acronym,
            singularName: singular.name,
            isOwnArea: ownAreaId != null && area.id === ownAreaId
          })
        );
      })
    );

    loading.value = false;
    loaded.value = true;
    items.value = perSingular.flat();
  }

  return { items: filtered, loading, loaded, search, load };
}
