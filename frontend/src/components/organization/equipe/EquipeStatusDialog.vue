<template>
  <DsDialog
    v-model="open"
    :title="dialogCopy.title"
    :subtitle="dialogCopy.subtitle"
    persistent
    min-width="480px"
  >
    <p class="equipe-status-dialog__message">
      {{ dialogCopy.message }}
    </p>

    <template #actions>
      <DsButton variant="ghost" :disable="loading" @click="close">
        {{ $t("equipe.statusDialog.cancel") }}
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
  isEquipeDeactivation,
  resolveTargetEquipeStatus
} from "@/composables/organization/equipe-status";
import type {
  EquipeResponse,
  EquipeStatus
} from "@/types/organization/equipe.types";

const props = withDefaults(
  defineProps<{
    equipe: EquipeResponse;
    loading?: boolean;
  }>(),
  {
    loading: false
  }
);

const emit = defineEmits<{
  confirm: [status: EquipeStatus];
}>();

const open = defineModel<boolean>({ default: false });

const { t } = useI18n();

const isDeactivation = computed(() =>
  isEquipeDeactivation(props.equipe.status)
);

const targetStatus = computed(() =>
  resolveTargetEquipeStatus(props.equipe.status)
);

const confirmVariant = computed(() =>
  isDeactivation.value ? "danger" : "primary"
);

const dialogCopy = computed(() => {
  const key = isDeactivation.value ? "deactivate" : "activate";

  return {
    title: t(`equipe.statusDialog.${key}.title`),
    subtitle: t(`equipe.statusDialog.${key}.subtitle`, {
      name: props.equipe.name
    }),
    message: t(`equipe.statusDialog.${key}.message`),
    confirm: t(`equipe.statusDialog.${key}.confirm`)
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
.equipe-status-dialog__message {
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
  line-height: 1.5;
  margin: 0;
}
</style>
