# SmartAdmin V3 — Code Wiki 完整文档

> **项目名称**: SmartAdmin  
> **版本**: 3.0.0  
> **作者**: 1024创新实验室 (1024lab)  
> **官方网站**: [https://smartadmin.1024lab.net](https://smartadmin.1024lab.net)  
> **源码仓库**: [https://github.com/1024-lab/smart-admin](https://github.com/1024-lab/smart-admin)  
> **许可证**: MIT  

---

## 目录

1. [项目整体概述](#1-项目整体概述)
2. [项目仓库结构](#2-项目仓库结构)
3. [后端架构 (Java 17 + Spring Boot 3)](#3-后端架构)
4. [前端架构 (TypeScript + Vue 3)](#4-前端架构)
5. [移动端架构 (uni-app)](#5-移动端架构)
6. [数据库设计](#6-数据库设计)
7. [核心业务流程](#7-核心业务流程)
8. [项目运行方式](#8-项目运行方式)
9. [依赖关系总览](#9-依赖关系总览)

---

## 1. 项目整体概述

SmartAdmin 是由 **1024创新实验室** 开发的一套企业级中后台管理系统解决方案，涵盖 Web 管理后台、移动端 App 以及完整的数据持久层。项目采用前后端分离架构，是目前主流的 Java + Vue 全栈技术栈实现。

### 1.1 核心特性

| 特性 | 说明 |
|------|------|
| **多端覆盖** | Web 管理后台 (TypeScript/JavaScript) + 移动端 App (uni-app) |
| **RBAC 权限模型** | 基于用户-角色-菜单的细粒度权限控制，支持数据权限范围 |
| **代码生成器** | 内置可视化代码生成器，一键生成前后端 CRUD 代码 |
| **安全防护** | 接口加密/解密、登录限制、密码复杂度、三级等保支持 |
| **操作审计** | 完整的登录日志、操作日志、数据变更追踪 |
| **定时任务** | 内置分布式定时任务调度 |
| **国际化** | 支持中英文等多语言切换（vue-i18n） |
| **API 文档** | 集成 Knife4j + SpringDoc，自动生成接口文档 |

### 1.2 技术架构全景图

```
┌──────────────────────────────────────────────────────────────┐
│                      前端展示层                                │
│  ┌─────────────────────┐  ┌──────────────────────────────┐   │
│  │  Web 管理后台 (TS)   │  │  移动端 App (uni-app)         │   │
│  │  Vue 3 + AntDV 4    │  │  Vue 3 + uni-ui              │   │
│  │  Vite 5 + Pinia     │  │  Vite 4 + Pinia              │   │
│  └──────────┬──────────┘  └──────────────┬───────────────┘   │
│             │          HTTP/REST API      │                    │
│             └──────────────┬──────────────┘                    │
├────────────────────────────┼──────────────────────────────────┤
│                      后端服务层                                │
│  ┌─────────────────────────┴──────────────────────────────┐   │
│  │  sa-admin (业务模块)                                    │   │
│  │  ├── system/  (登录、员工、部门、菜单、角色、岗位)        │   │
│  │  ├── business/ (商品、分类、OA、企业、发票、公告)         │   │
│  │  └── support/ (配置、字典、日志、缓存、代码生成、文件)    │   │
│  ├──────────────────────────────────────────────────────────┤   │
│  │  sa-base (基础模块)                                      │   │
│  │  ├── config/   (框架配置: 数据源、Redis、Sa-Token、CORS) │   │
│  │  ├── domain/   (通用模型: ResponseDTO、PageResult)       │   │
│  │  ├── handler/  (全局异常、MyBatis填充)                   │   │
│  │  ├── constant/ (缓存键、请求头常量)                       │   │
│  │  └── util/     (通用工具类)                               │   │
│  └──────────────────────────────────────────────────────────┘   │
├──────────────────────────────────────────────────────────────┤
│                      数据存储层                                │
│  ┌─────────────────┐ ┌──────────────┐ ┌──────────────────┐   │
│  │  MySQL 8.0      │ │  Redis       │ │  AWS S3 / 本地    │   │
│  │  (主数据库)      │ │  (缓存/Session)│ │  (文件存储)       │   │
│  └─────────────────┘ └──────────────┘ └──────────────────┘   │
└──────────────────────────────────────────────────────────────┘
```

---

## 2. 项目仓库结构

```
/workspace/
├── smart-admin-api-java17-springboot3/    # 后端 (Java 17 + Spring Boot 3)
│   ├── pom.xml                            # 父 POM
│   ├── sa-base/                           # 基础模块
│   │   ├── pom.xml
│   │   └── src/main/java/net/lab1024/sa/base/
│   └── sa-admin/                          # 业务模块
│       ├── pom.xml
│       └── src/main/java/net/lab1024/sa/admin/
│
├── smart-admin-api-java8-springboot2/     # 后端 (Java 8 + Spring Boot 2, 兼容版本)
│
├── smart-admin-web-typescript/            # Web 前端 (TypeScript 版, 推荐)
│   ├── package.json
│   ├── vite.config.ts
│   └── src/
│
├── smart-admin-web-javascript/            # Web 前端 (JavaScript 版)
│
├── smart-app/                             # 移动端 (uni-app)
│   ├── package.json
│   ├── pages.json
│   ├── manifest.json
│   └── src/
│
└── 数据库SQL脚本/
    └── mysql/
        └── smart_admin_v3.sql             # MySQL 初始化脚本 (含建表+初始数据)
```

---

## 3. 后端架构

### 3.1 模块结构

后端采用 Maven 多模块结构，分为两个模块：

| 模块 | groupId/artifactId | 版本 | 职责 |
|------|-------------------|------|------|
| **sa-parent** | `net.lab1024:sa-parent` | 3.0.0 | 父 POM，统一依赖版本管理 |
| **sa-base** | `net.lab1024:sa-base` | 3.0.0 | 基础模块：框架配置、通用工具、全局异常处理 |
| **sa-admin** | `net.lab1024:sa-admin` | 3.0.0 | 业务模块：所有业务 Controller/Service/Manager/Dao |

`sa-admin` 依赖 `sa-base`，启动类 `AdminApplication` 位于 `sa-admin` 模块。

### 3.2 技术栈 (后端)

| 技术 | 版本 | 说明 |
|------|------|------|
| **Java** | 17 | 运行环境 |
| **Spring Boot** | 3.5.4 | 核心框架 |
| **MyBatis Plus** | 3.5.12 | ORM 框架 |
| **Sa-Token** | 1.44.0 | 轻量级权限认证框架 |
| **MySQL** | 8.0+ | 关系型数据库 |
| **Druid** | 1.2.25 | 数据库连接池 |
| **Redis** | - | 缓存 / Session 存储 |
| **Redisson** | 3.50.0 | 分布式锁 |
| **Knife4j** | 4.6.0 | API 文档 |
| **FastExcel** | 1.2.0 | Excel 导入导出 |
| **Hutool** | 5.8.39 | 通用工具库 |
| **Log4j2** | - | 日志框架 |
| **Caffeine** | - | 本地缓存 |
| **AWS SDK S3** | 2.42.6 | 对象存储 |
| **ip2region** | 2.7.0 | IP 地址解析 |
| **SmartDB** | 1.2.0 | 1024 自研数据库工具 |

### 3.3 启动类

[AdminApplication.java](file:///workspace/smart-admin-api-java17-springboot3/sa-admin/src/main/java/net/lab1024/sa/admin/AdminApplication.java)

```java
@EnableCaching
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("net.lab1024.sa")
@MapperScan(value = "net.lab1024.sa", annotationClass = Mapper.class)
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class AdminApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AdminApplication.class);
        application.addListeners(new LogVariableListener(), new Ip2RegionListener());
        application.run(args);
    }
}
```

**关键注解说明**:
- `@EnableCaching` — 启用 Spring Cache（配合 Caffeine + Redis）
- `@EnableScheduling` — 启用定时任务
- `@EnableAspectJAutoProxy` — 启用 AOP（操作日志切面）
- `@ComponentScan("net.lab1024.sa")` — 扫描 sa-base 和 sa-admin 的组件
- `@MapperScan` — 扫描 MyBatis Mapper 接口
- 排除 `UserDetailsServiceAutoConfiguration` — 因为使用 Sa-Token 而非 Spring Security

### 3.4 sa-base 基础模块详解

#### 3.4.1 包结构

```
net.lab1024.sa.base/
├── common/
│   ├── annotation/          # 自定义注解
│   │   ├── NoNeedLogin      # 标记无需登录的接口
│   │   └── ...
│   ├── code/                # 错误码定义
│   │   ├── ErrorCode        # 错误码接口
│   │   ├── SystemErrorCode  # 系统级错误码
│   │   ├── UserErrorCode    # 用户级错误码
│   │   └── UnexpectedErrorCode  # 未预期错误码
│   └── ...
├── config/                  # 框架配置类
│   ├── TokenConfig          # Sa-Token 令牌配置
│   ├── MybatisPlusConfig    # MyBatis Plus 分页插件/自动填充
│   ├── RedisConfig          # Redis 序列化/连接配置
│   ├── DataSourceConfig     # 数据源/Druid 配置
│   ├── CorsFilterConfig     # CORS 跨域配置
│   ├── SwaggerConfig        # Knife4j API 文档配置
│   ├── JsonConfig           # JSON 序列化配置
│   ├── AsyncConfig          # 异步任务线程池
│   ├── FileConfig           # 文件上传配置
│   ├── ScheduleConfig       # 定时任务调度配置
│   ├── RepeatSubmitConfig   # 防重复提交配置
│   ├── RestClientConfig     # HTTP 客户端配置
│   ├── HeartBeatConfig      # 心跳检测配置
│   ├── UrlConfig            # URL 地址配置
│   └── SystemEnvironmentConfig # 系统环境配置
├── constant/                # 常量定义
│   ├── CacheKeyConst        # 缓存键常量
│   └── RequestHeaderConst   # 请求头常量
├── domain/                  # 通用数据模型
│   ├── ResponseDTO          # 统一 API 响应对象 (ok, code, message, data)
│   ├── PageResult           # 分页结果 (pageNum, pageSize, total, list)
│   ├── PageParam            # 分页参数
│   ├── RequestUser          # 当前请求用户信息
│   └── ValidateData         # 数据校验模型
├── exception/
│   └── BusinessException    # 业务异常类
├── handler/
│   ├── GlobalExceptionHandler  # 全局异常处理器 (统一捕获异常并返回标准格式)
│   └── MybatisPlusFillHandler  # MyBatis Plus 自动填充 (创建时间/更新时间)
├── listener/
│   ├── Ip2RegionListener    # IP 解析监听器 (加载 ip2region.db)
│   ├── LogVariableListener  # 日志变量监听器
│   └── WebServerListener    # Web 服务器启动/关闭监听
└── util/                    # 工具类
    ├── SmartRequestUtil     # 请求工具类
    └── SmartStringUtil      # 字符串工具类
```

#### 3.4.2 关键类说明

| 类名 | 路径 | 职责 |
|------|------|------|
| **ResponseDTO** | `base/domain/ResponseDTO.java` | 统一 API 响应封装，提供 `ok()`, `error()` 等静态工厂方法 |
| **PageResult** | `base/domain/PageResult.java` | 分页查询结果封装，包含 `list`, `total`, `pageNum`, `pageSize` |
| **BusinessException** | `base/exception/BusinessException.java` | 业务异常，由全局异常处理器统一捕获处理 |
| **GlobalExceptionHandler** | `base/handler/GlobalExceptionHandler.java` | 全局异常拦截，处理 `BindException`、`MethodArgumentNotValidException`、`BusinessException` 等 |
| **TokenConfig** | `base/config/TokenConfig.java` | 配置 Sa-Token 的 token 有效期、是否允许并发登录等 |
| **MybatisPlusConfig** | `base/config/MybatisPlusConfig.java` | 配置 MyBatis Plus 分页插件和自动填充处理器 |
| **CorsFilterConfig** | `base/config/CorsFilterConfig.java` | 配置跨域请求头，允许前端跨域访问 |
| **SwaggerConfig** | `base/config/SwaggerConfig.java` | 配置 Knife4j + SpringDoc，生成 API 接口文档 |

### 3.5 sa-admin 业务模块详解

#### 3.5.1 模块分层架构

```
sa-admin/src/main/java/net/lab1024/sa/admin/
├── AdminApplication.java        # 启动类
├── config/                      # 业务层配置
│   └── OperateLogAspectConfig   # 操作日志 AOP 切面
├── interceptor/
│   └── AdminInterceptor         # 后台请求拦截器
├── module/
│   ├── system/                  # 系统管理模块
│   │   ├── login/               # 登录认证
│   │   │   ├── controller/LoginController
│   │   │   └── service/LoginService
│   │   ├── employee/            # 员工管理
│   │   │   ├── controller/EmployeeController
│   │   │   ├── service/EmployeeService
│   │   │   └── manager/EmployeeManager
│   │   ├── department/          # 部门管理
│   │   │   └── controller/DepartmentController
│   │   ├── menu/                # 菜单/权限管理
│   │   │   └── controller/MenuController
│   │   ├── role/                # 角色管理
│   │   │   ├── controller/RoleController
│   │   │   ├── service/RoleService
│   │   │   └── manager/RoleManager
│   │   ├── position/            # 岗位管理
│   │   │   └── controller/PositionController
│   │   └── support/             # 系统支撑功能 (Controllers)
│   │       ├── AdminConfigController       # 系统配置
│   │       ├── AdminDictController         # 数据字典
│   │       ├── AdminFileController         # 文件上传
│   │       ├── AdminCacheController        # 缓存管理
│   │       ├── AdminLoginLogController     # 登录日志
│   │       ├── AdminOperateLogController   # 操作日志
│   │       ├── AdminHeartBeatController    # 心跳检测
│   │       ├── AdminSmartJobController     # 定时任务
│   │       ├── AdminSerialNumberController # 序列号生成
│   │       ├── AdminReloadController       # 热重载
│   │       ├── AdminProtectController      # 三级等保
│   │       ├── AdminHelpDocController      # 帮助文档
│   │       └── AdminApiEncryptController   # API 加密
│   └── business/                # 业务模块
│       ├── category/            # 商品分类
│       │   └── controller/CategoryController
│       ├── goods/               # 商品管理
│       │   └── controller/GoodsController
│       └── oa/                  # OA 办公模块
│           ├── bank/controller/BankController
│           ├── enterprise/controller/EnterpriseController
│           ├── invoice/controller/InvoiceController
│           └── notice/controller/NoticeController
```

#### 3.5.2 三层架构说明

每个业务模块遵循 **Controller → Service → Manager → Mapper** 的分层架构：

| 层次 | 职责 | 命名规范 |
|------|------|----------|
| **Controller** | 接收 HTTP 请求，参数校验，调用 Service，返回 ResponseDTO | `XxxController` |
| **Service** | 业务逻辑编排，调用 Manager 和 Mapper | `XxxService` |
| **Manager** | 核心业务处理，复杂业务逻辑封装 | `XxxManager` |
| **Mapper** | 数据库操作，MyBatis Plus BaseMapper | `XxxMapper` (interface) |
| **Domain/Entity** | 数据库实体映射 | `XxxEntity` |
| **DTO/VO** | 数据传输对象 / 视图对象 | `XxxDTO`, `XxxVO` |

#### 3.5.3 核心 Controller 一览

| Controller | 路径前缀 | 功能描述 |
|------------|----------|----------|
| **LoginController** | `/login` | 用户登录、退出、获取验证码、获取当前用户信息 |
| **EmployeeController** | `/employee` | 员工 CRUD、批量删除、重置密码、状态更新 |
| **DepartmentController** | `/department` | 部门树查询、部门 CRUD |
| **MenuController** | `/menu` | 菜单树查询、菜单/按钮权限 CRUD |
| **RoleController** | `/role` | 角色 CRUD、角色菜单分配、角色员工分配 |
| **PositionController** | `/position` | 岗位 CRUD |
| **AdminDictController** | `/dict` | 数据字典增删改查 |
| **AdminConfigController** | `/config` | 系统参数配置管理 |
| **AdminFileController** | `/file` | 文件上传/下载 |
| **AdminCacheController** | `/cache` | 缓存查看/清除 |
| **AdminOperateLogController** | `/operateLog` | 操作日志分页查询 |
| **AdminLoginLogController** | `/loginLog` | 登录日志分页查询 |
| **AdminSmartJobController** | `/smartJob` | 定时任务管理 |
| **AdminSerialNumberController** | `/serialNumber` | 业务序列号生成规则管理 |
| **AdminHeartBeatController** | `/heartBeat` | 心跳检测记录查询 |
| **AdminReloadController** | `/reload` | 系统配置热重载 |
| **AdminProtectController** | `/protect` | 三级等保：数据脱敏、登录限制等 |
| **AdminHelpDocController** | `/helpDoc` | 帮助文档管理 |
| **AdminApiEncryptController** | `/apiEncrypt` | API 接口加密/解密管理 |
| **CategoryController** | `/category` | 商品分类树 CRUD |
| **GoodsController** | `/goods` | 商品 CRUD |
| **EnterpriseController** | `/enterprise` | 企业/客户信息管理 |
| **NoticeController** | `/notice` | 通知公告管理 |
| **BankController** | `/bank` | 银行信息管理 |
| **InvoiceController** | `/invoice` | 发票管理 |

### 3.6 权限认证体系

#### 3.6.1 Sa-Token 集成

项目使用 **Sa-Token** 替代 Spring Security 进行权限认证：

- **Token 模式**: 客户端通过 Header 传递 `token` 进行认证
- **Session 存储**: 使用 Redis 存储 Session，支持分布式
- **注解鉴权**:
  - `@NoNeedLogin` — 标记接口无需登录即可访问
  - `@SaCheckPermission` — 基于权限码的接口鉴权
  - `@SaCheckRole` — 基于角色的接口鉴权

#### 3.6.2 RBAC 权限模型

```
t_employee (用户)
    │
    └── t_role_employee (用户-角色关联)
            │
            └── t_role (角色)
                    │
                    ├── t_role_menu (角色-菜单关联)
                    │       │
                    │       └── t_menu (菜单/按钮权限)
                    │
                    └── t_role_data_scope (角色数据范围)
                            │
                            └── 控制角色可查看的数据范围
```

### 3.7 关键配置

#### application.yaml (dev 环境)

```yaml
project:
  name: sa-admin
  log-directory: ${localPath:/home}/logs/smart_admin_v3/${project.name}/${spring.profiles.active}

server:
  port: 1024
  servlet:
    context-path: /

spring:
  profiles:
    active: '@profiles.active@'
```

**注意**: 更多通用配置（数据库连接、Redis、文件上传等）位于 `sa-base` 模块的 `sa-base.yaml` 中，支持多环境配置（dev/test/pre/prod）。

---

## 4. 前端架构

### 4.1 技术栈 (TypeScript 版)

| 技术 | 版本 | 说明 |
|------|------|------|
| **Vue** | 3.4.27 | 渐进式前端框架 |
| **TypeScript** | 5.6.3 | 类型安全 |
| **Vite** | 5.2.12 | 构建工具 |
| **Pinia** | 2.1.7 | 状态管理 |
| **Vue Router** | 4.3.2 | 路由管理 |
| **Ant Design Vue** | 4.2.5 | UI 组件库 |
| **Axios** | 1.6.8 | HTTP 客户端 |
| **ECharts** | 5.4.3 | 图表库 |
| **vue-i18n** | 9.13.1 | 国际化 |
| **Less** | 4.2.0 | CSS 预处理器 |
| **WangEditor** | 5.6.34 | 富文本编辑器 |
| **Day.js** | 1.10.5 | 日期处理 |

### 4.2 目录结构

```
smart-admin-web-typescript/src/
├── main.ts                          # 应用入口，初始化 Vue、Pinia、Router
├── App.vue                          # 根组件
├── api/                             # API 接口层
│   ├── base-model/                  # 基础模型
│   │   ├── page-param-model.ts      # 分页参数
│   │   ├── page-result-model.ts     # 分页结果
│   │   ├── response-model.ts        # 统一响应
│   │   └── sort-item-model.ts       # 排序项
│   ├── system/                      # 系统管理 API
│   │   ├── login-api.ts             # 登录
│   │   ├── employee-api.ts          # 员工
│   │   ├── department-api.ts        # 部门
│   │   ├── menu-api.ts              # 菜单
│   │   ├── role-api.ts              # 角色
│   │   ├── role-menu-api.ts         # 角色菜单
│   │   ├── position-api.ts          # 岗位
│   │   └── home-api.ts              # 首页
│   ├── business/                    # 业务 API
│   │   ├── category/category-api.ts # 分类
│   │   ├── goods/goods-api.ts       # 商品
│   │   └── oa/                      # OA 办公
│   │       ├── bank-api.ts
│   │       ├── enterprise-api.ts
│   │       ├── invoice-api.ts
│   │       └── notice-api.ts
│   └── support/                     # 支撑功能 API
│       ├── config-api.ts            # 系统配置
│       ├── dict-api.ts              # 字典
│       ├── file-api.ts              # 文件
│       ├── cache-api.ts             # 缓存
│       ├── login-log-api.ts         # 登录日志
│       ├── operate-log-api.ts       # 操作日志
│       ├── job-api.ts               # 定时任务
│       ├── code-generator-api.ts    # 代码生成器
│       ├── serial-number-api.ts     # 序列号
│       ├── message-api.ts           # 消息
│       ├── feedback-api.ts          # 反馈
│       ├── help-doc-api.ts          # 帮助文档
│       ├── change-log-api.ts        # 更新日志
│       ├── data-tracer-api.ts       # 数据追踪
│       ├── data-masking-api.ts      # 数据脱敏
│       ├── api-encrypt-api.ts       # API 加密
│       ├── heart-beat-api.ts        # 心跳检测
│       ├── reload-api.ts            # 热重载
│       ├── level3-protect-api.ts    # 三级等保
│       ├── login-fail-api.ts        # 登录失败记录
│       └── table-column-api.ts      # 表格列配置
├── router/                          # 路由配置
│   ├── index.ts                     # 路由实例
│   ├── routers.ts                   # 路由汇总
│   ├── system/                      # 系统路由
│   │   ├── home.ts                  # 主页路由
│   │   └── login.ts                 # 登录路由
│   └── support/
│       └── help-doc.ts              # 帮助文档路由
├── store/                           # 状态管理 (Pinia)
│   ├── index.ts                     # Store 实例
│   └── modules/system/
│       ├── user.ts                  # 用户信息 store
│       ├── app-config.ts            # 应用配置 store
│       ├── role.ts                  # 角色 store
│       ├── dict.ts                  # 字典 store
│       └── spin.ts                  # 加载状态 store
├── views/                           # 页面视图组件
│   ├── system/
│   │   ├── login/login.vue          # 登录页
│   │   ├── home/index.vue           # 首页/工作台
│   │   ├── account/index.vue        # 个人账户
│   │   ├── employee/index.vue       # 员工管理
│   │   ├── department/department-list.vue  # 部门管理
│   │   ├── menu/menu-list.vue       # 菜单管理
│   │   ├── role/index.vue           # 角色管理
│   │   ├── position/position-list.vue # 岗位管理
│   │   └── 40X/                     # 错误页面
│   │       ├── 403.vue              # 无权限
│   │       └── 404.vue              # 页面不存在
│   ├── business/
│   │   ├── erp/
│   │   │   ├── catalog/             # 分类管理
│   │   │   └── goods/               # 商品管理
│   │   └── oa/
│   │       ├── enterprise/enterprise-list.vue  # 企业列表
│   │       └── notice/notice-list.vue          # 通知公告
│   └── support/
│       ├── config/config-list.vue           # 系统配置
│       ├── dict/index.vue                   # 字典管理
│       ├── file/file-list.vue               # 文件管理
│       ├── cache/cache-list.vue             # 缓存管理
│       ├── login-log/login-log-list.vue     # 登录日志
│       ├── operate-log/operate-log-list.vue # 操作日志
│       ├── job/job-list.vue                 # 定时任务
│       ├── code-generator/code-generator-list.vue  # 代码生成器
│       ├── serial-number/serial-number-list.vue    # 序列号
│       ├── message/message-list.vue         # 消息
│       ├── feedback/feedback-list.vue       # 反馈
│       ├── help-doc/                        # 帮助文档
│       ├── change-log/change-log-list.vue   # 更新日志
│       ├── level3protect/data-masking-list.vue # 数据脱敏
│       ├── api-encrypt/api-encrypt-index.vue    # API 加密
│       ├── heart-beat/heart-beat-list.vue       # 心跳检测
│       ├── reload/reload-list.vue               # 热重载
│       └── login-fail/login-fail-list.vue       # 登录失败
├── components/                      # 公共组件
│   ├── framework/                   # 框架级组件
│   │   ├── area-cascader/           # 地区级联选择
│   │   ├── boolean-select/          # 布尔值选择
│   │   ├── icon-select/             # 图标选择
│   │   ├── iframe/                  # 内嵌 iframe
│   │   ├── smart-enum-select/       # 枚举下拉选择
│   │   ├── smart-enum-checkbox/     # 枚举多选
│   │   ├── smart-enum-radio/        # 枚举单选
│   │   ├── smart-copy-icon/         # 复制图标
│   │   ├── smart-loading/           # 加载动画
│   │   ├── text-ellipsis/           # 文本省略
│   │   └── wangeditor/              # 富文本编辑器
│   ├── system/                      # 系统业务组件
│   │   ├── department-tree-select/  # 部门树选择
│   │   ├── employee-select/         # 员工选择
│   │   ├── employee-table-select-modal/  # 员工表格选择弹窗
│   │   ├── menu-tree-select/        # 菜单树选择
│   │   └── position-select/         # 岗位选择
│   ├── support/                     # 支撑功能组件
│   │   ├── dict-select/             # 字典选择
│   │   ├── dict-label/              # 字典标签
│   │   ├── dict-code-select/        # 字典编码选择
│   │   ├── file-upload/             # 文件上传
│   │   ├── file-preview/            # 文件预览
│   │   ├── file-preview-modal/      # 文件预览弹窗
│   │   ├── table-header-cell/       # 表格头部单元格
│   │   ├── table-operator/          # 表格操作列
│   │   └── data-tracer/             # 数据追踪
│   └── business/
│       └── category-tree-select/    # 分类树选择
├── lib/
│   └── axios.ts                     # Axios 封装 (拦截器、token 注入)
├── constants/                       # 常量定义
│   ├── index.ts                     # 常量导出
│   ├── common-const.ts              # 公共常量
│   ├── layout-const.ts              # 布局常量
│   ├── local-storage-key-const.ts   # localStorage 键名
│   ├── regular-const.ts             # 正则表达式
│   ├── system/                      # 系统常量
│   ├── business/                    # 业务常量
│   └── support/                     # 支撑功能常量
├── layout/                          # 布局组件
│   └── index.vue                    # 主布局 (侧边栏+顶栏+内容区)
├── theme/                           # 主题配置
│   └── custom-variables.js          # 自定义 Ant Design Vue 主题变量
├── i18n/                            # 国际化
│   ├── index.ts                     # i18n 实例
│   └── lang/                        # 语言包
└── utils/                           # 工具函数
```

### 4.3 核心模块说明

#### 4.3.1 路由系统

路由采用模块化配置，按业务领域拆分：

- `routers.ts` — 汇总所有路由，包含 `loginRouters`、`homeRouters`、`helpDocRouters` 以及 404/403 兜底路由
- 主路由采用嵌套结构，由 Layout 组件包裹，不同业务模块作为子路由加载
- 支持动态路由：根据后端返回的用户菜单权限动态生成可访问路由

#### 4.3.2 状态管理 (Pinia)

| Store 模块 | 文件 | 职责 |
|------------|------|------|
| **user** | `store/modules/system/user.ts` | 存储当前登录用户信息、token、权限列表 |
| **app-config** | `store/modules/system/app-config.ts` | 应用全局配置（主题、布局等） |
| **role** | `store/modules/system/role.ts` | 角色列表缓存 |
| **dict** | `store/modules/system/dict.ts` | 字典数据缓存 |
| **spin** | `store/modules/system/spin.ts` | 全局加载状态控制 |

#### 4.3.3 Axios 封装

`lib/axios.ts` 统一封装了 HTTP 请求：
- **请求拦截器**: 自动注入 `token` 请求头
- **响应拦截器**: 统一处理错误码、登录过期自动跳转
- 支持 `sm-crypto` 国密加密传输

#### 4.3.4 构建配置

`vite.config.ts` 关键配置：
- **别名**: `/@/` → `src/`，简化导入路径
- **代理**: 开发环境 `/` 代理到后端 API 地址（通过环境变量 `VITE_APP_API_URL` 配置）
- **构建优化**: 按 `node_modules` 包名分包，开启 `terser` 压缩
- **CSS**: Less 预处理，支持自定义 Ant Design Vue 主题变量

---

## 5. 移动端架构

### 5.1 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| **uni-app** | 3.0.0 | 跨平台开发框架 |
| **Vue** | 3.2.47 | 核心框架 |
| **Vite** | 4.0.3 | 构建工具 |
| **Pinia** | 2.0.36 | 状态管理 |
| **uni-ui** | 1.5.0 | uni-app 官方 UI 组件库 |
| **vue-i18n** | 9.1.9 | 国际化 |
| **Day.js** | 1.11.10 | 日期处理 |
| **crypto-js** | 4.1.1 | 加密工具 |
| **sm-crypto** | 0.3.13 | 国密加密 |

### 5.2 目录结构

```
smart-app/src/
├── main.js                  # 应用入口
├── App.vue                  # 根组件
├── pages.json               # 页面路由配置 & TabBar 定义
├── manifest.json            # 应用配置 (AppID、权限等)
├── pages/                   # 页面目录
│   ├── home/index           # 首页
│   ├── login/login          # 登录页
│   ├── mine/mine            # 我的页面
│   ├── enterprise/          # 企业/客户
│   │   ├── enterprise-list      # 客户线索列表
│   │   ├── enterprise-form      # 添加客户
│   │   └── enterprise-detail    # 客户详情
│   ├── notice/              # 通知公告
│   │   ├── notice-index         # 公告列表
│   │   └── notice-detail        # 公告详情
│   ├── goods/goods-index    # 商品列表
│   ├── message/message      # 消息列表
│   ├── form/form            # 提交表单
│   ├── select-people/       # 选择人员
│   ├── list/list            # 常见列表样式1
│   ├── list2/list           # 常见列表样式2
│   ├── order-detail/        # 运单详情
│   └── support/
│       ├── change-log/          # 版本更新
│       └── feedback/            # 意见反馈
├── api/                     # API 接口层
│   └── business/
│       ├── goods/goods-api.js
│       ├── enterprise/
│       ├── notice/
│       ├── feedback/
│       ├── file/
│       └── message/
├── components/              # 公共组件
│   ├── dict-select/         # 字典选择器
│   └── smart-card/          # 智能卡片
├── store/                   # 状态管理
│   └── modules/
│       └── system/
│           └── user.js      # 用户信息 store
├── constants/               # 常量定义
│   └── business/
│       └── erp/goods-const.js
├── utils/                   # 工具函数
│   └── str-util.js
├── uni_modules/             # uni-app 插件模块
│   ├── uni-icons/           # 图标组件
│   ├── uni-load-more/       # 加载更多
│   ├── uni-mescroll/        # 下拉刷新/上拉加载
│   └── uni-data-picker/     # 数据选择器
├── static/                  # 静态资源
│   └── images/tabbar/       # TabBar 图标
└── theme/                   # 主题样式
```

### 5.3 页面路由与 TabBar

`pages.json` 定义了完整的页面路由和底部导航：

| TabBar | 页面路径 | 标题 |
|--------|---------|------|
| 首页 | `pages/home/index` | 首页 |
| 常见列表1 | `pages/list/list` | 常见列表样式1 |
| 常见列表2 | `pages/list2/list` | 常见列表样式2 |
| 消息 | `pages/message/message` | 消息 |
| 我的 | `pages/mine/mine` | 我的 |

其他功能页面（登录、客户、商品、通知、版本更新、反馈等）通过导航跳转访问。

### 5.4 多端支持

`package.json` 中定义了丰富的构建脚本，支持以下平台：

- **H5** (Web 移动端)
- **App** (Android / iOS)
- **微信小程序** (mp-weixin)
- **支付宝小程序** (mp-alipay)
- **百度小程序** (mp-baidu)
- **字节跳动小程序** (mp-toutiao)
- **QQ 小程序** (mp-qq)
- 以及其他小程序平台

---

## 6. 数据库设计

### 6.1 数据库信息

- **数据库**: MySQL 8.0+
- **数据库名**: `smart_admin_v3`
- **字符集**: `utf8mb4`
- **初始化脚本**: `数据库SQL脚本/mysql/smart_admin_v3.sql`

### 6.2 核心表结构

#### 6.2.1 组织架构表

| 表名 | 说明 | 关键字段 |
|------|------|----------|
| **t_employee** | 员工表 | `employee_id`, `login_name`, `actual_name`, `phone`, `department_id`, `position_id`, `disabled_flag`, `administrator_flag` |
| **t_department** | 部门表 | `department_id`, `department_name`, `parent_id` (树形结构), `sort` |
| **t_position** | 岗位表 | `position_id`, `position_name`, `sort` |

#### 6.2.2 权限管理表

| 表名 | 说明 | 关键字段 |
|------|------|----------|
| **t_menu** | 菜单/权限表 | `menu_id`, `menu_name`, `menu_type` (菜单/按钮), `parent_id`, `path`, `component`, `perms`, `sort` |
| **t_role** | 角色表 | `role_id`, `role_name`, `role_code`, `remark` |
| **t_role_menu** | 角色-菜单关联表 | `role_id`, `menu_id` |
| **t_role_employee** | 角色-员工关联表 | `role_id`, `employee_id` |
| **t_role_data_scope** | 角色数据范围表 | `role_id`, `data_scope_type` (全部/部门/自定义) |

#### 6.2.3 系统配置表

| 表名 | 说明 | 关键字段 |
|------|------|----------|
| **t_config** | 系统参数配置表 | `config_key`, `config_value`, `remark` |
| **t_dict** | 字典分类表 | `dict_id`, `dict_code`, `dict_name` |
| **t_dict_data** | 字典数据项表 | `dict_data_id`, `dict_id`, `code`, `value`, `sort` |
| **t_file** | 文件信息表 | `file_id`, `file_name`, `file_path`, `file_size`, `file_type` |
| **t_serial_number** | 序列号生成规则表 | `serial_number_id`, `business_name`, `format`, `rule_type` |
| **t_serial_number_record** | 序列号使用记录表 | `serial_number_record_id`, `serial_number_id`, `record_date` |

#### 6.2.4 日志审计表

| 表名 | 说明 | 关键字段 |
|------|------|----------|
| **t_login_log** | 登录日志表 | `login_log_id`, `employee_id`, `login_name`, `login_ip`, `login_time`, `login_result` |
| **t_operate_log** | 操作日志表 | `operate_log_id`, `employee_id`, `module`, `content`, `operate_type`, `operate_time`, `ip` |
| **t_password_log** | 密码变更日志表 | `password_log_id`, `employee_id`, `password_change_time` |

#### 6.2.5 业务数据表

| 表名 | 说明 | 关键字段 |
|------|------|----------|
| **t_category** | 商品分类表 | `category_id`, `category_name`, `parent_id` (树形), `category_type` |
| **t_goods** | 商品表 | `goods_id`, `goods_name`, `category_id`, `price`, `status` |
| **t_enterprise** | 企业/客户表 | `enterprise_id`, `enterprise_name`, `contact`, `phone` |
| **t_notice** | 通知公告表 | `notice_id`, `title`, `content`, `publish_time`, `notice_type` |
| **t_bank** | 银行信息表 | 银行基本信息 |
| **t_invoice** | 发票表 | 发票信息 |
| **t_message** | 消息表 | 系统消息 |
| **t_feedback** | 反馈表 | 用户反馈信息 |
| **t_change_log** | 系统更新日志表 | `change_log_id`, `update_version`, `type`, `content` |

#### 6.2.6 代码生成与工具表

| 表名 | 说明 | 关键字段 |
|------|------|----------|
| **t_code_generator_config** | 代码生成器配置表 | `table_name`, `basic`, `fields`, `query_fields` |
| **t_table_column** | 表列信息表 | 数据库表结构元数据 |

### 6.3 通用字段约定

所有业务表均包含以下标准字段：

| 字段 | 类型 | 说明 |
|------|------|------|
| `create_time` | `datetime` | 创建时间，自动填充 |
| `update_time` | `datetime` | 更新时间，自动更新 |
| `deleted_flag` | `tinyint` | 逻辑删除标记 (0=未删除, 1=已删除) |
| `disabled_flag` | `tinyint` | 禁用标记 (0=启用, 1=禁用) |
| `remark` | `varchar(255)` | 备注信息 |

---

## 7. 核心业务流程

### 7.1 用户登录流程

```
1. 前端发送 POST /login {loginName, password, captcha}
       │
2. LoginController 接收请求
       │
3. LoginService.auth() 验证用户名密码
       │   ├── 验证验证码 (Redis)
       │   ├── 查询 t_employee 表
       │   ├── BCrypt 密码比对 (spring-security-crypto)
       │   └── 检查账号状态 (disabled_flag)
       │
4. 登录成功 → Sa-Token 生成 token
       │   ├── 将用户信息存入 Redis Session
       │   └── 返回 token 给前端
       │
5. 记录登录日志 → t_login_log
       │
6. 前端存储 token → 后续请求自动携带在 Header 中
```

### 7.2 权限校验流程

```
1. 前端请求携带 token (Header: token=xxx)
       │
2. Sa-Token 拦截器校验 token 有效性
       │   ├── 无效 → 返回 401
       │   └── 有效 → 获取用户角色和权限列表
       │
3. 接口注解鉴权
       │   ├── @NoNeedLogin → 跳过鉴权
       │   ├── @SaCheckPermission("system:employee:add") → 检查权限码
       │   └── @SaCheckRole("admin") → 检查角色
       │
4. 数据权限过滤
       │   └── 根据 t_role_data_scope 配置的数据范围过滤查询结果
       │
5. 操作日志记录
       │   └── OperateLogAspectConfig AOP 切面记录操作日志
```

### 7.3 代码生成流程

```
1. 选择数据库表 → 配置生成参数
       │
2. 配置字段信息 (名称、类型、查询方式、列表显示、表单类型等)
       │
3. 保存配置 → t_code_generator_config
       │
4. 点击生成 → 后端使用 Velocity 模板引擎渲染代码
       │   ├── 生成 Java 代码: Entity, Mapper, Service, Manager, Controller
       │   ├── 生成 Vue 页面: list.vue, form.vue
       │   └── 生成 API 文件: xxx-api.ts
       │
5. 下载生成的代码文件
```

---

## 8. 项目运行方式

### 8.1 环境要求

| 组件 | 版本要求 |
|------|---------|
| **JDK** | 17+ |
| **Maven** | 3.6+ |
| **Node.js** | 18+ |
| **MySQL** | 8.0+ |
| **Redis** | 5.0+ |

### 8.2 后端启动

```bash
# 1. 初始化数据库
# 执行 数据库SQL脚本/mysql/smart_admin_v3.sql

# 2. 修改数据库连接配置
# 编辑 sa-base/src/main/resources/sa-base.yaml (或对应环境的配置)
# 修改 MySQL 连接地址、用户名、密码
# 修改 Redis 连接地址

# 3. 编译打包
cd smart-admin-api-java17-springboot3
mvn clean package -DskipTests

# 4. 启动应用
java -jar sa-admin/target/sa-admin-3.0.0.jar --spring.profiles.active=dev

# 或者开发环境直接运行
cd sa-admin
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**默认端口**: `1024`

**API 文档地址**: `http://localhost:1024/doc.html` (Knife4j)

### 8.3 前端启动

```bash
# TypeScript 版 (推荐)
cd smart-admin-web-typescript

# 安装依赖
npm install

# 开发环境启动 (默认代理到后端 API)
npm run dev

# 指定环境
npm run localhost    # 本地环境
npm run build:test   # 测试环境构建
npm run build:pre    # 预发布环境构建
npm run build:prod   # 生产环境构建
```

**默认端口**: `8081`

**注意**: 需要配置环境变量 `VITE_APP_API_URL` 指定后端 API 地址（在 `.env` 文件中配置）。

### 8.4 移动端启动

```bash
cd smart-app

# 安装依赖
npm install

# H5 开发
npm run dev:h5

# 微信小程序开发
npm run dev:mp-weixin

# App 开发
npm run dev:app
```

### 8.5 多环境配置

后端支持多环境配置，通过 `spring.profiles.active` 切换：

| 环境 | 配置目录 | 说明 |
|------|---------|------|
| `dev` | `sa-admin/src/main/resources/dev/` | 开发环境 |
| `test` | `sa-admin/src/main/resources/test/` | 测试环境 |
| `pre` | `sa-admin/src/main/resources/pre/` | 预发布环境 |
| `prod` | `sa-admin/src/main/resources/prod/` | 生产环境 |

---

## 9. 依赖关系总览

### 9.1 后端核心依赖图

```
sa-admin (业务模块)
  └── sa-base (基础模块)
        ├── spring-boot-starter-web          (Web 框架)
        ├── spring-boot-starter-aop           (AOP 切面)
        ├── spring-boot-starter-data-redis    (Redis 集成)
        ├── spring-boot-starter-validation    (参数校验)
        ├── spring-boot-starter-log4j2        (日志)
        ├── spring-boot-starter-mail          (邮件)
        ├── spring-security-crypto            (密码加密)
        ├── sa-token-spring-boot3-starter     (权限认证)
        ├── sa-token-redis-jackson            (Redis Session)
        ├── mybatis-plus-spring-boot3-starter (ORM)
        ├── mybatis-plus-jsqlparser           (SQL 解析)
        ├── mysql-connector-j                 (MySQL 驱动)
        ├── druid-spring-boot-3-starter       (连接池)
        ├── redisson-spring-boot-starter      (分布式锁)
        ├── caffeine                          (本地缓存)
        ├── knife4j-openapi3-jakarta          (API 文档)
        ├── fastjson                          (JSON 处理)
        ├── fastexcel + poi                   (Excel 处理)
        ├── hutool-all                        (工具库)
        ├── guava                             (Google 工具库)
        ├── commons-lang3 / commons-io / commons-collections4
        ├── awssdk-s3                         (对象存储)
        ├── velocity-engine-core              (代码生成模板引擎)
        ├── freemarker                        (模板引擎)
        ├── ip2region                         (IP 解析)
        ├── bcprov-jdk18on                    (加密算法)
        ├── smartdb                           (自研数据库工具)
        ├── jsoup                             (HTML 解析)
        ├── tika-core                         (文件类型检测)
        └── httpclient5                       (HTTP 客户端)
```

### 9.2 前端核心依赖图

```
smart-admin-web-typescript
  ├── vue 3.4.27                    (核心框架)
  ├── ant-design-vue 4.2.5          (UI 组件库)
  ├── pinia 2.1.7                   (状态管理)
  ├── vue-router 4.3.2              (路由)
  ├── axios 1.6.8                   (HTTP 请求)
  ├── vite 5.2.12                   (构建工具)
  ├── typescript 5.6.3              (类型系统)
  ├── vue-i18n 9.13.1               (国际化)
  ├── echarts 5.4.3                 (图表)
  ├── less 4.2.0                    (CSS 预处理)
  ├── dayjs 1.10.5                  (日期处理)
  ├── lodash 4.17.21                (工具库)
  ├── crypto-js 4.1.1               (加密)
  ├── sm-crypto 0.3.13              (国密加密)
  ├── @wangeditor-next/editor       (富文本编辑器)
  ├── diff + diff2html              (文本差异对比)
  ├── nprogress                     (进度条)
  ├── sortablejs                    (拖拽排序)
  └── clipboard                     (剪贴板)
```

### 9.3 移动端核心依赖图

```
smart-app
  ├── @dcloudio/uni-app 3.0.0       (uni-app 框架)
  ├── vue 3.2.47                    (核心框架)
  ├── pinia 2.0.36                  (状态管理)
  ├── vite 4.0.3                    (构建工具)
  ├── @dcloudio/uni-ui 1.5.0        (UI 组件库)
  ├── vue-i18n 9.1.9                (国际化)
  ├── dayjs 1.11.10                 (日期处理)
  ├── lodash 4.17.21                (工具库)
  ├── crypto-js 4.1.1               (加密)
  └── sm-crypto 0.3.13              (国密加密)
```

---

## 附录

### A. 项目版本说明

| 版本 | 发布时间 | 主要变更 |
|------|---------|----------|
| v1.0.0 | 2019-11-01 | 初始版本：人员管理、系统设置、心跳服务、定时任务 |
| v2.0.0 | 2022-10-22 | 重大升级：架构重构 |
| v3.0.0 | 2024-01-01 | 当前版本：从 Spring Security 迁移到 Sa-Token，升级 AntDV 4.x、Vite 5，新增 API 加密、三级等保、Knife4j 文档 |

### B. 相关链接

- **官方网站**: [https://smartadmin.1024lab.net](https://smartadmin.1024lab.net)
- **GitHub 仓库**: [https://github.com/1024-lab/smart-admin](https://github.com/1024-lab/smart-admin)
- **1024创新实验室**: [https://1024lab.net](https://1024lab.net)

### C. 项目作者

- **作者**: 1024创新实验室-主任：卓大
- **微信**: zhuoda1024
- **邮箱**: lab1024@163.com

---

> **文档生成时间**: 2026-06-30  
> **文档版本**: 1.0.0  
> **基于项目版本**: SmartAdmin V3.0.0