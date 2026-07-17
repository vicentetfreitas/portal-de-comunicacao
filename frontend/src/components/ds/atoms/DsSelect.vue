<template>
  <q-select
    v-model="model"
    class="ds-select"
    :options="options"
    :label="label"
    :placeholder="placeholder"
    :disable="disable"
    :readonly="readonly"
    :error="!!error"
    :error-message="error"
    :hint="hint"
    :dense="dense"
    :multiple="multiple"
    :use-chips="multiple"
    emit-value
    map-options
    outlined
    v-bind="$attrs"
  />
</template>

<script setup lang="ts">
import type { DsSelectOption } from "../types";

withDefaults(
  defineProps<{
    label?: string | undefined;
    placeholder?: string | undefined;
    options: DsSelectOption[];
    disable?: boolean;
    readonly?: boolean;
    error?: string | undefined;
    hint?: string | undefined;
    dense?: boolean;
    multiple?: boolean;
  }>(),
  {
    disable: false,
    readonly: false,
    dense: false,
    multiple: false
  }
);

const model = defineModel<string | string[] | null>({ default: null });
</script>
