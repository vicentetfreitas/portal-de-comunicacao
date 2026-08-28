<template>
  <div class="perfil-page">
    <DsPageHeader
      :title="$t('perfil.title')"
      :subtitle="$t('perfil.subtitle')"
      show-back
      @back="router.back()"
    />

    <p class="perfil-page__notice">{{ $t("perfil.localNotice") }}</p>

    <q-form class="perfil-page__form" @submit.prevent="onSave">
      <DsCard>
        <div class="perfil-page__fields">
          <DsInput
            :model-value="user?.email ?? ''"
            :label="$t('perfil.fields.loginEmail')"
            readonly
            disable
          />
          <DsInput
            v-model="fields.name"
            :label="$t('perfil.fields.name')"
            autocomplete="name"
          />
          <DsInput v-model="fields.cargo" :label="$t('perfil.fields.cargo')" />
          <DsInput
            v-model="fields.additionalEmail"
            type="email"
            :label="$t('perfil.fields.additionalEmail')"
            autocomplete="email"
          />
          <DsInput
            v-model="fields.phones"
            :label="$t('perfil.fields.phones')"
            autocomplete="tel"
          />
          <DsInput
            v-model="fields.ramais"
            :label="$t('perfil.fields.ramais')"
          />
          <DsInput
            v-model="fields.celulares"
            :label="$t('perfil.fields.celulares')"
            autocomplete="tel"
          />
        </div>

        <template #actions>
          <DsButton variant="primary" type="submit">
            {{ $t("perfil.save") }}
          </DsButton>
        </template>
      </DsCard>
    </q-form>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";

import {
  DsButton,
  DsCard,
  DsInput,
  DsPageHeader,
  dsNotifySuccess
} from "@/components/ds";
import { useAuth } from "@/composables/useAuth";
import { usePerfilLocalFields } from "@/composables/perfil/usePerfilLocalFields";

const router = useRouter();
const { t } = useI18n();
const { user } = useAuth();

const userId = computed(() => user.value?.id ?? null);
const sessionName = computed(() => user.value?.name ?? "");
const { fields, save } = usePerfilLocalFields(userId, sessionName);

function onSave(): void {
  save();
  dsNotifySuccess(t("perfil.saveSuccess"));
}
</script>

<style scoped lang="scss">
.perfil-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);

  &__notice {
    margin: 0;
    padding: var(--spacing-sm, 8px) var(--spacing-md, 16px);
    border-radius: var(--radius-md);
    background-color: var(--color-surface-muted);
    color: var(--color-text-secondary);
    font-size: var(--text-body-small-size);
  }

  &__form {
    max-width: 560px;
  }

  &__fields {
    display: flex;
    flex-direction: column;
    gap: var(--spacing-md, 16px);
  }
}
</style>
