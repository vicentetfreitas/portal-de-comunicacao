<template>
  <AppShell :nav-items="MAIN_NAV_ITEMS" variant="main">
    <slot />
  </AppShell>
</template>

<script setup lang="ts">
import { AppShell } from "@/components/app";
import { MAIN_NAV_ITEMS } from "@/constants/navigation";
</script>
