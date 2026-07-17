<template>
  <q-table
    class="ds-data-table"
    :title="title"
    :rows="rows"
    :columns="columns"
    :row-key="rowKey"
    :loading="loading"
    v-model:pagination="pagination"
    flat
    bordered
    v-bind="tableAttrs"
  >
    <template v-for="(_, slotName) in $slots" #[slotName]="slotProps">
      <slot :name="slotName" v-bind="slotProps ?? {}" />
    </template>
  </q-table>
</template>

<script setup lang="ts">
import { computed, useAttrs } from "vue";

import type { DsTableColumn } from "../types";

withDefaults(
  defineProps<{
    title?: string;
    rows: Record<string, unknown>[];
    columns: DsTableColumn[];
    rowKey?: string;
    loading?: boolean;
  }>(),
  {
    rowKey: "id",
    loading: false
  }
);

const pagination = defineModel<{
  sortBy?: string;
  descending?: boolean;
  page: number;
  rowsPerPage: number;
  rowsNumber: number;
}>("pagination", {
  default: () => ({
    page: 1,
    rowsPerPage: 10,
    rowsNumber: 0
  })
});

const attrs = useAttrs();
const tableAttrs = computed(() => {
  const { pagination: _pagination, ...rest } = attrs;
  return rest;
});
</script>
