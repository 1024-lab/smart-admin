# 菜单开发快速指南

## 核心原则

1. **开闭原则**: 主脚本不修改，用增量脚本扩展
2. **全量重建**: 改了任何SQL必须全部重新执行验证
3. **检查先行**: 创建前必须检查现有菜单，避免重复

## 菜单ID分配规则

### ID段规划
| ID段 | 用途 | 示例 |
|------|------|------|
| 1-99 | 系统功能 | 登录、菜单、角色 |
| 100-199 | 监控服务 | 日志、监控、定时任务 |
| 200-299 | 业务功能 | OA、ERP、CRM |
| 300-399 | 支撑功能 | 文件、配置、通知 |
| **400-499** | **原型功能** ⭐ | 学员跟进、企微助手 |

### 原型功能ID详细分配
```
400-499: 原型功能专用
├── 400: 页面原型（一级目录）
├── 401-412: 学员跟进（已占用）
├── 413-429: 预留
├── 430-439: 企微助手（已占用）
└── 440-499: 其他原型功能
```

## 开发流程速查

### Step 0: 创建前检查
```bash
# 检查现有菜单
docker exec -it smart-admin-mysql mysql -uroot -pSmartAdmin666 smart_admin_v3 -e "
SELECT menu_id, menu_name, parent_id FROM t_menu
WHERE menu_id BETWEEN 400 AND 499 OR menu_name LIKE '%原型%'
ORDER BY menu_id;
"
```

### Step 1: 创建增量脚本
文件位置: `数据库SQL脚本/mysql/sql-update-log/v[版本号]-[功能描述].sql`

模板:
```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ==========================================
-- 功能: [功能描述]
-- 作者: wangxiao
-- 日期: [YYYY-MM-DD]
-- ==========================================

-- 插入菜单 (确保parent_id存在)
INSERT INTO t_menu VALUES (xxx, '菜单名', 1, 400, ...);

-- 角色授权
INSERT INTO t_role_menu (role_id, menu_id) VALUES (1, xxx);

SET FOREIGN_KEY_CHECKS = 1;
```

### Step 2: 全量重建数据库
```bash
# 1. 执行主脚本
docker exec -i smart-admin-mysql mysql -uroot -pSmartAdmin666 smart_admin_v3 \
  < 数据库SQL脚本/mysql/smart_admin_v3.sql

# 2. 执行所有增量脚本
cd 数据库SQL脚本/mysql/sql-update-log
for f in v*.sql; do
  echo "执行: $f"
  docker exec -i smart-admin-mysql mysql -uroot -pSmartAdmin666 smart_admin_v3 < $f
done

# 3. 验证
docker exec -it smart-admin-mysql mysql -uroot -pSmartAdmin666 smart_admin_v3 -e "
SELECT menu_id, menu_name FROM t_menu WHERE menu_id BETWEEN 400 AND 499;
"
```

### Step 3: 前端路由集成
1. 创建路由文件: `src/router/prototype/[模块].ts`
2. 集成到主路由: `src/router/routers.ts`

## 菜单表核心字段

| 字段 | 类型 | 说明 | 示例 |
|------|------|------|------|
| menu_id | bigint | 菜单ID | 430 |
| menu_name | varchar | 菜单名称 | '企微助手' |
| menu_type | int | 类型 | 1=目录, 2=菜单, 3=功能点 |
| parent_id | bigint | 父菜单ID | 400=页面原型 |
| path | varchar | 前端路由 | '/prototype/wecom' |
| component | varchar | Vue组件路径 | '/prototype/wecom/chat-record-list.vue' |
| api_perms | varchar | 权限标识 | 'wecom:chat:query' |
| visible_flag | tinyint | 是否显示 | 1=显示 |
| cache_flag | tinyint | 是否缓存 | 1=缓存 |

## 常见错误避免

### ❌ 错误1: 重复创建父级菜单
不检查就创建，导致ID 400 "页面原型"被重复创建

### ✅ 正确做法
先查询是否存在，使用现有的 parent_id=400

### ❌ 错误2: ID段分配错误
原型功能使用500+的ID

### ✅ 正确做法
原型功能必须使用 400-499

### ❌ 错误3: parent_id指向不存在的菜单
先创建子菜单，父菜单ID不存在

### ✅ 正确做法
先确保父菜单存在，再创建子菜单

## 故障排查

### 菜单不显示？
1. 检查 `t_role_menu` 是否授权
2. 检查 `visible_flag` 是否为 1
3. 检查前端 `routers.ts` 是否注册路由

### 接口报"无权限"？
1. 检查 `@SaCheckPermission` 和 `api_perms` 是否一致
2. 开发环境检查Mock用户 `administratorFlag = true`

## 开发检查清单

### 创建菜单前
- [ ] 查询现有菜单，避免重复
- [ ] 确认ID段分配 (400-499)
- [ ] 检查已占用ID

### 创建脚本时
- [ ] 命名规范: `v[版本]-[功能].sql`
- [ ] parent_id 确保存在
- [ ] menu_id 连续且在正确ID段
- [ ] 包含角色授权SQL

### 执行后
- [ ] 全量重建数据库
- [ ] 验证菜单插入成功
- [ ] 前端路由配置正确
- [ ] 功能测试通过

## 参考文档
详细文档: `.claude/smartadmin_menu_workflow.md`