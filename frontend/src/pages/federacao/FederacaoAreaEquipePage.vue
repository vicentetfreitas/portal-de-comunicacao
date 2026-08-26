<template>
  <div class="federacao-area-equipe-page">
    <DsPageHeader
      :title="area?.name ?? $t('federacao.equipe.title')"
      :subtitle="
        area ? $t('federacao.equipe.subtitle', { area: area.name }) : ''
      "
      :show-back="showBackButton"
      @back="router.back()"
    />

    <AppLoadingSkeleton v-if="loading" />

    <AppEmptyState
      v-else-if="isEmpty"
      :title="$t('federacao.equipe.emptyTitle')"
      :description="$t('federacao.equipe.emptyDescription')"
      icon="mdi-account-group-outline"
    />

    <template v-else>
      <!--
        Plain rows, no card/table borders — matches how internal list items
        read elsewhere in this project (e.g. the sidebar directory list),
        not a bordered DsDataTable/DsCard-per-item grid. Explicit product
        decision this round.
      -->
      <ul class="federacao-area-equipe-page__list">
        <li
          v-for="member in members"
          :key="member.id"
          class="federacao-area-equipe-page__member"
        >
          <p class="federacao-area-equipe-page__name">{{ member.name }}</p>
          <p class="federacao-area-equipe-page__cargo">{{ member.cargo }}</p>
          <dl class="federacao-area-equipe-page__contact">
            <div>
              <dt>{{ $t("federacao.equipe.emailsLabel") }}</dt>
              <dd>{{ member.emails.join(", ") }}</dd>
            </div>
            <div>
              <dt>{{ $t("federacao.equipe.phonesLabel") }}</dt>
              <dd>{{ member.phones.join(", ") }}</dd>
            </div>
            <div>
              <dt>{{ $t("federacao.equipe.ramaisLabel") }}</dt>
              <dd>{{ member.ramais.join(", ") }}</dd>
            </div>
          </dl>
        </li>
      </ul>

      <div class="federacao-area-equipe-page__contato-setorial">
        <p class="federacao-area-equipe-page__contato-setorial-title">
          {{ $t("federacao.equipe.contatoSetorialTitle") }}
        </p>
        <p>{{ contatoSetorial.email }}</p>
        <p>{{ contatoSetorial.phone }}</p>
      </div>
    </template>
  </div>
</template>

<script setup lang="ts">
import { toRef } from "vue";
import { useRouter } from "vue-router";

import AppEmptyState from "@/components/shared/AppEmptyState.vue";
import AppLoadingSkeleton from "@/components/shared/AppLoadingSkeleton.vue";
import { DsPageHeader } from "@/components/ds";
import { useFederacaoAreaDetail } from "@/composables/federacao/useFederacaoAreaDetail";
import { useFederacaoAreaRoster } from "@/composables/federacao/useFederacaoAreaRoster";
import { useLayoutMeta } from "@/composables/useLayoutMeta";

const props = defineProps<{
  id: string;
}>();

const router = useRouter();
const idRef = toRef(props, "id");
const { area } = useFederacaoAreaDetail(idRef);
const { members, loading, isEmpty, contatoSetorial } =
  useFederacaoAreaRoster(idRef);
const { showBackButton } = useLayoutMeta();
</script>

<style scoped lang="scss">
.federacao-area-equipe-page {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg, 24px);

  &__list {
    display: flex;
    flex-direction: column;
    margin: 0;
    padding: 0;
    list-style: none;
  }

  &__member {
    padding: var(--spacing-md, 16px) 0;
    border-bottom: var(--border-width-thin) var(--border-style-default)
      var(--color-border);

    &:last-child {
      border-bottom: none;
    }
  }

  &__name {
    margin: 0;
    font-weight: var(--font-weight-semibold);
    color: var(--color-text-primary);
  }

  &__cargo {
    margin: var(--spacing-xs) 0 var(--spacing-sm, 8px);
    color: var(--color-text-secondary);
    font-size: var(--text-body-small-size);
  }

  &__contact {
    display: flex;
    flex-wrap: wrap;
    gap: var(--spacing-lg, 24px);
    margin: 0;

    dt {
      color: var(--color-text-secondary);
      font-size: var(--text-caption-size);
    }

    dd {
      margin: 0;
      color: var(--color-text-primary);
      font-size: var(--text-body-small-size);
    }
  }

  &__contato-setorial {
    &-title {
      margin: 0 0 var(--spacing-xs);
      font-weight: var(--font-weight-semibold);
      color: var(--color-text-primary);
    }

    p {
      margin: 0;
      color: var(--color-text-secondary);
      font-size: var(--text-body-small-size);
    }
  }
}
</style>
