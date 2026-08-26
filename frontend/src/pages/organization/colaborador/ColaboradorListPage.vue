<template>
  <div class="colaborador-list-page">
    <DsPageHeader
      :title="$t('colaborador.list.title')"
      :subtitle="$t('colaborador.list.subtitle')"
    >
      <template #actions>
        <DsButton variant="primary" :to="ROUTE_PATHS.COLABORADOR_CREATE">
          {{ $t("colaborador.list.createAction") }}
        </DsButton>
      </template>
    </DsPageHeader>

    <ColaboradorFilters
      :filters="filters"
      :singular-options="singularOptions"
      :area-options="areaOptions"
      :team-options="teamOptions"
      :loading-singulares="loadingSingulares"
      :loading-areas="loadingAreas"
      :loading-teams="loadingTeams"
      @apply="applyFilters"
      @reset="resetFilters"
    />

    <DsCard :title="$t('colaborador.list.cardTitle')">
      <AppEmptyState
        v-if="!loading && rows.length === 0"
        :title="$t('colaborador.list.emptyTitle')"
        :description="$t('colaborador.list.emptyDescription')"
      >
        <DsButton variant="primary" :to="ROUTE_PATHS.COLABORADOR_CREATE">
          {{ $t("colaborador.list.createAction") }}
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
              {{ $t("colaborador.list.viewAction") }}
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

import ColaboradorFilters from "@/components/organization/colaborador/ColaboradorFilters.vue";
import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import {
  DsBadge,
  DsButton,
  DsCard,
  DsDataTable,
  DsPageHeader
} from "@/components/ds";
import type { DsTableColumn } from "@/components/ds";
import { useColaboradorFilterOptions } from "@/composables/organization/useColaboradorFilterOptions";
import { useColaboradorList } from "@/composables/organization/useColaboradorList";
import { ROUTE_PATHS, colaboradorDetailPath } from "@/constants/routes";
import type { ColaboradorStatus } from "@/types/organization/colaborador.types";

const { t } = useI18n();
const {
  singularOptions,
  areaOptions,
  teamOptions,
  loadingSingulares,
  loadingAreas,
  loadingTeams
} = useColaboradorFilterOptions();
const {
  rows,
  loading,
  filters,
  pagination,
  applyFilters,
  resetFilters,
  onTableRequest
} = useColaboradorList();

const columns = computed<DsTableColumn[]>(() => [
  {
    name: "name",
    label: t("colaborador.form.name"),
    field: "name",
    align: "left",
    sortable: true
  },
  {
    name: "email",
    label: t("colaborador.form.email"),
    field: "email",
    align: "left",
    sortable: true
  },
  {
    name: "singularId",
    label: t("colaborador.form.singularId"),
    field: "singularId",
    align: "left"
  },
  {
    name: "areaId",
    label: t("colaborador.form.areaId"),
    field: "areaId",
    align: "left"
  },
  {
    name: "teamId",
    label: t("colaborador.form.teamId"),
    field: "teamId",
    align: "left"
  },
  {
    name: "status",
    label: t("colaborador.list.columns.status"),
    field: "status",
    align: "left"
  },
  {
    name: "actions",
    label: t("colaborador.list.columns.actions"),
    field: "actions",
    align: "right"
  }
]);

const tableRows = computed(
  () => rows.value as unknown as Record<string, unknown>[]
);

function rowStatus(row: Record<string, unknown>): ColaboradorStatus {
  return row.status as ColaboradorStatus;
}

function rowId(row: Record<string, unknown>): number {
  return Number(row.id);
}

function statusLabel(status: ColaboradorStatus): string {
  return t(`colaborador.status.${status}`);
}

function statusVariant(status: ColaboradorStatus): "positive" | "negative" {
  return status === "ACTIVE" ? "positive" : "negative";
}

function detailRoute(id: number) {
  return colaboradorDetailPath(id);
}

onMounted(() => {
  void onTableRequest({ pagination: pagination.value });
});
</script>

<style scoped lang="scss">
.colaborador-list-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);
}
</style>
