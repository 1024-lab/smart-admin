<!--
  * 接口加密、解密
  *
  * @Author:    1024创新实验室-主任-卓大
  * @Date:      2023-10-17 22:02:37
  * @Copyright  1024创新实验室
-->
<template>
  <a-alert closable>
    <template v-slot:message>
      <h4>接口加解密：</h4>
    </template>
    <template v-slot:description>
      <pre>
简介：接口加解密分为： 前端请求参数加解密 和 后端返回结果加解密。

- 支持国密SM、AES加密算法。前端修改:/lib/encrypt.js文件，后端：启动 ApiEncryptServiceAesImpl 或者 ApiEncryptServiceSmImpl
- 前端请看：/lib/encrypt.js、/lib/axios.js  /api/support/api-encrypt/api-encrypt-api.js 等文件
- 后端请看：@ApiEncrypt 和 @ApiDecrypt 注解，具体请看 sa-common项目中的 net.lab1024.sa.common.module.support.apiencrypt 包
- demo请看：前端：/views/support/api-encrypt 目录，后端 请看：sa-admin项目的：net.lab1024.sa.admin.module.system.support.AdminApiEncryptController
</pre
      >
    </template>
  </a-alert>
  <br />
  <a-alert
    message="当前加密算法为：SM2，若想改为 AES，前端请修改 'lib/encrypt.js'文件中的EncryptObject，后端请修改 ApiEncryptService 的实现类"
    type="error"
  />
  <br />
  <!---------- 请求参数加密 begin ----------->
  <a-card title="一、请求加密 Demo">
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="姓名" class="smart-query-form-item">
          <a-input v-model:value="requestEncryptForm.name" placeholder="姓名" />
        </a-form-item>
        <a-form-item label="年龄" class="smart-query-form-item">
          <a-input-number v-model:value="requestEncryptForm.age" placeholder="年龄" />
        </a-form-item>
        <a-form-item class="smart-query-form-item">
          <a-button type="primary" @click="testRequestEncrypt"> 测试：请求加密</a-button>
        </a-form-item>
      </a-row>
      <a-row class="smart-query-form-row">
        <div v-if="requestEncryptFormStr">请求参数：{{ requestEncryptFormStr }}</div>
      </a-row>
      <a-row class="smart-query-form-row">
        <div v-if="requestEncryptFormEncryptStr">请求参数加密：{{ requestEncryptFormEncryptStr }}</div>
      </a-row>
      <a-row class="smart-query-form-row">
        <div v-if="requestEncryptResponse">返回结果（不加密）：{{ requestEncryptResponse }}</div>
      </a-row>
    </a-form>
  </a-card>
  <!---------- 请求参数加密 end ----------->
  <br />
  <!---------- 返回结果解密 begin ----------->
  <a-card title="二、返回加密 Demo">
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="姓名" class="smart-query-form-item">
          <a-input v-model:value="responseEncryptForm.name" placeholder="姓名" />
        </a-form-item>
        <a-form-item label="年龄" class="smart-query-form-item">
          <a-input-number v-model:value="responseEncryptForm.age" placeholder="年龄" />
        </a-form-item>
        <a-form-item class="smart-query-form-item">
          <a-button type="primary" @click="testResponseEncrypt"> 测试：返回加密 </a-button>
        </a-form-item>
      </a-row>
      <a-row class="smart-query-form-row">
        <div v-if="responseEncryptFormStr">请求参数： {{ responseEncryptFormStr }}</div>
      </a-row>
      <a-row class="smart-query-form-row">
        <div v-if="responseEncryptStr">返回结果：{{ responseEncryptStr }}</div>
      </a-row>
      <a-row class="smart-query-form-row">
        <div v-if="responseStr">返回结果 解密：{{ responseStr }}</div>
      </a-row>
    </a-form>
  </a-card>
  <!---------- 返回结果解密 end ----------->

  <br />
  <!---------- 请求和返回都加密 begin ----------->
  <a-card title="三、请求和返回都加密 Demo">
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item label="姓名" class="smart-query-form-item">
          <a-input v-model:value="form.name" placeholder="姓名" />
        </a-form-item>
        <a-form-item label="年龄" class="smart-query-form-item">
          <a-input-number v-model:value="form.age" placeholder="年龄" />
        </a-form-item>
        <a-form-item class="smart-query-form-item">
          <a-button type="primary" @click="testBoth"> 测试：请求和返回都加密 </a-button>
        </a-form-item>
      </a-row>
      <a-row class="smart-query-form-row">
        <div v-if="formStr">请求参数： {{ formStr }}</div>
      </a-row>
      <a-row class="smart-query-form-row">
        <div v-if="formEncryptStr">请求参数加密： {{ formEncryptStr }}</div>
      </a-row>
      <a-row class="smart-query-form-row">
        <div v-if="responseEncrypt">返回结果：{{ responseEncrypt }}</div>
      </a-row>
      <a-row class="smart-query-form-row">
        <div v-if="responseDecryptStr">返回结果 解密：{{ responseDecryptStr }}</div>
      </a-row>
    </a-form>
  </a-card>
  <!---------- 返回结果解密 end ----------->

  <br />
  <!---------- 测试数组 begin ----------->
  <a-card title="四、测试数组 Demo">
    <a-form class="smart-query-form">
      <a-row class="smart-query-form-row">
        <a-form-item class="smart-query-form-item">
          <a-button type="primary" @click="testArray"> 测试：数组加解密 </a-button>
        </a-form-item>
      </a-row>
      <a-row class="smart-query-form-row">
        <div v-if="arrayFormStr">请求参数： {{ arrayFormStr }}</div>
      </a-row>
      <a-row class="smart-query-form-row">
        <div v-if="arrayFormEncryptStr">请求参数加密： {{ arrayFormEncryptStr }}</div>
      </a-row>
      <a-row class="smart-query-form-row">
        <div v-if="arrayFormResponseEncrypt">返回结果：{{ arrayFormResponseEncrypt }}</div>
      </a-row>
      <a-row class="smart-query-form-row">
        <div v-if="arrayFormResponseDecryptStr">返回结果 解密：{{ arrayFormResponseDecryptStr }}</div>
      </a-row>
    </a-form>
  </a-card>
  <!---------- 返回结果解密 end ----------->
