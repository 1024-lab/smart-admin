1 更新密码
EmployeeController.updatePwd

2 更新功能点
PrivilegeController functionSaveOrUpdate和menuBatchSave、batchSaveFunctionList

3 超管默认账号
sa/123456

4 执行脚本：
先执行：src/main/resources/sql/smart-admin.sql
再执行：src/main/resources/sql/quartz_mysql_2.3.0.sql

5 除dev之外文件

6 刷新页面，获取权限是否走缓存
LoginService.getSession

7 test类中去掉代码生成run
