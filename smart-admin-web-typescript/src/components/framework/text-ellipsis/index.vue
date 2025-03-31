<template>
  <div :class="className">
    <div v-if="overflow" class="ellipsis-box">
      <a-popover>
        <template #content>
          {{ text }}
          <SmartCopyIcon :value="text" />
        </template>
        <div>
          <slot>
            {{ text }}
          </slot>
        </div>
      </a-popover>
    </div>
    <div v-else>
      <slot>
        {{ text }}
      </slot>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { onMounted, ref, nextTick } from 'vue';
  import { Modal } from 'ant-design-vue';
  import { v4 as uuid } from 'uuid';
  import SmartCopyIcon from '/@/components/framework/smart-copy-icon/index.vue';

  const props = defineProps({
    text: {
      type: String,
      default: '',
    },
    classKey: {
      type: String,
      default: '',
    },
  });

  const overflow = ref(false);
  const className = ref();
  onMounted(() => {
    className.value = props.classKey + uuid();
    nextTick(() => {
      let doc = document.querySelector(`.${className.value}`);
      let fontSize = window.getComputedStyle(doc).fontSize.replace('px', '');
      let clientWidth = doc.clientWidth;
      let span = document.createElement('span');
      document.body.appendChild(span);
      span.style.fontSize = `${fontSize}px`;
      span.innerText = props.text;
      let width = span.offsetWidth;
      document.body.removeChild(span);
      overflow.value = width > clientWidth;
    });
  });

  function showModel() {
    Modal.info({
      content: props.text,
      icon: '',
      okText: '关闭',
    });
  }
</script>

<style lang="less" scoped>
  .ellipsis-box {
    display: flex;
    align-items: center;
    div {
      flex: 1;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }
</style>
