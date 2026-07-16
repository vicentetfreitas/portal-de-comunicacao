<template>
  <q-input
    v-model="model"
    class="ds-input"
    :class="[`ds-input--${variant}`]"
    :label="label"
    :placeholder="placeholder"
    :type="type"
    :disable="disable"
    :readonly="readonly"
    :error="!!error"
    :error-message="error"
    :hint="hint"
    :dense="dense"
    :outlined="variant === 'outlined'"
    :filled="variant === 'filled'"
    :standout="variant === 'standard'"
    v-bind="$attrs"
  >
    <template v-if="$slots.prepend" #prepend>
      <slot name="prepend" />
    </template>
    <template v-if="$slots.append" #append>
      <slot name="append" />
    </template>
  </q-input>
</template>

<script setup lang="ts">
import type { DsInputVariant } from "../types";

withDefaults(
  defineProps<{
    label?: string;
    placeholder?: string;
    type?: "text" | "password" | "email" | "number" | "search" | "tel" | "url";
    disable?: boolean;
    readonly?: boolean;
    error?: string;
    hint?: string;
    dense?: boolean;
    variant?: DsInputVariant;
  }>(),
  {
    type: "text",
    disable: false,
    readonly: false,
    dense: false,
    variant: "outlined"
  }
);

const model = defineModel<string | number | null>({ default: "" });
</script>
