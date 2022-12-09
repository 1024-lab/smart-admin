---
title: 2.3、Java规范 V2.0
date: 2022-09-07
---
## 阅读须知

<font color="#DC143C">**在阅读《Java规范》之前，请一定先阅读上一篇《规范导读 V2.0》！！！**</font>  ，[前往阅读](./basic.md)

## 一、Java 项目规范

### 1.1、Java 项目命名规范

全部采用小写方式， 以中划线分隔。

```java
正例：`mall-management-system / order-service-client / user-api`

反例：`mall_management-system / mallManagementSystem / orderServiceClient`
```

### 1.2、方法参数规范

无论是 `controller，service，manager，dao` 亦或是**其他 class 的**代码，每个方法最多 `5` 个参数，如果超出 `5` 个参数的话，要封装成 `javabean` 对象。

- 方便他人调用，降低出错几率。尤其是当参数是同一种类型，仅仅依靠顺序区分，稍有不慎便是灾难性后果，而且排查起来也极其恶心。
- 保持代码整洁、清晰度。当一个个方法里充斥着一堆堆参数的时候，再坚强的人，也会身心疲惫。

反例：

```java
/**
* 使用证书加密数据工具方法
*
* @param param 参数
* @param password 加密密码
* @param priCert 私钥
* @param pubCert 公钥
* @param username 用户名
* @param ip ip地址
* @param userAgent 用户特征
* @return 返回加密后的字符串
*/
public String signEnvelop(JdRequestParam param, String password, String priCert, String pubCert, String username, String ip, String userAgent){

}
```

### 1.3、代码目录结构

统一的目录结构是所有项目的基础。

```xml
src                               源码目录
|-- common                            各个项目的通用类库
|-- config                            项目的配置信息
|-- constant                          全局公共常量
|-- handler                           全局处理器
|-- interceptor                       全局连接器
|-- listener                          全局监听器
|-- module                            各个业务(方便将来拆成微服务)
|-- |--- employee                         员工模块
|-- |--- role                             角色模块
|-- |--- login                            登录模块
|-- third                             三方服务，比如redis, oss，微信sdk等等
|-- util                              全局工具类
|-- Application.java                  启动类
```

### 1.4、common 目录规范

common 目录用于存放各个项目通用的项目，但是又可以依照项目进行特定的修改。

```java
src 源码目录
|-- common 各个项目的通用类库
|-- |--- anno          通用注解，比如权限，登录等等
|-- |--- constant      通用常量，比如 ResponseCodeConst
|-- |--- domain        全局的 javabean，比如 BaseEntity,PageParamDTO 等
|-- |--- exception     全局异常，如 BusinessException
|-- |--- json          json 类库，如 LongJsonDeserializer，LongJsonSerializer
|-- |--- swagger       swagger 文档
|-- |--- validator     适合各个项目的通用 validator，如 CheckEnum，CheckBigDecimal 等
```

### 1.5、module 目录规范

module 目录里写项目的各个业务，每个业务一个独立的顶级文件夹，在文件里进行 mvc 的相关划分。
其中，domain 包里存放 entity, dto, vo，bo 等 javabean 对象

```java
src
|-- module                         所有业务模块
|-- |-- role                          角色模块
|-- |-- |--RoleController.java              controller
|-- |-- |--RoleConst.java                   role相关的常量
|-- |-- |--RoleService.java                 service
|-- |-- |--RoleDao.java                     dao
|-- |-- |--domain                           domain
|-- |-- |-- |-- RoleEntity.java                  表对应实体
|-- |-- |-- |-- RoleForm.java                     请求Form对象
|-- |-- |-- |-- RoleVO.java                      返回对象
|-- |-- employee                      员工模块
|-- |-- login                         登录模块
|-- |-- email                         邮件模块
|-- |-- ....                          其他
```

## 二、MVC 规范

### 2.1、整体分层

- controller 层
- service 层
- manager 层
- dao 层

