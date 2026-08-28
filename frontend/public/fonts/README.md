# Corporate fonts (Unimed)

Font binaries are **not versioned** in this repository (R-FE-S0-04).

Place the following files here for production typography:

| File                             | Family       | Weight/style           |
| -------------------------------- | ------------ | ---------------------- |
| `UnimedSans-Light.otf`           | Unimed Sans  | 300 normal             |
| `UnimedSans-LightItalic.otf`     | Unimed Sans  | 300 italic             |
| `UnimedSans-Regular.otf`         | Unimed Sans  | 400 normal             |
| `UnimedSans-RegularItalic.otf`   | Unimed Sans  | 400 italic             |
| `UnimedSans-SemiBold.otf`        | Unimed Sans  | 600 normal             |
| `UnimedSans-SemiBoldItalic.otf`  | Unimed Sans  | 600 italic             |
| `UnimedSans-Bold.otf`            | Unimed Sans  | 700 normal             |
| `UnimedSans-BoldItalic.otf`      | Unimed Sans  | 700 italic             |
| `UnimedSans-ExtraBold.otf`       | Unimed Sans  | 800 normal             |
| `UnimedSans-ExtraBoldItalic.otf` | Unimed Sans  | 800 italic             |
| `UnimedSans-Black.otf`           | Unimed Sans  | 900 normal             |
| `UnimedSans-BlackItalic.otf`     | Unimed Sans  | 900 italic             |
| `UnimedSerif-Regular.otf`        | Unimed Serif | 400 700 normal (range) |
| `UnimedSlab-Regular.otf`         | Unimed Slab  | 400 700 normal (range) |
| `UnimedBrush-Regular.otf`        | Unimed Brush | 400 normal             |

Each Unimed Sans weight/style is its **own** `@font-face` in `src/css/fonts.scss` —
not one `font-weight: 300 900` range on the Regular file. A range descriptor tells
the browser one file covers every weight in it; pointing that at a single static
(non-variable) file means every requested weight silently renders as Regular, with
no error and no fallback (this is what happened to the Home page's `font-weight:
800` title before this was fixed — computed style read "800", the glyphs stayed
Regular). Serif/Slab keep the original range-on-Regular declaration since only
their 400 weight is actually used anywhere in the app today; widen them the same
way if a heavier Serif/Slab weight is ever needed.

Referenced by `src/css/fonts.scss`. Any file that's missing just falls back to
Inter and the system stack (`src/css/tokens/_typography.scss`) for that specific
weight/style — the app still renders, just without that face.
