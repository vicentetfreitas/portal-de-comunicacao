<template>
  <div class="ds-showcase-page">
    <DsPageHeader
      :title="$t('showcase.title')"
      :subtitle="$t('showcase.subtitle')"
    />

    <section class="ds-showcase__section">
      <h2 class="ds-showcase__section-title">{{
        $t("showcase.typography")
      }}</h2>
      <div class="column q-gutter-sm">
        <p class="ds-text-display">Display — Portal de Comunicação</p>
        <p class="ds-text-page-title">Page Title — Página inicial</p>
        <p class="ds-text-section-title">Section Title — Últimas notícias</p>
        <p class="ds-text-card-title">Card Title — Comunicado interno</p>
        <p class="ds-text-body-large"
          >Body Large — Texto de destaque para leitura confortável.</p
        >
        <p class="ds-text-body"
          >Body — Texto padrão para conteúdo geral do portal.</p
        >
        <p class="ds-text-body-small"
          >Body Small — Informações complementares e metadados.</p
        >
        <p class="ds-text-caption">Caption — Há 2 dias · 5 min de leitura</p>
        <p class="ds-text-label">Label — Campo obrigatório</p>
      </div>
    </section>

    <section class="ds-showcase__section">
      <h2 class="ds-showcase__section-title">{{ $t("showcase.atoms") }}</h2>

      <div class="ds-showcase__row q-mb-md">
        <DsButton variant="primary">{{ $t("showcase.primary") }}</DsButton>
        <DsButton variant="secondary">{{ $t("showcase.secondary") }}</DsButton>
        <DsButton variant="ghost">{{ $t("showcase.ghost") }}</DsButton>
        <DsButton variant="outline">{{ $t("showcase.outline") }}</DsButton>
        <DsButton variant="danger">{{ $t("showcase.danger") }}</DsButton>
        <DsButton variant="link">{{ $t("showcase.link") }}</DsButton>
        <DsButton variant="primary" loading>{{
          $t("showcase.loading")
        }}</DsButton>
      </div>

      <div class="row q-col-gutter-md q-mb-md">
        <div class="col-12 col-md-4">
          <DsInput
            v-model="sampleInput"
            :label="$t('showcase.inputLabel')"
            :hint="$t('showcase.inputHint')"
          />
        </div>
        <div class="col-12 col-md-4">
          <DsInput
            v-model="sampleErrorInput"
            :label="$t('showcase.inputError')"
            :error="$t('showcase.inputErrorMessage')"
          />
        </div>
        <div class="col-12 col-md-4">
          <DsInput
            v-model="sampleFilledInput"
            variant="filled"
            :label="$t('showcase.inputFilled')"
          />
        </div>
      </div>

      <div class="row q-col-gutter-md q-mb-md">
        <div class="col-12 col-md-4">
          <DsSelect
            v-model="sampleSelect"
            :label="$t('showcase.selectLabel')"
            :options="selectOptions"
          />
        </div>
      </div>

      <div class="ds-showcase__row q-mb-md">
        <DsIcon name="mdi-home" size="lg" />
        <DsIcon name="mdi-bell-outline" size="md" />
        <DsIcon name="mdi-cog" size="sm" />
        <DsBadge label="Info" variant="info" />
        <DsBadge label="Success" variant="positive" />
        <DsBadge label="Warning" variant="warning" />
        <DsBadge label="Error" variant="negative" />
        <DsAvatar initials="UN" size="xs" />
        <DsAvatar initials="UN" size="sm" />
        <DsAvatar initials="UN" />
        <DsAvatar initials="PC" size="lg" color="secondary" text-color="dark" />
        <DsAvatar initials="XL" size="xl" />
      </div>
    </section>

    <section class="ds-showcase__section">
      <h2 class="ds-showcase__section-title">{{ $t("showcase.molecules") }}</h2>

      <DsBreadcrumbs :items="breadcrumbs" class="q-mb-md" />

      <div class="row q-col-gutter-md q-mb-md">
        <div class="col-12 col-md-4">
          <DsSearchInput v-model="searchQuery" />
        </div>
        <div class="col-12 col-md-4">
          <DsCard
            :title="$t('showcase.cardTitle')"
            :subtitle="$t('showcase.cardSubtitle')"
          >
            {{ $t("showcase.cardContent") }}
            <template #actions>
              <DsButton variant="ghost" size="sm">{{
                $t("showcase.cancel")
              }}</DsButton>
              <DsButton variant="primary" size="sm">{{
                $t("showcase.confirm")
              }}</DsButton>
            </template>
          </DsCard>
        </div>
        <div class="col-12 col-md-4">
          <DsCard variant="outlined" :title="$t('showcase.cardOutlined')">
            {{ $t("showcase.cardContent") }}
          </DsCard>
        </div>
        <div class="col-12 col-md-4">
          <DsCard variant="flat" :title="$t('showcase.cardFlat')">
            {{ $t("showcase.cardContent") }}
          </DsCard>
        </div>
      </div>

      <DsButton variant="outline" @click="dialogOpen = true">
        {{ $t("showcase.openDialog") }}
      </DsButton>
      <DsDialog
        v-model="dialogOpen"
        :title="$t('showcase.dialogTitle')"
        :subtitle="$t('showcase.dialogSubtitle')"
      >
        <p>{{ $t("showcase.dialogContent") }}</p>
        <template #actions>
          <DsButton variant="ghost" @click="dialogOpen = false">
            {{ $t("showcase.cancel") }}
          </DsButton>
          <DsButton variant="primary" @click="dialogOpen = false">
            {{ $t("showcase.confirm") }}
          </DsButton>
        </template>
      </DsDialog>
    </section>

    <section class="ds-showcase__section">
      <h2 class="ds-showcase__section-title">{{
        $t("showcase.navigation")
      }}</h2>
      <div class="row q-col-gutter-md">
        <div class="col-12 col-md-4">
          <nav class="column q-gutter-xs">
            <DsNavItem
              :label="$t('showcase.navHome')"
              icon="mdi-home"
              to="/"
              :active="true"
            />
            <DsNavItem
              :label="$t('showcase.navNews')"
              icon="mdi-newspaper-variant-outline"
              to="/showcase"
            />
            <DsNavItem
              :label="$t('showcase.navProfile')"
              icon="mdi-account-outline"
              to="/app"
            />
          </nav>
        </div>
      </div>
    </section>

    <section class="ds-showcase__section">
      <h2 class="ds-showcase__section-title">{{ $t("showcase.profile") }}</h2>
      <div class="row q-col-gutter-md">
        <div class="col-12 col-md-6">
          <DsProfileSummary
            :name="$t('showcase.profileName')"
            :greeting="$t('showcase.profileGreeting')"
            :subtitle="$t('showcase.profileSubtitle')"
            avatar-initials="MS"
            :edit-label="$t('showcase.profileEdit')"
            @edit="onProfileEdit"
          />
        </div>
      </div>
    </section>

    <section class="ds-showcase__section">
      <h2 class="ds-showcase__section-title">{{ $t("showcase.content") }}</h2>

      <DsSectionHeader
        :title="$t('showcase.sectionTitle')"
        :subtitle="$t('showcase.sectionSubtitle')"
        class="q-mb-md"
      >
        <template #actions>
          <DsButton variant="outline" size="sm">Ver todas</DsButton>
        </template>
      </DsSectionHeader>

      <DsContentCard
        class="q-mb-md"
        :title="$t('showcase.contentCardTitle')"
        :description="$t('showcase.contentCardDescription')"
        :meta="$t('showcase.contentCardMeta')"
      >
        <template #media>
          <div class="ds-showcase__media-placeholder">
            <DsIcon name="mdi-image-outline" size="lg" />
          </div>
        </template>
      </DsContentCard>

      <DsContentCardCompact
        class="q-mb-md"
        icon="mdi-file-document-outline"
        :title="$t('showcase.compactCardTitle')"
        :description="$t('showcase.compactCardDescription')"
        clickable
      />

      <div class="ds-showcase__card-grid q-mb-md">
        <DsServiceCard
          icon="mdi-download-outline"
          :title="$t('showcase.serviceCardTitle')"
          :description="$t('showcase.serviceCardDescription')"
        />
        <DsServiceCard
          icon="mdi-cog-outline"
          title="Configurações"
          description="Preferências da área de trabalho."
        />
      </div>

      <div class="ds-showcase__grid">
        <DsActionCard
          icon="mdi-account-group-outline"
          :label="$t('showcase.actionCardLabel')"
          :description="$t('showcase.actionCardDescription')"
        />
        <DsActionCard
          variant="secondary"
          icon="mdi-domain"
          :label="$t('showcase.actionCardAreas')"
        />
        <DsActionCard
          variant="outline"
          icon="mdi-handshake-outline"
          :label="$t('showcase.actionCardServices')"
        />
      </div>
    </section>

    <section class="ds-showcase__section">
      <h2 class="ds-showcase__section-title">{{ $t("showcase.organisms") }}</h2>

      <DsFormCard :title="$t('showcase.formCardTitle')" class="q-mb-lg">
        <DsInput v-model="formName" :label="$t('showcase.formName')" />
        <DsInput
          v-model="formEmail"
          type="email"
          :label="$t('showcase.formEmail')"
        />
        <template #actions>
          <DsButton variant="primary">{{ $t("showcase.save") }}</DsButton>
        </template>
      </DsFormCard>

      <DsDataTable
        :title="$t('showcase.tableTitle')"
        :rows="tableRows"
        :columns="tableColumns"
      />

      <div class="ds-showcase__row q-mt-md">
        <DsButton variant="secondary" @click="showSuccessNotify">
          {{ $t("showcase.notifySuccess") }}
        </DsButton>
        <DsButton variant="danger" @click="showErrorNotify">
          {{ $t("showcase.notifyError") }}
        </DsButton>
      </div>
    </section>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { useI18n } from "vue-i18n";

