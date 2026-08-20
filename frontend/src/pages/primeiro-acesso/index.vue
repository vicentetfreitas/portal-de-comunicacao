<template>
  <div class="primeiro-acesso-page">
    <DsPageHeader
      :title="
        isBlocked
          ? $t('layout.primeiroAcesso.blockedTitle')
          : $t('layout.primeiroAcesso.title')
      "
      :subtitle="
        isBlocked
          ? $t('layout.primeiroAcesso.blockedSubtitle')
          : $t('layout.primeiroAcesso.subtitle')
      "
    />
    <q-banner class="primeiro-acesso-page__banner" dense rounded>
      {{
        isBlocked
          ? $t("layout.primeiroAcesso.blockedMessage")
          : $t("layout.primeiroAcesso.message")
      }}
    </q-banner>
    <p v-if="user" class="primeiro-acesso-page__identity">
      {{ user.email }}
    </p>

    <DsCard v-if="!isBlocked" :title="$t('layout.primeiroAcesso.areaLabel')">
      <DsSelect
        v-model="selectedAreaId"
        :label="$t('layout.primeiroAcesso.areaLabel')"
        :placeholder="$t('layout.primeiroAcesso.areaPlaceholder')"
        :options="areaOptions"
        :disable="loadingAreas || submitting"
        :error="areaError"
        :hint="
          !loadingAreas && areaOptions.length === 0
            ? $t('layout.primeiroAcesso.emptyAreas')
            : undefined
        "
      />
      <template #actions>
        <DsButton
          variant="primary"
          :loading="submitting"
          :disable="!canSubmit || areaOptions.length === 0"
          @click="confirm"
        >
          {{ $t("layout.primeiroAcesso.confirm") }}
        </DsButton>
      </template>
    </DsCard>

    <DsButton variant="secondary" :disable="submitting" @click="logout">
      {{ $t("layout.auth.logout") }}
    </DsButton>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from "vue";

import { DsButton, DsCard, DsPageHeader, DsSelect } from "@/components/ds";
import { usePrimeiroAcessoPage } from "@/composables/usePrimeiroAcessoPage";

const {
  user,
  logout,
  isBlocked,
  areaOptions,
  selectedAreaId,
  loadingAreas,
  submitting,
  areaError,
  canSubmit,
  loadAreas,
  confirm
} = usePrimeiroAcessoPage();

onMounted(() => {
  void loadAreas();
});
</script>

<style scoped lang="scss">
.primeiro-acesso-page {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: var(--spacing-md);

  &__banner {
    width: 100%;
    background-color: rgb(239 108 0 / 0.08);
    color: var(--color-warning);
    font-size: var(--text-body-small-size);
  }

  &__identity {
    margin: 0;
    color: var(--color-text-secondary);
    font-size: var(--text-body-small-size);
  }

  :deep(.ds-card) {
    width: 100%;
  }
}
</style>
