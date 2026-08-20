<template>
  <DsCard class="colaborador-info-card" :title="colaborador.name">
    <template #header>
      <div class="colaborador-info-card__header">
        <div class="ds-text-card-title">{{ colaborador.name }}</div>
        <DsBadge
          role="status"
          :aria-label="statusLabel"
          :label="statusLabel"
          :variant="statusVariant"
        />
      </div>
    </template>

    <dl class="colaborador-info-card__grid">
      <div class="colaborador-info-card__item">
        <dt>{{ $t("colaborador.detail.fields.id") }}</dt>
        <dd>{{ colaborador.id }}</dd>
      </div>
      <div class="colaborador-info-card__item">
        <dt>{{ $t("colaborador.detail.fields.email") }}</dt>
        <dd>{{ colaborador.email }}</dd>
      </div>
      <div class="colaborador-info-card__item">
        <dt>{{ $t("colaborador.detail.fields.zimbraId") }}</dt>
        <dd>{{ colaborador.zimbraId }}</dd>
      </div>
      <div class="colaborador-info-card__item">
        <dt>{{ $t("colaborador.detail.fields.singularId") }}</dt>
        <dd>{{ optionalId(colaborador.singularId) }}</dd>
      </div>
      <div class="colaborador-info-card__item">
        <dt>{{ $t("colaborador.detail.fields.areaId") }}</dt>
        <dd>{{ optionalId(colaborador.areaId) }}</dd>
      </div>
      <div class="colaborador-info-card__item">
        <dt>{{ $t("colaborador.detail.fields.teamId") }}</dt>
        <dd>{{ optionalId(colaborador.teamId) }}</dd>
      </div>
      <div class="colaborador-info-card__item">
        <dt>{{ $t("colaborador.detail.fields.managerId") }}</dt>
        <dd>{{ optionalId(colaborador.managerId) }}</dd>
      </div>
      <div class="colaborador-info-card__item">
        <dt>{{ $t("colaborador.detail.fields.biography") }}</dt>
        <dd>{{ biographyLabel }}</dd>
      </div>
      <div class="colaborador-info-card__item">
        <dt>{{ $t("colaborador.detail.fields.birthDate") }}</dt>
        <dd>{{ formatOptionalDate(colaborador.birthDate) }}</dd>
      </div>
      <div class="colaborador-info-card__item">
        <dt>{{ $t("colaborador.detail.fields.hireDate") }}</dt>
        <dd>{{ formatOptionalDate(colaborador.hireDate) }}</dd>
      </div>
      <div class="colaborador-info-card__item">
        <dt>{{ $t("colaborador.detail.fields.lastAccessAt") }}</dt>
        <dd>{{ formatOptionalDateTime(colaborador.lastAccessAt) }}</dd>
      </div>
      <div class="colaborador-info-card__item">
        <dt>{{ $t("colaborador.detail.fields.createdAt") }}</dt>
        <dd>{{ formatDateTime(colaborador.createdAt) }}</dd>
      </div>
      <div class="colaborador-info-card__item">
        <dt>{{ $t("colaborador.detail.fields.updatedAt") }}</dt>
        <dd>{{ formatOptionalDateTime(colaborador.updatedAt) }}</dd>
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
import type { ColaboradorResponse } from "@/types/organization/colaborador.types";

const props = defineProps<{
  colaborador: ColaboradorResponse;
}>();

const { t } = useI18n();

const statusVariant = computed(() =>
  props.colaborador.status === "ACTIVE" ? "positive" : "negative"
);

const statusLabel = computed(() =>
  t(`colaborador.status.${props.colaborador.status}`)
);

const biographyLabel = computed(() => {
  if (!props.colaborador.biography) {
    return t("colaborador.detail.fields.notAvailable");
  }

  return props.colaborador.biography;
});

function optionalId(value: number | null): string {
  if (value === null) {
    return t("colaborador.detail.fields.notAvailable");
  }

  return String(value);
}

function formatDateTime(value: string): string {
  return new Intl.DateTimeFormat("pt-BR", {
    dateStyle: "short",
    timeStyle: "short"
  }).format(new Date(value));
}

function formatOptionalDateTime(value: string | null): string {
  if (!value) {
    return t("colaborador.detail.fields.notAvailable");
  }

  return formatDateTime(value);
}

function formatOptionalDate(value: string | null): string {
  if (!value) {
    return t("colaborador.detail.fields.notAvailable");
  }

  return new Intl.DateTimeFormat("pt-BR", { dateStyle: "short" }).format(
    new Date(value)
  );
}
</script>

<style scoped lang="scss">
.colaborador-info-card {
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
