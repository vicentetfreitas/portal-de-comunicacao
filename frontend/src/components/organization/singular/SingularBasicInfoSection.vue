<template>
  <div class="singular-basic-info">
    <DsInput
      v-if="mode === 'create'"
      :model-value="model.federationId ?? ''"
      type="number"
      :label="$t('singular.form.federationId')"
      :hint="$t('singular.form.federationHint')"
      readonly
      disable
    />

    <DsInput
      v-else
      :model-value="model.federationId ?? ''"
      type="number"
      :label="$t('singular.form.federationId')"
      readonly
      disable
    />

    <DsInput
      v-model="model.name"
      :label="$t('singular.form.name')"
      :error="errors.name"
      autocomplete="organization"
    />

    <DsInput
      v-model="model.acronym"
      :label="$t('singular.form.acronym')"
      :error="errors.acronym"
      autocomplete="off"
    />

    <DsInput
      v-model="model.unimedCode"
      :label="$t('singular.form.unimedCode')"
      :error="errors.unimedCode"
      autocomplete="off"
    />
  </div>
</template>

<script setup lang="ts">
import { DsInput } from "@/components/ds";
import type { SingularFormModel } from "@/composables/organization/useSingularForm";

export type SingularFormMode = "create" | "edit";

defineProps<{
  model: SingularFormModel;
  errors: Record<string, string>;
  mode: SingularFormMode;
}>();
</script>

<style scoped lang="scss">
.singular-basic-info {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md, 16px);
}
</style>
