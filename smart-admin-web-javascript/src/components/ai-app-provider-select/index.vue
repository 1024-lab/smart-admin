<template>
  <a-select
    v-model:value="selectValue"
    :placeholder="placeholder"
    :allow-clear="allowClear"
    :disabled="disabled"
    show-search
    :filter-option="filterOption"
    @change="handleChange"
  >
    <a-select-option
      v-for="provider in providerList"
      :key="provider.id"
      :value="provider.id"
    >
      <div class="provider-option">
        <span class="provider-name">{{ provider.name }}</span>
        <a-tag v-if="provider.name === 'dify'" color="blue">Dify</a-tag>
        <a-tag v-else-if="provider.name === 'ragflow'" color="green">RAGFlow</a-tag>
        <a-tag v-else color="default">{{ provider.name }}</a-tag>
      </div>
    </a-select-option>
  </a-select>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { aiAppProviderApi } from '/@/api/business/ai-app-provider/ai-app-provider-api';
import { message } from 'ant-design-vue';

const props = defineProps({
  value: {
    type: String,
    default: ''
  },
  placeholder: {
    type: String,
    default: '请选择智能体平台'
  },
  allowClear: {
    type: Boolean,
    default: true
  },
  disabled: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['update:value', 'change']);

const selectValue = ref(props.value);
const providerList = ref([]);

// 监听外部值变化
watch(() => props.value, (newVal) => {
  selectValue.value = newVal;
});

// 加载供应商列表
const loadProviderList = async () => {
  try {
    const res = await aiAppProviderApi.queryAll();
    if (res.data) {
      providerList.value = res.data;
    }
  } catch (error) {
    message.error('加载供应商列表失败');
    console.error(error);
  }
};

// 搜索过滤
const filterOption = (input, option) => {
  const provider = providerList.value.find(p => p.id === option.value);
  if (!provider) return false;

  const searchText = input.toLowerCase();
  return (
    provider.name.toLowerCase().includes(searchText) ||
    (provider.baseUrl && provider.baseUrl.toLowerCase().includes(searchText))
  );
};

// 处理选择变化
const handleChange = (value) => {
  emit('update:value', value);

  const selectedProvider = providerList.value.find(p => p.id === value);
  emit('change', value, selectedProvider);
};

onMounted(() => {
  loadProviderList();
});
</script>

<style scoped>
.provider-option {
  display: flex;
  align-items: center;
  gap: 8px;
}

.provider-name {
  flex: 1;
}
</style>
