<template>
  <q-form class="equipe-form" @submit.prevent="emit('submit')">
    <DsFormCard :title="title">
      <EquipeBasicInfoSection
        :model="model"
        :errors="errors"
        :mode="mode"
        :area-options="areaOptions"
        :loading-areas="loadingAreas"
      />

      <template #actions>
        <DsButton variant="ghost" :disable="loading" @click="emit('cancel')">
          {{ $t("equipe.form.cancel") }}
        </DsButton>
        <DsButton variant="primary" type="submit" :loading="loading">
          {{ submitLabel }}
        </DsButton>
      </template>
    </DsFormCard>
  </q-form>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useI18n } from "vue-i18n";

import { DsButton, DsFormCard, type DsSelectOption } from "@/components/ds";
import type { EquipeFormModel } from "@/composables/organization/useEquipeForm";

import EquipeBasicInfoSection, {
  type EquipeFormMode
} from "./EquipeBasicInfoSection.vue";

const props = withDefaults(
  defineProps<{
    model: EquipeFormModel;
    errors?: Record<string, string>;
    mode?: EquipeFormMode;
    loading?: boolean;
    title?: string;
    areaOptions?: DsSelectOption<string>[];
    loadingAreas?: boolean;
  }>(),
  {
    errors: () => ({}),
    mode: "create",
    loading: false,
    loadingAreas: false,
    areaOptions: () => []
  }
);

const emit = defineEmits<{
  submit: [];
  cancel: [];
}>();

const { t } = useI18n();

const submitLabel = computed(() =>
  props.mode === "create"
    ? t("equipe.form.submitCreate")
    : t("equipe.form.submitEdit")
);
</script>

<style scoped lang="scss">
.equipe-form {
  max-width: 720px;
}
</style>
