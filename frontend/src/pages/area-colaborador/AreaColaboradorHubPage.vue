<template>
  <div class="area-colaborador-hub-page">
    <DsPageHeader
      :title="$t('areaColaborador.hub.title')"
      :subtitle="$t('areaColaborador.hub.subtitle')"
    />

    <DsCard :title="$t('areaColaborador.hub.cardTitle')">
      <div class="area-colaborador-hub-page__grid">
        <DsActionCard
          :label="$t('areaColaborador.hub.equipeAction')"
          :description="$t('areaColaborador.hub.equipeDescription')"
          icon="mdi-account-group"
          variant="outline"
          disabled
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
import { DsActionCard, DsCard, DsPageHeader } from "@/components/ds";
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
