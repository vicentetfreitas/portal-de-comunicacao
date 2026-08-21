<template>
  <div class="area-colaborador-hub-page">
    <DsPageHeader
      :title="$t('areaColaborador.hub.title')"
      :subtitle="$t('areaColaborador.hub.subtitle')"
    />

    <AppLoadingSkeleton v-if="loading" />

    <AppEmptyState
      v-else-if="notFound"
      :title="$t('areaColaborador.hub.notFoundTitle')"
      :description="$t('areaColaborador.hub.notFoundDescription')"
      icon="mdi-alert-circle-outline"
    />

    <DsCard v-else-if="area" :title="area.name">
      <p v-if="area.description">{{ area.description }}</p>
    </DsCard>

    <DsCard :title="$t('areaColaborador.hub.cardTitle')">
      <div class="area-colaborador-hub-page__grid">
        <DsActionCard
          :label="$t('areaColaborador.hub.equipeAction')"
          :description="$t('areaColaborador.hub.equipeDescription')"
          icon="mdi-account-group"
          variant="outline"
          @click="goToEquipe"
        />
        <DsActionCard
          :label="$t('areaColaborador.hub.arquivosAction')"
          :description="$t('areaColaborador.hub.arquivosDescription')"
          icon="mdi-folder-multiple"
          variant="outline"
          disabled
        />
      </div>
    </DsCard>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";

import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import AppLoadingSkeleton from "@/components/shared/AppLoadingSkeleton.vue";
import { DsActionCard, DsCard, DsPageHeader } from "@/components/ds";
import { useAreaColaboradorDetail } from "@/composables/area-colaborador/useAreaColaboradorDetail";
import { ROUTE_PATHS } from "@/constants/routes";

const { area, loading, notFound } = useAreaColaboradorDetail();
const router = useRouter();

function goToEquipe(): void {
  void router.push(ROUTE_PATHS.AREA_COLABORADOR_EQUIPE);
}
</script>

<style scoped lang="scss">
.area-colaborador-hub-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);

  &__grid {
    display: grid;
    gap: var(--spacing-md, 16px);

    @media (min-width: 768px) {
      grid-template-columns: repeat(2, minmax(0, 1fr));
    }
  }
}
</style>
