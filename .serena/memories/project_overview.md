# SmartAdmin 项目概览

## 项目定位
SmartAdmin 是由中国·洛阳 1024创新实验室开发的企业级快速开发平台，以"高质量代码"为核心理念。

## 核心特点
- **安全合规**: 满足国家三级等保要求，支持国产数据库和国产加密算法
- **双技术栈**: 前端提供 JavaScript 和 TypeScript 双版本，后端提供 Java8+SpringBoot2 和 Java17+SpringBoot3 双版本
- **代码规范**: 开源验证过的《高质量代码思想》、《Vue3规范》、《Java规范》
- **完整功能**: 包含权限管理、OA办公、代码生成、数据变更记录、在线文档等企业级功能

## 技术架构

### 后端技术栈 (Java17 + SpringBoot3)
- **框架**: SpringBoot 3.5.4 + Java 17
- **权限**: Sa-Token 1.44.0 (轻量级权限框架)
- **ORM**: MyBatis-Plus 3.5.12
- **数据库**: MySQL (支持国产数据库: 达梦、金仓、南大通用等)
- **连接池**: Druid 1.2.25 + P6Spy 3.9.1 (SQL监控)
- **缓存**: Redis + Redisson 3.50.0
- **工具库**: Hutool 5.8.39, Apache Commons, Guava
- **API文档**: Knife4j 4.6.0 (OpenAPI 3.0)
- **加密**: BouncyCastle 1.80 (支持国产SM算法)

### 前端技术栈 (Vue3 + TypeScript)
- **框架**: Vue 3.4.27 + Vite 5.2.12
- **UI组件**: Ant Design Vue 4.2.5
- **状态管理**: Pinia 2.1.7
- **路由**: Vue Router 4.3.2
- **富文本**: WangEditor Next 5.6.34
- **图表**: ECharts 5.4.3
- **工具库**: Lodash, Day.js, Decimal.js
- **加密**: sm-crypto 0.3.13 (国产SM算法)

## 项目结构

```
smart-admin/
├── smart-admin-api-java17-springboot3/  # 后端项目 (Java17 + SpringBoot3)
│   ├── sa-base/                          # 基础模块
│   └── sa-admin/                         # 管理后台模块
├── smart-admin-web-typescript/           # 前端项目 (Vue3 + TypeScript)
│   ├── src/
│   │   ├── api/                          # API接口定义
│   │   ├── components/                   # 通用组件
│   │   ├── constants/                    # 常量枚举
│   │   ├── router/                       # 路由配置
│   │   ├── views/                        # 页面视图
│   │   └── layout/                       # 布局组件
│   └── public/                           # 静态资源
├── 数据库SQL脚本/                        # 数据库脚本
│   └── mysql/
│       ├── smart_admin_v3.sql           # 主脚本
│       └── sql-update-log/              # 增量脚本
└── .claude/                              # Claude 项目文档
    ├── smartadmin_menu_workflow.md      # 菜单开发流程
    └── smartadmin_database_config.md    # 数据库配置
```

## 开发环境
- **Node.js**: >= 18
- **Java**: 17
- **数据库**: MySQL (Docker容器: smart-admin-mysql)
- **操作系统**: macOS (Darwin 24.3.0)