import {
  DsActionCard,
  DsAvatar,
  DsBadge,
  DsBreadcrumbs,
  DsButton,
  DsCard,
  DsContentCard,
  DsContentCardCompact,
  DsDataTable,
  DsDialog,
  DsFormCard,
  DsIcon,
  DsInput,
  DsNavItem,
  DsPageHeader,
  DsProfileSummary,
  DsSearchInput,
  DsSectionHeader,
  DsSelect,
  DsServiceCard,
  dsNotifyError,
  dsNotifySuccess,
  type DsBreadcrumbItem,
  type DsSelectOption,
  type DsTableColumn
} from "@/components/ds";

const { t } = useI18n();

const sampleInput = ref("Portal de Comunicação");
const sampleErrorInput = ref("");
const sampleFilledInput = ref("Campo preenchido");
const sampleSelect = ref("option-a");
const searchQuery = ref("");
const dialogOpen = ref(false);
const formName = ref("");
const formEmail = ref("");

const selectOptions: DsSelectOption[] = [
  { label: "Opção A", value: "option-a" },
  { label: "Opção B", value: "option-b" },
  { label: "Opção C", value: "option-c" }
];

const breadcrumbs: DsBreadcrumbItem[] = [
  { label: "Home", to: "/", icon: "mdi-home" },
  { label: "Design System", icon: "mdi-palette" }
];

const tableColumns: DsTableColumn[] = [
  { name: "name", label: "Nome", field: "name", sortable: true },
  { name: "status", label: "Status", field: "status", align: "center" },
  { name: "role", label: "Papel", field: "role" }
];

const tableRows = [
  { id: 1, name: "Componente A", status: "Ativo", role: "Infraestrutura" },
  { id: 2, name: "Componente B", status: "Ativo", role: "Infraestrutura" },
  { id: 3, name: "Componente C", status: "Inativo", role: "Base" }
];

function onProfileEdit(): void {
  dsNotifySuccess(t("showcase.profileEdit"));
}

function showSuccessNotify(): void {
  dsNotifySuccess(t("showcase.notifySuccessMessage"));
}

function showErrorNotify(): void {
  dsNotifyError(t("showcase.notifyErrorMessage"));
}
</script>

<style scoped lang="scss">
.ds-showcase-page {
  background-color: var(--color-background);
  color: var(--color-text-primary);
  max-width: 1280px;
  margin: 0 auto;
}

.ds-showcase__media-placeholder {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 120px;
  height: 80px;
  background-color: var(--color-surface-muted);
  color: var(--color-text-disabled);
  border-radius: var(--radius-md);
}
</style>
