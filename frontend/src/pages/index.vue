<template>
  <div class="home-page">
    <DsPageHeader
      :title="$t('layout.home.title')"
      :subtitle="$t('layout.home.subtitle')"
    />

    <div class="home-page__grid">
      <DsCard
        :title="$t('layout.home.foundationCard')"
        :subtitle="$t('layout.home.foundationSubtitle')"
      >
        <p class="home-page__text">{{ $t("layout.home.foundationText") }}</p>
        <p class="home-page__env"
          >{{ $t("layout.home.environment") }}: {{ appEnv }}</p
        >
      </DsCard>

      <DsCard :title="$t('layout.home.exploreCard')">
        <div class="home-page__links">
          <router-link :to="ROUTE_PATHS.SHOWCASE" class="home-page__link">
            {{ $t("layout.nav.showcase") }}
          </router-link>
          <router-link :to="ROUTE_PATHS.AUTH" class="home-page__link">
            {{ $t("layout.nav.auth") }}
          </router-link>
          <router-link :to="ROUTE_PATHS.APP" class="home-page__link">
            {{ $t("layout.nav.app") }}
          </router-link>
          <router-link :to="ROUTE_PATHS.ADMIN" class="home-page__link">
            {{ $t("layout.nav.admin") }}
          </router-link>
        </div>
      </DsCard>
    </div>
  </div>
</template>

<script setup lang="ts">
import { DsCard, DsPageHeader } from "@/components/ds";
import { ROUTE_PATHS } from "@/constants/routes";

const appEnv = import.meta.env.VITE_APP_ENV ?? "local";
</script>

<style scoped lang="scss">
.home-page {
  max-width: 960px;
  margin: 0 auto;

  &__grid {
    display: grid;
    gap: var(--spacing-lg);
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  }

  &__text,
  &__env {
    color: var(--color-text-secondary);
    font-size: var(--font-size-sm);
    margin: 0 0 var(--spacing-sm);
  }

  &__links {
    display: flex;
    flex-direction: column;
    gap: var(--spacing-sm);
  }

  &__link {
    color: var(--color-primary);
    font-weight: var(--font-weight-medium);
    text-decoration: none;

    &:hover {
      color: var(--color-primary-dark);
    }
  }
}
</style>
