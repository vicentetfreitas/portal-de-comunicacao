<template>
  <DsDialog
    v-model="open"
    :title="dialogCopy.title"
    :subtitle="dialogCopy.subtitle"
    persistent
    min-width="480px"
  >
    <p class="singular-status-dialog__message">
      {{ dialogCopy.message }}
    </p>

    <template #actions>
      <DsButton variant="ghost" :disable="loading" @click="close">
        {{ $t("singular.statusDialog.cancel") }}
      </DsButton>
      <DsButton :variant="confirmVariant" :loading="loading" @click="onConfirm">
        {{ dialogCopy.confirm }}
      </DsButton>
    </template>
  </DsDialog>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useI18n } from "vue-i18n";

import { DsButton, DsDialog } from "@/components/ds";
import {
  isSingularDeactivation,
  resolveTargetSingularStatus
} from "@/composables/organization/singular-status";
import type {
  SingularResponse,
  SingularStatus
} from "@/types/organization/singular.types";

const props = withDefaults(
  defineProps<{
    singular: SingularResponse;
    loading?: boolean;
  }>(),
  {
    loading: false
  }
);

const emit = defineEmits<{
  confirm: [status: SingularStatus];
}>();

const open = defineModel<boolean>({ default: false });

const { t } = useI18n();

const isDeactivation = computed(() =>
  isSingularDeactivation(props.singular.status)
);

const targetStatus = computed(() =>
  resolveTargetSingularStatus(props.singular.status)
);

const confirmVariant = computed(() =>
  isDeactivation.value ? "danger" : "primary"
);

const dialogCopy = computed(() => {
  const key = isDeactivation.value ? "deactivate" : "activate";

  return {
    title: t(`singular.statusDialog.${key}.title`),
    subtitle: t(`singular.statusDialog.${key}.subtitle`, {
      name: props.singular.name
    }),
    message: t(`singular.statusDialog.${key}.message`),
    confirm: t(`singular.statusDialog.${key}.confirm`)
  };
});

function close(): void {
  open.value = false;
}

function onConfirm(): void {
  emit("confirm", targetStatus.value);
}
</script>

<style scoped lang="scss">
.singular-status-dialog__message {
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
  line-height: 1.5;
  margin: 0;
}
</style>
