# 任务完成检查清单

## 通用检查项

### 代码质量
- [ ] 代码遵循项目命名规范
- [ ] 添加了必要的注释和文档字符串
- [ ] 没有遗留 TODO 或 FIXME 注释
- [ ] 没有调试代码 (console.log, System.out.println)
- [ ] 代码格式化符合项目标准

### 功能完整性
- [ ] 功能按需求完整实现
- [ ] 边界条件处理正确
- [ ] 错误处理完善
- [ ] 用户提示信息清晰

### 署名规范
- [ ] 新文件添加了署名信息 (作者、日期、版权)
- [ ] 重大修改更新了署名日期
- [ ] 署名格式符合规范 (YAML Front Matter / JavaDoc / 注释)

## 后端开发检查

### 代码规范
- [ ] 类名、方法名、变量名符合命名规范
- [ ] 使用了统一的 ResponseDTO 返回格式
- [ ] 权限注解 @SaCheckPermission 配置正确
- [ ] 事务注解 @Transactional 使用正确

### 数据库操作
- [ ] SQL语句经过测试验证
- [ ] 增量SQL脚本命名规范: `v[版本]-[功能].sql`
- [ ] SQL脚本包含回滚方案 (如需要)
- [ ] 数据库索引优化检查

### 菜单开发
- [ ] 检查了现有菜单，避免重复创建
- [ ] 菜单ID分配正确 (400-499 原型功能)
- [ ] 创建了增量SQL脚本
- [ ] 全量重建数据库并验证
- [ ] 角色授权配置正确

### API文档
- [ ] Controller方法添加了Swagger注解
- [ ] API接口在 Knife4j 中可见且可测试
- [ ] 接口权限标识与数据库 api_perms 一致

### 测试
- [ ] 单元测试覆盖核心逻辑
- [ ] 集成测试验证完整流程
- [ ] 异常场景测试完成

## 前端开发检查

### 代码规范
- [ ] 文件名、组件名、变量名符合命名规范
- [ ] TypeScript类型定义完整，无 any 类型滥用
- [ ] 使用了项目常量枚举，避免魔法数字
- [ ] API调用使用统一的 API 模块

### 路由配置
- [ ] 路由配置正确 (路径、组件、meta信息)
- [ ] 路由已集成到主路由文件 routers.ts
- [ ] 菜单权限与后端一致

### 组件开发
- [ ] 组件职责单一，可复用性强
- [ ] Props和Emits定义清晰
- [ ] 响应式数据使用正确 (ref, reactive)
- [ ] 组件生命周期管理正确

### 样式规范
- [ ] CSS类名使用 kebab-case
- [ ] 使用 scoped 样式避免污染
- [ ] 响应式布局适配移动端

### 代码质量检查
```bash
# ESLint检查
npx eslint src/ --ext .ts,.vue

# TypeScript类型检查
npx vue-tsc --noEmit

# Prettier格式化
npx prettier --write src/
```

## 数据库变更检查

### SQL脚本规范
- [ ] 脚本命名: `v[版本号]-[功能描述].sql`
- [ ] 包含作者和日期信息
- [ ] SET NAMES utf8mb4
- [ ] SET FOREIGN_KEY_CHECKS = 0/1
- [ ] 包含验证SQL语句

### 数据库执行流程
```bash
# 1. 执行主脚本
docker exec -i smart-admin-mysql mysql -uroot -pSmartAdmin666 smart_admin_v3 \
  < 数据库SQL脚本/mysql/smart_admin_v3.sql

# 2. 按顺序执行所有增量脚本
cd 数据库SQL脚本/mysql/sql-update-log
for f in v*.sql; do
  echo "执行: $f"
  docker exec -i smart-admin-mysql mysql -uroot -pSmartAdmin666 smart_admin_v3 < $f
done

# 3. 验证结果
docker exec -it smart-admin-mysql mysql -uroot -pSmartAdmin666 smart_admin_v3 -e \
  "SELECT COUNT(*) FROM t_menu; SELECT menu_id, menu_name FROM t_menu WHERE menu_id >= 400;"
```

## 功能测试检查

### 功能验证
- [ ] 前端页面正常显示
- [ ] 表单提交成功
- [ ] 列表查询正常
- [ ] 数据增删改查功能正常
- [ ] 权限控制生效
- [ ] 异常提示清晰

### 浏览器兼容性
- [ ] Chrome (主要)
- [ ] Firefox
- [ ] Safari
- [ ] Edge

### 性能检查
- [ ] 页面加载时间 < 3秒
- [ ] API响应时间 < 500ms
- [ ] 无内存泄漏
- [ ] 大数据量处理正常

## Git提交检查

### 提交前
- [ ] git status 检查变更文件
- [ ] git diff 查看具体修改
- [ ] 确认没有敏感信息 (密码、密钥)
- [ ] 确认没有临时文件 (node_modules, target)

### 提交规范
```bash
# 提交消息格式
feat: 新功能描述
fix: 修复问题描述
docs: 文档更新描述
style: 代码格式调整
refactor: 代码重构描述
test: 测试相关
chore: 构建工具或依赖更新
```

### 提交命令
```bash
git add .
git commit -m "feat: 实现企微聊天记录查询功能"
git push origin feature/wecom-chat-record
```

## 文档更新检查

### 需要更新的文档
- [ ] README.md (如有重大功能)
- [ ] API文档 (Swagger注解)
- [ ] 数据库变更记录
- [ ] 开发规范文档 (如有新规范)

### Claude项目文档
- [ ] .claude/CLAUDE.md (如有设计文档)
- [ ] 项目记忆文件更新 (重要架构变更)

## 部署前检查

### 环境配置
- [ ] 开发环境测试通过
- [ ] 测试环境配置正确
- [ ] 生产环境配置检查
- [ ] 环境变量配置正确

### 构建检查
```bash
# 后端打包
cd smart-admin-api-java17-springboot3
mvn clean package -P prod

# 前端构建
cd smart-admin-web-typescript
npm run build:prod
```

### 上线清单
- [ ] 数据库备份
- [ ] 增量SQL脚本准备
- [ ] 回滚方案准备
- [ ] 监控告警配置
- [ ] 上线通知相关人员

## 最终确认

- [ ] 所有检查项已完成
- [ ] 功能测试通过
- [ ] 代码已提交到Git
- [ ] 文档已更新
- [ ] 团队成员已知晓变更

**提交前最后确认**: 是否遗漏任何重要检查项？