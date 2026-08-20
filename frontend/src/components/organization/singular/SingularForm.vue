<template>
  <q-form class="singular-form" @submit.prevent="emit('submit')">
    <DsCard :title="title">
      <SingularBasicInfoSection :model="model" :errors="errors" :mode="mode" />

      <template #actions>
        <DsButton variant="ghost" :disable="loading" @click="emit('cancel')">
          {{ $t("singular.form.cancel") }}
        </DsButton>
        <DsButton variant="primary" type="submit" :loading="loading">
          {{ submitLabel }}
        </DsButton>
      </template>
    </DsCard>
  </q-form>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useI18n } from "vue-i18n";

import { DsButton, DsCard } from "@/components/ds";
import type { SingularFormModel } from "@/composables/organization/useSingularForm";

import SingularBasicInfoSection, {
  type SingularFormMode
} from "./SingularBasicInfoSection.vue";

const props = withDefaults(
  defineProps<{
    model: SingularFormModel;
    errors?: Record<string, string>;
    mode?: SingularFormMode;
    loading?: boolean;
    title?: string;
  }>(),
  {
    errors: () => ({}),
    mode: "create",
    loading: false
  }
);

const emit = defineEmits<{
  submit: [];
  cancel: [];
}>();

const { t } = useI18n();

const submitLabel = computed(() =>
  props.mode === "create"
    ? t("singular.form.submitCreate")
    : t("singular.form.submitEdit")
);
</script>

<style scoped lang="scss">
.singular-form {
  max-width: 720px;
}
</style>
