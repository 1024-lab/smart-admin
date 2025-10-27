# SmartAdmin 代码风格与约定

## 署名规范

### Markdown文档
使用YAML Front Matter:
```markdown
---
title: 文档标题
author: wangxiao
company: 子午线高科智能科技
date: 2025-01-08
---
```

### Java代码
```java
/*
 * 类功能描述
 *
 * @Author:    wangxiao
 * @Date:      2025-01-08
 * @Copyright  子午线高科智能科技 2025
 */
public class ClassName {
    /**
     * 方法描述 @author wangxiao
     */
    public void methodName() {
        // 实现
    }
}
```

### TypeScript/JavaScript代码
```typescript
/*
 * 文件功能描述
 *
 * @Author:    wangxiao
 * @Date:      2025-01-08
 * @Copyright  子午线高科智能科技 2025
 */
export const functionName = () => {
  // 实现
};
```

### Vue组件
```vue
<!--
  组件功能描述

  @Author:    wangxiao
  @Date:      2025-01-08
  @Copyright  子午线高科智能科技 2025
-->
<template>
  <!-- 模板内容 -->
</template>
```

### SQL脚本
```sql
-- ==========================================
-- 功能: 功能描述
-- 作者: wangxiao
-- 日期: 2025-01-08
-- ==========================================
```

## 命名约定

### 后端命名 (Java)
- **包名**: 全小写，点分隔 (net.lab1024.sa.admin.module)
- **类名**: PascalCase (MenuController, UserService, MenuMapper)
- **接口名**: PascalCase，不使用I前缀 (UserService, not IUserService)
- **方法名**: camelCase (queryMenuList, addMenu, updateUser)
- **变量名**: camelCase (menuList, userId, userName)
- **常量**: UPPER_SNAKE_CASE (MAX_SIZE, DEFAULT_PAGE_SIZE)
- **枚举**: PascalCase类名 + UPPER_SNAKE_CASE值 (MenuTypeEnum.CATALOG)

### 前端命名 (TypeScript/Vue)
- **文件名**: kebab-case (menu-list.vue, user-api.ts)
- **组件名**: PascalCase (MenuList, UserForm)
- **函数/变量**: camelCase (getUserInfo, menuList)
- **常量**: UPPER_SNAKE_CASE (MAX_PAGE_SIZE, API_TIMEOUT)
- **CSS类名**: kebab-case (menu-list, user-form)
- **TypeScript接口**: PascalCase (MenuVO, UserQuery)

## 注释规范

### Java注释
```java
/**
 * 类级别JavaDoc注释
 * 
 * @author wangxiao
 */
public class MenuService {
    
    /**
     * 方法级别JavaDoc注释
     * 说明方法用途
     * 
     * @param form 表单参数
     * @return 执行结果
     * @author wangxiao
     */
    public ResponseDTO<String> addMenu(MenuAddForm form) {
        // 单行注释：具体实现说明
        return ResponseDTO.ok();
    }
}
```

### TypeScript注释
```typescript
/**
 * 函数说明
 * @param params 参数说明
 * @returns 返回值说明
 * @author wangxiao
 */
export const queryMenuList = (params: QueryParams) => {
  // 单行注释：实现说明
  return postRequest('/api/menu/list', params);
};
```

## 代码格式化

### Java格式化
- 缩进: 4空格
- 行宽: 120字符
- 大括号: K&R风格 (左大括号不换行)
- 导入顺序: java → javax → 第三方 → 项目内部

### TypeScript/Vue格式化 (Prettier)
- 缩进: 2空格
- 行宽: 150字符
- 引号: 单引号
- 分号: 必须使用
- 尾随逗号: ES5标准
- 大括号: 空格包围

## 目录组织规范

### 后端目录结构
```
module/
├── system/              # 系统模块
│   ├── menu/
│   │   ├── controller/  # 控制器
│   │   ├── service/     # 服务层
│   │   ├── manager/     # 管理层
│   │   ├── dao/         # 数据访问层
│   │   ├── domain/      # 领域对象
│   │   │   ├── entity/  # 实体类
│   │   │   ├── form/    # 表单对象
│   │   │   └── vo/      # 视图对象
│   │   └── constant/    # 常量枚举
```

### 前端目录结构
```
src/
├── views/
│   └── system/
│       └── menu/
│           ├── menu-list.vue       # 列表页
│           ├── menu-form.vue       # 表单弹窗
│           └── components/         # 页面私有组件
├── api/
│   └── system/
│       └── menu-api.ts             # API定义
├── constants/
│   └── system/
│       └── menu-const.ts           # 常量枚举
└── router/
    └── system/
        └── menu.ts                 # 路由配置
```

## 导入顺序规范

### Java导入顺序
```java
// 1. Java标准库
import java.util.List;
import java.util.Map;

// 2. 第三方库
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

// 3. 项目内部
import net.lab1024.sa.admin.module.system.menu.domain.vo.MenuVO;
import net.lab1024.sa.admin.module.system.menu.service.MenuService;
```

### TypeScript导入顺序
```typescript
// 1. Vue核心
import { ref, reactive } from 'vue';

// 2. 第三方库
import { message } from 'ant-design-vue';

// 3. 项目内部 - API
import { menuApi } from '/@/api/system/menu-api';

// 4. 项目内部 - 常量
import { MENU_TYPE_ENUM } from '/@/constants/system/menu-const';

// 5. 项目内部 - 组件
import MenuForm from './components/menu-form.vue';
```

## 返回值规范

### 后端统一返回格式
```java
// 成功返回
ResponseDTO.ok()                    // 无数据
ResponseDTO.ok(data)                // 有数据
ResponseDTO.okMsg("操作成功")        // 带消息

// 失败返回
ResponseDTO.error(UserErrorCode.USER_NOT_EXIST)
ResponseDTO.userErrorParam("参数错误")
```

### 前端API调用规范
```typescript
// 统一使用 async/await
const handleQuery = async () => {
  try {
    const res = await menuApi.queryPage(queryForm);
    if (res.data) {
      tableData.value = res.data.list;
    }
  } catch (error) {
    message.error('查询失败');
  }
};
```

## 常量枚举规范

### 后端枚举定义
```java
@Getter
@AllArgsConstructor
public enum MenuTypeEnum implements BaseEnum {
    CATALOG(1, "目录"),
    MENU(2, "菜单"),
    POINTS(3, "功能点");
    
    private final Integer value;
    private final String desc;
}
```

### 前端常量定义
```typescript
export const MENU_TYPE_ENUM = {
  CATALOG: { value: 1, label: '目录' },
  MENU: { value: 2, label: '菜单' },
  POINTS: { value: 3, label: '功能点' },
};
```

## 日志规范

### 后端日志 (Slf4j)
```java
@Slf4j
public class MenuService {
    public void addMenu(MenuAddForm form) {
        log.info("添加菜单: {}", form.getMenuName());
        log.warn("菜单名称重复: {}", form.getMenuName());
        log.error("添加菜单失败", exception);
    }
}
```

### 前端日志 (Console)
```typescript
// 开发环境可用，生产环境自动移除
console.log('查询参数:', queryForm);
console.warn('数据为空');
console.error('请求失败:', error);
```

## 测试规范

### 测试文件命名
- 后端: `XxxTest.java` (放在 src/test/java)
- 前端: `xxx.test.ts` (放在 __tests__ 目录)

### 测试方法命名
```java
// 格式: test + 方法名 + 预期结果
@Test
public void testAddMenu_Success() { }

@Test
public void testAddMenu_DuplicateName() { }
```