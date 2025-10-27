# SmartAdmin 常用开发命令

## 后端开发命令

### Maven命令
```bash
# 编译项目
cd smart-admin-api-java17-springboot3
mvn clean compile

# 打包项目 (默认dev环境)
mvn clean package

# 打包指定环境
mvn clean package -P test    # 测试环境
mvn clean package -P pre     # 预发布环境
mvn clean package -P prod    # 生产环境

# 跳过测试打包
mvn clean package -DskipTests

# 运行Spring Boot应用
mvn spring-boot:run

# 安装到本地仓库
mvn clean install
```

### 后端启动方式
1. **IDE启动**: 运行 `AdminApplication.java`
2. **Maven启动**: `mvn spring-boot:run`
3. **Jar包启动**: `java -jar target/sa-admin-dev-3.0.0.jar`

### 后端端口
- 应用端口: 8084
- API文档: http://localhost:8084/doc.html

## 前端开发命令

### NPM命令
```bash
cd smart-admin-web-typescript

# 安装依赖
npm install

# 本地开发 (localhost环境变量)
npm run localhost

# 开发环境
npm run dev

# 开发环境 (带日志输出)
npm run dev:log

# 构建测试环境
npm run build:test

# 构建预发布环境
npm run build:pre

# 构建生产环境
npm run build:prod
```

### 代码质量检查
```bash
# ESLint检查
npx eslint src/

# ESLint修复
npx eslint src/ --fix

# Prettier格式化
npx prettier --write src/

# Stylelint检查
npx stylelint "src/**/*.{css,less,vue}"

# Stylelint修复
npx stylelint "src/**/*.{css,less,vue}" --fix

# TypeScript类型检查
npx vue-tsc --noEmit
```

### 前端端口
- 开发端口: 8085
- 访问地址: http://localhost:8085

## 数据库管理命令

### Docker MySQL操作
```bash
# 连接MySQL容器
docker exec -it smart-admin-mysql mysql -uroot -pSmartAdmin666 smart_admin_v3

# 查看容器状态
docker ps | grep smart-admin-mysql

# 查看容器日志
docker logs smart-admin-mysql

# 重启MySQL容器
docker restart smart-admin-mysql
```

### 数据库初始化
```bash
# 执行主脚本 (会DROP TABLE重建)
docker exec -i smart-admin-mysql mysql -uroot -pSmartAdmin666 smart_admin_v3 \
  < 数据库SQL脚本/mysql/smart_admin_v3.sql

# 执行增量脚本
cd 数据库SQL脚本/mysql/sql-update-log
for f in v*.sql; do
  echo "执行: $f"
  docker exec -i smart-admin-mysql mysql -uroot -pSmartAdmin666 smart_admin_v3 < $f
done
```

### 常用SQL查询
```bash
# 查询菜单信息
docker exec -it smart-admin-mysql mysql -uroot -pSmartAdmin666 smart_admin_v3 -e \
  "SELECT menu_id, menu_name, parent_id FROM t_menu ORDER BY menu_id;"

# 查询原型功能菜单
docker exec -it smart-admin-mysql mysql -uroot -pSmartAdmin666 smart_admin_v3 -e \
  "SELECT menu_id, menu_name, parent_id FROM t_menu WHERE menu_id BETWEEN 400 AND 499;"

# 统计菜单数量
docker exec -it smart-admin-mysql mysql -uroot -pSmartAdmin666 smart_admin_v3 -e \
  "SELECT COUNT(*) as total_menus FROM t_menu;"
```

## Git命令

### 常规开发流程
```bash
# 查看状态
git status

# 查看分支
git branch

# 创建并切换到新分支
git checkout -b feature/功能名称

# 添加变更
git add .

# 提交变更
git commit -m "feat: 功能描述"

# 推送到远程
git push origin feature/功能名称

# 查看差异
git diff

# 查看提交历史
git log --oneline -10
```

## 项目完整启动流程

### 首次启动
```bash
# 1. 启动MySQL容器 (如果未启动)
docker start smart-admin-mysql

# 2. 初始化数据库
docker exec -i smart-admin-mysql mysql -uroot -pSmartAdmin666 smart_admin_v3 \
  < 数据库SQL脚本/mysql/smart_admin_v3.sql

# 3. 启动后端
cd smart-admin-api-java17-springboot3
mvn spring-boot:run

# 4. 启动前端 (新终端)
cd smart-admin-web-typescript
npm run localhost

# 5. 访问应用
# 前端: http://localhost:8085
# 后端API文档: http://localhost:8084/doc.html
```

### 日常开发启动
```bash
# 1. 确认MySQL运行
docker ps | grep smart-admin-mysql

# 2. 启动后端 (终端1)
cd smart-admin-api-java17-springboot3
mvn spring-boot:run

# 3. 启动前端 (终端2)
cd smart-admin-web-typescript
npm run localhost
```

## macOS特定命令

```bash
# 查看端口占用
lsof -i :8084
lsof -i :8085

# 杀死进程
kill -9 <PID>

# 查看系统信息
uname -a

# 清理node_modules
rm -rf node_modules && npm install

# 清理Maven缓存
rm -rf ~/.m2/repository
```