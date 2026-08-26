import { onMounted, ref, type Ref } from "vue";

import { DEFAULT_FEDERATION_ID } from "@/config/organization";
import type { DsSelectOption } from "@/components/ds";
import {
  areaService,
  equipeService,
  singularService
} from "@/services/organization";

export function useColaboradorFilterOptions(): {
  singularOptions: Ref<DsSelectOption<string>[]>;
  areaOptions: Ref<DsSelectOption<string>[]>;
  teamOptions: Ref<DsSelectOption<string>[]>;
  loadingSingulares: Ref<boolean>;
  loadingAreas: Ref<boolean>;
  loadingTeams: Ref<boolean>;
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
    loadingTeams.value = true;

    try {
      const page = await equipeService.list({
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

  onMounted(() => {
    void loadSingulares();
    void loadAreas();
    void loadTeams();
  });

  return {
    singularOptions,
    areaOptions,
    teamOptions,
    loadingSingulares,
    loadingAreas,
    loadingTeams
  };
}
