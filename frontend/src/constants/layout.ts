/** Layout dimensions — mirror of src/css/tokens/_layout.scss */
export const LAYOUT_HEADER_HEIGHT = 128;
export const LAYOUT_FOOTER_HEIGHT = 72;
/** Total in-flow rail width (panel + its own left inset) — see _layout.scss. */
export const LAYOUT_SIDEBAR_WIDTH = 464;
/**
 * Floor for `LAYOUT_SIDEBAR_WIDTH`'s fluid scaling below 1920px viewport
 * width (464 × 1280/1920, same proportional-scale derivation as the
 * `clamp()` tokens in `_layout.scss`) — kept here, not there, because this
 * value feeds Quasar's `<q-drawer :width>` prop in `AppSidebar.vue`, which
 * needs a JS number rather than a CSS value.
 */
export const LAYOUT_SIDEBAR_WIDTH_FLOOR = 309;
/** Viewport width below which `LAYOUT_SIDEBAR_WIDTH` stops scaling down. */
export const LAYOUT_SIDEBAR_WIDTH_FLOOR_VIEWPORT = 1280;
/** Viewport width `LAYOUT_SIDEBAR_WIDTH` is measured at (the Figma frame). */
export const LAYOUT_SIDEBAR_WIDTH_REFERENCE_VIEWPORT = 1920;
/** Sidebar panel's own left margin within the rail — see _layout.scss. */
export const LAYOUT_SIDEBAR_INSET = 116;
/** Gap between the rail's right edge and where main page content starts. */
export const LAYOUT_CONTENT_GUTTER = 166;
