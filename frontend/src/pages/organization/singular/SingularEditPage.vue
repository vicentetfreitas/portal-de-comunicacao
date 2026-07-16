<template>
  <div class="singular-edit-page">
    <DsPageHeader
      :title="$t('singular.edit.title')"
      :subtitle="$t('singular.edit.subtitle', { id })"
    />

    <AppLoadingSkeleton v-if="loading" />

    <AppEmptyState
      v-else-if="notFound"
      :title="$t('singular.detail.notFoundTitle')"
      :description="$t('singular.detail.notFoundDescription')"
      icon="mdi-alert-circle-outline"
    >
      <DsButton variant="primary" :to="{ name: ROUTE_NAMES.SINGULAR_LIST }">
        {{ $t("singular.detail.backToList") }}
      </DsButton>
    </AppEmptyState>

    <SingularForm
      v-else
      :model="form"
      :errors="fieldErrors"
      mode="edit"
      :loading="submitting"
      :title="$t('singular.edit.cardTitle')"
      @submit="onSubmit"
      @cancel="onCancel"
    />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";

import SingularForm from "@/components/organization/singular/SingularForm.vue";
import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import AppLoadingSkeleton from "@/components/shared/AppLoadingSkeleton.vue";
import { DsButton, DsPageHeader, dsNotifySuccess } from "@/components/ds";
import {
  mapSingularFieldErrors,
  mapSingularToForm,
  useSingularForm
} from "@/composables/organization/useSingularForm";
import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { ROUTE_NAMES } from "@/constants/routes";
import { singularService } from "@/services/organization";

const props = defineProps<{
  id: string;
}>();

const router = useRouter();
const { t } = useI18n();
const { form, validateUpdate, toUpdateRequest, reset } = useSingularForm();
const { handleError } = useStandardErrorHandling();

const fieldErrors = ref<Record<string, string>>({});
const submitting = ref(false);
const loading = ref(true);
const notFound = ref(false);

async function loadSingular(): Promise<void> {
  loading.value = true;
  notFound.value = false;

  const numericId = Number(props.id);
  if (!Number.isFinite(numericId) || numericId <= 0) {
    notFound.value = true;
    loading.value = false;
    return;
  }

  try {
    const singular = await singularService.getById(numericId);
    reset(mapSingularToForm(singular));
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
    await singularService.update(numericId, toUpdateRequest());
    dsNotifySuccess(t("singular.edit.success"));
    await router.push({
      name: ROUTE_NAMES.SINGULAR_DETAIL,
      params: { id: props.id }
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
  void router.push({
    name: ROUTE_NAMES.SINGULAR_DETAIL,
    params: { id: props.id }
  });
}

onMounted(() => {
  void loadSingular();
});
</script>

<style scoped lang="scss">
.singular-edit-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);
}
</style>
