<template>
  <div class="colaborador-edit-page">
    <DsPageHeader
      :title="$t('colaborador.edit.title')"
      :subtitle="$t('colaborador.edit.subtitle', { id })"
    />

    <AppLoadingSkeleton v-if="loading" />

    <AppEmptyState
      v-else-if="notFound"
      :title="$t('colaborador.detail.notFoundTitle')"
      :description="$t('colaborador.detail.notFoundDescription')"
      icon="mdi-alert-circle-outline"
    >
      <DsButton variant="primary" :to="ROUTE_PATHS.COLABORADOR_LIST">
        {{ $t("colaborador.detail.backToList") }}
      </DsButton>
    </AppEmptyState>

    <ColaboradorForm
      v-else
      :model="form"
      :errors="fieldErrors"
      mode="edit"
      :loading="submitting"
      :title="$t('colaborador.edit.cardTitle')"
      :singular-options="singularOptions"
      :area-options="areaOptions"
      :team-options="teamOptions"
      :loading-singulares="loadingSingulares"
      :loading-areas="loadingAreas"
      :loading-teams="loadingTeams"
      @submit="onSubmit"
      @cancel="onCancel"
    />
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";

import ColaboradorForm from "@/components/organization/colaborador/ColaboradorForm.vue";
import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import AppLoadingSkeleton from "@/components/shared/AppLoadingSkeleton.vue";
import { DsButton, DsPageHeader, dsNotifySuccess } from "@/components/ds";
import { useColaboradorOrganizationalOptions } from "@/composables/organization/useColaboradorOrganizationalOptions";
import {
  mapColaboradorFieldErrors,
  mapColaboradorToForm,
  useColaboradorForm
} from "@/composables/organization/useColaboradorForm";
import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { ROUTE_PATHS, colaboradorDetailPath } from "@/constants/routes";
import { colaboradorService } from "@/services/organization";

const props = defineProps<{
  id: string;
}>();

const router = useRouter();
const { t } = useI18n();
const { form, validateUpdate, toUpdateRequest, reset } = useColaboradorForm();
const {
  singularOptions,
  areaOptions,
  teamOptions,
  loadingSingulares,
  loadingAreas,
  loadingTeams,
  loadOptionsFor
} = useColaboradorOrganizationalOptions(form);
const { handleError } = useStandardErrorHandling();

const fieldErrors = ref<Record<string, string>>({});
const submitting = ref(false);
const loading = ref(true);
const notFound = ref(false);

async function loadColaborador(): Promise<void> {
  loading.value = true;
  notFound.value = false;

  const numericId = Number(props.id);
  if (!Number.isFinite(numericId) || numericId <= 0) {
    notFound.value = true;
    loading.value = false;
    return;
  }

  try {
    const colaborador = await colaboradorService.getById(numericId);
    reset(mapColaboradorToForm(colaborador));
    await loadOptionsFor();
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
    await colaboradorService.update(numericId, toUpdateRequest());
    dsNotifySuccess(t("colaborador.edit.success"));
    await router.push(colaboradorDetailPath(props.id));
  } catch (error) {
    const apiError = handleError(error);
    if (apiError.fieldErrors && apiError.fieldErrors.length > 0) {
      fieldErrors.value = mapColaboradorFieldErrors(apiError.fieldErrors);
    }
  } finally {
    submitting.value = false;
  }
}

function onCancel(): void {
  void router.push(colaboradorDetailPath(props.id));
}

onMounted(() => {
  void loadColaborador();
});
</script>

<style scoped lang="scss">
.colaborador-edit-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);
}
</style>
