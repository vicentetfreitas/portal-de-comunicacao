import { computed, ref } from "vue";
import { useI18n } from "vue-i18n";
import { useRouter } from "vue-router";

import { dsNotifySuccess, type DsSelectOption } from "@/components/ds";
import { useAuth } from "@/composables/useAuth";
import { useSession } from "@/composables/useSession";
import { useStandardErrorHandling } from "@/composables/useStandardErrorHandling";
import { ROUTE_PATHS } from "@/constants/routes";
import { authService } from "@/services/auth/auth.service";

export function usePrimeiroAcessoPage() {
  const { t } = useI18n();
  const router = useRouter();
  const { user, logout, hydrateSession } = useAuth();
  const { isBlocked, isReady } = useSession();
  const { handleError } = useStandardErrorHandling();

  const areaOptions = ref<DsSelectOption[]>([]);
  const selectedAreaId = ref<string | null>(null);
  const loadingAreas = ref(false);
  const submitting = ref(false);
  const areaError = ref<string | undefined>();

  const canSubmit = computed(
    () => !isBlocked.value && !loadingAreas.value && !submitting.value
  );

  async function loadAreas(): Promise<void> {
    if (isBlocked.value) {
      return;
    }

    loadingAreas.value = true;
    areaError.value = undefined;

    try {
      const areas = await authService.listPrimeiroAcessoAreas();
      areaOptions.value = areas.map(area => ({
        label: area.acronym ? `${area.name} (${area.acronym})` : area.name,
        value: String(area.id)
      }));
    } catch (error) {
      handleError(error);
      areaOptions.value = [];
    } finally {
      loadingAreas.value = false;
    }
  }

  async function confirm(): Promise<void> {
    areaError.value = undefined;

    if (isBlocked.value) {
      return;
    }

    if (selectedAreaId.value == null || selectedAreaId.value.length === 0) {
      areaError.value = t("layout.primeiroAcesso.areaRequired");
      return;
    }

    submitting.value = true;

    try {
      await authService.completePrimeiroAcesso({
        areaId: Number(selectedAreaId.value)
      });
      await hydrateSession({ force: true });
      if (isReady.value) {
        dsNotifySuccess(t("layout.primeiroAcesso.completeSuccess"));
        await router.replace(ROUTE_PATHS.APP);
      }
    } catch (error) {
      handleError(error);
    } finally {
      submitting.value = false;
    }
  }

  return {
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
  };
}
