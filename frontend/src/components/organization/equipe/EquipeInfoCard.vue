<template>
  <DsCard class="equipe-info-card" :title="equipe.name">
    <template #header>
      <div class="equipe-info-card__header">
        <div class="ds-text-card-title">{{ equipe.name }}</div>
        <DsBadge
          role="status"
          :aria-label="statusLabel"
          :label="statusLabel"
          :variant="statusVariant"
        />
      </div>
    </template>

    <dl class="equipe-info-card__grid">
      <div class="equipe-info-card__item">
        <dt>{{ $t("equipe.detail.fields.id") }}</dt>
        <dd>{{ equipe.id }}</dd>
      </div>
      <div class="equipe-info-card__item">
        <dt>{{ $t("equipe.form.areaId") }}</dt>
        <dd>{{ equipe.areaId }}</dd>
      </div>
      <div class="equipe-info-card__item">
        <dt>{{ $t("equipe.form.name") }}</dt>
        <dd>{{ equipe.name }}</dd>
      </div>
      <div class="equipe-info-card__item">
        <dt>{{ $t("equipe.form.description") }}</dt>
        <dd>{{ descriptionLabel }}</dd>
      </div>
      <div class="equipe-info-card__item">
        <dt>{{ $t("equipe.form.leaderId") }}</dt>
        <dd>{{ leaderLabel }}</dd>
      </div>
      <div class="equipe-info-card__item">
        <dt>{{ $t("equipe.detail.fields.createdAt") }}</dt>
        <dd>{{ formatDateTime(equipe.createdAt) }}</dd>
      </div>
      <div class="equipe-info-card__item">
        <dt>{{ $t("equipe.detail.fields.updatedAt") }}</dt>
        <dd>{{ formatOptionalDateTime(equipe.updatedAt) }}</dd>
      </div>
    </dl>

    <template v-if="$slots.actions" #actions>
      <slot name="actions" />
    </template>
  </DsCard>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useI18n } from "vue-i18n";

import { DsBadge, DsCard } from "@/components/ds";
import type { EquipeResponse } from "@/types/organization/equipe.types";

const props = defineProps<{
  equipe: EquipeResponse;
}>();

const { t } = useI18n();

const statusVariant = computed(() =>
  props.equipe.status === "ACTIVE" ? "positive" : "negative"
);

const statusLabel = computed(() => t(`equipe.status.${props.equipe.status}`));

const descriptionLabel = computed(() => {
  if (!props.equipe.description) {
    return t("equipe.detail.fields.notAvailable");
  }

  return props.equipe.description;
});

const leaderLabel = computed(() => {
  if (props.equipe.leaderId === null) {
    return t("equipe.detail.fields.notAvailable");
  }

  return String(props.equipe.leaderId);
});

function formatDateTime(value: string): string {
  return new Intl.DateTimeFormat("pt-BR", {
    dateStyle: "short",
    timeStyle: "short"
  }).format(new Date(value));
}

function formatOptionalDateTime(value: string | null): string {
  if (!value) {
    return t("equipe.detail.fields.notAvailable");
  }

  return formatDateTime(value);
}
</script>

<style scoped lang="scss">
.equipe-info-card {
  &__header {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: var(--spacing-md, 16px);
  }

  &__grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
    gap: var(--spacing-md, 16px);
    margin: 0;
  }

  &__item {
    dt {
      color: var(--color-text-secondary);
      font-size: var(--font-size-sm);
      margin-bottom: var(--spacing-2xs, 4px);
    }

    dd {
      margin: 0;
      font-size: var(--font-size-base);
      color: var(--color-text-primary);
    }
  }
}
</style>
