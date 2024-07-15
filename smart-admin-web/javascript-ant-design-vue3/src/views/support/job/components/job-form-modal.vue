<!--
  * JOB 表单
  * @Author:    huke
  * @Date:      2024/06/29
-->
<template>
  <div>
    <!-- 编辑 -->
    <a-modal :open="updateModalShow" :width="650" title="编辑" ok-text="确认" cancel-text="取消" @cancel="closeUpdateModal" @ok="confirmUpdateJob">
      <a-form ref="updateFormRef" :model="updateForm" :rules="updateRules" :label-col="{ span: 4 }">
        <a-form-item label="任务名称" name="jobName">
          <a-input placeholder="请输入任务名称" v-model:value="updateForm.jobName" :maxlength="100" :showCount="true" />
        </a-form-item>
        <a-form-item label="任务描述" name="remark">
          <a-textarea
            :auto-size="{ minRows: 2, maxRows: 4 }"
            v-model:value="updateForm.remark"
            placeholder="(可选)请输入任务备注描述"
            :maxlength="250"
            :showCount="true"
          />
        </a-form-item>
        <a-form-item label="排序" name="sort">
          <a-input-number
            v-model:value="updateForm.sort"
            :min="-99999999"
            :max="99999999"
            :precision="0"
            style="width: 100%"
            placeholder="值越小越靠前"
          >
          </a-input-number>
        </a-form-item>
        <a-form-item label="执行类" name="jobClass">
          <a-textarea
            :auto-size="{ minRows: 2, maxRows: 4 }"
            v-model:value="updateForm.jobClass"
            placeholder="示例：net.lab1024.sa.base.module.support.job.sample.SmartJobSample1"
            :maxlength="200"
            :showCount="true"
          />
        </a-form-item>
        <a-form-item label="任务参数" name="param">
          <a-textarea
            :auto-size="{ minRows: 3, maxRows: 6 }"
            v-model:value="updateForm.param"
            placeholder="(可选)请输入任务执行参数"
            :maxlength="1000"
            :showCount="true"
          />
        </a-form-item>
        <a-form-item label="触发类型" name="triggerType">
          <a-radio-group v-model:value="updateForm.triggerType">
            <a-radio-button :value="TRIGGER_TYPE_ENUM.CRON.value">CRON表达式</a-radio-button>
            <a-radio-button :value="TRIGGER_TYPE_ENUM.FIXED_DELAY.value">固定间隔</a-radio-button>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="触发时间" name="triggerTime">
          <a-input
            v-if="updateForm.triggerType === TRIGGER_TYPE_ENUM.CRON.value"
            placeholder="示例：10 15 0/1 * * *"
            v-model:value="updateForm.cron"
            :maxlength="100"
            :showCount="true"
          />
          <a-input-number
            v-else-if="updateForm.triggerType === TRIGGER_TYPE_ENUM.FIXED_DELAY.value"
            v-model:value="updateForm.fixedDelay"
            :min="1"
            :max="100000000"
            :precision="0"
            :defaultValue="100"
          >
            <template #addonBefore>每隔</template>
            <template #addonAfter>秒</template>
          </a-input-number>
        </a-form-item>
        <a-form-item label="是否开启" name="enabledFlag">
          <a-switch v-model:checked="updateForm.enabledFlag" />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 立即执行 -->
    <a-modal
      :open="executeModalShow"
      :width="650"
      title="执行任务"
      ok-text="执行"
      cancel-text="取消"
      @cancel="closeExecuteModal"
      @ok="confirmExecuteJob"
    >
      <br />
      <a-alert type="info" show-icon style="margin-left: 25px">
        <template #message> 点击【执行】后会按照【任务参数】，无论任务是否开启，都会立即执行。 </template>
      </a-alert>
      <br />
      <a-form :label-col="{ span: 4 }">
        <a-form-item label="任务名称" name="jobName">
          <a-input v-model:value="executeForm.jobName" :disabled="true" />
        </a-form-item>
        <a-form-item label="任务类名" name="jobClass">
          <a-textarea :auto-size="{ minRows: 2, maxRows: 4 }" v-model:value="executeForm.jobClass" :disabled="true" />
        </a-form-item>
        <a-form-item label="任务参数" name="param">
          <a-textarea
            :auto-size="{ minRows: 3, maxRows: 6 }"
            v-model:value="executeForm.param"
            placeholder="(可选)请输入任务执行参数"
            :maxlength="1000"
            :showCount="true"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>
