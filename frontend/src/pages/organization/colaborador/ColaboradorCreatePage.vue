<template>
  <div class="colaborador-create-page">
    <DsPageHeader
      :title="$t('colaborador.create.title')"
      :subtitle="$t('colaborador.create.subtitle')"
    />

    <ColaboradorForm
      :model="form"
      :errors="fieldErrors"
      mode="create"
      :loading="submitting"
      :title="$t('colaborador.create.cardTitle')"
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
import { ref } from "vue";
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";

import ColaboradorForm from "@/components/organization/colaborador/ColaboradorForm.vue";
import { DsPageHeader, dsNotifySuccess } from "@/components/ds";
import { useColaboradorOrganizationalOptions } from "@/composables/organization/useColaboradorOrganizationalOptions";
import {
  mapColaboradorFieldErrors,
  useColaboradorForm
} from "@/composables/organization/useColaboradorForm";
import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { ROUTE_PATHS, colaboradorDetailPath } from "@/constants/routes";
import { colaboradorService } from "@/services/organization";

const router = useRouter();
const { t } = useI18n();
const { form, validateCreate, toCreateRequest } = useColaboradorForm();
const {
  singularOptions,
  areaOptions,
  teamOptions,
  loadingSingulares,
  loadingAreas,
  loadingTeams
} = useColaboradorOrganizationalOptions(form);
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
    const created = await colaboradorService.create(toCreateRequest());
    dsNotifySuccess(t("colaborador.create.success"));
    await router.push(colaboradorDetailPath(created.id));
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
  void router.push(ROUTE_PATHS.COLABORADOR_LIST);
}
</script>

<style scoped lang="scss">
.colaborador-create-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);
}
</style>
