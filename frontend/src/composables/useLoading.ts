import { computed, ref } from "vue";

export function useLoading(initial = false) {
  const loading = ref(initial);
  const isLoading = computed(() => loading.value);

  function start(): void {
    loading.value = true;
  }

  function stop(): void {
    loading.value = false;
  }

  async function withLoading<T>(operation: () => Promise<T>): Promise<T> {
    start();
    try {
      return await operation();
    } finally {
      stop();
    }
  }

  return {
    loading,
    isLoading,
    start,
    stop,
    withLoading
  };
}
