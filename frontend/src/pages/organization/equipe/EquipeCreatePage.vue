<template>
  <div class="equipe-create-page">
    <DsPageHeader
      :title="$t('equipe.create.title')"
      :subtitle="$t('equipe.create.subtitle')"
    />

    <EquipeForm
      :model="form"
      :errors="fieldErrors"
      mode="create"
      :loading="submitting"
      :title="$t('equipe.create.cardTitle')"
      :area-options="areaOptions"
      :loading-areas="loadingAreas"
      @submit="onSubmit"
      @cancel="onCancel"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";

import EquipeForm from "@/components/organization/equipe/EquipeForm.vue";
import { dsNotifySuccess } from "@/components/ds";
import { useEquipeAreaOptions } from "@/composables/organization/useEquipeAreaOptions";
import {
  mapEquipeFieldErrors,
  useEquipeForm
} from "@/composables/organization/useEquipeForm";
import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { ROUTE_PATHS, equipeDetailPath } from "@/constants/routes";
import { equipeService } from "@/services/organization";

const router = useRouter();
const { t } = useI18n();
const { form, validateCreate, toCreateRequest } = useEquipeForm();
const { areaOptions, loadingAreas } = useEquipeAreaOptions();
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
    const created = await equipeService.create(toCreateRequest());
    dsNotifySuccess(t("equipe.create.success"));
    await router.push(equipeDetailPath(created.id));
  } catch (error) {
    const apiError = handleError(error);
    if (apiError.fieldErrors && apiError.fieldErrors.length > 0) {
      fieldErrors.value = mapEquipeFieldErrors(apiError.fieldErrors);
    }
  } finally {
    submitting.value = false;
  }
}

function onCancel(): void {
  void router.push(ROUTE_PATHS.EQUIPE_LIST);
}
</script>

<style scoped lang="scss">
.equipe-create-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);
}
</style>
