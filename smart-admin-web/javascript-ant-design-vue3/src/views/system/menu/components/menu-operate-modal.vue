<!--
  * 菜单 表单弹窗
  *
  * @Author:    1024创新实验室-主任：卓大
  * @Date:      2022-06-12 20:11:39
  * @Wechat:    zhuda1024
  * @Email:     lab1024@163.com
  * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
-->
<template>
  <a-drawer
    :body-style="{ paddingBottom: '80px' }"
    :maskClosable="true"
    :title="form.menuId ? '编辑' : '添加'"
    :open="visible"
    :width="600"
    @close="onClose"
  >
    <a-form ref="formRef" :labelCol="{ span: labelColSpan }" :labelWrap="true" :model="form" :rules="rules">
      <a-form-item label="菜单类型" name="menuType">
        <a-radio-group v-model:value="form.menuType" button-style="solid">
          <a-radio-button v-for="item in MENU_TYPE_ENUM" :key="item.value" :value="item.value">
            {{ item.desc }}
          </a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item :label="form.menuType === MENU_TYPE_ENUM.CATALOG.value ? '上级目录' : '上级菜单'">
        <MenuTreeSelect ref="parentMenuTreeSelect" v-model:value="form.parentId" />
      </a-form-item>
      <!--      目录 菜单 start   -->
      <template v-if="form.menuType === MENU_TYPE_ENUM.CATALOG.value || form.menuType === MENU_TYPE_ENUM.MENU.value">
        <a-form-item label="菜单名称" name="menuName">
          <a-input v-model:value="form.menuName" placeholder="请输入菜单名称" />
        </a-form-item>
        <a-form-item label="菜单图标" name="icon">
          <IconSelect @updateIcon="selectIcon">
            <template #iconSelect>
              <a-input v-model:value="form.icon" placeholder="请输入菜单图标" style="width: 200px" />
              <component :is="$antIcons[form.icon]" class="smart-margin-left15" style="font-size: 20px" />
            </template>
          </IconSelect>
        </a-form-item>
        <a-form-item v-if="form.menuType === MENU_TYPE_ENUM.MENU.value" label="路由地址" name="path">
          <a-input v-model:value="form.path" placeholder="请输入路由地址" />
        </a-form-item>
        <template v-if="form.menuType === MENU_TYPE_ENUM.MENU.value">
          <a-form-item v-if="form.frameFlag" label="外链地址" name="frameUrl">
            <a-input v-model:value="form.frameUrl" placeholder="请输入外链地址" />
          </a-form-item>
          <a-form-item v-else label="组件地址" name="component" help="比如 商品列表：/business/erp/goods/goods-list.vue">
            <a-input v-model:value="form.component" placeholder="请输入组件地址 默认带有开头/@/views" />
          </a-form-item>
        </template>
        <a-form-item v-if="form.menuType === MENU_TYPE_ENUM.MENU.value" label="是否缓存" name="cacheFlag">
          <a-switch v-model:checked="form.cacheFlag" checked-children="开启缓存" un-checked-children="不缓存" />
        </a-form-item>
        <a-form-item v-if="form.menuType === MENU_TYPE_ENUM.MENU.value" label="是否外链" name="frameFlag">
          <a-switch v-model:checked="form.frameFlag" checked-children="是外链" un-checked-children="不是外链" />
        </a-form-item>
        <a-form-item label="显示状态" name="frameFlag">
          <a-switch v-model:checked="form.visibleFlag" checked-children="显示" un-checked-children="不显示" />
        </a-form-item>
        <a-form-item label="禁用状态" name="frameFlag">
          <a-switch v-model:checked="form.disabledFlag" checked-children="启用" un-checked-children="禁用" />
        </a-form-item>
      </template>
      <!--      目录 菜单 end   -->
      <!--      按钮 start   -->
      <template v-if="form.menuType === MENU_TYPE_ENUM.POINTS.value">
        <a-form-item label="功能点名称" name="menuName">
          <a-input v-model:value="form.menuName" placeholder="请输入功能点名称" />
        </a-form-item>
        <a-form-item label="功能点关联菜单">
          <MenuTreeSelect ref="contextMenuTreeSelect" v-model:value="form.contextMenuId" />
        </a-form-item>
        <a-form-item label="功能点状态" name="frameFlag">
          <a-switch v-model:checked="form.disabledFlag" checked-children="启用" un-checked-children="禁用" />
        </a-form-item>
        <a-form-item label="权限类型" name="permsType">
          <a-radio-group v-model:value="form.permsType">
            <a-radio v-for="item in MENU_PERMS_TYPE_ENUM" :key="item.value" :value="item.value">
              {{ item.desc }}
            </a-radio>
          </a-radio-group>
        </a-form-item>
        <a-form-item label="前端权限" name="webPerms" help="用于前端按钮等功能的展示和隐藏，搭配v-privilege使用">
          <a-input v-model:value="form.webPerms" placeholder="请输入前端权限" />
        </a-form-item>
        <a-form-item label="后端权限" name="apiPerms" help="后端@SaCheckPermission中的权限字符串，多个以英文逗号,分割">
          <a-input v-model:value="form.apiPerms" placeholder="请输入后端权限" />
        </a-form-item>
      </template>
      <!--      按钮 end   -->
      <a-form-item label="排序" name="sort" help="值越小越靠前">
        <a-input-number v-model:value="form.sort" :min="0" placeholder="请输入排序" style="width: 100px" />
      </a-form-item>
    </a-form>
    <div class="footer">
      <a-button style="margin-right: 8px" @click="onClose">取消</a-button>
      <a-button style="margin-right: 8px" type="primary" @click="onSubmit(false)">提交 </a-button>
      <a-button v-if="!form.menuId" type="primary" @click="onSubmit(true)">提交并添加下一个 </a-button>
    </div>
  </a-drawer>
