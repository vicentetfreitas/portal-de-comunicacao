<template>
  <DsProfileSummary
    :name="name"
    v-bind="profileAttrs"
    :edit-label="editLabel"
    :show-edit="showEdit"
    :mini="mini"
    @edit="$emit('edit')"
  />
</template>

<script setup lang="ts">
import { computed } from "vue";

import DsProfileSummary from "@/components/ds/molecules/DsProfileSummary.vue";

const props = withDefaults(
  defineProps<{
    name: string;
    greeting?: string | undefined;
    subtitle?: string | undefined;
    avatarSrc?: string | undefined;
    avatarInitials?: string | undefined;
    editLabel?: string;
    showEdit?: boolean;
    mini?: boolean;
  }>(),
  {
    editLabel: "Editar perfil",
    showEdit: true,
    mini: false
  }
);

defineEmits<{
  edit: [];
}>();

const profileAttrs = computed(() => ({
  ...(props.greeting !== undefined ? { greeting: props.greeting } : {}),
  ...(props.subtitle !== undefined ? { subtitle: props.subtitle } : {}),
  ...(props.avatarSrc !== undefined ? { avatarSrc: props.avatarSrc } : {}),
  ...(props.avatarInitials !== undefined
    ? { avatarInitials: props.avatarInitials }
    : {})
}));
</script>
