<template>
  <DsCard class="singular-filters" :title="$t('singular.list.filters.title')">
    <div class="singular-filters__grid">
      <DsSelect
        v-model="statusModel"
        :label="$t('singular.list.filters.status')"
        :options="statusOptions"
        clearable
      />

      <DsInput
        v-model="federationIdModel"
        type="number"
        :label="$t('singular.form.federationId')"
      />

      <DsInput v-model="filters.name" :label="$t('singular.form.name')" />

      <DsInput v-model="filters.acronym" :label="$t('singular.form.acronym')" />

      <DsInput
        v-model="filters.unimedCode"
        :label="$t('singular.form.unimedCode')"
      />
    </div>

    <template #actions>
      <DsButton variant="ghost" @click="emit('reset')">
        {{ $t("singular.list.filters.clear") }}
      </DsButton>
      <DsButton variant="primary" @click="emit('apply')">
        {{ $t("singular.list.filters.apply") }}
      </DsButton>
    </template>
  </DsCard>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useI18n } from "vue-i18n";

import { DsButton, DsCard, DsInput, DsSelect } from "@/components/ds";
import type { SingularListFilters } from "@/composables/organization/useSingularList";
import type { SingularStatus } from "@/types/organization/singular.types";

const props = defineProps<{
  filters: SingularListFilters;
}>();

const emit = defineEmits<{
  apply: [];
  reset: [];
}>();

const { t } = useI18n();

const statusOptions = computed(() => [
  { label: t("singular.status.ACTIVE"), value: "ACTIVE" as SingularStatus },
  { label: t("singular.status.INACTIVE"), value: "INACTIVE" as SingularStatus }
]);

const statusModel = computed({
  get: () => props.filters.status,
  set: (value: SingularStatus | null) => {
    props.filters.status = value;
  }
});

const federationIdModel = computed({
  get: () => props.filters.federationId ?? "",
  set: (value: string | number | null) => {
    if (value === "" || value === null) {
      props.filters.federationId = null;
      return;
    }

    props.filters.federationId = Number(value);
  }
});
</script>

<style scoped lang="scss">
.singular-filters {
  &__grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
    gap: var(--spacing-md, 16px);
  }
}
</style>
