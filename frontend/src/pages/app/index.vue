<template>
  <div class="app-page">
    <DsSectionHeader
      class="app-page__section-header"
      :title="$t('layout.app.newsSectionTitle')"
    />

    <div class="app-page__news">
      <DsContentCard
        class="app-page__news-primary"
        :title="primaryNewsItem.title"
        :description="primaryNewsItem.description"
        :image-src="primaryNewsItem.image"
        :image-alt="primaryNewsItem.imageAlt"
        variant="flat"
      />

      <div class="app-page__news-secondary">
        <DsContentCard
          v-for="item in secondaryNewsItems"
          :key="item.id"
          class="app-page__news-secondary-card"
          :title="item.title"
          :description="item.description"
          :image-src="item.image"
          :image-alt="item.imageAlt"
          variant="flat"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { DsContentCard, DsSectionHeader } from "@/components/ds";

/**
 * FRONTEND SPIKE — sem spec/DoR (FT-NOTICIA não existe) e sem backend.
 * Reproduz layout/tipografia/espaçamento/imagem do frame `Home` do Figma
 * (board "Portal de Comunicação", node 7:3) com dados fixos mockados,
 * incluindo a própria imagem de exemplo usada no protótipo. Não é a
 * feature de Notícias — trocar por integração real quando especificada.
 */
const MOCK_NEWS_IMAGE = "/images/news-momento-bem-estar.png";
const MOCK_NEWS_IMAGE_ALT = "Momento Bem-estar — Especial Dia da Mulher";

const MOCK_NEWS_ITEMS = [
  {
    id: "mock-1",
    title: "Atenção Integral à Saúde lança Momento Bem-estar",
    description:
      "Uma iniciativa para cuidar da saúde física e emocional do time, com atividades e conteúdos exclusivos.",
    image: MOCK_NEWS_IMAGE,
    imageAlt: MOCK_NEWS_IMAGE_ALT
  },
  {
    id: "mock-2",
    title: "Atenção Integral à Saúde lança Momento Bem-estar",
    description: "Descritivo pequeno sobre a notícia",
    image: MOCK_NEWS_IMAGE,
    imageAlt: MOCK_NEWS_IMAGE_ALT
  },
  {
    id: "mock-3",
    title: "Atenção Integral à Saúde lança Momento Bem-estar",
    description: "Descritivo pequeno sobre a notícia",
    image: MOCK_NEWS_IMAGE,
    imageAlt: MOCK_NEWS_IMAGE_ALT
  }
] as const;

const [primaryNewsItem, ...secondaryNewsItems] = MOCK_NEWS_ITEMS;
</script>

