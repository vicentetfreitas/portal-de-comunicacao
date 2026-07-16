<template>
  <q-table
    class="ds-data-table"
    :title="title"
    :rows="rows"
    :columns="columns"
    :row-key="rowKey"
    :loading="loading"
    :pagination="pagination"
    flat
    bordered
    v-bind="$attrs"
  >
    <template v-for="(_, slotName) in $slots" #[slotName]="slotProps">
      <slot :name="slotName" v-bind="slotProps ?? {}" />
    </template>
  </q-table>
</template>

<script setup lang="ts">
import type { DsTableColumn } from "../types";

withDefaults(
  defineProps<{
    title?: string;
    rows: Record<string, unknown>[];
    columns: DsTableColumn[];
    rowKey?: string;
    loading?: boolean;
    pagination?: { rowsPerPage: number };
  }>(),
  {
    rowKey: "id",
    loading: false,
    pagination: () => ({ rowsPerPage: 10 })
  }
);
</script>
