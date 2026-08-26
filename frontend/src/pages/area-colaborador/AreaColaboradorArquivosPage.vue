<template>
  <div class="area-colaborador-arquivos-page">
    <DsPageHeader
      :title="$t('areaColaborador.arquivos.title')"
      :subtitle="$t('areaColaborador.arquivos.subtitle')"
    />

    <AppLoadingSkeleton v-if="loading" />

    <AppEmptyState
      v-else-if="isEmpty"
      :title="$t('areaColaborador.arquivos.emptyTitle')"
      :description="$t('areaColaborador.arquivos.emptyDescription')"
      icon="mdi-folder-open-outline"
    />

    <div v-else class="area-colaborador-arquivos-page__pastas">
      <DsCard v-for="pasta in pastas" :key="pasta.id">
        <DsSectionHeader :title="pasta.nome" />

        <DsContentCardCompact
          v-for="documento in pasta.documentos"
          :key="documento.id"
          :title="documento.nome"
          :meta="documento.formato"
        >
          <template #trailing>
            <DsButton
              variant="secondary"
              size="sm"
              :loading="downloadingId === documento.id"
              :aria-label="$t('areaColaborador.arquivos.downloadLabel')"
              @click="baixarDocumento(documento)"
            >
              <DsIcon name="mdi-download" size="sm" />
            </DsButton>
          </template>
        </DsContentCardCompact>
      </DsCard>
    </div>
  </div>
</template>

<script setup lang="ts">
import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import AppLoadingSkeleton from "@/components/shared/AppLoadingSkeleton.vue";
import {
  DsButton,
  DsCard,
  DsContentCardCompact,
  DsIcon,
  DsPageHeader,
  DsSectionHeader
} from "@/components/ds";
import { useAreaColaboradorArquivos } from "@/composables/documento/useAreaColaboradorArquivos";

const { pastas, loading, isEmpty, downloadingId, baixarDocumento } =
  useAreaColaboradorArquivos();
</script>

<style scoped lang="scss">
.area-colaborador-arquivos-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);

  &__pastas {
    display: flex;
    flex-direction: column;
    gap: var(--spacing-md, 16px);
  }
}
</style>
