<template>
  <DsInput
    v-model="model"
    class="ds-search-input"
    type="search"
    :placeholder="placeholder"
    :disable="disable"
    :dense="dense"
    clearable
    v-bind="inputAttrs"
  >
    <template #prepend>
      <DsIcon name="mdi-magnify" size="sm" />
    </template>
  </DsInput>
</template>

<script setup lang="ts">
import { computed, useAttrs } from "vue";

import DsIcon from "../atoms/DsIcon.vue";
import DsInput from "../atoms/DsInput.vue";

const props = withDefaults(
  defineProps<{
    label?: string;
    placeholder?: string;
    disable?: boolean;
    dense?: boolean;
  }>(),
  {
    placeholder: "Buscar...",
    dense: true
  }
);

const attrs = useAttrs();

const inputAttrs = computed(() => {
  const merged = { ...attrs } as Record<string, unknown>;
  if (props.label !== undefined) {
    merged.label = props.label;
  }
  return merged;
});

const model = defineModel<string | null>({ default: "" });
</script>
