<template>
  <div class="singular-create-page">
    <DsPageHeader
      :title="$t('singular.create.title')"
      :subtitle="$t('singular.create.subtitle')"
    />

    <SingularForm
      :model="form"
      :errors="fieldErrors"
      mode="create"
      :loading="submitting"
      :title="$t('singular.create.cardTitle')"
      @submit="onSubmit"
      @cancel="onCancel"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";

import SingularForm from "@/components/organization/singular/SingularForm.vue";
import { dsNotifySuccess } from "@/components/ds";
import {
  mapSingularFieldErrors,
  useSingularForm
} from "@/composables/organization/useSingularForm";
import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { ROUTE_NAMES } from "@/constants/routes";
import { singularService } from "@/services/organization";

const router = useRouter();
const { t } = useI18n();
const { form, validateCreate, toCreateRequest } = useSingularForm();
const { handleError } = useStandardErrorHandling();

const fieldErrors = ref<Record<string, string>>({});
const submitting = ref(false);

async function onSubmit(): Promise<void> {
  fieldErrors.value = {};

  const validation = validateCreate();
  if (!validation.valid) {
    fieldErrors.value = validation.errors;
    return;
  }

  submitting.value = true;

  try {
    const created = await singularService.create(toCreateRequest());
    dsNotifySuccess(t("singular.create.success"));
    await router.push({
      name: ROUTE_NAMES.SINGULAR_DETAIL,
      params: { id: String(created.id) }
    });
  } catch (error) {
    const apiError = handleError(error);
    if (apiError.fieldErrors && apiError.fieldErrors.length > 0) {
      fieldErrors.value = mapSingularFieldErrors(apiError.fieldErrors);
    }
  } finally {
    submitting.value = false;
  }
}

function onCancel(): void {
  void router.push({ name: ROUTE_NAMES.SINGULAR_LIST });
}
</script>

<style scoped lang="scss">
.singular-create-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);
}
</style>
