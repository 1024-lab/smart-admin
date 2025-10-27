# AI销售助手前端原型页面 - 使用说明

> **作者**: wangxiao
> **创建时间**: 2025-10-06
> **企业**: 子午线高科智能科技

---

## 📋 项目说明

基于SmartAdmin框架,实现了AI销售助手的前端原型页面,包含客户管理的核心功能。

### 实现的功能模块

1. **客户列表页面(双池视图)**
   - 本期营Tab: 显示当前训练营学员,支持按意向等级筛选
   - 长期池Tab: 显示往期未成交学员,支持潜力筛选
   - 训练营统计卡片: 展示本期数据概览
   - 意向分级标签: S/A/B量可视化展示
   - 关键节点追踪: 已申请规划、已规划报价、已成交

2. **客户360°详情页面**
   - 基本信息卡片: 客户档案完整展示
   - AI意向分析: 综合评分+四维度雷达图+分析建议
   - 训练营进度: 时间线展示关键节点
   - 行为轨迹: 客户所有行为记录
   - 聊天记录检索: 支持关键词搜索和分类查看

---

## 🚀 如何访问原型页面

### 步骤一: 初始化菜单数据

在访问页面前,需要先在数据库中添加菜单配置:

```bash
# 方式1: 使用MySQL客户端执行SQL脚本
mysql -u root -p smart_admin < 数据库SQL脚本/mysql/sql-update-log/ai-sales-menu.sql

# 方式2: 在Navicat/DataGrip等工具中直接执行
# 打开文件: 数据库SQL脚本/mysql/sql-update-log/ai-sales-menu.sql
# 全选并执行
```

**SQL脚本说明**:
- 文件位置: `数据库SQL脚本/mysql/sql-update-log/ai-sales-menu.sql`
- 功能: 添加AI销售助手菜单项到 `t_menu` 表
- 自动授权: 为管理员角色(role_id=1)自动授予所有权限

**菜单结构**:
```
AI销售助手 (menu_id: 300)
└── 客户管理 (menu_id: 301)
    ├── 客户详情 (menu_id: 302, 隐藏)
    ├── 查询客户 (menu_id: 303, 功能点)
    ├── 新增客户 (menu_id: 304, 功能点)
    ├── 编辑客户 (menu_id: 305, 功能点)
    ├── 删除客户 (menu_id: 306, 功能点)
    ├── 查看详情 (menu_id: 307, 功能点)
    ├── 调整评分 (menu_id: 308, 功能点)
    ├── AI话术 (menu_id: 309, 功能点)
    └── 批量操作 (menu_id: 310, 功能点)
```

### 步骤二: 启动前端项目

```bash
# 进入前端项目目录
cd smart-admin-web-typescript

# 安装依赖(如果还没安装)
npm install

# 启动开发服务器
npm run dev
```

### 步骤三: 访问页面

**方式一: 直接访问URL**
- **客户列表**: `http://localhost:3000/#/business/ai-sales/customer`
- **客户详情**: `http://localhost:3000/#/business/ai-sales/customer/detail?id=1`

**方式二: 通过菜单导航** (推荐)
1. 登录SmartAdmin系统
2. 在左侧菜单找到 **"AI销售助手"** (机器人图标)
3. 点击展开,选择 **"客户管理"**
4. 进入客户列表页面

---

## 📁 文件结构

```
smart-admin-web-typescript/src/
├── api/business/ai-sales/
│   ├── customer-api.ts          # 客户管理API定义
│   └── customer-model.ts        # 数据模型定义
├── constants/business/ai-sales/
│   └── customer-const.ts        # 常量枚举定义
├── views/business/ai-sales/customer/
│   ├── customer-list.vue        # 客户列表页面
│   ├── customer-detail.vue      # 客户详情页面
│   └── components/              # 子组件目录(预留)
└── router/business/
    └── ai-sales.ts              # 路由配置
```

---

## 🎨 页面功能说明

### 客户列表页面

#### 本期营Tab

**统计卡片**:
- 第10期训练营 - DAY 3/5
- 本期新学员: 120人
- 已成交: 3人 (2.5%)
- S量: 8人 | A量: 12人 | B量: 18人
- 今日待办: 13项

**查询功能**:
- 关键字搜索: 姓名/手机号
- 意向等级筛选: S/A/B量
- 关键节点筛选: 已申请规划、已规划报价、已成交

**列表展示**:
- 客户信息: 头像、姓名、性别、职业、手机号
- 意向分级: 等级标签 + 综合评分
- 关键节点: 节点状态标签
- 最近动态: 最后联系时间和内容
- 直播观看: 观看次数
- 操作按钮: 查看详情、联系客户、获取话术

#### 长期池Tab

**筛选功能**:
- 潜力筛选: 往期S/A量(高潜力)、往期B量(中潜力)、沉默学员(30天+)
- 未成交原因: 价格敏感、延迟购买、效果怀疑、需要商量

**列表展示**:
- 客户信息: 姓名、期数、手机号
- 历史意向: 往期等级 + 评分
- 未成交原因: 原因标签
- 最后联系: 联系时间
- 沉默天数: 未联系天数
- 操作按钮: 查看详情、激活话术

### 客户360°详情页面

#### 左侧栏

**基本信息卡片**:
- 头像、姓名、性别、年龄
- 职业、手机、微信号
- 来源渠道、添加时间
- 训练营期数
- 客户标签

**AI意向分析卡片**:
- 综合评分: 大号显示,颜色编码
- 意向等级标签: S/A/B量
- 四维度进度条:
  - 💰 价格维度
  - 🎯 需求维度
  - ✅ 共识维度
  - 🤝 信任维度
