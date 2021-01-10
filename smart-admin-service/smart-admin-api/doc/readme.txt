1 超管默认账号
sa/123456

2 执行脚本：
先执行：src/main/resources/sql/smart-admin.sql
再执行：src/main/resources/sql/quartz_mysql_2.3.0.sql

3 除dev之外文件

4 刷新页面，获取权限是否走缓存
LoginService.getSession

5 test类中去掉代码生成run

6 前端百度统计