<template>
  <div class="equipe-hub-page">
    <DsPageHeader
      :title="$t('equipe.hub.title')"
      :subtitle="$t('equipe.hub.subtitle')"
    />

    <DsCard :title="$t('equipe.hub.cardTitle')">
      <div class="equipe-hub-page__grid">
        <DsActionCard
          :label="$t('equipe.hub.listAction')"
          :description="$t('equipe.hub.listDescription')"
          icon="mdi-format-list-bulleted"
          variant="outline"
          @click="goToList"
        />
        <DsActionCard
          :label="$t('equipe.hub.createAction')"
          :description="$t('equipe.hub.createDescription')"
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
  void router.push(ROUTE_PATHS.EQUIPE_LIST);
}

function goToCreate(): void {
  void router.push(ROUTE_PATHS.EQUIPE_CREATE);
}
</script>

<style scoped lang="scss">
.equipe-hub-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);

  &__grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
    gap: var(--spacing-md, 16px);
  }
}
</style>