</template>
<script setup>
  import { message } from 'ant-design-vue';
  import _ from 'lodash';
  import { computed, nextTick, reactive, ref } from 'vue';
  import MenuTreeSelect from './menu-tree-select.vue';
  import { menuApi } from '/@/api/system/menu-api';
  import IconSelect from '/@/components/framework/icon-select/index.vue';
  import { MENU_DEFAULT_PARENT_ID, MENU_PERMS_TYPE_ENUM, MENU_TYPE_ENUM } from '/@/constants/system/menu-const';
  import { smartSentry } from '/@/lib/smart-sentry';
  import { SmartLoading } from '/@/components/framework/smart-loading';

  // ----------------------- 以下是字段定义 emits props ------------------------
  // emit
  const emit = defineEmits(['reloadList']);

  // ----------------------- 展开、隐藏编辑窗口 ------------------------

  // 是否展示抽屉
  const visible = ref(false);

  const labelColSpan = computed(() => {
    if (form.menuType === MENU_TYPE_ENUM.POINTS.value) {
      return 6;
    }
    return 4;
  });

  const contextMenuTreeSelect = ref();
  const parentMenuTreeSelect = ref();

  //展开编辑窗口
  async function showDrawer(rowData) {
    Object.assign(form, formDefault);
    if (rowData && !_.isEmpty(rowData)) {
      Object.assign(form, rowData);
      if (form.parentId === MENU_DEFAULT_PARENT_ID) {
        form.parentId = null;
      }
    }
    visible.value = true;
    refreshParentAndContext();
  }

  function refreshParentAndContext() {
    nextTick(() => {
      if (contextMenuTreeSelect.value) {
        contextMenuTreeSelect.value.queryMenuTree();
      }
      if (parentMenuTreeSelect.value) {
        parentMenuTreeSelect.value.queryMenuTree();
      }
    });
  }

  // 隐藏窗口
  function onClose() {
    Object.assign(form, formDefault);
    formRef.value.resetFields();
    visible.value = false;
  }

  // ----------------------- form表单相关操作 ------------------------

  const formRef = ref();
  const formDefault = {
    menuId: undefined,
    menuName: undefined,
    menuType: MENU_TYPE_ENUM.CATALOG.value,
    icon: undefined,
    parentId: undefined,
    path: undefined,
    permsType: MENU_PERMS_TYPE_ENUM.SA_TOKEN.value,
    webPerms: undefined,
    apiPerms: undefined,
    sort: undefined,
    visibleFlag: true,
    cacheFlag: true,
    component: undefined,
    contextMenuId: undefined,
    disabledFlag: false,
    frameFlag: false,
    frameUrl: undefined,
  };
  let form = reactive({ ...formDefault });

  function continueResetForm() {
    refreshParentAndContext();
    const menuType = form.menuType;
    const parentId = form.parentId;
    const webPerms = form.webPerms;
    Object.assign(form, formDefault);
    formRef.value.resetFields();
    form.menuType = menuType;
    form.parentId = parentId;
    // 移除最后一个：后面的内容
    if (webPerms && webPerms.lastIndexOf(':')) {
      form.webPerms = webPerms.substring(0, webPerms.lastIndexOf(':') + 1);
    }
  }

  const rules = {
    menuType: [{ required: true, message: '菜单类型不能为空' }],
    permsType: [{ required: true, message: '权限类型不能为空' }],
    menuName: [
      { required: true, message: '菜单名称不能为空' },
      { max: 20, message: '菜单名称不能大于20个字符', trigger: 'blur' },
    ],
    frameUrl: [
      { required: true, message: '外链地址不能为空' },
      { max: 500, message: '外链地址不能大于500个字符', trigger: 'blur' },
    ],
    path: [
      { required: true, message: '路由地址不能为空' },
      { max: 100, message: '路由地址不能大于100个字符', trigger: 'blur' },
    ],
  };

  function validateForm(formRef) {
    return new Promise((resolve) => {
      formRef
        .validate()
        .then(() => {
          resolve(true);
        })
        .catch(() => {
          resolve(false);
        });
    });
  }

  const onSubmit = async (continueFlag) => {
    let validateFormRes = await validateForm(formRef.value);
    if (!validateFormRes) {
      message.error('参数验证错误，请仔细填写表单数据!');
      return;
    }
    SmartLoading.show();
    try {
      let params = _.cloneDeep(form);
      // 若无父级ID 默认设置为0
      if (!params.parentId) {
        params.parentId = 0;
      }
      if (params.menuId) {
        await menuApi.updateMenu(params);
      } else {
        await menuApi.addMenu(params);
      }
      message.success(`${params.menuId ? '修改' : '添加'}成功`);
      if (continueFlag) {
        continueResetForm();
      } else {
        onClose();
      }
      emit('reloadList');
    } catch (error) {
      smartSentry.captureError(error);
    } finally {
      SmartLoading.hide();
    }
  };

  function selectIcon(icon) {
    form.icon = icon;
  }

  // ----------------------- 以下是暴露的方法内容 ------------------------
  defineExpose({
    showDrawer,
  });
</script>
<style lang="less" scoped>
  .footer {
    position: absolute;
    right: 0;
    bottom: 0;
    width: 100%;
    border-top: 1px solid #e9e9e9;
    padding: 10px 16px;
    background: #fff;
    text-align: left;
    z-index: 1;
  }
</style>
