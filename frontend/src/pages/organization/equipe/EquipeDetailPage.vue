<template>
  <div class="equipe-detail-page">
    <DsPageHeader
      :title="pageTitle"
      :subtitle="$t('equipe.detail.subtitle', { id })"
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

    <EquipeInfoCard v-else-if="equipe" :equipe="equipe">
      <template #actions>
        <DsButton variant="ghost" :to="ROUTE_PATHS.EQUIPE_LIST">
          {{ $t("equipe.detail.backToList") }}
        </DsButton>
        <DsButton :variant="statusActionVariant" @click="openStatusDialog">
          {{ statusActionLabel }}
        </DsButton>
        <DsButton variant="primary" :to="editRoute">
          {{ $t("equipe.detail.editAction") }}
        </DsButton>
      </template>
    </EquipeInfoCard>

    <EquipeStatusDialog
      v-if="equipe"
      v-model="statusDialogOpen"
      :equipe="equipe"
      :loading="statusSubmitting"
      @confirm="onConfirmStatusChange"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import { useI18n } from "vue-i18n";

import EquipeInfoCard from "@/components/organization/equipe/EquipeInfoCard.vue";
import EquipeStatusDialog from "@/components/organization/equipe/EquipeStatusDialog.vue";
import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import AppLoadingSkeleton from "@/components/shared/AppLoadingSkeleton.vue";
import { DsButton, DsPageHeader, dsNotifySuccess } from "@/components/ds";
import { isEquipeDeactivation } from "@/composables/organization/equipe-status";
import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { ROUTE_PATHS, equipeEditPath } from "@/constants/routes";
import { equipeService } from "@/services/organization";
import type {
  EquipeResponse,
  EquipeStatus
} from "@/types/organization/equipe.types";

const props = defineProps<{
  id: string;
}>();

const { t } = useI18n();
const { handleError } = useStandardErrorHandling();

const equipe = ref<EquipeResponse | null>(null);
const loading = ref(true);
const notFound = ref(false);
const statusDialogOpen = ref(false);
const statusSubmitting = ref(false);

const pageTitle = computed(
  () => equipe.value?.name ?? t("equipe.detail.title")
);

const editRoute = computed(() => equipeEditPath(props.id));

const statusActionLabel = computed(() => {
  if (!equipe.value) {
    return t("equipe.detail.changeStatusAction");
  }

  return isEquipeDeactivation(equipe.value.status)
    ? t("equipe.detail.deactivateAction")
    : t("equipe.detail.activateAction");
});

const statusActionVariant = computed(() =>
  equipe.value && isEquipeDeactivation(equipe.value.status)
    ? "outline"
    : "secondary"
);

async function loadEquipe(): Promise<void> {
  loading.value = true;
  notFound.value = false;
  equipe.value = null;

  const numericId = Number(props.id);
  if (!Number.isFinite(numericId) || numericId <= 0) {
    notFound.value = true;
    loading.value = false;
    return;
  }

  try {
    equipe.value = await equipeService.getById(numericId);
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
  targetStatus: EquipeStatus
): Promise<void> {
  if (!equipe.value) {
    return;
  }

  statusSubmitting.value = true;

  try {
    const updated = await equipeService.updateStatus(equipe.value.id, {
      status: targetStatus
    });
    equipe.value = updated;
    statusDialogOpen.value = false;

    const successKey =
      targetStatus === "INACTIVE"
        ? "equipe.statusDialog.successDeactivate"
        : "equipe.statusDialog.successActivate";
    dsNotifySuccess(t(successKey));
  } catch (error) {
    handleError(error);
  } finally {
    statusSubmitting.value = false;
  }
}

onMounted(() => {
  void loadEquipe();
});
</script>

<style scoped lang="scss">
.equipe-detail-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);
}
</style>
