<template>
  <DsCard class="singular-info-card" :title="singular.name">
    <template #header>
      <div class="singular-info-card__header">
        <div>
          <div class="ds-text-card-title">{{ singular.name }}</div>
          <div class="singular-info-card__subtitle">{{ singular.acronym }}</div>
        </div>
        <DsBadge
          :label="statusLabel"
          :variant="statusVariant"
        />
      </div>
    </template>

    <dl class="singular-info-card__grid">
      <div class="singular-info-card__item">
        <dt>{{ $t("singular.detail.fields.id") }}</dt>
        <dd>{{ singular.id }}</dd>
      </div>
      <div class="singular-info-card__item">
        <dt>{{ $t("singular.form.federationId") }}</dt>
        <dd>{{ singular.federationId }}</dd>
      </div>
      <div class="singular-info-card__item">
        <dt>{{ $t("singular.form.name") }}</dt>
        <dd>{{ singular.name }}</dd>
      </div>
      <div class="singular-info-card__item">
        <dt>{{ $t("singular.form.acronym") }}</dt>
        <dd>{{ singular.acronym }}</dd>
      </div>
      <div class="singular-info-card__item">
        <dt>{{ $t("singular.form.unimedCode") }}</dt>
        <dd>{{ singular.unimedCode }}</dd>
      </div>
      <div class="singular-info-card__item">
        <dt>{{ $t("singular.detail.fields.createdAt") }}</dt>
        <dd>{{ formatDateTime(singular.createdAt) }}</dd>
      </div>
      <div class="singular-info-card__item">
        <dt>{{ $t("singular.detail.fields.updatedAt") }}</dt>
        <dd>{{ formatOptionalDateTime(singular.updatedAt) }}</dd>
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
import type { SingularResponse, SingularStatus } from "@/types/organization/singular.types";

const props = defineProps<{
  singular: SingularResponse;
}>();

const { t } = useI18n();

const statusVariant = computed(() =>
  props.singular.status === "ACTIVE" ? "positive" : "negative"
);

const statusLabel = computed(() =>
  t(`singular.status.${props.singular.status}`)
);

function formatDateTime(value: string): string {
  return new Intl.DateTimeFormat("pt-BR", {
    dateStyle: "short",
    timeStyle: "short"
  }).format(new Date(value));
}

function formatOptionalDateTime(value: string | null): string {
  if (!value) {
    return t("singular.detail.fields.notAvailable");
  }

  return formatDateTime(value);
}
</script>

<style scoped lang="scss">
.singular-info-card {
  &__header {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: var(--spacing-md, 16px);
  }

  &__subtitle {
    color: var(--color-text-secondary);
    font-size: var(--font-size-sm);
    margin-top: var(--spacing-2xs, 4px);
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
