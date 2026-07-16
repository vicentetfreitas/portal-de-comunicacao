<template>
  <AppShell :nav-items="ADMIN_NAV_ITEMS" variant="admin">
    <slot />
  </AppShell>
</template>

<script setup lang="ts">
import { AppShell } from "@/components/app";
import { ADMIN_NAV_ITEMS } from "@/constants/navigation";
</script>