</template>
<script setup>
  import { reactive, ref } from 'vue';
  import { encryptApi } from '/@/api/support/api-encrypt-api';
  import { encryptData } from '/@/lib/encrypt';

  // ---------------------------- 第一种：请求参数加密 ----------------------------

  //请求参数加密
  const requestEncryptForm = reactive({
    age: 100, // 年龄
    name: '卓大', //姓名
  });

  // 参数字符串
  const requestEncryptFormStr = ref('');
  // 参数字符串 加密
  const requestEncryptFormEncryptStr = ref('');
  // 返回结果
  const requestEncryptResponse = ref('');

  async function testRequestEncrypt() {
    // 参数加密
    requestEncryptFormStr.value = JSON.stringify(requestEncryptForm);
    requestEncryptFormEncryptStr.value = encryptData(requestEncryptForm);

    // 发送请求
    const result = await encryptApi.testRequestEncrypt(requestEncryptForm);
    requestEncryptResponse.value = JSON.stringify(result.data);
  }

  // ---------------------------- 第二种：返回结果解密 ----------------------------

  const responseEncryptForm = reactive({
    age: 100, // 年龄
    name: '卓大', //姓名
  });

  const responseEncryptFormStr = ref('');
  const responseEncryptStr = ref('');
  const responseStr = ref('');

  async function testResponseEncrypt() {
    responseEncryptFormStr.value = JSON.stringify(responseEncryptForm);
    const result = await encryptApi.testResponseEncrypt(responseEncryptForm);
    responseEncryptStr.value = result.encryptData;
    responseStr.value = JSON.stringify(result.data);
  }

  // ---------------------------- 第三种：请求加密、返回解密 ----------------------------

  const form = reactive({
    age: 100, // 年龄
    name: '卓大', //姓名
  });

  const formStr = ref('');
  const formEncryptStr = ref('');
  const responseEncrypt = ref('');
  const responseDecryptStr = ref('');

  async function testBoth() {
    formStr.value = JSON.stringify(form);
    formEncryptStr.value = encryptData(form);
    const result = await encryptApi.testDecryptAndEncrypt(form);
    responseEncrypt.value = result.encryptData;
    responseDecryptStr.value = JSON.stringify(result.data);
  }

  // ---------------------------- 第四种：测试数组 ----------------------------

  const arrayForm = reactive([
    {
      age: 1, // 年龄
      name: '卓1', //姓名
    },
    {
      age: 2, // 年龄
      name: '卓2', //姓名
    },
    {
      age: 3, // 年龄
      name: '卓3', //姓名
    },
  ]);

  const arrayFormStr = ref('');
  const arrayFormEncryptStr = ref('');
  const arrayFormResponseEncrypt = ref('');
  const arrayFormResponseDecryptStr = ref('');

  async function testArray() {
    arrayFormStr.value = JSON.stringify(arrayForm);
    arrayFormEncryptStr.value = encryptData(arrayForm);
    const result = await encryptApi.testArray(arrayForm);
    arrayFormResponseEncrypt.value = result.encryptData;
    arrayFormResponseDecryptStr.value = JSON.stringify(result.data);
  }
</script>
