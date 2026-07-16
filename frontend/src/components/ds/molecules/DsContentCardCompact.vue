<template>
  <article
    class="ds-content-card-compact"
    :class="{ 'ds-content-card-compact--clickable': clickable }"
  >
    <div v-if="icon" class="ds-content-card-compact__icon">
      <DsIcon :name="icon" size="md" />
    </div>
    <div v-else-if="imageSrc" class="ds-content-card-compact__thumb">
      <img :src="imageSrc" :alt="imageAlt" />
    </div>
    <div class="ds-content-card-compact__body">
      <p v-if="meta" class="ds-content-card-compact__meta">{{ meta }}</p>
      <h4 class="ds-content-card-compact__title">
        <slot name="title">{{ title }}</slot>
      </h4>
      <p v-if="description" class="ds-content-card-compact__description">{{
        description
      }}</p>
    </div>
    <div v-if="$slots.trailing" class="ds-content-card-compact__trailing">
      <slot name="trailing" />
    </div>
  </article>
</template>

<script setup lang="ts">
import DsIcon from "../atoms/DsIcon.vue";

withDefaults(
  defineProps<{
    title?: string;
    description?: string;
    meta?: string;
    icon?: string;
    imageSrc?: string;
    imageAlt?: string;
    clickable?: boolean;
  }>(),
  {
    imageAlt: "",
    clickable: false
  }
);
</script>
