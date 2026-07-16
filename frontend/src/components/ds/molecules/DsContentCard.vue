<template>
  <article class="ds-content-card" :class="`ds-content-card--${variant}`">
    <div v-if="imageSrc || $slots.media" class="ds-content-card__media">
      <slot name="media">
        <img :src="imageSrc" :alt="imageAlt" class="ds-content-card__image" />
      </slot>
    </div>
    <div class="ds-content-card__body">
      <p v-if="meta" class="ds-content-card__meta">{{ meta }}</p>
      <h3 class="ds-content-card__title">
        <slot name="title">{{ title }}</slot>
      </h3>
      <p
        v-if="description || $slots.description"
        class="ds-content-card__description"
      >
        <slot name="description">{{ description }}</slot>
      </p>
      <div v-if="$slots.footer" class="ds-content-card__footer">
        <slot name="footer" />
      </div>
    </div>
    <div v-if="$slots.actions" class="ds-content-card__actions">
      <slot name="actions" />
    </div>
  </article>
</template>

<script setup lang="ts">
import type { DsCardVariant } from "../types";

withDefaults(
  defineProps<{
    title?: string;
    description?: string;
    meta?: string;
    imageSrc?: string;
    imageAlt?: string;
    variant?: DsCardVariant;
  }>(),
  {
    variant: "outlined",
    imageAlt: ""
  }
);
</script>