- 💡 AI分析建议: 文字说明
- 手动调整评分: ±5分按钮

#### 右侧栏

**训练营进度追踪**:
- 时间线展示关键节点
- ✅ DAY0 入营
- ✅ DAY1 申请规划卡片
- 💰 DAY1-4 规划报价沟通
- ⏳ DAY2-5 收单节点
- 🎉 成交状态(如果已成交)

**行为轨迹**:
- 表格形式展示
- 时间、行为类型、行为描述
- 颜色标签区分行为类型

**聊天记录检索**:
- 搜索框: 支持关键词搜索
- Tab分类: 全部、价格相关、课程内容、副业变现
- 聊天消息列表:
  - 客户消息: 蓝色背景
  - 销售消息: 绿色背景
  - 时间戳、消息内容
  - 自动分类标签

---

## 🎯 核心特性

### 符合SmartAdmin规范

✅ **文件命名规范**:
- API文件: `customer-api.ts`
- 常量文件: `customer-const.ts`
- 页面文件: `customer-list.vue`, `customer-detail.vue`

✅ **代码注释规范**:
- 文件头注释包含: 功能描述、作者(wangxiao)、日期、版权(子午线高科智能科技 2025)
- 方法注释包含: 功能说明 + `@author wangxiao`

✅ **目录结构规范**:
- 四目录对应: `api/` ↔ `constants/` ↔ `views/` ↔ `router/`
- 业务分类: 统一在 `business/ai-sales/` 下

✅ **SmartEnum常量体系**:
- 所有枚举使用SmartEnum定义
- 包含value和desc
- 统一导出使用

### 使用Ant Design Vue组件

✅ **完全使用官方组件**:
- `<a-table>`: 列表展示
- `<a-tabs>`: Tab切换
- `<a-form>`: 查询表单
- `<a-card>`: 卡片容器
- `<a-timeline>`: 时间线
- `<a-progress>`: 进度条
- `<a-tag>`: 标签
- `<a-avatar>`: 头像
- `<a-pagination>`: 分页
- 等等...

✅ **无自定义组件**:
- 不重复造轮子
- 提高可维护性
- 保持UI风格统一

---

## 💡 技术亮点

### 1. 模拟数据支持

由于后端API未实现,页面内置了模拟数据生成函数:
- `generateMockData()`: 生成本期营模拟数据
- `generateMockLongTermData()`: 生成长期池模拟数据
- `loadMockData()`: 加载详情页模拟数据

当API调用失败时,自动切换到模拟数据,确保页面可演示。

### 2. 响应式设计

- 使用Grid布局实现响应式
- 表格支持横向滚动
- 适配不同屏幕尺寸

### 3. 交互优化

- 加载状态: 使用 `a-spin` 显示加载中
- 消息提示: 操作后显示友好提示
- 分页记忆: 保持查询条件和分页状态
- Tab切换: 智能加载对应数据

### 4. 样式美化

- 意向等级颜色编码:
  - S量: 红色(high priority)
  - A量: 橙色(medium-high priority)
  - B量: 金色(medium priority)
- 关键节点图标化
- 聊天消息区分样式
- 卡片阴影和圆角

---

## 🔧 后续开发建议

### 1. 后端API对接

需要实现的后端接口:
- `POST /business/ai-sales/customer/queryPage` - 分页查询
- `GET /business/ai-sales/customer/detail/{id}` - 获取详情
- `POST /business/ai-sales/customer/add` - 新增客户
- `POST /business/ai-sales/customer/update` - 更新客户
- `GET /business/ai-sales/customer/delete/{id}` - 删除客户
- `POST /business/ai-sales/customer/adjustScore` - 调整评分
- `POST /business/ai-sales/customer/batchOperate` - 批量操作
- `POST /business/ai-sales/customer/scriptRecommend` - AI话术推荐
- `GET /business/ai-sales/stat/trainingCamp/{period}` - 训练营统计
- `GET /business/ai-sales/stat/longTermPool` - 长期池统计
- `GET /business/ai-sales/chat/search` - 搜索聊天记录
- `GET /business/ai-sales/ai/analyze/{id}` - 触发AI分析

### 2. 功能增强

**客户列表**:
- 批量操作: 批量添加标签、批量转池、批量群发
- 导出功能: 导出Excel数据
- 高级筛选: 更多筛选条件
- 排序功能: 支持多列排序

**客户详情**:
- 编辑功能: 支持编辑基本信息
- 标签管理: 添加/删除标签
- 提醒设置: 设置跟进提醒
- AI话术生成: 调用AI接口生成话术
- 聊天记录分页: 支持滚动加载更多

### 3. 新增页面

根据PRD,还可以实现:
- 主管视图: 团队看板和异常预警
- 数据统计: 转化率分析、漏斗图
- 长期池激活: 智能推荐激活列表
- 话术库管理: 话术模板管理

---

## 📝 备注

### 数据说明

当前页面使用的所有数据都是模拟数据,包括:
- 客户基本信息
- AI评分和分析
- 训练营进度
- 行为轨迹
- 聊天记录

真实数据需要后端API提供。

### 权限说明

页面已预留权限控制点位,可通过 `v-privilege` 指令控制:
- 查询权限
- 新增权限
- 编辑权限
- 删除权限

需要在后端配置对应的权限点。

### 兼容性

- Vue 3.x
- Ant Design Vue 3.x
- 现代浏览器(Chrome, Firefox, Safari, Edge)

---

## 📞 联系方式

如有问题,请联系:
- 作者: wangxiao
- 企业: 子午线高科智能科技

---

**文档更新时间**: 2025-10-06
