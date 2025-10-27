# 前端架构设计

## 目录结构

```
smart-admin-web-typescript/
├── src/
│   ├── api/              # API接口定义 (按模块组织)
│   ├── assets/           # 静态资源
│   ├── components/       # 通用组件
│   ├── constants/        # 常量枚举 (vue-enum模式)
│   ├── hooks/            # Composition API Hooks
│   ├── layout/           # 布局组件 (业内最清晰的layout)
│   ├── lib/              # 第三方库封装
│   ├── router/           # 路由配置
│   │   ├── index.ts      # 路由主入口
│   │   ├── routers.ts    # 路由数组集合
│   │   ├── system/       # 系统路由
│   │   ├── business/     # 业务路由
│   │   └── prototype/    # 原型功能路由
│   ├── store/            # Pinia状态管理
│   ├── theme/            # 主题配置
│   ├── utils/            # 工具函数
│   ├── views/            # 页面视图
│   │   ├── system/       # 系统功能页面
│   │   ├── business/     # 业务功能页面
│   │   └── prototype/    # 原型功能页面
│   ├── App.vue           # 根组件
│   └── main.ts           # 入口文件 (业内最佳router加载方式)
├── public/               # 公共静态资源
├── .env.localhost        # 本地环境配置
├── .env.development      # 开发环境配置
├── .env.test             # 测试环境配置
├── .env.pre              # 预发布环境配置
├── .env.production       # 生产环境配置
└── vite.config.ts        # Vite配置
```

## 核心特色

### 1. 常量维护 (vue-enum)
拒绝魔法数字，使用枚举管理所有常量:
```typescript
// src/constants/module/constant-name.ts
export const STATUS_ENUM = {
  ENABLED: { value: 1, label: '启用' },
  DISABLED: { value: 0, label: '禁用' }
};
```

### 2. 多环境配置
独有的5个环境配置文件:
- `.env.localhost`: 本地开发 (独立后端服务)
- `.env.development`: 开发环境
- `.env.test`: 测试环境
- `.env.pre`: 预发布环境
- `.env.production`: 生产环境

### 3. 路由组织
模块化路由管理:
```typescript
// src/router/routers.ts
import { systemRouters } from './system/index';
import { businessRouters } from './business/index';
import { prototypeRouters } from './prototype/index';

export const routerArray = [
  ...systemRouters,
  ...businessRouters,
  ...prototypeRouters,
];
```

### 4. API接口管理
统一的API接口定义:
```typescript
// src/api/module/api-name.ts
export const moduleApi = {
  queryPage: (params) => postRequest('/api/path', params),
  add: (data) => postRequest('/api/path/add', data),
};
```

## 编码规范

### 命名规范
- **文件名**: kebab-case (menu-list.vue, user-api.ts)
- **组件名**: PascalCase (MenuList, UserForm)
- **变量/函数**: camelCase (menuList, getUserInfo)
- **常量**: UPPER_SNAKE_CASE (MAX_PAGE_SIZE, API_PREFIX)
- **CSS类名**: kebab-case (menu-list, user-form)

### Vue组件规范
```vue
<template>
  <!-- 模板内容 -->
</template>

<script setup lang="ts">
// 1. 导入
import { ref, reactive } from 'vue';

// 2. Props定义
const props = defineProps<{
  id: number;
}>();

// 3. Emits定义
const emit = defineEmits<{
  (e: 'update', value: any): void;
}>();

// 4. 响应式数据
const data = ref<Type>([]);

// 5. 计算属性
// 6. 方法定义
// 7. 生命周期
</script>

<style scoped lang="less">
/* 样式 */
</style>
```

### TypeScript规范
- 所有变量、参数、返回值必须有明确类型
- 使用接口定义数据结构
- 避免使用 `any` 类型

## 构建配置

### Vite配置亮点
- **路径别名**: `/@/` 映射到 `src/`
- **代理配置**: 开发环境代理到后端 (8084端口)
- **构建优化**: 
  - 自动去除 console 和 debugger
  - 按node_modules拆包
  - Terser压缩
- **端口**: 8085

### 构建命令
```bash
npm run localhost        # 本地开发 (localhost环境)
npm run dev             # 开发环境
npm run build:test      # 构建测试环境
npm run build:pre       # 构建预发布环境
npm run build:prod      # 构建生产环境
```

## 代码质量工具

### ESLint配置
- Parser: vue-eslint-parser
- 规则: Vue3 Essential + ESLint Recommended
- 支持 TypeScript

### Prettier配置
- printWidth: 150
- tabWidth: 2
- singleQuote: true
- semi: true
- trailingComma: 'es5'

### Stylelint配置
- Standard规则
- Order插件 (CSS属性排序)