### 2.2、 `controller` 层规范

#### 1） 只允许在 method 上添加 `RequestMapping` 注解

只允许在 method 上添加 `RequestMapping` 注解，不允许加在 `class` 上（为了方便的查找 url，放到 class 上 url 不能一次性查找出来）

正例：

```java
@RestController
public class DepartmentController {

    @GetMapping("/department/list")
    public ResponseDTO<List<DepartmentVO>> listDepartment() {
        return departmentService.listDepartment();
    }
```

反例：

```java
@RequestMapping ("/department")
public class DepartmentController {

    @GetMapping("/list")
    public ResponseDTO<List<DepartmentVO>> listDepartment() {
        return departmentService.listDepartment();
    }
```

#### 2）不推荐使用 restful 命名 url

不推荐使用 restful 命名 url， 只能使用 `get/post` 方法。url 命名遵循：**`/业务模块/子模块/动作`** ;  
其中 `业务模块和子模块` 使用 名字， `动作` 使用动词；
原因：

> **虽然 restful 大法好，但是有时并不能一眼根据 url 看出来是什么操作，所以我们选择了后者，这个没有对与错，只有哪个更适合我们的团队**

正例：

```java
GET  /department/get/{id}      查询某个部门详细信息
POST /department/query         复杂查询
POST /department/add           添加部门
POST /department/update        更新部门
GET  /department/delete/{id}   删除部门

GET  /department/employee/delete/{id}   删除部门员工
```

#### 3）swagger 接口注释必须加上后端作者

每个方法必须添加 `swagger` 文档注解 `@ApiOperation` ，并填写接口描述信息，描述最后必须加上接口的作者信息，格式如下： `@author 卓大`

正例：

```java
    @ApiOperation("更新部门信息 @author 卓大")
    @PostMapping("/department/update")
    public ResponseDTO<String> updateDepartment(@Valid @RequestBody DepartmentUpdateForm departmentUpdateForm) {
        return departmentService.updateDepartment(departmentUpdateForm);
    }

```

#### 4）controller 每个方法要保持简洁

controller 在mvc中负责协同和委派业务，充当路由的角色，所以要保持代码少量和清晰，要做到如下要求：

- 不做任何的业务逻辑操作
- 不做任何的参数、业务校验，参数校验只允许使用@Valid 注解做简单的校验
- 不做任何的数据组合、拼装、赋值等操作

正例：

```java
    @ApiOperation("添加部门 @author 卓大")
    @PostMapping("/department/add")
    public ResponseDTO<String> addDepartment(@Valid @RequestBody DepartmentAddForm departmentAddForm) {
        return departmentService.addDepartment(departmentAddForm);
    }
```

#### 5）只能在 `controller` 层获取当前请求用户  

只能在 `controller` 层获取当前请求用户，并传递给 `service` 层。
> **因为获取当前请求用户是从 ThreadLocal 里获取取的，在 service、manager、dao 层极有可能是其他非 request 线程调用，会出现 null 的情况，尽量避免**

```java
    @ApiOperation("添加员工 @author 卓大")
    @PostMapping("/employee/add")
    public ResponseDTO<String> addEmployee(@Valid @RequestBody EmployeeAddForm employeeAddForm) {
        RequestUser requestUser = SmartRequestUtil.getRequestUser();
        return employeeService.addEmployee(employeeAddForm, requestUser);
    }
```

### 2.3、 `service` 层规范

#### 1） 合理拆分 service 业务

我们不建议service文件行数太大 ，如果业务较大，请拆分为多个 service；  

如订单业务,所有业务都写到 OrderService 中会导致文件过大，故需要进行拆分如下：

- `OrderQueryService` 订单查询业务
- `OrderCreateService` 订单新建业务
- `OrderDeliverService` 订单发货业务
- `OrderValidatorService` 订单验证业务

#### 2) 谨慎使用 `@Transactional` 事务注解  

