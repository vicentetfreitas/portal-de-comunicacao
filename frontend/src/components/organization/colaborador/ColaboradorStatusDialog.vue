<template>
  <DsDialog
    v-model="open"
    :title="dialogCopy.title"
    :subtitle="dialogCopy.subtitle"
    persistent
    min-width="480px"
  >
    <p class="colaborador-status-dialog__message">
      {{ dialogCopy.message }}
    </p>

    <template #actions>
      <DsButton variant="ghost" :disable="loading" @click="close">
        {{ $t("colaborador.statusDialog.cancel") }}
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
  isColaboradorDeactivation,
  resolveTargetColaboradorStatus
} from "@/composables/organization/colaborador-status";
import type {
  ColaboradorResponse,
  ColaboradorStatus
} from "@/types/organization/colaborador.types";

const props = withDefaults(
  defineProps<{
    colaborador: ColaboradorResponse;
    loading?: boolean;
  }>(),
  {
    loading: false
  }
);

const emit = defineEmits<{
  confirm: [status: ColaboradorStatus];
}>();

const open = defineModel<boolean>({ default: false });

const { t } = useI18n();

const isDeactivation = computed(() =>
  isColaboradorDeactivation(props.colaborador.status)
);

const targetStatus = computed(() =>
  resolveTargetColaboradorStatus(props.colaborador.status)
);

const confirmVariant = computed(() =>
  isDeactivation.value ? "danger" : "primary"
);

const dialogCopy = computed(() => {
  const key = isDeactivation.value ? "deactivate" : "activate";

  return {
    title: t(`colaborador.statusDialog.${key}.title`),
    subtitle: t(`colaborador.statusDialog.${key}.subtitle`, {
      name: props.colaborador.name
    }),
    message: t(`colaborador.statusDialog.${key}.message`),
    confirm: t(`colaborador.statusDialog.${key}.confirm`)
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
.colaborador-status-dialog__message {
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
  line-height: 1.5;
  margin: 0;
}
</style>