<script setup>
  import { message } from 'ant-design-vue';
  import { reactive, ref } from 'vue';
  import { jobApi } from '/src/api/support/job-api';
  import { smartSentry } from '/src/lib/smart-sentry';
  import { SmartLoading } from '/src/components/framework/smart-loading/index.js';
  import { TRIGGER_TYPE_ENUM } from '/src/constants/support/job-const.js';

  // emit
  const emit = defineEmits(['reloadList']);

  const updateModalShow = ref(false);
  const updateFormRef = ref();

  const updateFormDefault = {
    jobId: null,
    jobName: '',
    jobClass: '',
    triggerType: null,
    triggerValue: null,
    cron: '',
    fixedDelay: null,
    param: '',
    enabledFlag: false,
    remark: '',
    sort: null,
  };
  let updateForm = reactive({ ...updateFormDefault });
  const updateRules = {
    jobName: [{ required: true, message: '请输入任务名称' }],
    jobClass: [{ required: true, message: '请输入执行类' }],
    triggerType: [{ required: true, message: '请选择触发类型' }],
    sort: [{ required: true, message: '请输入排序' }],
  };

  // 打开编辑弹框
  function openUpdateModal(record) {
    Object.assign(updateForm, record);
    if (TRIGGER_TYPE_ENUM.CRON.value === record.triggerType) {
      updateForm.cron = record.triggerValue;
    }
    if (TRIGGER_TYPE_ENUM.FIXED_DELAY.value === record.triggerType) {
      updateForm.fixedDelay = record.triggerValue;
    }
    updateModalShow.value = true;
  }

  // 关闭编辑弹框
  function closeUpdateModal() {
    Object.assign(updateForm, updateFormDefault);
    updateModalShow.value = false;
  }

  // 确认更新
  async function confirmUpdateJob() {
    updateFormRef.value
      .validate()
      .then(async () => {
        SmartLoading.show();
        if (TRIGGER_TYPE_ENUM.CRON.value === updateForm.triggerType) {
          updateForm.triggerValue = updateForm.cron;
        }
        if (TRIGGER_TYPE_ENUM.FIXED_DELAY.value === updateForm.triggerType) {
          updateForm.triggerValue = updateForm.fixedDelay;
        }
        try {
          await jobApi.updateJob(updateForm);
          message.success('更新成功');
          closeUpdateModal();
          emit('reloadList');
        } catch (error) {
          smartSentry.captureError(error);
        } finally {
          SmartLoading.hide();
        }
      })
      .catch((error) => {
        console.log('error', error);
        message.error('参数验证错误，请检查表单数据');
      });
  }

  // ------------------------------------ 执行任务 -------------------------------------
  const executeModalShow = ref(false);
  const executeFormDefault = {
    jobId: null,
    jobName: '',
    jobClass: '',
    param: null,
  };
  let executeForm = reactive({ ...executeFormDefault });

  // 打开执行弹框
  function openExecuteModal(record) {
    Object.assign(executeForm, record);
    executeModalShow.value = true;
  }

  // 关闭执行弹框
  function closeExecuteModal() {
    Object.assign(executeForm, executeFormDefault);
    executeModalShow.value = false;
  }

  // 确认执行
  async function confirmExecuteJob() {
    try {
      let executeParam = {
        jobId: executeForm.jobId,
        param: executeForm.param,
      };
      await jobApi.executeJob(executeParam);
      // loading 延迟后再提示刷新
      SmartLoading.show();
      await new Promise((resolve) => setTimeout(resolve, 2000));
      message.success('执行成功');
      closeExecuteModal();
      emit('reloadList');
    } catch (e) {
      smartSentry.captureError(e);
    } finally {
      SmartLoading.hide();
    }
  }

  defineExpose({
    openUpdateModal,
    openExecuteModal,
  });
</script>