谨慎使用 `@Transactional` 事务注解的使用，不要简单对 `service` 的方法添加个 `@Transactional` 注解就觉得万事大吉了。  
应当合并对数据库的操作，尽量减少添加了`@Transactional`方法内的业务逻辑。  
`@Transactional` 注解内的 `rollbackFor` 值必须使用异常 `Exception.class`

> _对于@Transactional 注解，当 spring 遇到该注解时，会自动从数据库连接池中获取 connection，并开启事务然后绑定到 ThreadLocal 上，如果业务并没有进入到最终的 操作数据库环节，那么就没有必要获取连接并开启事务，应该直接将 connection 返回给数据库连接池，供其他使用（比较难以讲解清楚，如果不懂的话就主动去问）。_

反例：

```java
    @Transactional(rollbackFor = Exception.class)
    public ResponseDTO<String> upOrDown(Long departmentId, Long swapId) {
        // 验证 1
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.NOT_EXISTS);
        }
        // 验证 2
        DepartmentEntity swapEntity = departmentDao.selectById(swapId);
        if (swapEntity == null) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.NOT_EXISTS);
        }
        // 验证 3
        Long count = employeeDao.countByDepartmentId(departmentId)
        if (count != null && count > 0) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.EXIST_EMPLOYEE);
        }
        // 操作数据库 4
        Long departmentSort = departmentEntity.getSort();
        departmentEntity.setSort(swapEntity.getSort());
        departmentDao.updateById(departmentEntity);
        swapEntity.setSort(departmentSort);
        departmentDao.updateById(swapEntity);
        return ResponseDTO.succ();
    }
```

以上代码前三步都是使用 connection 进行验证操作，由于方法上有@Transactional 注解，所以这三个验证都是使用的同一个 connection。

若对于复杂业务、复杂的验证逻辑，会导致整个验证过程始终占用该 connection 连接，占用时间可能会很长，直至方法结束，connection 才会交还给数据库连接池。

对于复杂业务的不可预计的情况，长时间占用同一个 connection 连接不是好的事情，应该尽量缩短占用时间。

正例：

```java
    DepartmentService.java

    public ResponseDTO<String> upOrDown(Long departmentId, Long swapId) {
        DepartmentEntity departmentEntity = departmentDao.selectById(departmentId);
        if (departmentEntity == null) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.NOT_EXISTS);
        }
        DepartmentEntity swapEntity = departmentDao.selectById(swapId);
        if (swapEntity == null) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.NOT_EXISTS);
        }
        Long count = employeeDao.countByDepartmentId(departmentId)
        if (count != null && count > 0) {
            return ResponseDTO.wrap(DepartmentResponseCodeConst.EXIST_EMPLOYEE);
        }
        departmentManager.upOrDown(departmentSort,swapEntity);
        return ResponseDTO.succ();
    }


    DepartmentManager.java

    @Transactional(rollbackFor = Throwable.class)
    public void upOrDown(DepartmentEntity departmentEntity ,DepartmentEntity swapEntity){
        Long departmentSort = departmentEntity.getSort();
        departmentEntity.setSort(swapEntity.getSort());
        departmentDao.updateById(departmentEntity);
        swapEntity.setSort(departmentSort);
        departmentDao.updateById(swapEntity);
    }

```

将数据在 service 层准备好，然后传递给 manager 层，由 manager 层添加@Transactional 进行数据库操作。

**以上是使用`manager`去处理解决的，其实也可以是使用spring的 `TransactionTemplate ` 事务模板解决**

#### 3）需要注意的是：注解 `@Transactional` 事务在类的内部方法调用是不会生效的

反例：如果发生异常，saveData 方法上的事务注解并不会起作用

```java
@Service
public class OrderService{

    public void createOrder(OrderAddForm addForm){
        this.saveData(addForm);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveData(OrderAddForm addForm){
        orderDao.insert(addForm);
    }
}
```

