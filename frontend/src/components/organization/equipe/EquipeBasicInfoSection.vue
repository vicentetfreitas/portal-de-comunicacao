<template>
  <div class="equipe-basic-info">
    <DsSelect
      v-if="mode === 'create'"
      v-model="areaIdModel"
      :label="$t('equipe.form.areaId')"
      :hint="$t('equipe.form.areaHint')"
      :options="resolvedAreaOptions"
      :disable="loadingAreas"
      :error="errors.areaId"
    />

    <DsInput
      v-else
      :model-value="model.areaId ?? ''"
      type="number"
      :label="$t('equipe.form.areaId')"
      readonly
      disable
    />

    <DsInput
      v-model="model.name"
      :label="$t('equipe.form.name')"
      :error="errors.name"
      autocomplete="organization"
    />

    <DsInput
      v-model="model.description"
      :label="$t('equipe.form.description')"
      :error="errors.description"
      autocomplete="off"
    />

    <DsInput
      v-model="leaderIdModel"
      type="number"
      :label="$t('equipe.form.leaderId')"
      :hint="$t('equipe.form.leaderHint')"
      :error="errors.leaderId"
    />
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";

import { DsInput, DsSelect, type DsSelectOption } from "@/components/ds";
import type { EquipeFormModel } from "@/composables/organization/useEquipeForm";

export type EquipeFormMode = "create" | "edit";

const props = defineProps<{
  model: EquipeFormModel;
  errors: Record<string, string>;
  mode: EquipeFormMode;
  areaOptions?: DsSelectOption<string>[];
  loadingAreas?: boolean;
}>();

const resolvedAreaOptions = computed(() => props.areaOptions ?? []);

const areaIdModel = computed({
  get: () => (props.model.areaId === null ? null : String(props.model.areaId)),
  set: (value: string | null) => {
    if (value === null || value === "") {
      props.model.areaId = null;
      return;
    }

    props.model.areaId = Number(value);
  }
});

const leaderIdModel = computed({
  get: () => props.model.leaderId ?? "",
  set: (value: string | number | null) => {
    if (value === "" || value === null) {
      props.model.leaderId = null;
      return;
    }

    props.model.leaderId = Number(value);
  }
});
</script>

<style scoped lang="scss">
.equipe-basic-info {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md, 16px);
}
</style>
