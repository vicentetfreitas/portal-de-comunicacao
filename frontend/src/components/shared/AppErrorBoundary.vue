<template>
  <slot v-if="!capturedError" />
  <AppEmptyState
    v-else
    :title="title"
    :description="description"
    icon="mdi-alert-circle-outline"
  >
    <DsButton v-if="showRetry" variant="outline" size="sm" @click="reset">
      {{ retryLabel }}
    </DsButton>
  </AppEmptyState>
</template>

<script setup lang="ts">
import { onErrorCaptured, ref, watch } from "vue";

import { DsButton } from "@/components/ds";

import AppEmptyState from "./AppEmptyState.vue";

const props = withDefaults(
  defineProps<{
    title?: string;
    description?: string;
    retryLabel?: string;
    showRetry?: boolean;
    resetKey?: string | number;
  }>(),
  {
    title: "Algo deu errado",
    description: "Não foi possível renderizar este conteúdo.",
    retryLabel: "Tentar novamente",
    showRetry: true
  }
);

const emit = defineEmits<{
  error: [error: Error];
  reset: [];
}>();

const capturedError = ref<Error | null>(null);

onErrorCaptured(error => {
  capturedError.value =
    error instanceof Error ? error : new Error(String(error));
  emit("error", capturedError.value);
  return false;
});

watch(
  () => props.resetKey,
  () => {
    capturedError.value = null;
  }
);

function reset(): void {
  capturedError.value = null;
  emit("reset");
}
</script>
