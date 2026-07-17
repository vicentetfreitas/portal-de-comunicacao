<template>
  <div class="equipe-edit-page">
    <DsPageHeader
      :title="$t('equipe.edit.title')"
      :subtitle="$t('equipe.edit.subtitle', { id })"
    />

    <AppLoadingSkeleton v-if="loading" />

    <AppEmptyState
      v-else-if="notFound"
      :title="$t('equipe.detail.notFoundTitle')"
      :description="$t('equipe.detail.notFoundDescription')"
      icon="mdi-alert-circle-outline"
    >
      <DsButton variant="primary" :to="ROUTE_PATHS.EQUIPE_LIST">
        {{ $t("equipe.detail.backToList") }}
      </DsButton>
    </AppEmptyState>

    <EquipeForm
      v-else
      :model="form"
      :errors="fieldErrors"
      mode="edit"
      :loading="submitting"
      :title="$t('equipe.edit.cardTitle')"
      @submit="onSubmit"
      @cancel="onCancel"
    />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";

import EquipeForm from "@/components/organization/equipe/EquipeForm.vue";
import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import AppLoadingSkeleton from "@/components/shared/AppLoadingSkeleton.vue";
import { DsButton, DsPageHeader, dsNotifySuccess } from "@/components/ds";
import {
  mapEquipeFieldErrors,
  mapEquipeToForm,
  useEquipeForm
} from "@/composables/organization/useEquipeForm";
import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { ROUTE_PATHS, equipeDetailPath } from "@/constants/routes";
import { equipeService } from "@/services/organization";

const props = defineProps<{
  id: string;
}>();

const router = useRouter();
const { t } = useI18n();
const { form, validateUpdate, toUpdateRequest, reset } = useEquipeForm();
const { handleError } = useStandardErrorHandling();

const fieldErrors = ref<Record<string, string>>({});
const submitting = ref(false);
const loading = ref(true);
const notFound = ref(false);

async function loadEquipe(): Promise<void> {
  loading.value = true;
  notFound.value = false;

  const numericId = Number(props.id);
  if (!Number.isFinite(numericId) || numericId <= 0) {
    notFound.value = true;
    loading.value = false;
    return;
  }

  try {
    const equipe = await equipeService.getById(numericId);
    reset(mapEquipeToForm(equipe));
  } catch (error) {
    const apiError = handleError(error, { silent: true });
    if (apiError.status === 404 || apiError.category === "not_found") {
      notFound.value = true;
      return;
    }

    handleError(error);
  } finally {
    loading.value = false;
  }
}

async function onSubmit(): Promise<void> {
  fieldErrors.value = {};

  const validation = validateUpdate();
  if (!validation.valid) {
    fieldErrors.value = validation.errors;
    return;
  }

  const numericId = Number(props.id);
  if (!Number.isFinite(numericId) || numericId <= 0) {
    notFound.value = true;
    return;
  }

  submitting.value = true;

  try {
    await equipeService.update(numericId, toUpdateRequest());
    dsNotifySuccess(t("equipe.edit.success"));
    await router.push(equipeDetailPath(props.id));
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
  void router.push(equipeDetailPath(props.id));
}

onMounted(() => {
  void loadEquipe();
});
</script>

<style scoped lang="scss">
.equipe-edit-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);
}
</style>
