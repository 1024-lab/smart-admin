# 后端架构设计

## 项目模块结构

### sa-parent (父工程)
- **GroupId**: net.lab1024
- **ArtifactId**: sa-parent
- **Version**: 3.0.0
- **子模块**: sa-base, sa-admin

### sa-base (基础模块)
提供公共配置、工具类、基础功能支持

### sa-admin (管理后台模块)
业务功能实现，包含系统管理、业务模块等

## 四层架构

SmartAdmin 采用业内独创的四层架构设计:

```
Controller (控制层)
    ↓
Service (服务层)
    ↓
Manager (管理层) - SmartAdmin 特色
    ↓
DAO (数据访问层)
```

### 各层职责
1. **Controller**: 接收请求、参数校验、返回响应
2. **Service**: 业务逻辑编排、事务控制
3. **Manager**: 通用业务逻辑封装、可复用的业务片段
4. **DAO**: 数据库操作、SQL执行

## 代码包结构

```
net.lab1024.sa.admin/
├── config/           # 配置类
├── constant/         # 常量定义
├── interceptor/      # 拦截器
├── util/             # 工具类
└── module/           # 业务模块
    ├── system/       # 系统模块 (菜单、角色、权限等)
    ├── business/     # 业务模块
    └── prototype/    # 原型功能模块
```

## 多环境配置

Maven Profile 支持:
- **dev**: 开发环境 (默认激活)
- **test**: 测试环境
- **pre**: 预发布环境
- **prod**: 生产环境

配置文件位置: `src/main/resources/{profile}/`

## 数据库配置

### 连接信息
- **Host**: 127.0.0.1:3306
- **Database**: smart_admin_v3
- **Username**: root
- **Password**: SmartAdmin666
- **Driver**: P6SpyDriver (SQL监控)

### 连接池配置
- initial-size: 2
- min-idle: 2
- max-active: 10
- max-wait: 60000ms

## 权限框架 (Sa-Token)

### 权限注解
- `@SaCheckPermission("permission:code")`: 接口权限校验
- `@SaCheckLogin`: 登录校验
- `@SaCheckRole("role")`: 角色校验

### 权限标识规范
格式: `module:resource:action`
示例: `system:menu:add`, `wecom:chat:query`

## API文档

### Knife4j (Swagger UI)
- 访问地址: `http://localhost:8084/doc.html`
- OpenAPI 3.0 规范
- 支持在线调试

## 编码规范

### 命名规范
- **类名**: PascalCase (MenuController, MenuService)
- **方法名**: camelCase (queryMenuList, addMenu)
- **常量**: UPPER_SNAKE_CASE (MAX_SIZE, DEFAULT_VALUE)
- **包名**: lowercase (net.lab1024.sa.admin)

### 返回值规范
统一使用 `ResponseDTO<T>` 封装返回结果:
```java
ResponseDTO<String> addMenu(MenuAddForm form)
ResponseDTO<List<MenuVO>> queryMenuList()
```

### Lombok 使用
- `@Data`: 实体类
- `@Slf4j`: 日志
- `@RequiredArgsConstructor`: 构造器注入