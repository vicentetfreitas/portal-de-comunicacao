<template>
  <div class="equipe-list-page">
    <DsPageHeader
      :title="$t('equipe.list.title')"
      :subtitle="$t('equipe.list.subtitle')"
    >
      <template #actions>
        <DsButton variant="primary" :to="ROUTE_PATHS.EQUIPE_CREATE">
          {{ $t("equipe.list.createAction") }}
        </DsButton>
      </template>
    </DsPageHeader>

    <EquipeFilters
      :filters="filters"
      :area-options="areaOptions"
      :loading-areas="loadingAreas"
      @apply="applyFilters"
      @reset="resetFilters"
    />

    <DsCard :title="$t('equipe.list.cardTitle')">
      <AppEmptyState
        v-if="!loading && rows.length === 0"
        :title="$t('equipe.list.emptyTitle')"
        :description="$t('equipe.list.emptyDescription')"
      >
        <DsButton variant="primary" :to="ROUTE_PATHS.EQUIPE_CREATE">
          {{ $t("equipe.list.createAction") }}
        </DsButton>
      </AppEmptyState>

      <DsDataTable
        v-else
        v-model:pagination="pagination"
        :rows="tableRows"
        :columns="columns"
        row-key="id"
        :loading="loading"
        binary-state-sort
        @request="onTableRequest"
      >
        <template #body-cell-status="slotProps">
          <q-td :props="slotProps">
            <DsBadge
              :label="statusLabel(rowStatus(slotProps.row))"
              :variant="statusVariant(rowStatus(slotProps.row))"
            />
          </q-td>
        </template>

        <template #body-cell-actions="slotProps">
          <q-td :props="slotProps">
            <DsButton variant="link" :to="detailRoute(rowId(slotProps.row))">
              {{ $t("equipe.list.viewAction") }}
            </DsButton>
          </q-td>
        </template>
      </DsDataTable>
    </DsCard>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from "vue";
import { useI18n } from "vue-i18n";

import EquipeFilters from "@/components/organization/equipe/EquipeFilters.vue";
import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import {
  DsBadge,
  DsButton,
  DsCard,
  DsDataTable,
  DsPageHeader
} from "@/components/ds";
import type { DsTableColumn } from "@/components/ds";
import { useEquipeAreaOptions } from "@/composables/organization/useEquipeAreaOptions";
import { useEquipeList } from "@/composables/organization/useEquipeList";
import { ROUTE_PATHS, equipeDetailPath } from "@/constants/routes";
import type { EquipeStatus } from "@/types/organization/equipe.types";

const { t } = useI18n();
const { areaOptions, loadingAreas } = useEquipeAreaOptions();
const {
  rows,
  loading,
  filters,
  pagination,
  applyFilters,
  resetFilters,
  onTableRequest
} = useEquipeList();

const columns = computed<DsTableColumn[]>(() => [
  {
    name: "name",
    label: t("equipe.form.name"),
    field: "name",
    align: "left",
    sortable: true
  },
  {
    name: "areaId",
    label: t("equipe.form.areaId"),
    field: "areaId",
    align: "left",
    sortable: true
  },
  {
    name: "status",
    label: t("equipe.list.columns.status"),
    field: "status",
    align: "left"
  },
  {
    name: "actions",
    label: t("equipe.list.columns.actions"),
    field: "actions",
    align: "right"
  }
]);

const tableRows = computed(
  () => rows.value as unknown as Record<string, unknown>[]
);

function rowStatus(row: Record<string, unknown>): EquipeStatus {
  return row.status as EquipeStatus;
}

function rowId(row: Record<string, unknown>): number {
  return Number(row.id);
}

function statusLabel(status: EquipeStatus): string {
  return t(`equipe.status.${status}`);
}

function statusVariant(status: EquipeStatus): "positive" | "negative" {
  return status === "ACTIVE" ? "positive" : "negative";
}

function detailRoute(id: number) {
  return equipeDetailPath(id);
}

onMounted(() => {
  void onTableRequest({ pagination: pagination.value });
});
</script>

<style scoped lang="scss">
.equipe-list-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);
}
</style>
