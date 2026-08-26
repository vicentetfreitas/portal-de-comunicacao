<template>
  <header
    class="ds-page-header row items-center justify-between q-col-gutter-md"
  >
    <div class="col ds-page-header__heading">
      <DsButton
        v-if="showBack"
        variant="ghost"
        size="sm"
        class="ds-page-header__back"
        :aria-label="backLabel"
        @click="$emit('back')"
      >
        <DsIcon name="mdi-arrow-left" size="md" />
      </DsButton>
      <div>
        <h1 class="ds-page-header__title">{{ title }}</h1>
        <p v-if="subtitle" class="ds-page-header__subtitle">{{ subtitle }}</p>
      </div>
    </div>
    <div v-if="$slots.actions" class="col-auto ds-page-header__actions">
      <slot name="actions" />
    </div>
  </header>
</template>

<script setup lang="ts">
import DsButton from "../atoms/DsButton.vue";
import DsIcon from "../atoms/DsIcon.vue";

withDefaults(
  defineProps<{
    title: string;
    subtitle?: string;
    showBack?: boolean;
    backLabel?: string;
  }>(),
  {
    showBack: false,
    backLabel: "Voltar"
  }
);

defineEmits<{
  back: [];
}>();
</script>
