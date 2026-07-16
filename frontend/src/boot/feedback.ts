import { defineBoot } from "#q-app";

import { registerGlobalHttpErrorHandler } from "@/composables/useStandardErrorHandling";

export default defineBoot(() => {
  registerGlobalHttpErrorHandler();
});
