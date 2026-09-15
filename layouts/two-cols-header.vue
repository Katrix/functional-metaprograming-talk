<template>
  <div class="h-full flex flex-col">
    <Breadcrumbs />

    <div class="slidev-layout two-cols-header w-full flex-1 min-h-0" :class="layoutClass">
      <slot />

      <div class="col-left" :class="props.class">
        <slot name="left" />
      </div>
      <div class="col-right" :class="props.class">
        <slot name="right" />
      </div>
      <div class="col-bottom" :class="props.class">
        <slot name="bottom" />
      </div>
    </div>

    <PageCounter />
  </div>
</template>

<script setup lang="ts">
import Breadcrumbs from '../components/Breadcrumbs.vue'
import PageCounter from '../components/PageCounter.vue'

const props = defineProps({
  class: {
    type: String,
  },
  layoutClass: {
    type: String,
  },
})
</script>

<style scoped>
.two-cols-header {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  grid-template-rows: auto 1fr auto;
}

.col-left {
  grid-area: 2 / 1 / 3 / 2;
}
.col-right {
  grid-area: 2 / 2 / 3 / 3;
}
.col-bottom {
  align-self: end;
  grid-area: 3 / 1 / 3 / 3;
}
</style>
