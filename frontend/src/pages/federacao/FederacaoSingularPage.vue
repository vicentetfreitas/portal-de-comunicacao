<template>
  <div class="federacao-singular-page">
    <DsPageHeader
      :title="singular?.name ?? $t('layout.sidebar.singularLabel')"
      :subtitle="singular ? $t('federacao.singular.subtitle') : ''"
      :show-back="showBackButton"
      @back="router.back()"
    />

    <AppLoadingSkeleton v-if="loading" />

    <AppEmptyState
      v-else-if="notFound"
      :title="$t('federacao.singular.notFoundTitle')"
      :description="$t('federacao.singular.notFoundDescription')"
      icon="mdi-alert-circle-outline"
    />

    <template v-else>
      <DsCard :title="$t('federacao.singular.areasTitle')">
        <AppEmptyState
          v-if="isAreasEmpty"
          :title="$t('federacao.singular.areasEmptyTitle')"
          :description="$t('federacao.singular.areasEmptyDescription')"
          icon="mdi-domain-off"
        />
        <div v-else class="federacao-singular-page__grid">
          <DsActionCard
            v-for="area in areas"
            :key="area.id"
            :label="area.name"
            :description="area.description ?? ''"
            icon="mdi-office-building-outline"
            variant="outline"
            @click="goToArea(area.id)"
          />
        </div>
      </DsCard>
    </template>
  </div>
</template>

<script setup lang="ts">
import { toRef } from "vue";
import { useRouter } from "vue-router";

import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import AppLoadingSkeleton from "@/components/shared/AppLoadingSkeleton.vue";
import { DsActionCard, DsCard, DsPageHeader } from "@/components/ds";
import { useFederacaoSingularDetail } from "@/composables/federacao/useFederacaoSingularDetail";
import { useLayoutMeta } from "@/composables/useLayoutMeta";
import { federacaoAreaPath } from "@/constants/routes";

const props = defineProps<{
  id: string;
}>();

const router = useRouter();
const { singular, areas, loading, notFound, isAreasEmpty } =
  useFederacaoSingularDetail(toRef(props, "id"));
const { showBackButton } = useLayoutMeta();

function goToArea(areaId: number): void {
  void router.push(federacaoAreaPath(areaId));
}
</script>

<style scoped lang="scss">
.federacao-singular-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);

  &__grid {
    display: grid;
    // Same responsive pattern as SingularHubPage.vue/EquipeHubPage.vue —
    // a Singular can have many áreas, so this shouldn't stay capped at 2
    // columns forever on wide desktop; minmax's floor (240px) keeps it well
    // under any sensible column-count ceiling within this app's own
    // 1280px content max-width.
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
    gap: var(--spacing-md, 16px);
  }
}
</style>
