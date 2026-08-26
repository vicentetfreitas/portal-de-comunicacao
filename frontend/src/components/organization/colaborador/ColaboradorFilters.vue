<template>
  <DsCard
    class="colaborador-filters"
    :title="$t('colaborador.list.filters.title')"
  >
    <div class="colaborador-filters__grid">
      <DsSelect
        v-model="statusModel"
        :label="$t('colaborador.list.filters.status')"
        :options="statusOptions"
        clearable
      />

      <DsSelect
        v-model="singularIdModel"
        :label="$t('colaborador.form.singularId')"
        :options="singularOptions"
        :disable="loadingSingulares"
        clearable
      />

      <DsSelect
        v-model="areaIdModel"
        :label="$t('colaborador.form.areaId')"
        :options="areaOptions"
        :disable="loadingAreas"
        clearable
      />

      <DsSelect
        v-model="teamIdModel"
        :label="$t('colaborador.form.teamId')"
        :options="teamOptions"
        :disable="loadingTeams"
        clearable
      />

      <DsInput v-model="filters.name" :label="$t('colaborador.form.name')" />

      <DsInput v-model="filters.email" :label="$t('colaborador.form.email')" />
    </div>

    <template #actions>
      <DsButton variant="ghost" @click="emit('reset')">
        {{ $t("colaborador.list.filters.clear") }}
      </DsButton>
      <DsButton variant="primary" @click="emit('apply')">
        {{ $t("colaborador.list.filters.apply") }}
      </DsButton>
    </template>
  </DsCard>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useI18n } from "vue-i18n";

import {
  DsButton,
  DsCard,
  DsInput,
  DsSelect,
  type DsSelectOption
} from "@/components/ds";
import type { ColaboradorListFilters } from "@/composables/organization/useColaboradorList";
import type { ColaboradorStatus } from "@/types/organization/colaborador.types";

const props = defineProps<{
  filters: ColaboradorListFilters;
  singularOptions: DsSelectOption<string>[];
  areaOptions: DsSelectOption<string>[];
  teamOptions: DsSelectOption<string>[];
  loadingSingulares?: boolean;
  loadingAreas?: boolean;
  loadingTeams?: boolean;
}>();

const emit = defineEmits<{
  apply: [];
  reset: [];
}>();

const { t } = useI18n();

const statusOptions = computed(() => [
  {
    label: t("colaborador.status.ACTIVE"),
    value: "ACTIVE" as ColaboradorStatus
  },
  {
    label: t("colaborador.status.INACTIVE"),
    value: "INACTIVE" as ColaboradorStatus
  }
]);

const statusModel = computed({
  get: () => props.filters.status,
  set: (value: ColaboradorStatus | null) => {
    props.filters.status = value;
  }
});

function numberModel(key: "singularId" | "areaId" | "teamId") {
  return computed({
    get: () =>
      props.filters[key] === null ? null : String(props.filters[key]),
    set: (value: string | null) => {
      props.filters[key] =
        value === null || value === "" ? null : Number(value);
    }
  });
}

const singularIdModel = numberModel("singularId");
const areaIdModel = numberModel("areaId");
const teamIdModel = numberModel("teamId");
</script>

<style scoped lang="scss">
.colaborador-filters {
  &__grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
    gap: var(--spacing-md, 16px);
  }
}
</style>
