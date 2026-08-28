<template>
  <div class="colaborador-detail-page">
    <DsPageHeader
      :title="pageTitle"
      :subtitle="$t('colaborador.detail.subtitle', { id })"
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

    <ColaboradorInfoCard v-else-if="colaborador" :colaborador="colaborador">
      <template #actions>
        <DsButton variant="ghost" :to="ROUTE_PATHS.COLABORADOR_LIST">
          {{ $t("colaborador.detail.backToList") }}
        </DsButton>
        <DsButton :variant="statusActionVariant" @click="openStatusDialog">
          {{ statusActionLabel }}
        </DsButton>
        <DsButton variant="primary" :to="editRoute">
          {{ $t("colaborador.detail.editAction") }}
        </DsButton>
      </template>
    </ColaboradorInfoCard>

    <ColaboradorStatusDialog
      v-if="colaborador"
      v-model="statusDialogOpen"
      :colaborador="colaborador"
      :loading="statusSubmitting"
      @confirm="onConfirmStatusChange"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import { useI18n } from "vue-i18n";

import ColaboradorInfoCard from "@/components/organization/colaborador/ColaboradorInfoCard.vue";
import ColaboradorStatusDialog from "@/components/organization/colaborador/ColaboradorStatusDialog.vue";
import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import AppLoadingSkeleton from "@/components/shared/AppLoadingSkeleton.vue";
import { DsButton, DsPageHeader, dsNotifySuccess } from "@/components/ds";
import { isColaboradorDeactivation } from "@/composables/organization/colaborador-status";
import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { ROUTE_PATHS, colaboradorEditPath } from "@/constants/routes";
import { colaboradorService } from "@/services/organization";
import type {
  ColaboradorResponse,
  ColaboradorStatus
} from "@/types/organization/colaborador.types";

const props = defineProps<{
  id: string;
}>();

const { t } = useI18n();
const { handleError } = useStandardErrorHandling();

const colaborador = ref<ColaboradorResponse | null>(null);
const loading = ref(true);
const notFound = ref(false);
const statusDialogOpen = ref(false);
const statusSubmitting = ref(false);

const pageTitle = computed(
  () => colaborador.value?.name ?? t("colaborador.detail.title")
);

const editRoute = computed(() => colaboradorEditPath(props.id));

const statusActionLabel = computed(() => {
  if (!colaborador.value) {
    return t("colaborador.detail.changeStatusAction");
  }

  return isColaboradorDeactivation(colaborador.value.status)
    ? t("colaborador.detail.deactivateAction")
    : t("colaborador.detail.activateAction");
});

const statusActionVariant = computed(() =>
  colaborador.value && isColaboradorDeactivation(colaborador.value.status)
    ? "outline"
    : "secondary"
);

async function loadColaborador(): Promise<void> {
  loading.value = true;
  notFound.value = false;
  colaborador.value = null;

  const numericId = Number(props.id);
  if (!Number.isFinite(numericId) || numericId <= 0) {
    notFound.value = true;
    loading.value = false;
    return;
  }

  try {
    colaborador.value = await colaboradorService.getById(numericId);
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

function openStatusDialog(): void {
  statusDialogOpen.value = true;
}

async function onConfirmStatusChange(
  targetStatus: ColaboradorStatus
): Promise<void> {
  if (!colaborador.value) {
    return;
  }

  statusSubmitting.value = true;

  try {
    const updated = await colaboradorService.updateStatus(
      colaborador.value.id,
      {
        status: targetStatus
      }
    );
    colaborador.value = updated;
    statusDialogOpen.value = false;

    const successKey =
      targetStatus === "INACTIVE"
        ? "colaborador.statusDialog.successDeactivate"
        : "colaborador.statusDialog.successActivate";
    dsNotifySuccess(t(successKey));
  } catch (error) {
    handleError(error);
  } finally {
    statusSubmitting.value = false;
  }
}

onMounted(() => {
  void loadColaborador();
});
</script>

<style scoped lang="scss">
.colaborador-detail-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);
}
</style>
