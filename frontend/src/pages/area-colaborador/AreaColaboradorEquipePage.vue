<template>
  <div class="area-colaborador-equipe-page">
    <DsPageHeader
      :title="area?.name ?? $t('areaColaborador.equipe.title')"
      :subtitle="$t('areaColaborador.equipe.subtitle')"
    />

    <AppLoadingSkeleton v-if="loading" />

    <AppEmptyState
      v-else-if="isEmpty"
      :title="$t('areaColaborador.equipe.emptyTitle')"
      :description="$t('areaColaborador.equipe.emptyDescription')"
      icon="mdi-account-group-outline"
    />

    <div v-else class="area-colaborador-equipe-page__grid">
      <DsContentCard
        v-for="equipe in equipes"
        :key="equipe.id"
        :title="equipe.name"
        :description="equipe.description ?? ''"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import AppLoadingSkeleton from "@/components/shared/AppLoadingSkeleton.vue";
import { DsContentCard, DsPageHeader } from "@/components/ds";
import { useAreaColaboradorDetail } from "@/composables/area-colaborador/useAreaColaboradorDetail";
import { useAreaColaboradorEquipes } from "@/composables/area-colaborador/useAreaColaboradorEquipes";

const { area } = useAreaColaboradorDetail();
const { equipes, loading, isEmpty } = useAreaColaboradorEquipes();
</script>

<style scoped lang="scss">
.area-colaborador-equipe-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);

  &__grid {
    display: grid;
    // Same responsive pattern as SingularHubPage.vue/EquipeHubPage.vue.
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
    gap: var(--spacing-md, 16px);
  }
}
</style>
