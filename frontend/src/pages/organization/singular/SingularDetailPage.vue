<template>
  <div class="singular-detail-page">
    <DsPageHeader
      :title="pageTitle"
      :subtitle="$t('singular.detail.subtitle', { id })"
    />

    <AppLoadingSkeleton v-if="loading" />

    <AppEmptyState
      v-else-if="notFound"
      :title="$t('singular.detail.notFoundTitle')"
      :description="$t('singular.detail.notFoundDescription')"
      icon="mdi-alert-circle-outline"
    >
      <DsButton variant="primary" :to="ROUTE_PATHS.SINGULAR_LIST">
        {{ $t("singular.detail.backToList") }}
      </DsButton>
    </AppEmptyState>

    <SingularInfoCard v-else-if="singular" :singular="singular">
      <template #actions>
        <DsButton
          variant="ghost"
          :to="ROUTE_PATHS.SINGULAR_LIST"
        >
          {{ $t("singular.detail.backToList") }}
        </DsButton>
        <DsButton
          :variant="statusActionVariant"
          @click="openStatusDialog"
        >
          {{ statusActionLabel }}
        </DsButton>
        <DsButton
          variant="primary"
          :to="editRoute"
        >
          {{ $t("singular.detail.editAction") }}
        </DsButton>
      </template>
    </SingularInfoCard>

    <SingularStatusDialog
      v-if="singular"
      v-model="statusDialogOpen"
      :singular="singular"
      :loading="statusSubmitting"
      @confirm="onConfirmStatusChange"
    />
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import { useI18n } from "vue-i18n";

import SingularInfoCard from "@/components/organization/singular/SingularInfoCard.vue";
import SingularStatusDialog from "@/components/organization/singular/SingularStatusDialog.vue";
import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import AppLoadingSkeleton from "@/components/shared/AppLoadingSkeleton.vue";
import { DsButton, DsPageHeader, dsNotifySuccess } from "@/components/ds";
import {
  isSingularDeactivation
} from "@/composables/organization/singular-status";
import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { ROUTE_PATHS, singularEditPath } from "@/constants/routes";
import { singularService } from "@/services/organization";
import type { SingularResponse, SingularStatus } from "@/types/organization/singular.types";

const props = defineProps<{
  id: string;
}>();

const { t } = useI18n();
const { handleError } = useStandardErrorHandling();

const singular = ref<SingularResponse | null>(null);
const loading = ref(true);
const notFound = ref(false);
const statusDialogOpen = ref(false);
const statusSubmitting = ref(false);

const pageTitle = computed(() =>
  singular.value?.name ?? t("singular.detail.title")
);

const editRoute = computed(() => singularEditPath(props.id));

const statusActionLabel = computed(() => {
  if (!singular.value) {
    return t("singular.detail.changeStatusAction");
  }

  return isSingularDeactivation(singular.value.status)
    ? t("singular.detail.deactivateAction")
    : t("singular.detail.activateAction");
});

const statusActionVariant = computed(() =>
  singular.value && isSingularDeactivation(singular.value.status)
    ? "outline"
    : "secondary"
);

async function loadSingular(): Promise<void> {
  loading.value = true;
  notFound.value = false;
  singular.value = null;

  const numericId = Number(props.id);
  if (!Number.isFinite(numericId) || numericId <= 0) {
    notFound.value = true;
    loading.value = false;
    return;
  }

  try {
    singular.value = await singularService.getById(numericId);
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

async function onConfirmStatusChange(targetStatus: SingularStatus): Promise<void> {
  if (!singular.value) {
    return;
  }

  statusSubmitting.value = true;

  try {
    const updated = await singularService.updateStatus(
      singular.value.id,
      { status: targetStatus }
    );
    singular.value = updated;
    statusDialogOpen.value = false;

    const successKey =
      targetStatus === "INACTIVE"
        ? "singular.statusDialog.successDeactivate"
        : "singular.statusDialog.successActivate";
    dsNotifySuccess(t(successKey));
  } catch (error) {
    handleError(error);
  } finally {
    statusSubmitting.value = false;
  }
}

onMounted(() => {
  void loadSingular();
});
</script>

<style scoped lang="scss">
.singular-detail-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);
}
</style>
