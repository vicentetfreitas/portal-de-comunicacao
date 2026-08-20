<template>
  <main class="login-page" aria-labelledby="login-page-title">
    <div class="login-page__backdrop" aria-hidden="true">
      <span class="login-page__blob login-page__blob--left" />
      <span class="login-page__blob login-page__blob--top" />
      <span class="login-page__blob login-page__blob--right" />
      <span class="login-page__panel-bg" />
    </div>

    <div class="login-page__grid">
      <div class="login-page__content">
        <header class="login-page__brand">
          <h1 id="login-page-title" class="login-page__title">
            <span class="login-page__title-line">{{
              $t("layout.auth.figma.titlePortal")
            }}</span>
            <span class="login-page__title-highlight">{{
              $t("layout.auth.figma.titleComunicacao")
            }}</span>
          </h1>
        </header>

        <section
          class="login-page__form-section"
          aria-label="Formulário de login"
        >
          <div class="login-page__form-card">
            <form
              class="login-page__form"
              novalidate
              @submit.prevent="handleSubmit"
            >
              <q-banner
                v-if="bannerMessage"
                class="login-page__banner"
                dense
                rounded
                role="alert"
              >
                {{ bannerMessage }}
              </q-banner>

              <div class="login-page__field">
                <label class="login-page__label" for="login-usuario">
                  {{ $t("layout.auth.figma.userLabel") }}
                </label>
                <DsInput
                  id="login-usuario"
                  v-model="usuario"
                  class="login-page__input"
                  variant="filled"
                  type="email"
                  autocomplete="username"
                  :disable="isSubmitting"
                  :error="fieldErrors.usuario"
                  :aria-invalid="fieldErrors.usuario ? 'true' : undefined"
                  @update:model-value="clearFieldError('usuario')"
                />
              </div>

              <div class="login-page__field">
                <label class="login-page__label" for="login-senha">
                  {{ $t("layout.auth.figma.passwordLabel") }}
                </label>
                <DsInput
                  id="login-senha"
                  v-model="senha"
                  class="login-page__input"
                  variant="filled"
                  type="password"
                  autocomplete="current-password"
                  :disable="isSubmitting"
                  :error="fieldErrors.senha"
                  :aria-invalid="fieldErrors.senha ? 'true' : undefined"
                  @update:model-value="clearFieldError('senha')"
                />
              </div>

              <q-checkbox
                v-model="rememberMe"
                class="login-page__remember"
                dense
                :disable="isSubmitting"
                :label="$t('layout.auth.rememberMe')"
              />

              <DsButton
                type="submit"
                variant="primary"
                class="login-page__submit full-width"
                size="lg"
                :loading="isSubmitting"
                :disable="isSubmitDisabled"
              >
                {{ $t("layout.auth.figma.submit") }}
              </DsButton>

              <p
                v-if="fieldErrors.senha && !bannerMessage"
                class="login-page__hint"
                role="alert"
              >
                {{ fieldErrors.senha }}
              </p>
            </form>
          </div>
        </section>
      </div>

      <aside class="login-page__hero" aria-hidden="true">
        <img
          class="login-page__hero-image"
          src="/images/login-hero.svg"
          alt=""
          width="522"
          height="520"
          loading="eager"
          decoding="async"
        />
      </aside>
    </div>
  </main>
</template>

<script setup lang="ts">
import { DsButton, DsInput } from "@/components/ds";
import {
  useLoginPage,
  type LoginFieldErrors
} from "@/composables/useLoginPage";

const {
  usuario,
  senha,
  rememberMe,
  isSubmitting,
  fieldErrors,
  bannerMessage,
  isSubmitDisabled,
  handleSubmit
} = useLoginPage();

function clearFieldError(field: keyof LoginFieldErrors): void {
  if (fieldErrors.value[field]) {
    const next = { ...fieldErrors.value };
    delete next[field];
    fieldErrors.value = next;
  }
}
</script>

<style scoped lang="scss" src="./login-page.scss"></style>