> _Spring 采用动态代理(AOP)实现对 bean 的管理和切片，它为我们的每个 class 生成一个代理对象。只有在代理对象之间进行调用时，可以触发切面逻辑。而在同一个 class 中，方法 A 调用方法 B，调用的是原对象的方法，而不通过代理对象。所以 Spring 无法拦截到这次调用，也就无法通过注解保证事务了。简单来说，在同一个类中的方法调用，不会被方法拦截器拦截到，因此事务不会起作用。_

解决方案：

1. 可以将方法放入另一个类，如新增 `manager层`，通过 spring 注入，这样符合了在对象之间调用的条件。
2. 启动类添加` @EnableAspectJAutoProxy(exposeProxy = true)`，方法内使用`AopContext.currentProxy()`获得代理类，使用事务。

```java
SpringBootApplication.java

@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
public class SpringBootApplication {}

OrderService.java

public void createOrder(OrderCreateDTO createDTO){
    OrderService orderService = (OrderService)AopContext.currentProxy();
    orderService.saveData(createDTO);
}
```


### 2.4、 manager 层规范

manager 层的作用(引自《阿里 java 手册》)：

- 对第三方平台封装的层，预处理返回结果及转化异常信息；
- 对 Service 层通用能力的下沉，如缓存方案、中间件通用处理；
- 与 DAO 层交互，对多个 DAO 的组合复用。


### 2.5、 dao 层规范

