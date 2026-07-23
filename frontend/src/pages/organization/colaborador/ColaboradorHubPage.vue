<template>
  <div class="colaborador-hub-page">
    <DsPageHeader
      :title="$t('colaborador.hub.title')"
      :subtitle="$t('colaborador.hub.subtitle')"
    />

    <DsCard :title="$t('colaborador.hub.cardTitle')">
      <div class="colaborador-hub-page__grid">
        <DsActionCard
          :label="$t('colaborador.hub.listAction')"
          :description="$t('colaborador.hub.listDescription')"
          icon="mdi-format-list-bulleted"
          variant="outline"
          @click="goToList"
        />
        <DsActionCard
          :label="$t('colaborador.hub.createAction')"
          :description="$t('colaborador.hub.createDescription')"
          icon="mdi-plus-circle"
          variant="primary"
          @click="goToCreate"
        />
      </div>
    </DsCard>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";

import { DsActionCard, DsCard, DsPageHeader } from "@/components/ds";
import { ROUTE_PATHS } from "@/constants/routes";

const router = useRouter();

function goToList(): void {
  void router.push(ROUTE_PATHS.COLABORADOR_LIST);
}

function goToCreate(): void {
  void router.push(ROUTE_PATHS.COLABORADOR_CREATE);
}
</script>

<style scoped lang="scss">
.colaborador-hub-page {
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
