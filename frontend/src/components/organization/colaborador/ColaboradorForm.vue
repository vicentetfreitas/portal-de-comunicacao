<template>
  <q-form class="colaborador-form" @submit.prevent="emit('submit')">
    <DsCard :title="title">
      <ColaboradorBasicInfoSection
        :model="model"
        :errors="errors"
        :mode="mode"
        :singular-options="singularOptions"
        :area-options="areaOptions"
        :team-options="teamOptions"
        :loading-singulares="loadingSingulares"
        :loading-areas="loadingAreas"
        :loading-teams="loadingTeams"
      />

      <template #actions>
        <DsButton variant="ghost" :disable="loading" @click="emit('cancel')">
          {{ $t("colaborador.form.cancel") }}
        </DsButton>
        <DsButton variant="primary" type="submit" :loading="loading">
          {{ submitLabel }}
        </DsButton>
      </template>
    </DsCard>
  </q-form>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useI18n } from "vue-i18n";

import { DsButton, DsCard, type DsSelectOption } from "@/components/ds";
import type { ColaboradorFormModel } from "@/composables/organization/useColaboradorForm";

import ColaboradorBasicInfoSection, {
  type ColaboradorFormMode
} from "./ColaboradorBasicInfoSection.vue";

const props = withDefaults(
  defineProps<{
    model: ColaboradorFormModel;
    errors?: Record<string, string>;
    mode?: ColaboradorFormMode;
    loading?: boolean;
    title?: string;
    singularOptions?: DsSelectOption<string>[];
    areaOptions?: DsSelectOption<string>[];
    teamOptions?: DsSelectOption<string>[];
    loadingSingulares?: boolean;
    loadingAreas?: boolean;
    loadingTeams?: boolean;
  }>(),
  {
    errors: () => ({}),
    mode: "create",
    loading: false,
    loadingSingulares: false,
    loadingAreas: false,
    loadingTeams: false,
    singularOptions: () => [],
    areaOptions: () => [],
    teamOptions: () => []
  }
);

const emit = defineEmits<{
  submit: [];
  cancel: [];
}>();

const { t } = useI18n();

const submitLabel = computed(() =>
  props.mode === "create"
    ? t("colaborador.form.submitCreate")
    : t("colaborador.form.submitEdit")
);
</script>

<style scoped lang="scss">
.colaborador-form {
  max-width: 720px;
}
</style>
