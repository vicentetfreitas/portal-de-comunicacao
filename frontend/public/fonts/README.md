# Corporate fonts (Unimed)

Font binaries are **not versioned** in this repository (R-FE-S0-04).

Place the following files here for production typography:

| File | Family |
|------|--------|
| `UnimedSans-Regular.otf` | Unimed Sans |
| `UnimedSerif-Regular.otf` | Unimed Serif |
| `UnimedSlab-Regular.otf` | Unimed Slab |
| `UnimedBrush-Regular.otf` | Unimed Brush |

Referenced by `src/css/fonts.scss`. Until files are present, the app uses Inter and the system stack defined in `src/css/tokens/_typography.scss`.
