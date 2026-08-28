<template>
  <div class="federacao-area-hub-page">
    <DsPageHeader
      :title="area?.name ?? $t('layout.sidebar.federationLabel')"
      :show-back="showBackButton"
      @back="router.back()"
    />

    <AppLoadingSkeleton v-if="loading" />

    <AppEmptyState
      v-else-if="notFound"
      :title="$t('federacao.area.notFoundTitle')"
      :description="$t('federacao.area.notFoundDescription')"
      icon="mdi-alert-circle-outline"
    />

    <template v-else-if="area">
      <p v-if="area.description" class="federacao-area-hub-page__description">
        {{ area.description }}
      </p>

      <div class="federacao-area-hub-page__actions">
        <DsButton variant="primary" size="lg" @click="goToEquipe">
          <DsIcon name="mdi-account-group" size="md" />
          <span>{{ $t("federacao.area.equipeAction") }}</span>
        </DsButton>
        <!--
          FT-DOCUMENTO (Arquivos e Documentos) is IMPLEMENTING (spec approved,
          AreaColaboradorArquivosPage.vue consumes it) — but that endpoint is scoped to
          the collaborator's own Contexto Ativo, not an arbitrary area by :id as this
          hub browses. Kept disabled here until/unless an explicit-area query is decided
          (out of scope per specification.md § Escopo) — enabling it would silently show
          the viewer's own files instead of this area's.
        -->
        <DsButton variant="secondary" size="lg" disable>
          <DsIcon name="mdi-folder-multiple" size="md" />
          <span>{{ $t("federacao.area.arquivosAction") }}</span>
        </DsButton>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { toRef } from "vue";
import { useRouter } from "vue-router";

import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import AppLoadingSkeleton from "@/components/shared/AppLoadingSkeleton.vue";
import { DsButton, DsIcon, DsPageHeader } from "@/components/ds";
import { useFederacaoAreaDetail } from "@/composables/federacao/useFederacaoAreaDetail";
import { useLayoutMeta } from "@/composables/useLayoutMeta";
import { federacaoAreaEquipePath } from "@/constants/routes";

const props = defineProps<{
  id: string;
}>();

const router = useRouter();
const { area, loading, notFound } = useFederacaoAreaDetail(toRef(props, "id"));
const { showBackButton } = useLayoutMeta();

function goToEquipe(): void {
  void router.push(federacaoAreaEquipePath(props.id));
}
</script>

<style scoped lang="scss">
.federacao-area-hub-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);

  &__description {
    margin: 0;
    color: var(--color-text-secondary);
  }

  &__actions {
    display: flex;
    flex-wrap: wrap;
    gap: var(--spacing-md, 16px);

    // Icon-forward button format (not the boxed DsActionCard) — icon and
    // label sit inline, same spirit as the header's own icon buttons.
    :deep(.q-btn__content) {
      gap: var(--spacing-xs, 4px);
    }
  }
}
</style>
