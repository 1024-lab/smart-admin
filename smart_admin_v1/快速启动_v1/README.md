### 快速启动
如果你是全栈开发者，你完全可以快速过一遍此文章，甚至也可以不用看，如果你是前端OR后端，请仔细阅读。
>*前置条件：*
>*1、java版本1.8+（含1.8）*
>*2、Mysql 5.7+(含5.7)*
>*3、Redis*
>*4、Maven*
>*5、idea 、eclipse 必须安装lombok*
>*6、nodejs*
>*7、npm*
>*8、vscode、webstorm*


### 1 拉取代码
从   [https://gitee.com/lab1024/smart-admin](https://gitee.com/lab1024/smart-admin) 拉取最新代码

### 2 启动后端
#### 2.1 执行Sql脚本（Mysql5.7+）
Sql脚本：`/smart-admin-service/smart-admin-api/src/main/resources/sql`
先执行 `smart-admin.sql`
再执行 `quartz_mysql_2.3.0.sql`

#### 2.2 启动Redis
如果有redis环境，可以直接忽略，如果没有，请安装：
Linux版本：[https://redis.io/download](https://redis.io/download)
Windows版本：[https://github.com/microsoftarchive/redis/releases](https://github.com/microsoftarchive/redis/releases)

#### 2.3 将后端项目导入idea或者eclipse （java8+）

将`smart-admin-service` 项目导入到idea或者eclipse中（<font color="red">以maven项目导入！</font>）

#### 2.4 修改配置文件
进入 `smart-admin-api` 项目，打开`src/main/resources/dev/application.properties`文件

（1）修改jdbc参数,改为你的mysql地址和账号
```
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/smart-admin-dev?autoReconnect=true&useServerPreparedStmts=false&rewriteBatchedStatements=true&characterEncoding=UTF-8&useSSL=false&allowMultiQueries=true&serverTimezone=UTC
spring.datasource.username=erp
spring.datasource.password=listen1015
```
(2)修改redis参数
```
spring.redis.database=1
spring.redis.host=127.0.0.1
spring.redis.jedis.pool.max-active=100
spring.redis.jedis.pool.min-idle=5
spring.redis.jedis.pool.max-idle=10
spring.redis.jedis.pool.max-wait=30000ms
spring.redis.port=6379
spring.redis.timeout=10000ms
spring.redis.password=
```
#### 2.5 启动，发射

启动前：检查redis和mysql没问题，sql脚本执行成功，jdbc和redis配置参数正确

找到`SmartAdminApplication`启动类，运行。
访问：[http://localhost:10086/smart-admin-api/swagger-ui.html](http://localhost:10086/smart-admin-api/swagger-ui.html) 能看到swagger文档
到此，后端启动成功！

### 3 启动前端
启动前，确保nodejs版本和npm版本支持 vue2.x

#### 3.1 安装依赖
命令行 进入`/smart-admin-web`，执行`npm install`命令，安装依赖

#### 3.2 启动local环境
安装依赖后，在`/smart-admin-web`目录，命令行执行 `npm run local`

#### 3.3 访问
访问 [http://localhost:8080](http://localhost:8080)  ， 账号：sa/123456



---
作者简介:
[卓大](http://zhuoluodada.cn)， 1024创新实验室主任，混迹于各个技术，熟悉点java，略懂点前端。