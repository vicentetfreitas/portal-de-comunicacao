import { nextTick, onMounted, ref, watch, type Ref } from "vue";

import { DEFAULT_FEDERATION_ID } from "@/config/organization";
import type { DsSelectOption } from "@/components/ds";
import {
  areaService,
  equipeService,
  singularService
} from "@/services/organization";
import type { ColaboradorFormModel } from "@/composables/organization/useColaboradorForm";

export function useColaboradorOrganizationalOptions(
  form: ColaboradorFormModel
): {
  singularOptions: Ref<DsSelectOption<string>[]>;
  areaOptions: Ref<DsSelectOption<string>[]>;
  teamOptions: Ref<DsSelectOption<string>[]>;
  loadingSingulares: Ref<boolean>;
  loadingAreas: Ref<boolean>;
  loadingTeams: Ref<boolean>;
  /**
   * Loads área/equipe options for the form's *current* `singularId`/
   * `areaId` (call after `reset()` hydrates a real colaborador into the
   * form) without going through the `singularId`/`areaId` watchers below,
   * which unconditionally null out the next field down on every change —
   * correct for a user picking a new Singular by hand, but would silently
   * wipe the colaborador's real `areaId`/`teamId` right after loading them
   * if hydration ran through the same path (the watchers fire async, after
   * `reset()` already set every field, so they'd clobber the just-loaded
   * values).
   */
  loadOptionsFor: () => Promise<void>;
} {
  const singularOptions = ref<DsSelectOption<string>[]>([]);
  const areaOptions = ref<DsSelectOption<string>[]>([]);
  const teamOptions = ref<DsSelectOption<string>[]>([]);
  const loadingSingulares = ref(false);
  const loadingAreas = ref(false);
  const loadingTeams = ref(false);

  async function loadSingulares(): Promise<void> {
    loadingSingulares.value = true;

    try {
      const page = await singularService.list({
        federationId: DEFAULT_FEDERATION_ID,
        status: "ACTIVE",
        page: 0,
        size: 100,
        sort: "name,asc"
      });

      singularOptions.value = page.content.map(singular => ({
        label: singular.name,
        value: String(singular.id)
      }));
    } finally {
      loadingSingulares.value = false;
    }
  }

  async function loadAreas(): Promise<void> {
    loadingAreas.value = true;

    try {
      const page = await areaService.list({
        ...(form.singularId !== null ? { singularId: form.singularId } : {}),
        status: "ACTIVE",
        page: 0,
        size: 100,
        sort: "name,asc"
      });

      areaOptions.value = page.content.map(area => ({
        label: area.name,
        value: String(area.id)
      }));
    } finally {
      loadingAreas.value = false;
    }
  }

  async function loadTeams(): Promise<void> {
    if (form.areaId === null) {
      teamOptions.value = [];
      return;
    }

    loadingTeams.value = true;

    try {
      const page = await equipeService.list({
        areaId: form.areaId,
        status: "ACTIVE",
        page: 0,
        size: 100,
        sort: "name,asc"
      });

      teamOptions.value = page.content.map(equipe => ({
        label: equipe.name,
        value: String(equipe.id)
      }));
    } finally {
      loadingTeams.value = false;
    }
  }

  const hydrating = ref(false);

  watch(
    () => form.singularId,
    () => {
      if (hydrating.value) {
        return;
      }
      form.areaId = null;
      void loadAreas();
    }
  );

  watch(
    () => form.areaId,
    () => {
      if (hydrating.value) {
        return;
      }
      form.teamId = null;
      void loadTeams();
    }
  );

  onMounted(() => {
    void loadSingulares();
    void loadAreas();
  });

  async function loadOptionsFor(): Promise<void> {
    hydrating.value = true;
    try {
      await Promise.all([loadAreas(), loadTeams()]);
    } finally {
      // The `singularId`/`areaId` watchers above run on Vue's default
      // ('pre') flush timing — `nextTick` here waits past that flush so
      // they've already observed (and skipped, via the flag) the field
      // changes `reset()` made before this ran, instead of un-pausing
      // before they've had a chance to fire.
      await nextTick();
      hydrating.value = false;
    }
  }

  return {
    singularOptions,
    areaOptions,
    teamOptions,
    loadOptionsFor,
    loadingSingulares,
    loadingAreas,
    loadingTeams
  };
}
