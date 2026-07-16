<template>
  <div class="singular-hub-page">
    <DsPageHeader
      :title="$t('singular.hub.title')"
      :subtitle="$t('singular.hub.subtitle')"
    />

    <DsCard :title="$t('singular.hub.cardTitle')">
      <div class="singular-hub-page__grid">
        <DsActionCard
          :label="$t('singular.hub.listAction')"
          :description="$t('singular.hub.listDescription')"
          icon="mdi-format-list-bulleted"
          variant="outline"
          @click="goToList"
        />
        <DsActionCard
          :label="$t('singular.hub.createAction')"
          :description="$t('singular.hub.createDescription')"
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
  void router.push(ROUTE_PATHS.SINGULAR_LIST);
}

function goToCreate(): void {
  void router.push(ROUTE_PATHS.SINGULAR_CREATE);
}
</script>

<style scoped lang="scss">
.singular-hub-page {
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
