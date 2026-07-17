<template>
  <div class="singular-list-page">
    <DsPageHeader
      :title="$t('singular.list.title')"
      :subtitle="$t('singular.list.subtitle')"
    >
      <template #actions>
        <DsButton variant="primary" :to="ROUTE_PATHS.SINGULAR_CREATE">
          {{ $t("singular.list.createAction") }}
        </DsButton>
      </template>
    </DsPageHeader>

    <SingularFilters
      :filters="filters"
      @apply="applyFilters"
      @reset="resetFilters"
    />

    <DsCard :title="$t('singular.list.cardTitle')">
      <AppEmptyState
        v-if="!loading && rows.length === 0"
        :title="$t('singular.list.emptyTitle')"
        :description="$t('singular.list.emptyDescription')"
      >
        <DsButton variant="primary" :to="ROUTE_PATHS.SINGULAR_CREATE">
          {{ $t("singular.list.createAction") }}
        </DsButton>
      </AppEmptyState>

      <DsDataTable
        v-else
        :rows="tableRows"
        :columns="columns"
        row-key="id"
        :loading="loading"
        :pagination="pagination"
        :rows-number="pagination.rowsNumber"
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
              {{ $t("singular.list.viewAction") }}
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

import SingularFilters from "@/components/organization/singular/SingularFilters.vue";
import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import {
  DsBadge,
  DsButton,
  DsCard,
  DsDataTable,
  DsPageHeader
} from "@/components/ds";
import type { DsTableColumn } from "@/components/ds";
import { useSingularList } from "@/composables/organization/useSingularList";
import { ROUTE_PATHS, singularDetailPath } from "@/constants/routes";
import type { SingularStatus } from "@/types/organization/singular.types";

const { t } = useI18n();
const {
  rows,
  loading,
  filters,
  pagination,
  fetchPage,
  applyFilters,
  resetFilters,
  onTableRequest
} = useSingularList();

const columns = computed<DsTableColumn[]>(() => [
  {
    name: "name",
    label: t("singular.form.name"),
    field: "name",
    align: "left",
    sortable: true
  },
  {
    name: "acronym",
    label: t("singular.form.acronym"),
    field: "acronym",
    align: "left",
    sortable: true
  },
  {
    name: "unimedCode",
    label: t("singular.form.unimedCode"),
    field: "unimedCode",
    align: "left",
    sortable: true
  },
  {
    name: "status",
    label: t("singular.list.columns.status"),
    field: "status",
    align: "left"
  },
  {
    name: "actions",
    label: t("singular.list.columns.actions"),
    field: "actions",
    align: "right"
  }
]);

const tableRows = computed(
  () => rows.value as unknown as Record<string, unknown>[]
);

function rowStatus(row: Record<string, unknown>): SingularStatus {
  return row.status as SingularStatus;
}

function rowId(row: Record<string, unknown>): number {
  return Number(row.id);
}

function statusLabel(status: SingularStatus): string {
  return t(`singular.status.${status}`);
}

function statusVariant(status: SingularStatus): "positive" | "negative" {
  return status === "ACTIVE" ? "positive" : "negative";
}

function detailRoute(id: number) {
  return singularDetailPath(id);
}

onMounted(() => {
  void fetchPage();
});
</script>

<style scoped lang="scss">
.singular-list-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);
}
</style>