#### 1）持久层框架选择
优先使用 mybatis-plus 框架。如果需要多个数据源操作的，可以选择使用 我们实验室的 [SmartDb](https://gitee.com/lab1024/smartdb.git) 框架。

#### 2）使用mybatis-plus的要求

- 所有 Dao 继承自 BaseMapper
- 禁止使用 Mybatis-plus 的 Wrapper 条件构建器
- 禁止直接在 mybatis xml 中写死常量，应从 dao 中传入到 xml 中


正例：
NoticeDao.java  常量在参数中传入到xml
```java
public interface NoticeDao{

    Integer noticeCount(@Param("sendStatus") Integer sendStatus);

}
```
NoticeMapper.xml
```xml
    <select id="noticeCount" resultType="integer">
        select
        count(1)
        from t_notice
        where
        send_status = #{sendStatus}
    </select>
```

反例：常量直接写死到 xml 中

```java
public interface NoticeDao{

    Integer noticeCount();

}
```
NoticeMapper.xml
```xml
    <select id="noticeCount" resultType="integer">
        select
        count(1)
        from t_notice
        where
        send_status = 0
    </select>
```

#### 4）连接join写法   
建议在xml中的 join 关联写法使用表名的全称，而不是用别名，对于关联表太多的话，在xml格式中，其实很难记住 别名是什么意思！

反例： t_notice 别名 tn，t_employee别名 e， 在xml中已经很难区分是什么意思了，索性不如使用全称
```xml
<select id="queryPage" resultType="net.lab1024.sa.admin.module.business.oa.notice.domain.vo.NoticeVO">
        SELECT tn.*,
        e.actual_name AS createUserName
        FROM t_notice tn
        LEFT JOIN t_employee e ON tn.create_user_id = e.employee_id
        <where>
            tn.deleted_flag = #{queryForm.deletedFlag}
            <if test="queryForm.keywords != null and queryForm.keywords != ''">
                AND (INSTR(tn.notice_title,#{queryForm.keywords}) OR INSTR(e.actual_name,#{queryForm.keywords}))
            </if>
            <if test="queryForm.noticeType != null">
                AND tn.notice_type = #{queryForm.noticeType}
            </if>
            <if test="queryForm.noticeBelongType != null">
                AND tn.notice_belong_type = #{queryForm.noticeBelongType}
            </if>
            <if test="queryForm.startTime != null">
                AND DATE_FORMAT(tn.publish_time, '%Y-%m-%d') &gt;= #{queryForm.startTime}
            </if>
            <if test="queryForm.endTime != null">
                AND DATE_FORMAT(tn.publish_time, '%Y-%m-%d') &lt;= #{queryForm.endTime}
            </if>
            <if test="queryForm.disabledFlag != null">
                AND tn.disabled_flag = #{queryForm.disabledFlag}
            </if>
        </where>
        <if test="queryForm.sortItemList == null or queryForm.sortItemList.size == 0">
            ORDER BY tn.top_flag DESC,tn.publish_time DESC
        </if>
    </select>
```
正确：使用全称
```xml
    <select id="queryPage" resultType="net.lab1024.sa.admin.module.business.oa.notice.domain.vo.NoticeVO">
        SELECT t_notice.*,
        t_employee.actual_name AS createUserName
        FROM t_notice
        LEFT JOIN t_employee  ON t_notice.create_user_id = t_employee.employee_id
        <where>
            t_notice.deleted_flag = #{queryForm.deletedFlag}
            <if test="queryForm.keywords != null and queryForm.keywords != ''">
                AND (INSTR(t_notice.notice_title,#{queryForm.keywords}) OR INSTR(t_employee.actual_name,#{queryForm.keywords}))
            </if>
            <if test="queryForm.noticeType != null">
                AND t_notice.notice_type = #{queryForm.noticeType}
            </if>
            <if test="queryForm.noticeBelongType != null">
                AND t_notice.notice_belong_type = #{queryForm.noticeBelongType}
            </if>
            <if test="queryForm.startTime != null">
                AND DATE_FORMAT(t_notice.publish_time, '%Y-%m-%d') &gt;= #{queryForm.startTime}
            </if>
            <if test="queryForm.endTime != null">
                AND DATE_FORMAT(t_notice.publish_time, '%Y-%m-%d') &lt;= #{queryForm.endTime}
            </if>
            <if test="queryForm.disabledFlag != null">
                AND t_notice.disabled_flag = #{queryForm.disabledFlag}
            </if>
        </where>
        <if test="queryForm.sortItemList == null or queryForm.sortItemList.size == 0">
            ORDER BY t_notice.top_flag DESC,t_notice.publish_time DESC
        </if>
    </select>
```

### 2.6、 javabean 命名规范

1） `javabean` 的整体要求：
- 不得有任何的业务逻辑或者计算
- 基本数据类型必须使用包装类型`（Integer, Double、Boolean 等）`
- 不允许有任何的默认值
- 每个属性必须添加注释，并且必须使用多行注释。
- 必须使用 `lombok` 简化 `getter/setter` 方法
- 建议对象使用 `lombok` 的 `@Builder ，@NoArgsConstructor`，同时使用这两个注解，简化对象构造方法以及 set 方法。

2）javabean 名字划分：
- XxxEntity 数据库持久对象
- XxxVO 返回前端对象 （一些大厂用 Resp结尾，比如 XxxxResp）
- XxxForm 前端请求对象 （一些大厂用 Req结尾，比如 XxxxReq）
- XxxDTO 数据传输对象
- XxxBO 内部处理对象

3）数据对象；`XxxxEntity`，要求：

- 以 `Entity` 为结尾（阿里是为 DO 为结尾）
- Xxxx 与数据库表名保持一致
- 类中字段要与数据库字段保持一致，不能缺失或者多余
- 类中的每个字段添加注释，并与数据库注释保持一致
- 不允许有组合
- 项目内的日期类型必须统一，使用 `java.time.LocalDateTime` 或者 `java.time.LocalDate`

4）请求对象；`XxxxForm`，要求：

- 不可以继承自 `Entity`
- `Form` 可以继承、组合其他 `DTO，VO，BO` 等对象
- `Form` 只能用于前端、RPC 的请求参数

3）返回对象；`XxxxVO`，要求：

- 不可继承自 `Entity`
- `VO` 可以继承、组合其他 `DTO，VO，BO` 等对象
- `VO` 只能用于返回前端、rpc 的业务数据封装对象

4）业务对象 `BO`，要求：

- 不可以继承自 `Entity`
- `BO` 对象只能用于 `service，manager，dao` 层，不得用于 `controller` 层

### 2.7、boolean 类型的属性命名规范

> 类中布尔类型的变量，都不要加 is，否则部分框架解析会引起序列化错误。反例：定义为基本数据类型 Boolean isDeleted；的属性，它的方法也是 isDeleted()，RPC 在反向解析的时候，“以为”对应的属性名称是 deleted，导致属性获取不到，进而抛出异常。

这是阿里巴巴开发手册中的原文，我们团队的规定是：`boolean` 类型的类属性和数据表字段都统一使用 `flag` 结尾。虽然使用 `isDeleted，is_deleted` 从字面语义上更直观，但是比起可能出现的潜在错误，这点牺牲还是值得的。

正例：

```
deletedFlag，deleted_flag，onlineFlag，online_flag
```

## 三、数据库 规范

### 3.1、数据库命名   

全部采用小写方式， 以下划线分隔，并且区分是什么环境的数据库  

```sql
正例：smart_admin_v2_dev / smart_admin_v2_prod / smart_admin_v2_test

反例：mall_management-system / mallManagementSystem / orderServiceClient
```

### 3.2、表命名
全部采用小写方式， 以下划线分隔，并且并且以 `t_` 开头   

比如：员工表：`t_employee`、部门表：`t_department`、配置表：`t_config`

### 3.3、建表规范

表必备三字段：id, create_time, update_time

- id 字段 Long 类型，单表自增，自增长度为 1
- create_time 字段 datetime 类型，默认值 CURRENT_TIMESTAMP
- update_time 字段 datetime 类型，默认值 CURRENT_TIMESTAMP, On update CURRENT_TIMESTAMP

**#### 枚举类表字段注释需要将所有枚举含义进行注释**

修改或增加字段的状态描述，必须要及时同步更新注释。如下表的 `sync_status` 字段 `同步状态 0 未开始 1同步中 2同步成功 3失败`。

正例：
```sql
CREATE TABLE `t_change_data` (
	`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	`sync_status` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '同步状态 0 未开始 1同步中 2同步成功 3失败',
	`sync_time` DATETIME NULL DEFAULT NULL COMMENT '同步时间',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`change_data_id`)
)
```

反例：
```sql
CREATE TABLE `t_change_data` (
	`id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
	`sync_status` TINYINT(3) UNSIGNED NOT NULL DEFAULT '0' COMMENT '同步状态 ',
	`sync_time` DATETIME NULL DEFAULT NULL COMMENT '同步时间',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`update_time` DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY (`change_data_id`)
)
```

### 3.4、索引规范

具体索引规范请参照《阿里巴巴 Java 开发手册》索引规约


## 联系我们

[1024创新实验室-主任：卓大](https://zhuoda.vip)，混迹于各个技术圈，研究过计算机，熟悉点 java，略懂点前端。  
[1024创新实验室（河南·洛阳）](https://1024lab.net) 致力于成为中原领先、国内一流的技术团队，以技术创新为驱动，合作各类项目。
<table>
<tr>
  <td><img src="http://img.zhuoluodada.cn/wechat/zhuoda-wechat.jpg"/></td>
  <td><img src="http://img.zhuoluodada.cn/wechat/xiaozhen-gzh.jpg"/></td>
  <td><img src="http://img.zhuoluodada.cn/wechat/zhuoda-wechat-money-v1.jpg"/></td>
</tr>
<tr>
  <td style="text-align:center">加 主任 “卓大” 微信 <br> 拉你入群，一起学习</td>
  <td style="text-align:center">关注 “小镇程序员” <br> 分享代码与生活、技术与赚钱</td>
  <td style="text-align:center">请 “1024创新实验室” 喝咖啡 <br> 支持我们的开源与分享 </td>
</tr>
</table>
