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
        <DsButton variant="primary" :to="editRoute">
          {{ $t("colaborador.detail.editAction") }}
        </DsButton>
      </template>
    </ColaboradorInfoCard>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted, ref } from "vue";
import { useI18n } from "vue-i18n";

import ColaboradorInfoCard from "@/components/organization/colaborador/ColaboradorInfoCard.vue";
import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import AppLoadingSkeleton from "@/components/shared/AppLoadingSkeleton.vue";
import { DsButton, DsPageHeader } from "@/components/ds";
import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { ROUTE_PATHS, colaboradorEditPath } from "@/constants/routes";
import { colaboradorService } from "@/services/organization";
import type { ColaboradorResponse } from "@/types/organization/colaborador.types";

const props = defineProps<{
  id: string;
}>();

const { t } = useI18n();
const { handleError } = useStandardErrorHandling();

const colaborador = ref<ColaboradorResponse | null>(null);
const loading = ref(true);
const notFound = ref(false);

const pageTitle = computed(
  () => colaborador.value?.name ?? t("colaborador.detail.title")
);

const editRoute = computed(() => colaboradorEditPath(props.id));

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
