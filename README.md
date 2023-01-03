### SmartAdmin

SmartAdmin 由河南·洛阳 [1024 创新实验室](https://www.1024lab.net/)使用SpringBoot2 和 Vue3 Setup标签、 Composition Api (同时支持JavaScript和TypeScript双版本) ，开发出的一套简洁、易用的中后台解决方案！  

**我们开源一套漂亮的代码和一套整洁的代码规范**，让大家在这浮躁的代码世界里感受到一股把代码写好的清流！同时又让开发者节省大量的时间，减少加班，快乐工作，保持谦逊，保持学习，热爱代码，更热爱生活！

### 地址

在线预览： [http://preview.smartadmin.1024lab.net](http://preview.smartadmin.1024lab.net)  
部署文档：[https://smartadmin.1024lab.net](https://smartadmin.1024lab.net)  （文档在努力更新中）  
vue2版本：请查看 feature/1.x 分支

### 理念与思想

- 我们分享的不是徒劳无功的各种功能，而是必须有的功能，如：数据变动记录、系统说明文档、版本更新记录、意见反馈、日志、心跳、单号生成器等等。
- 我们分享的还有经过上百家公司验证过的前端、后端、vue3等代码规范，好的规范能让我们敲下的每行代码更铿锵有力！
- **我们推崇高质量的代码，身为开发，代码即利剑，键盘上一套行云流水，宛如侠客，事了拂衣去，深藏身与名。**
- **我们推崇团队的高度配合默契、互相帮助，从不加班，而不是一看到别人的代码就头皮发麻，留其 [996.ICU](https://baike.baidu.com/item/996.ICU)**
- **我们主动思考，保持谦逊，保持学习，热爱代码，更热爱生活。**
- <font color="#DC143C">**我们希望你，希望你能花费3分钟认真阅读下面的每一个点，让你感受从未有过的技术体验！**</font>

### 演示图
<table>
<tr>
  <td><img src="https://gitee.com/lab1024/smart-admin/raw/master/%E6%88%AA%E5%9B%BE/1-1.png"/></td>
  <td><img src="https://gitee.com/lab1024/smart-admin/raw/master/%E6%88%AA%E5%9B%BE/1-2.png"/></td>
</tr>
<tr>
  <td><img src="https://gitee.com/lab1024/smart-admin/raw/master/%E6%88%AA%E5%9B%BE/2-1.png"/></td>
  <td><img src="https://gitee.com/lab1024/smart-admin/raw/master/%E6%88%AA%E5%9B%BE/2-2.png"/></td>
</tr>
<tr>
  <td><img src="https://gitee.com/lab1024/smart-admin/raw/master/%E6%88%AA%E5%9B%BE/3-1.png"/></td>
  <td><img src="https://gitee.com/lab1024/smart-admin/raw/master/%E6%88%AA%E5%9B%BE/3-2.png"/></td>
</tr>
<tr>
  <td><img src="https://gitee.com/lab1024/smart-admin/raw/master/%E6%88%AA%E5%9B%BE/4-1.png"/></td>
  <td><img src="https://gitee.com/lab1024/smart-admin/raw/master/%E6%88%AA%E5%9B%BE/4-2.png"/></td>
</tr>
<tr>
  <td><img src="https://gitee.com/lab1024/smart-admin/raw/master/%E6%88%AA%E5%9B%BE/5-1.png"/></td>
  <td><img src="https://gitee.com/lab1024/smart-admin/raw/master/%E6%88%AA%E5%9B%BE/5-2.png"/></td>
</tr>
<tr>
  <td><img src="https://gitee.com/lab1024/smart-admin/raw/master/%E6%88%AA%E5%9B%BE/6-1.png"/></td>
  <td><img src="https://gitee.com/lab1024/smart-admin/raw/master/%E6%88%AA%E5%9B%BE/6-2.png"/></td>
</tr>

</table>

### 技术体系

- 前端：Vue3.2 + Vue-Router + Pinia + Ant Design Vue 3.X + Vite
- 后端：Java8 + SpringBoot2.X + Mybatis-plus + jwt + druid + mysql

### 功能特点

- 表格：持久化表格自定义列、国际化、水印 等
- OA办公：公司信息（发票、银行、员工等）、通知公告（阅读记录、次数等）
- 文档：系统手册、意见反馈、更新记录 等
- 监控：心跳监控、数据库监控
- 日志：登录日志、操作日志（IP、浏览器、操作系统等设备信息）
- 系统：员工、部门、角色、权限、菜单 等
- 工具：文件管理、系统参数、数据字典、单号生成 等
- 代码生成： 基于每个表的配置、在线预览代码、下载 等

### 前端特点

- vue3.2 Composition 模式中如何正确的写好并组织好代码
- 提供 js 和 ts 双版本，清晰的代码结构
- 漂亮的 UI，菜单栏、标签页，体验、交互更好用的员工、部门、角色、菜单管理等等
- 多种布局layout模式
- 前端常量维护: vue-enum，拒绝出现魔法数字，常量枚举不可维护的现象
- main.js 中正确的加载方式
- 漂亮的首页、很好的帮助文档功能、等等，太多好的细节需要你的发现......

### 后端特点

- 高质量的 Java 代码、分包结构、和代码注释
- 业内独创的请求返回码维护，非常值得一看
- 四层架构（controller, service, manager, dao）
- 配合前端 vue-enum 的 swagger 文档注解
- 心跳服务，让你发现有哪些机器再跑，哪些人在偷偷的跑你的 Job
- smart-reload，为系统预留钩子，动态加载，在不重启程序前提下执行一些代码，你懂的
- 以上只是一些举例，更多灿若繁星的惊喜和细节，等待着你的发现！

### 前端代码规范

- vue3 项目目录结构如何划分
- Composition setup 模式下如何编写代码
- 文件、文件夹、目录结构、组件、变量等等怎么命名
- router 和 store ( pinia ) 该怎么划分扩展性更好
- 网络请求 axios 如何封装
- 以及更多，数不胜数让你觉得实用，同时身心愉悦的规范

### 后端代码规范

- 四层架构（controller, service, manager, dao） 是什么，为什么要有四层
- 各个层的代码该怎么写才能让团队配合默契，高度一致
- vo, bo, form, entity ，各种 javabean 怎么区分和使用
- spring 的 @Transactional 你用对了吗
- 方法参数个数、注释、todo 这些也要有规范，你遵守过吗
- 数据库列如何命名等等
- 以上举例，只是沧海一粟，更多的细节等待你的发现！

ps：以上规范基础都是以团队出发，让团队开心快乐的写代码，而不是为了代码规范而规范，不喜勿喷！谢谢。

### 联系我们

<table>
<tr>
  <td><img src="https://gitee.com/lab1024/smart-admin/raw/master/%E6%88%AA%E5%9B%BE/zhuoda-wechat.jpg"/></td>
  <td><img src="https://gitee.com/lab1024/smart-admin/raw/master/%E6%88%AA%E5%9B%BE/xiaozhen-gzh.jpg"/></td>
  <td><img src="https://gitee.com/lab1024/smart-admin/raw/master/%E6%88%AA%E5%9B%BE/zhuoda-wechat-money-v1.jpg"/></td>
</tr>
<tr>
  <td style="text-align:center">加我微信，拉你入群</td>
  <td style="text-align:center">关注“小镇程序员”，了解我们</td>
  <td style="text-align:center">请我们喝杯咖啡</td>
</tr>
</table>

### 作者

[1024创新实验室-主任：卓大](https://zhuoda.vip)，混迹于各个技术圈，研究过计算机，熟悉点 java，略懂点前端。  
[1024创新实验室（河南·洛阳）](https://1024lab.net) 致力于成为中原领先、国内一流的技术团队，以技术创新为驱动，合作各类项目。

