<template>
  <div class="colaborador-basic-info">
    <DsSelect
      v-model="singularIdModel"
      :label="$t('colaborador.form.singularId')"
      :hint="$t('colaborador.form.singularHint')"
      :options="resolvedSingularOptions"
      :disable="loadingSingulares"
      :error="errors.singularId"
    />

    <DsSelect
      v-model="areaIdModel"
      :label="$t('colaborador.form.areaId')"
      :hint="$t('colaborador.form.areaHint')"
      :options="resolvedAreaOptions"
      :disable="loadingAreas"
      :error="errors.areaId"
    />

    <DsSelect
      v-model="teamIdModel"
      :label="$t('colaborador.form.teamId')"
      :hint="$t('colaborador.form.teamHint')"
      :options="resolvedTeamOptions"
      :disable="loadingTeams"
      :error="errors.teamId"
    />

    <DsInput
      v-model="model.name"
      :label="$t('colaborador.form.name')"
      :error="errors.name"
      autocomplete="name"
    />

    <DsInput
      v-if="mode === 'edit'"
      :model-value="model.email"
      type="email"
      :label="$t('colaborador.form.email')"
      :hint="$t('colaborador.form.emailEditHint')"
      readonly
      disable
    />
    <DsInput
      v-else
      v-model="model.email"
      type="email"
      :label="$t('colaborador.form.email')"
      :error="errors.email"
      autocomplete="email"
    />

    <DsInput
      v-model="model.zimbraId"
      :label="$t('colaborador.form.zimbraId')"
      :hint="$t('colaborador.form.zimbraIdHint')"
      :error="errors.zimbraId"
      autocomplete="off"
    />
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";

import { DsInput, DsSelect, type DsSelectOption } from "@/components/ds";
import type { ColaboradorFormModel } from "@/composables/organization/useColaboradorForm";

export type ColaboradorFormMode = "create" | "edit";

const props = defineProps<{
  model: ColaboradorFormModel;
  errors: Record<string, string>;
  mode: ColaboradorFormMode;
  singularOptions?: DsSelectOption<string>[];
  areaOptions?: DsSelectOption<string>[];
  teamOptions?: DsSelectOption<string>[];
  loadingSingulares?: boolean;
  loadingAreas?: boolean;
  loadingTeams?: boolean;
}>();

const resolvedSingularOptions = computed(() => props.singularOptions ?? []);
const resolvedAreaOptions = computed(() => props.areaOptions ?? []);
const resolvedTeamOptions = computed(() => props.teamOptions ?? []);

function toOptionModel(key: "singularId" | "areaId" | "teamId") {
  return computed({
    get: () => (props.model[key] === null ? null : String(props.model[key])),
    set: (value: string | null) => {
      props.model[key] = value === null || value === "" ? null : Number(value);
    }
  });
}

const singularIdModel = toOptionModel("singularId");
const areaIdModel = toOptionModel("areaId");
const teamIdModel = toOptionModel("teamId");
</script>

<style scoped lang="scss">
.colaborador-basic-info {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md, 16px);
}
</style>
