<template>
  <DsCard class="equipe-filters" :title="$t('equipe.list.filters.title')">
    <div class="equipe-filters__grid">
      <DsSelect
        v-model="statusModel"
        :label="$t('equipe.list.filters.status')"
        :options="statusOptions"
        clearable
      />

      <DsSelect
        v-model="areaIdModel"
        :label="$t('equipe.form.areaId')"
        :options="areaOptions"
        :disable="loadingAreas"
        clearable
      />

      <DsInput v-model="filters.name" :label="$t('equipe.form.name')" />
    </div>

    <template #actions>
      <DsButton variant="ghost" @click="emit('reset')">
        {{ $t("equipe.list.filters.clear") }}
      </DsButton>
      <DsButton variant="primary" @click="emit('apply')">
        {{ $t("equipe.list.filters.apply") }}
      </DsButton>
    </template>
  </DsCard>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useI18n } from "vue-i18n";

import {
  DsButton,
  DsCard,
  DsInput,
  DsSelect,
  type DsSelectOption
} from "@/components/ds";
import type { EquipeListFilters } from "@/composables/organization/useEquipeList";
import type { EquipeStatus } from "@/types/organization/equipe.types";

const props = defineProps<{
  filters: EquipeListFilters;
  areaOptions: DsSelectOption<string>[];
  loadingAreas?: boolean;
}>();

const emit = defineEmits<{
  apply: [];
  reset: [];
}>();

const { t } = useI18n();

const statusOptions = computed(() => [
  { label: t("equipe.status.ACTIVE"), value: "ACTIVE" as EquipeStatus },
  { label: t("equipe.status.INACTIVE"), value: "INACTIVE" as EquipeStatus }
]);

const statusModel = computed({
  get: () => props.filters.status,
  set: (value: EquipeStatus | null) => {
    props.filters.status = value;
  }
});

const areaIdModel = computed({
  get: () =>
    props.filters.areaId === null ? null : String(props.filters.areaId),
  set: (value: string | null) => {
    if (value === null || value === "") {
      props.filters.areaId = null;
      return;
    }

    props.filters.areaId = Number(value);
  }
});
</script>

<style scoped lang="scss">
.equipe-filters {
  &__grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
    gap: var(--spacing-md, 16px);
  }
}
</style>
