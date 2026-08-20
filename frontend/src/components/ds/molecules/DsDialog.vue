<template>
  <q-dialog
    v-model="model"
    :persistent="persistent"
    :maximized="maximized"
    v-bind="$attrs"
  >
    <q-card class="ds-card ds-dialog" :style="{ minWidth }">
      <q-card-section class="row items-center q-pb-none">
        <div class="text-h6">{{ title }}</div>
        <q-space />
        <q-btn
          v-close-popup
          icon="mdi-close"
          flat
          round
          dense
          :aria-label="$t('common.close')"
        />
      </q-card-section>

      <q-card-section v-if="subtitle" class="q-pt-sm">
        <div class="ds-dialog__subtitle">{{ subtitle }}</div>
      </q-card-section>

      <q-card-section>
        <slot />
      </q-card-section>

      <q-card-actions v-if="$slots.actions" align="right">
        <slot name="actions" />
      </q-card-actions>
    </q-card>
  </q-dialog>
</template>

<script setup lang="ts">
withDefaults(
  defineProps<{
    title: string;
    subtitle?: string;
    persistent?: boolean;
    maximized?: boolean;
    minWidth?: string;
  }>(),
  {
    persistent: false,
    maximized: false,
    minWidth: "400px"
  }
);

const model = defineModel<boolean>({ default: false });
</script>

<style scoped lang="scss">
.ds-dialog {
  border-radius: var(--radius-lg);
  box-shadow: var(--elevation-modal);
}
</style>