<style scoped lang="scss">
.app-page {
  // Figma's "cinza" — the exact grey ink (`var(--cinza,#585c65)` in the
  // extracted node data) used for every text element on this frame:
  // heading, card titles, card descriptions (nodes 64:46, 64:12/25/43,
  // 64:19/26/44). Noticeably cooler/darker than this app's generic
  // `--color-text-secondary` (#757575) — scoped to this page's light
  // rendering only, since Figma has no dark-mode frame to source an
  // equivalent from; dark theme keeps the app's own secondary-text token.
  --app-page-ink: #585c65;

  // Figma's card column (heading + news) measures 837.6px wide at the
  // 1920px frame (home.txt "Rectangle 9"/"Noticia 2"+"Noticia 3" span
  // 629px..1465.48px) — this page's shared `.app-shell__content` (1280px)
  // is much wider, stretching the cards far past their Figma proportions.
  // Capping width here (page-scoped, not a change to the shared shell)
  // reproduces the narrower column and its right-side breathing room
  // without touching `AppShell.vue`'s `max-width`, which every other
  // authenticated page uses. Confirmed via live DOM measurement
  // (getBoundingClientRect) against a real Chromium render, not just this
  // comment — a prior pass used 900px as a rounder approximation; this is
  // the literal figure.
  max-width: 838px;

  // Figma's static frame only ever has to fit its own fixed placeholder
  // content — this app's sidebar next to it stretches to fill the real
  // available viewport height instead (`AppSidebar.vue`'s own `max-height`
  // formula), which on a tall viewport left this column visibly shorter
  // than the sidebar beside it (up to ~122px measured via Playwright at
  // 1920×1080 — the two panels' bottom edges no longer lined up). Matching
  // the same available-height budget here (`AppShell.vue`'s own
  // `.app-shell__page` height formula, minus the top/bottom padding it
  // applies) and letting the primary card grow into it below (`&__news`,
  // `&__news-primary`) closes that gap instead of leaving a shorter column.
  display: flex;
  flex-direction: column;

  // `min-height: 1000px` (not just `min-width`): at the shorter desktop
  // heights this page was already hardened against (900/768/720 — see the
  // title's own `-webkit-line-clamp` comment below, "keeps the page inside
  // the viewport without scrolling"), this budget comes out *smaller* than
  // the cards' own natural content size, so it's a no-op floor there — the
  // actual constraint ends up being the content's own minimums, which
  // already fit those heights without alignment help. Gating on height
  // keeps this a pure addition at genuinely tall viewports (1920×1080 and
  // up) instead of risking the scrolling regression that range was
  // specifically fixed against.
  @media (min-width: 960px) and (min-height: 1000px) {
    min-height: calc(
      100vh - var(--layout-header-height) - var(--layout-footer-height) - var(
          --border-width-thin
        ) - var(--layout-content-top-offset) - var(--spacing-lg)
    );
  }

  // Figma's "Fique por dentro" heading is a large italic display title in
  // that grey ink, left-aligned flush with the cards below it (not
  // centered on the page) — scoped to this page only, DsSectionHeader
  // elsewhere (e.g. showcase) keeps its own default style.
  &__section-header {
    // Figma measures ~40px between the heading and the primary card
    // (307 - (217 + 50)) — bigger than `.ds-section-header`'s own default
    // `margin-bottom` (`--spacing-lg`, 24px). Same element as the scope
    // class (DsSectionHeader's root), so no `:deep()` needed.
    margin-bottom: 40px;

    :deep(.ds-section-header__title) {
      // Figma measures this title at 40px (node 64:46), not the 48px
      // `--text-display-size` token — using the token here over-sized the
      // heading and ate into the space available for the cards below.
      // Fluid off the same contain-fit reference as the layout tokens
      // (`--layout-figma-viewport`, design-tokens.scss) — a fixed 40px
      // title was one of the most visually oversized elements reported
      // against a real screenshot once the header/sidebar geometry around
      // it had already been made fluid (floor 26.67px = 40×1280/1920).
      font-size: clamp(
        26.67px,
        calc(0.0208333 * var(--layout-figma-viewport)),
        40px
      );
      font-style: italic;
      // Figma: weight 800 (ExtraBold) — heavier than `--text-section-title-weight`
      // (600/SemiBold, `.ds-section-header__title`'s own default). No
      // `--font-weight-extrabold` CSS var exists yet, so this is a literal.
      font-weight: 800;
      color: var(--app-page-ink);
    }
  }

  &__news {
    display: flex;
    flex-direction: column;
    // Figma measures ~13px between the primary and secondary card rows
    // (591.67 - (307 + 271.543)) — spacing-md (16px) is the closest token,
    // and also keeps the page inside the viewport without scrolling at
    // common desktop heights (was spacing-lg/24px, taller than Figma shows).
    gap: var(--spacing-md);
    // Receives the extra height `.app-page`'s own `min-height` (above)
    // makes available; the secondary row keeps its Figma-measured size, the
    // primary card below is the one that actually grows into it. Same
    // height gate as `.app-page`'s own rule — a no-op below it.
    @media (min-width: 960px) and (min-height: 1000px) {
      flex: 1;
      min-height: 0;
    }
  }

  // Figma card titles/descriptions read in a single grey ink (no
  // title/description contrast), titles are italic — matches the login
  // page's existing use of synthetic italic (no italic font file registered).
  // Image aspect ratios below are measured directly off the Figma node
  // (837.6×271.5 card, 382.3×237.1 primary image ≈ 1.61:1, 342.5×138.6
  // secondary image ≈ 2.47:1). Card fill/radius use the `flat` variant's own
  // defaults (`--color-surface-muted`, `--radius-lg`) — close enough to
  // Figma's #D9D9D9-at-50% (~9/255 off) and, unlike a hardcoded rgba, already
  // dark-mode-correct; a previous `:deep(.ds-content-card)` override here
  // never actually matched (the class is on the same element as the scope,
  // not a descendant, so the generated descendant selector never applies).
  &__news-primary,
  &__news-secondary-card {
    // Figma measures this card's own background rectangle at 24px radius
    // (node 64:21/64:24/64:42, "Rectangle 9") — a bit heavier than
    // `--radius-xl` (16px).
    border-radius: 24px;
    // `FT-NOTICIA` doesn't exist yet, so there's no real destination to
    // link/focus to — no `tabindex`/`role=button` here, since a focusable
    // control with no keyboard action would itself be an accessibility
    // defect. This is only the visual hover affordance a future clickable
    // card will need; `DsContentCard`'s own `<article>`/`<h3>` structure
    // already gives it a reasonable semantic shape to wrap in a real link
    // once the feature exists.
    cursor: pointer;

    &:hover {
      box-shadow: var(--elevation-card);
    }
  }

  &__news-primary {
    // Figma has no mobile frame for this screen to compare against — stack
    // like the secondary cards below 768px so the title has room to read.
    flex-direction: column;
    align-items: stretch;

    @media (min-width: 768px) {
      flex-direction: row;
    }

    // The one card that absorbs `&__news`'s extra flex-grown height (see
    // `.app-page`'s own `min-height` above) — the secondary row below stays
    // at its Figma-measured size. `align-items: stretch` (`.ds-content-card`
    // default) then carries this row's grown height into both the media and
    // body children automatically, without sizing them individually. Same
    // height gate as `.app-page`'s own rule — a no-op below it.
    @media (min-width: 960px) and (min-height: 1000px) {
      flex: 1;
      min-height: 0;
    }

    :deep(.ds-content-card__media) {
      width: 100%;
      aspect-ratio: 1.61;
      // Figma measures this image at 58px radius on a 382px-wide box (node
      // 64:10) — a much heavier, near-superellipse round than `--radius-xl`
      // (16px). `overflow: hidden` (ds.scss) clips the inner `<img>` to
      // this same radius, so only the container needs it.
      border-radius: 58px;

      @media (min-width: 768px) {
        // Figma measures the primary image at 382.29px on an 837.6px-wide
        // card (node 64:10 vs "Rectangle 9"). A flex-basis percentage
        // resolves against `.ds-content-card`'s content box, not the outer
        // card width — `ds.scss` gives that card `--spacing-md` (16px)
        // padding on every side, so the box this percentage actually
        // applies to is 837.6 - 2×16 = 805.6px, not 837.6px. 382.29/805.6 =
        // 47.45%, not the naive 382.29/837.6 = 45.65%. Confirmed against a
        // live browser render (Playwright screenshot vs home.png, both at
        // the Figma frame's own 1920×1080) — 45.65% measured visibly
        // narrower than Figma (368px rendered vs 382px target).
        flex: 0 0 47.45%;
        width: 47.45%;
        // Figma measures this image at 237.09px tall (382.29×1.61) —
        // matches the aspect-ratio above exactly, so no crop is needed at
        // this width/aspect combination. Fluid below the Figma frame's own
        // 1920×1080 (floor 158px = 237×1280/1920), same contain-fit
        // derivation (`--layout-figma-viewport`, see design-tokens.scss) as
        // the shared layout tokens in `_layout.scss` — a fixed 237px cap
        // was eating an ever-larger share of a shorter/narrower viewport
        // otherwise, including a *wide-but-short* one (plain `vw` alone
        // doesn't shrink when width is near 1920 but height is well under
        // 1080).
        max-height: clamp(
          158px,
          calc(0.1234375 * var(--layout-figma-viewport)),
          237px
        );
      }

      // Same height gate as `&__news-primary`'s own `flex: 1` — this only
      // ever matters where that growth is actually active, and must come
      // after the block above in source order to win (both match at once
      // on a tall+wide viewport).
      @media (min-width: 768px) and (min-height: 1000px) {
        // `min-height`, not `max-height` above: a floor, not a ceiling, so
        // `&__news-primary`'s `flex: 1` can make this row taller than
        // Figma's own measurement when the sidebar beside it has more room
        // to stretch into (`AppSidebar.vue`'s own `max-height`) —
        // `align-items: stretch` then grows this media box (and
        // `object-fit: cover` on its `<img>`) to match, rather than leaving
        // a gap below a height-capped image. `aspect-ratio` is unset for
        // the same reason: with the height now coming from stretch, keeping
        // it would fight that height instead of just describing the
        // *smallest* case.
        aspect-ratio: auto;
        max-height: none;
        min-height: clamp(
          158px,
          calc(0.1234375 * var(--layout-figma-viewport)),
          237px
        );
      }
    }

    :deep(.ds-content-card__title) {
      // `--text-page-title-size` is 30px, matching Figma's own title size
      // (node 64:46) exactly — fluid below 1920px off the same contain-fit
      // reference as the layout tokens (floor 20px = 30×1280/1920), same
      // reasoning as `--layout-figma-viewport`'s own comment: this title
      // was one of the most visually oversized elements once the
      // surrounding header/sidebar geometry had already been made fluid.
      font-size: clamp(
        20px,
        calc(0.015625 * var(--layout-figma-viewport)),
        var(--text-page-title-size)
      );
      font-style: italic;
      font-weight: var(--text-body-weight);
      color: var(--app-page-ink);
      // Figma's title box (node 64:46, "Atenção Integral à Saúde lança
      // Momento Bem-estar") wraps to exactly 2 lines. Without a cap, the
      // same sentence wraps to 3 lines once the responsive text column
      // narrows below ~1400px (measured via Playwright at 1366×768) — that
      // extra line grows the card past the image's own 237px height, and
      // that growth is what pushed the whole page past the viewport at
      // 1600×900/1440×900/1366×768 (confirmed: document.scrollHeight
      // exceeded window.innerHeight only at those widths, not at 1920).
      // Clamping keeps the card's height anchored to the image at every
      // width this page supports, matching how Figma's fixed frame reads.
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;
    }

    :deep(.ds-content-card__description) {
      // `--text-section-title-size` is 24px — fluid off the same
      // contain-fit reference (floor 16px = 24×1280/1920), same reasoning
      // as the title above.
      font-size: clamp(
        16px,
        calc(0.0125 * var(--layout-figma-viewport)),
        var(--text-section-title-size)
      );
      // Figma: weight 300 (Light), not the DS default body weight (400).
      font-weight: var(--font-weight-light);
      color: var(--app-page-ink);
      // Same reasoning as the title clamp above — bounds this card's own
      // (longer than Figma's placeholder) copy so it can't grow the row
      // taller than the image at any supported width.
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 3;
      overflow: hidden;
    }
  }

  &__news-secondary {
    display: grid;
    gap: var(--spacing-lg);
    grid-template-columns: 1fr;

    @media (min-width: 768px) {
      grid-template-columns: repeat(2, minmax(0, 1fr));
    }
  }

  &__news-secondary-card {
    flex-direction: column;
    align-items: stretch;

    // `.ds-content-card__media` is sized for the horizontal layout
    // (fixed width, height driven by row stretch) — widen it to fill the
    // card and give it an aspect ratio now that the card stacks vertically.
    :deep(.ds-content-card__media) {
      width: 100%;
      aspect-ratio: 2.47;
      // Figma measures this image at 38px radius on a 342px-wide box
      // (node 64:28/64:45) — see the primary card's media rule above.
      border-radius: 38px;

      @media (min-width: 768px) {
        // See the primary card's media rule above for why this is capped
        // and fluid (floor 92px = 138×1280/1920) off the shared contain-fit
        // reference rather than plain `vw`.
        max-height: clamp(
          92px,
          calc(0.071875 * var(--layout-figma-viewport)),
          138px
        );
      }
    }

    :deep(.ds-content-card__title) {
      // Same fluid derivation as the primary card's title above
      // (`--text-section-title-size` = 24px, floor 16px).
      font-size: clamp(
        16px,
        calc(0.0125 * var(--layout-figma-viewport)),
        var(--text-section-title-size)
      );
      font-style: italic;
      font-weight: var(--text-body-weight);
      color: var(--app-page-ink);
      // Same reasoning as the primary card's title clamp above — Figma's
      // secondary title box (node 64:19/64:26) also reads as 2 lines.
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;
    }

    :deep(.ds-content-card__description) {
      // `--text-body-small-size` (14px, Figma's own measurement for this
      // box) read as too small in practice — explicit product decision to
      // size it off `--text-body-size` (16px) instead, same as the primary
      // card's own body copy. Floored at 14px (not the strict proportional
      // 10.67px) for the same legibility reason as the old floor.
      font-size: clamp(
        14px,
        calc(0.0083333 * var(--layout-figma-viewport)),
        var(--text-body-size)
      );
      // Figma: weight 300 (Light), not the DS default body weight (400).
      font-weight: var(--font-weight-light);
      color: var(--app-page-ink);
      // Figma's secondary description box (node 64:20/64:27) is a single
      // line — clamped to 2 as a safety margin rather than an exact match,
      // since this page's actual copy can run longer than Figma's
      // placeholder text.
      display: -webkit-box;
      -webkit-box-orient: vertical;
      -webkit-line-clamp: 2;
      overflow: hidden;
    }
  }
}

[data-theme="dark"] .app-page {
  --app-page-ink: var(--color-text-secondary);
}
</style>
