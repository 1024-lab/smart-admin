import { convertUpperCamel } from '/@/utils/str-util';

// -------------------------------- java 类型 --------------------------------
export const JavaTypeMap = new Map();
JavaTypeMap.set('int', 'Integer');
JavaTypeMap.set('tinyint', 'Integer');
JavaTypeMap.set('smallint', 'Integer');
JavaTypeMap.set('integer', 'Integer');
JavaTypeMap.set('year', 'Integer');
JavaTypeMap.set('bigint', 'Long');
JavaTypeMap.set('float', 'BigDecimal');
JavaTypeMap.set('double', 'BigDecimal');
JavaTypeMap.set('decimal', 'BigDecimal');
JavaTypeMap.set('char', 'String');
JavaTypeMap.set('varchar', 'String');
JavaTypeMap.set('tinytext', 'String');
JavaTypeMap.set('text', 'String');
JavaTypeMap.set('longtext', 'String');
JavaTypeMap.set('blob', 'String');
JavaTypeMap.set('date', 'LocalDate');
JavaTypeMap.set('datetime', 'LocalDateTime');

export const JavaTypeList = [
  'Boolean', //
  'Integer', //
  'Long', //
  'Double', //
  'String', //
  'BigDecimal', //
  'LocalDate', //
  'LocalDateTime', //
];

export function getJavaType(dataType) {
  return JavaTypeMap.get(dataType);
}

// -------------------------------- js 类型 --------------------------------
export const JsTypeMap = new Map();
JsTypeMap.set('int', 'Number');
JsTypeMap.set('tinyint', 'Number');
JsTypeMap.set('smallint', 'Number');
JsTypeMap.set('integer', 'Number');
JsTypeMap.set('year', 'Number');
JsTypeMap.set('bigint', 'Number');
JsTypeMap.set('float', 'Number');
JsTypeMap.set('double', 'Number');
JsTypeMap.set('decimal', 'Number');
JsTypeMap.set('char', 'String');
JsTypeMap.set('varchar', 'String');
JsTypeMap.set('tinytext', 'String');
JsTypeMap.set('text', 'String');
JsTypeMap.set('longtext', 'String');
JsTypeMap.set('blob', 'String');
JsTypeMap.set('date', 'Date');
JsTypeMap.set('datetime', 'Date');

export const JsTypeList = [
  'Number', //
  'String', //
  'Date', //
  'Boolean', //
  'String', //
];

export function getJsType(dataType) {
  return JsTypeMap.get(dataType);
}

// -------------------------------- 前端组件 --------------------------------

export const FrontComponentMap = new Map();
FrontComponentMap.set('int', 'InputNumber');
FrontComponentMap.set('tinyint', 'BooleanSelect');
FrontComponentMap.set('smallint', 'InputNumber');
FrontComponentMap.set('integer', 'InputNumber');
FrontComponentMap.set('year', 'Date');
FrontComponentMap.set('bigint', 'InputNumber');
FrontComponentMap.set('float', 'InputNumber');
FrontComponentMap.set('double', 'InputNumber');
FrontComponentMap.set('decimal', 'InputNumber');
FrontComponentMap.set('char', 'Input');
FrontComponentMap.set('varchar', 'Input');
FrontComponentMap.set('tinytext', 'Input');
FrontComponentMap.set('text', 'Textarea');
FrontComponentMap.set('longtext', 'Textarea');
FrontComponentMap.set('blob', 'Upload');
FrontComponentMap.set('date', 'Date');
FrontComponentMap.set('datetime', 'DateTime');

export function getFrontComponent(dataType) {
  return FrontComponentMap.get(dataType);
}

// -------------------------------- 前端文件 --------------------------------

export const LANGUAGE_LIST = [
  'js', //
  'ts', //
  'java', //
];

export const JS_FILE_LIST = [
  'js/list.vue', //
  'js/form.vue', //
  'js/api.js', //
  'js/const.js', //
];

export const TS_FILE_LIST = [
  'ts/list.vue', //
  'ts/form.vue', //
  'ts/api.js', //
  'ts/const.js', //
];


// -------------------------------- 后端文件 --------------------------------

export const JAVA_DOMAIN_FILE_LIST = [
  'Entity.java', //
  'AddForm.java', //
  'UpdateForm.java', //
  'QueryForm.java', //
  'VO.java', //
];

export const JAVA_FILE_LIST = [
  'Controller.java', //
  'Service.java', //
  'Manager.java', //
  'Dao.java', //
  'Mapper.xml', //
  ...JAVA_DOMAIN_FILE_LIST,
];

// -------------------------------- 枚举enum --------------------------------

export function convertJavaEnumName(moduleName, columnName) {
  return moduleName + convertUpperCamel(columnName) + 'Enum';
}

/**
 * 检测是否有枚举
 */
export function checkExistEnum(comment) {
  if (!comment) {
    return false;
  }

  // 检测是否存在  [ ] 或者 【 】
  let leftBracketIndex = comment.indexOf('[');
  if (leftBracketIndex === -1) {
    leftBracketIndex = comment.indexOf('【');
  }

  let rightBracketIndex = comment.indexOf(']');
  if (rightBracketIndex === -1) {
    leftBracketIndex = comment.indexOf('】');
  }

  if (leftBracketIndex === -1 || rightBracketIndex === -1) {
    return false;
  }

  if (comment.indexOf(':') === -1) {
    return false;
  }

  return true;
}
