<!--
  * 文件上传
  * 
  * @Author:    1024创新实验室：善逸
  * @Date:      2022-08-12 20:19:39 
  * @Wechat:    zhuda1024 
  * @Email:     lab1024@163.com 
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012 
  *
-->
<template>
  <div class="clearfix">
    <a-upload
      :accept="props.accept"
      :before-upload="beforeUpload"
      :customRequest="customRequest"
      :file-list="fileList"
      :headers="{ 'x-access-token': useUserStore().getToken }"
      :list-type="listType"
      @change="handleChange"
      @preview="handlePreview"
      @remove="handleRemove"
    >
      <div v-if="fileList.length < props.maxUploadSize">
        <template v-if="listType == 'picture-card'">
          <PlusOutlined />
          <div class="ant-upload-text">
            {{ buttonText }}
          </div>
        </template>
        <template v-if="listType == 'text'">
          <a-button>
            <upload-outlined />
            {{ buttonText }}
          </a-button>
        </template>
      </div>
    </a-upload>
    <a-modal :footer="null" :visible="previewVisible" @cancel="handleCancel">
      <img :src="previewUrl" alt="example" style="width: 100%" />
    </a-modal>
  </div>
</template>
<script setup>
  import { computed, ref, watch } from 'vue';
  import { message } from 'ant-design-vue';
  import { fileApi } from '/@/api/support/file/file-api';
  import { useUserStore } from '/@/store/modules/system/user';
  import { SmartLoading } from '/@/components/framework/smart-loading';
  import { FILE_FOLDER_TYPE_ENUM } from '/@/constants/support/file-const';
  import { download } from '/@/lib/axios';
  import { smartSentry } from '/@/lib/smart-sentry';
  const props = defineProps({
    value: String,
    buttonText: {
      type: String,
      default: '点击上传附件',
    },
    showUploadBtn: {
      type: Boolean,
      default: true,
    },
    defaultFileList: {
      type: Array,
      default: () => [],
    },
    multiple: {
      type: Boolean,
      default: false,
    },
    // 最多上传文件数量
    maxUploadSize: {
      type: Number,
      default: 10,
    },
    maxSize: {
      type: Number,
      default: 10,
    },
    // 上传的文件类型
    accept: {
      type: String,
      default: '',
    },
    // 文件上传类型
    folder: {
      type: Number,
      default: FILE_FOLDER_TYPE_ENUM.COMMON.value,
    },
    // 上传列表的内建样式，支持三种基本样式 text, picture 和 picture-card
    listType: {
      type: String,
      default: 'picture-card',
    },
  });

  // 图片类型的后缀名
  const imgFileType = ['jpg', 'jpeg', 'png', 'gif'];

  // 重新修改图片展示字段
  const files = computed(() => {
    let res = [];
    if (props.defaultFileList && props.defaultFileList.length > 0) {
      props.defaultFileList.forEach((element) => {
        element.url = element.fileUrl;
        element.name = element.fileName;
        res.push(element);
      });
      return res;
    }
    return res;
  });
  // -------------------- 逻辑 --------------------

  const previewVisible = ref(false);
  const fileList = ref([]);
  const previewUrl = ref('');

  watch(
    files,
    (value) => {
      fileList.value = value;
    },
    {
      immediate: true,
    }
  );

  const emit = defineEmits(['update:value', 'change']);
  const customRequest = async (options) => {
    SmartLoading.show();
    try {
      console.log(options);
      const formData = new FormData();
      formData.append('file', options.file);
      let res = await fileApi.uploadFile(formData, props.folder);
      let file = res.data;
      file.url = file.fileUrl;
      file.name = file.fileName;
      fileList.value.push(file);
      emit('change', fileList.value);
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  };

  function handleChange(info) {
    let fileStatus = info.file.status;
    let file = info.file;
    if (fileStatus == 'removed') {
      let index = fileList.value.findIndex((e) => e.fileId == file.fileId);
      if (index != -1) {
        fileList.value.splice(index, 1);
        emit('change', fileList.value);
      }
    }
  }

  function handleRemove(file) {
    console.log(fileList.value);
  }

  function beforeUpload(file) {
    const isLimitSize = file.size / 1024 / 1024 < props.maxSize;
    if (!isLimitSize) {
      return message.error(`上传的文件必须小于${props.maxSize}Mb`);
    }
    return isLimitSize;
  }

  function handleCancel() {
    previewVisible.value = false;
  }

  const handlePreview = async (file) => {
    if (imgFileType.some((e) => e === file.fileType)) {
      previewUrl.value = file.url || file.preview;
      previewVisible.value = true;
    } else {
      download(file.fileName, file.fileUrl);
    }
  };

  // ------------------------ 清空 上传 ------------------------
  function clear() {
    fileList.value = [];
  }

  defineExpose({
    clear,
  });
</script>
<style lang="less" scoped>
  :deep(.ant-upload-picture-card-wrapper) {
    display: flex;
  }
</style>
