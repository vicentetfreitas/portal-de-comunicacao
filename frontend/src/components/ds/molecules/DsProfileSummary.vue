<template>
  <div class="ds-profile-summary" :class="{ 'ds-profile-summary--mini': mini }">
    <DsAvatar v-bind="avatarAttrs" class="ds-profile-summary__avatar" />
    <div v-if="!mini" class="ds-profile-summary__content">
      <p v-if="greeting" class="ds-profile-summary__greeting">{{ greeting }}</p>
      <p class="ds-profile-summary__name">{{ name }}</p>
      <p v-if="subtitle" class="ds-profile-summary__subtitle">{{ subtitle }}</p>
      <DsButton
        v-if="showEdit"
        variant="link"
        size="sm"
        class="ds-profile-summary__edit"
        @click="$emit('edit')"
      >
        <DsIcon name="mdi-pencil-outline" size="sm" />
        {{ editLabel }}
      </DsButton>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";

import DsAvatar from "../atoms/DsAvatar.vue";
import DsButton from "../atoms/DsButton.vue";
import DsIcon from "../atoms/DsIcon.vue";

import type { DsSize } from "../types";

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
    avatarSize?: DsSize;
  }>(),
  {
    editLabel: "Editar perfil",
    showEdit: true,
    mini: false,
    avatarSize: "lg"
  }
);

defineEmits<{
  edit: [];
}>();

const resolvedAvatarSize = computed<DsSize>(() =>
  props.mini ? "md" : props.avatarSize
);

const avatarAttrs = computed(() => ({
  size: resolvedAvatarSize.value,
  ...(props.avatarSrc !== undefined ? { src: props.avatarSrc } : {}),
  ...(props.avatarInitials !== undefined
    ? { initials: props.avatarInitials }
    : {})
}));
</script>
