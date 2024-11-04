/*
 * 代码生成 枚举
 *
 * @Author:    1024创新实验室-主任：卓大
 * @Date:      2022-09-03 22:09:10
 * @Wechat:    zhuda1024
 * @Email:     lab1024@163.com
 * @Copyright  1024创新实验室 （ https://1024lab.net ），Since 2012
 */

// 前端组件类型
export const CODE_FRONT_COMPONENT_ENUM = {
  INPUT: {
    value: 'Input',
    desc: '输入框',
  },
  INPUT_NUMBER: {
    value: 'InputNumber',
    desc: '数字输入框',
  },
  TEXTAREA: {
    value: 'Textarea',
    desc: '文本',
  },
  BOOLEAN_SELECT: {
    value: 'BooleanSelect',
    desc: '布尔下拉框',
  },
  ENUM_SELECT: {
    value: 'SmartEnumSelect',
    desc: '枚举下拉框',
  },
  DICT_SELECT: {
    value: 'DictSelect',
    desc: '字典下拉',
  },
  DATE: {
    value: 'Date',
    desc: '日期选择',
  },
  DATE_TIME: {
    value: 'DateTime',
    desc: '时间选择',
  },
  FILE_UPLOAD: {
    value: 'FileUpload',
    desc: '文件上传',
  },
};

// 新增、修改 页面类型
export const CODE_INSERT_AND_UPDATE_PAGE_ENUM = {
  MODAL: {
    value: 'modal',
    desc: '弹窗',
  },
  DRAWER: {
    value: 'drawer',
    desc: '抽屉',
  },
  PAGE: {
    value: 'Page',
    desc: '新页面',
  },
};

// 删除类型
export const CODE_DELETE_ENUM = {
  SINGLE: {
    value: 'Single',
    desc: '单个删除',
  },
  BATCH: {
    value: 'Batch',
    desc: '批量删除',
  },
  SINGLE_AND_BATCH: {
    value: 'SingleAndBatch',
    desc: '单个删除和批量删除',
  },
};

/**
 * 查询类型
 */
export const CODE_QUERY_FIELD_QUERY_TYPE_ENUM = {
  LIKE: {
    value: 'Like',
    desc: '模糊查询',
  },
  EQUAL: {
    value: 'Equal',
    desc: '等于查询',
  },
  DATE_RANGE: {
    value: 'DateRange',
    desc: '日期范围',
  },
  DATE: {
    value: 'Date',
    desc: '指定日期',
  },
  ENUM: {
    value: 'Enum',
    desc: '枚举',
  },
  DICT: {
    value: 'Dict',
    desc: '字典',
  },
};

export default {
  CODE_FRONT_COMPONENT_ENUM,
  CODE_INSERT_AND_UPDATE_PAGE_ENUM,
  CODE_DELETE_ENUM,
  CODE_QUERY_FIELD_QUERY_TYPE_ENUM
};
