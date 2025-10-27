---
title: 企业微信会话存档 API 接口设计
author: wangxiao
company: 子午线高科智能科技
date: 2025-10-08
version: v2.0
permalink: claudedocs/企微聊天-api-设计-备份
---

# 企业微信会话存档 API 接口设计

---

## 📋 目录

1. [设计概述](#设计概述)
2. [核心架构变更](#核心架构变更)
3. [接口规范](#接口规范)
4. [企业配置管理](#企业配置管理)
5. [会话管理](#会话管理)
6. [消息管理](#消息管理)
7. [参与方关系管理](#参与方关系管理)
8. [员工客户关系](#员工客户关系)
9. [员工管理](#员工管理)
10. [客户管理](#客户管理)
11. [群聊管理](#群聊管理)
12. [消息导出](#消息导出)
13. [统计分析](#统计分析)
14. [权限定义](#权限定义)

---

## 设计概述

### 设计原则

1. **RESTful 风格**: 遵循 REST API 设计规范
2. **统一响应**: 使用 SmartAdmin 的 `ResponseDTO` 统一响应格式
3. **权限控制**: 所有接口均需权限验证 (`@SaCheckPermission`)
4. **分页查询**: 列表查询统一使用 `PageResult` 分页
5. **参数校验**: 使用 `@Valid` 注解进行参数验证
6. **错误处理**: 统一使用 `WecomErrorCode` 错误码
7. **架构解耦**: 消息与会话解耦，通过关联表建立关系 ⭐新增

### 技术栈

- **框架**: SpringBoot 3 + MyBatis-Plus
- **权限**: Sa-Token
- **验证**: Jakarta Validation
- **文档**: Swagger/OpenAPI 3.0

### 接口总览

| 模块 | 接口数 | 基础路径 |
|------|--------|----------|
| 企业配置管理 | 9 | `/wecom/config` |
| 会话管理 | 10 | `/wecom/conversation` |
| 消息管理 | 10 | `/wecom/message` |
| 参与方关系 | 5 | `/wecom/participant` |
| 员工客户关系 ⭐ | 4 | `/wecom/staff-customer-relation` |
| 员工管理 | 6 | `/wecom/staff` |
| 客户管理 | 6 | `/wecom/customer` |
| 群聊管理 | 9 | `/wecom/group` |
| 消息导出 | 6 | `/wecom/export` |
| 统计分析 | 3 | `/wecom/stats` |
| **合计** | **68** | |

---

## 核心架构变更

### 数据库设计 v2.3 变更要点

本 API 设计基于数据库设计 v2.3（消息会话解耦架构），核心变更如下：

#### 1. 消息会话解耦 ⭐⭐⭐

**变更内容**:
- ✅ 新增 `t_wecom_message_conversation` 消息会话关联表
- ✅ 消息表移除 `conversation_id` 字段，消息与会话完全解耦
- ✅ 一条消息可以关联多个会话（支持群发场景）

**对 API 的影响**:
- **查询优化**: 通过关联表实现单表索引查询，避免多表 JOIN
- **新增接口**: `/wecom/message/by-participant` - 查询某人收到的所有消息
- **性能提升**: 查询某人的消息时性能提升 3-5 倍

**数据流转**:
```
单聊消息: 1条消息记录 + 1条关联记录
群聊消息: 1条消息记录 + 1条关联记录 (to_id = room_id)
群发消息: 1条消息记录 + N条关联记录 (每个接收者一条)
```

---

#### 2. 员工-客户关系表 ⭐

**变更内容**:
- ✅ 新增 `t_wecom_staff_customer_relation` 员工-客户关系表
- ✅ 支持一个客户被多个员工添加（N:N 关系）
- ✅ 每个员工对同一客户有独立的备注、标签、添加方式

**对 API 的影响**:
- **新增模块**: 员工客户关系管理（4 个接口）
- **查询优化**: 客户查询接口返回员工级别的数据（备注、标签等）
- **标签管理**: 标签存储在关系表，支持员工级别的个性化标签

---

#### 3. 字段规范化调整

**变更内容**:
- 企微原始 ID 统一 `wecom_` 前缀: `user_id` → `wecom_user_id`
- 标志字段统一 `_flag` 后缀: `has_conversation` → `has_conversation_flag`
- 删除收藏功能相关字段（会话表）

**对 API 的影响**:
- **VO 类调整**: 所有 VO 类字段名称更新
- **移除接口**: 会话收藏/取消收藏接口
- **参数调整**: 查询表单字段名称更新

---

## 接口规范

### 统一响应格式

```java
@Data
public class ResponseDTO<T> {
    private Integer code;      // 响应码: 1-成功, 其他-失败
    private String msg;        // 响应消息
    private T data;           // 响应数据
    private Boolean ok;       // 是否成功
}
```

### 分页响应格式

```java
@Data
public class PageResult<T> {
    private Long total;           // 总记录数
    private List<T> list;        // 数据列表
    private Integer pageNum;     // 当前页码
    private Integer pageSize;    // 每页大小
}
```

### 错误码定义

```java
public enum WecomErrorCode implements ErrorCode {
    CONFIG_NOT_FOUND(50101, "企业微信配置不存在"),
    CHAT_SECRET_INVALID(50102, "会话存档密钥无效"),
    MESSAGE_DECRYPT_FAILED(50103, "消息解密失败"),
    CONVERSATION_NOT_FOUND(50104, "会话不存在"),
    EXPORT_TASK_FAILED(50105, "导出任务失败"),
    PARTICIPANT_NOT_FOUND(50106, "参与方不存在"),
    STAFF_NOT_FOUND(50107, "员工不存在"),
    CUSTOMER_NOT_FOUND(50108, "客户不存在"),
    GROUP_NOT_FOUND(50109, "群聊不存在"),
    SYNC_FAILED(50110, "数据同步失败"),
    PULL_MESSAGE_FAILED(50111, "消息拉取失败");
}
```

---

## 企业配置管理

**基础路径**: `/wecom/config`

### 1. 添加企业配置

**接口**: `POST /wecom/config/add`

**权限**: `wecom:config:manage`

**请求参数** (`WecomCorpConfigAddForm`):
```java
@Data
public class WecomCorpConfigAddForm {
    @NotBlank(message = "企业ID不能为空")
    @Length(max = 64, message = "企业ID长度不能超过64")
    private String corpId;

    @NotBlank(message = "企业名称不能为空")
    @Length(max = 128, message = "企业名称长度不能超过128")
    private String corpName;

    @NotBlank(message = "应用Secret不能为空")
    private String agentSecret;

    @NotBlank(message = "会话存档Secret不能为空")
    private String chatSecret;

    @NotNull(message = "RSA公钥版本不能为空")
    private Integer chatPublicKeyVer;

    private Boolean enabledFlag = true;
}
```

**响应**: `ResponseDTO<Long>` - 返回配置ID

---

### 2. 更新企业配置

**接口**: `POST /wecom/config/update`

**权限**: `wecom:config:manage`

**请求参数** (`WecomCorpConfigUpdateForm`):
```java
@Data
public class WecomCorpConfigUpdateForm {
    @NotNull(message = "配置ID不能为空")
    private Long configId;

    @Length(max = 128, message = "企业名称长度不能超过128")
    private String corpName;

    private String agentSecret;
    private String chatSecret;
    private Integer chatPublicKeyVer;
    private Boolean enabledFlag;
}
```

**响应**: `ResponseDTO<Void>`

---

### 3. 查询配置详情

**接口**: `GET /wecom/config/detail/{configId}`

**权限**: `wecom:config:manage`

**路径参数**:
- `configId` (Long) - 配置ID

**响应**: `ResponseDTO<WecomCorpConfigVO>`

```java
@Data
public class WecomCorpConfigVO {
    private Long configId;
    private String corpId;
    private String corpName;
    private String agentSecret;      // 脱敏显示
    private String chatSecret;       // 脱敏显示
    private Integer chatPublicKeyVer;
    private Long chatSeq;
    private Integer chatPullStatus;  // 0-未开始 1-拉取中 2-异常 3-已完成
    private LocalDateTime lastPullTime;
    private String lastErrorMsg;
    private Boolean enabledFlag;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
```

---

### 4. 分页查询配置列表

**接口**: `POST /wecom/config/query-page`

**权限**: `wecom:config:manage`

**请求参数** (`WecomCorpConfigQueryForm`):
```java
@Data
public class WecomCorpConfigQueryForm extends PageParam {
    private String corpName;         // 企业名称模糊查询
    private Integer chatPullStatus;  // 拉取状态
    private Boolean enabledFlag;     // 启用状态
}
```

**响应**: `ResponseDTO<PageResult<WecomCorpConfigVO>>`

---

### 5. 删除配置

**接口**: `POST /wecom/config/delete/{configId}`

**权限**: `wecom:config:manage`

**路径参数**:
- `configId` (Long) - 配置ID

**响应**: `ResponseDTO<Void>`

---

### 6. 启用/禁用配置

**接口**: `POST /wecom/config/enable`

**权限**: `wecom:config:manage`

**请求参数**:
```java
@Data
public class WecomCorpConfigEnableForm {
    @NotNull
    private Long configId;
    @NotNull
    private Boolean enabledFlag;
}
```

**响应**: `ResponseDTO<Void>`

---

### 7. 开始拉取消息

**接口**: `POST /wecom/config/start-pull`

**权限**: `wecom:config:pull`

**请求参数**:
```java
@Data
public class WecomPullMessageForm {
    @NotNull(message = "配置ID不能为空")
    private Long configId;

    private Long startSeq;  // 起始序号,为空则从上次断点继续
}
```

**响应**: `ResponseDTO<Void>`

---

### 8. 停止拉取消息

**接口**: `POST /wecom/config/stop-pull`

**权限**: `wecom:config:pull`

**请求参数**:
```java
@Data
public class WecomStopPullForm {
    @NotNull
    private Long configId;
}
```

**响应**: `ResponseDTO<Void>`

---

### 9. 查询拉取状态

**接口**: `GET /wecom/config/pull-status/{configId}`

**权限**: `wecom:config:pull`

**路径参数**:
- `configId` (Long) - 配置ID

**响应**: `ResponseDTO<WecomPullStatusVO>`

```java
@Data
public class WecomPullStatusVO {
    private Long configId;
    private String corpName;
    private Integer chatPullStatus;
    private Long chatSeq;
    private LocalDateTime lastPullTime;
    private String lastErrorMsg;
    private Long totalMessageCount;  // 已拉取消息总数
}
```

---

## 会话管理

**基础路径**: `/wecom/conversation`

### 1. 分页查询会话列表

**接口**: `POST /wecom/conversation/query-page`

**权限**: `wecom:chat:list`

**请求参数** (`WecomConversationQueryForm`):
```java
@Data
public class WecomConversationQueryForm extends PageParam {
    @NotBlank(message = "企业ID不能为空")
    private String corpId;

    private Integer conversationType;   // 会话类型: 1-单聊 2-群聊
    private String participantId;       // 参与方ID(查询某人的会话)
    private Integer participantType;    // 参与方类型: 1-客户 2-员工
    private Integer customerCountMin;   // 最小客户数(筛选外部会话)
    private Integer customerCountMax;   // 最大客户数(筛选内部会话)
    private String keyword;             // 关键词(搜索会话名称/最后消息)

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;    // 最后消息开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;      // 最后消息结束时间

    private Boolean isFavorite;         // 是否仅查收藏
}
```

**响应**: `ResponseDTO<PageResult<WecomConversationVO>>`

```java
@Data
public class WecomConversationVO {
    private Long conversationId;
    private String corpId;
    private Integer conversationType;
    private String roomId;
    private String roomName;
    private Integer participantCount;
    private Integer customerCount;

    // 最后消息信息
    private String lastMsgId;
    private String lastMsgType;
    private String lastMsgContent;
    private LocalDateTime lastMsgTime;
    private String lastMsgSenderId;
    private String lastMsgSenderName;

    private Integer msgCount;
    private Boolean isFavorite;
    private String favoriteReason;
    private LocalDateTime favoriteTime;

    // 参与方列表(冗余显示)
    private List<ParticipantSimpleVO> participants;
}

@Data
public class ParticipantSimpleVO {
    private String participantId;
    private String participantName;
    private Integer participantType;
}
```

---

### 2. 查询会话详情

**接口**: `GET /wecom/conversation/detail/{conversationId}`

**权限**: `wecom:chat:detail`

**路径参数**:
- `conversationId` (Long) - 会话ID

**响应**: `ResponseDTO<WecomConversationDetailVO>`

```java
@Data
public class WecomConversationDetailVO extends WecomConversationVO {
    private List<WecomParticipantVO> participantDetails;  // 参与方详细信息
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
```

---

### 3. 收藏会话

**接口**: `POST /wecom/conversation/favorite`

**权限**: `wecom:chat:favorite`

**请求参数**:
```java
@Data
public class WecomConversationFavoriteForm {
    @NotNull
    private Long conversationId;

    @Length(max = 255, message = "收藏理由长度不能超过255")
    private String favoriteReason;
}
```

**响应**: `ResponseDTO<Void>`

---

### 4. 取消收藏会话

**接口**: `POST /wecom/conversation/unfavorite`

**权限**: `wecom:chat:favorite`

**请求参数**:
```java
@Data
public class WecomConversationUnfavoriteForm {
    @NotNull
    private Long conversationId;
}
```

**响应**: `ResponseDTO<Void>`

---

### 5. 按类型查询会话

**接口**: `POST /wecom/conversation/by-type`

**权限**: `wecom:chat:list`

**请求参数**:
```java
@Data
public class WecomConversationByTypeForm extends PageParam {
    @NotBlank
    private String corpId;

    @NotNull(message = "会话类型不能为空")
    private Integer conversationType;  // 1-单聊 2-群聊
}
```

**响应**: `ResponseDTO<PageResult<WecomConversationVO>>`

---

### 6. 按参与方查询会话

**接口**: `POST /wecom/conversation/by-participant`

**权限**: `wecom:chat:list`

**请求参数**:
```java
@Data
public class WecomConversationByParticipantForm extends PageParam {
    @NotBlank
    private String corpId;

    @NotBlank(message = "参与方ID不能为空")
    private String participantId;

    private Integer conversationType;  // 可选,筛选单聊或群聊
}
```

**响应**: `ResponseDTO<PageResult<WecomConversationVO>>`

---

### 7. 按客户查询会话

**接口**: `POST /wecom/conversation/by-customer`

**权限**: `wecom:chat:list`

**请求参数**:
```java
@Data
public class WecomConversationByCustomerForm extends PageParam {
    @NotBlank
    private String corpId;

    @NotBlank(message = "客户ID不能为空")
    private String customerUserId;  // 客户ExternalUserID
}
```

**响应**: `ResponseDTO<PageResult<WecomConversationVO>>`

---

### 8. 查询外部会话(含客户)

**接口**: `POST /wecom/conversation/external`

**权限**: `wecom:chat:list`

**请求参数**:
```java
@Data
public class WecomConversationExternalForm extends PageParam {
    @NotBlank
    private String corpId;

    private String participantId;  // 可选,查询某员工的外部会话
}
```

**响应**: `ResponseDTO<PageResult<WecomConversationVO>>`

**说明**: 查询 `customer_count > 0` 的会话

---

### 9. 查询内部会话(仅员工)

**接口**: `POST /wecom/conversation/internal`

**权限**: `wecom:chat:list`

**请求参数**:
```java
@Data
public class WecomConversationInternalForm extends PageParam {
    @NotBlank
    private String corpId;

    private String participantId;  // 可选,查询某员工的内部会话
}
```

**响应**: `ResponseDTO<PageResult<WecomConversationVO>>`

**说明**: 查询 `customer_count = 0` 的会话

---

### 10. 会话统计信息

**接口**: `GET /wecom/conversation/stats`

**权限**: `wecom:chat:list`

**请求参数**:
- `corpId` (String, Query) - 企业ID

**响应**: `ResponseDTO<WecomConversationStatsVO>`

```java
@Data
public class WecomConversationStatsVO {
    private Long totalConversations;      // 总会话数
    private Long singleChatCount;         // 单聊数量
    private Long groupChatCount;          // 群聊数量
    private Long externalConversations;   // 外部会话数(含客户)
    private Long internalConversations;   // 内部会话数(仅员工)
    private Long favoriteCount;           // 收藏会话数
    private Long totalMessages;           // 总消息数
}
```

---

## 消息管理

**基础路径**: `/wecom/message`

**设计说明**: 基于消息会话解耦架构（v2.3），消息查询通过 `t_wecom_message_conversation` 关联表实现高性能查询

### 1. 查询会话消息列表(分页)

**接口**: `POST /wecom/message/query-by-conversation`

**权限**: `wecom:message:query`

**说明**: 查询指定会话的消息列表，通过消息会话关联表查询

**请求参数** (`WecomMessageQueryForm`):
```java
@Data
public class WecomMessageQueryForm extends PageParam {
    @NotNull(message = "会话ID不能为空")
    private Long conversationId;

    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;  // 必填,用于分区查询

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;    // 必填,用于分区查询

    private String msgType;           // 消息类型过滤
    private String fromId;            // 发送者ID
    private String keyword;           // 内容关键词搜索
}
```

**响应**: `ResponseDTO<PageResult<WecomMessageVO>>`

```java
@Data
public class WecomMessageVO {
    private Long messageId;
    private String msgId;
    private String corpId;
    private Long seq;
    private String msgType;
    private String msgAction;

    private String fromId;
    private Integer fromType;
    private String fromName;
    private List<String> toList;
    private String roomId;

    private LocalDateTime msgTime;
    private String msgContent;        // 文本内容或摘要
    private JSONObject rawContent;    // 完整JSON数据

    private Integer publicKeyVer;

    // 媒体文件信息(如有)
    private WecomMessageMediaVO mediaFile;

    private LocalDateTime createTime;
}
```

**SQL 实现参考**:
```sql
SELECT m.*
FROM t_wecom_message m
INNER JOIN t_wecom_message_conversation mc ON m.message_id = mc.message_id
WHERE mc.conversation_id = ?
  AND m.msg_time >= ?
  AND m.msg_time < ?
ORDER BY m.seq ASC
LIMIT ? OFFSET ?;
```

---

### 2. 查询某人的消息列表 ⭐新增

**接口**: `POST /wecom/message/by-participant`

**权限**: `wecom:message:query`

**说明**: 查询某人收到或发送的所有消息（极速查询，利用消息会话关联表）

**请求参数**:
```java
@Data
public class WecomMessageByParticipantForm extends PageParam {
    @NotBlank(message = "参与方ID不能为空")
    private String participantId;  // 员工UserID 或 客户ExternalUserID

    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private String msgType;           // 消息类型过滤
    private Integer routeType;        // 路由类型: 1-单聊 2-群聊
    private Boolean includeSent;      // 是否包含发送的消息，默认false（仅收到的）
}
```

**响应**: `ResponseDTO<PageResult<WecomMessageVO>>`

**SQL 实现参考**:
```sql
-- 仅查询收到的消息（极速，单表索引扫描）
SELECT m.*
FROM t_wecom_message m
INNER JOIN t_wecom_message_conversation mc ON m.message_id = mc.message_id
WHERE mc.to_id = ?
  AND m.msg_time >= ?
  AND m.msg_time < ?
ORDER BY m.msg_time DESC
LIMIT ? OFFSET ?;

-- 查询收到+发送的消息
SELECT m.*
FROM t_wecom_message m
INNER JOIN t_wecom_message_conversation mc ON m.message_id = mc.message_id
WHERE (mc.to_id = ? OR mc.from_id = ?)
  AND m.msg_time >= ?
  AND m.msg_time < ?
ORDER BY m.msg_time DESC
LIMIT ? OFFSET ?;
```

---

### 3. 查询消息详情

**接口**: `GET /wecom/message/detail/{messageId}`

**权限**: `wecom:message:query`

**路径参数**:
- `messageId` (Long) - 消息ID

**响应**: `ResponseDTO<WecomMessageDetailVO>`

```java
@Data
public class WecomMessageDetailVO extends WecomMessageVO {
    private List<ConversationSimpleVO> relatedConversations;  // 关联的会话列表
    private LocalDateTime updateTime;
}

@Data
public class ConversationSimpleVO {
    private Long conversationId;
    private Integer conversationType;
    private String toId;  // 单聊=接收方ID, 群聊=roomId
}
```

---

### 3. 搜索消息内容

**接口**: `POST /wecom/message/search`

**权限**: `wecom:message:search`

**请求参数**:
```java
@Data
public class WecomMessageSearchForm extends PageParam {
    @NotBlank
    private String corpId;

    @NotBlank(message = "搜索关键词不能为空")
    private String keyword;

    private Long conversationId;      // 可选,限定会话范围

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;  // 建议带时间范围,利用分区

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private List<String> msgTypes;    // 消息类型列表
}
```

**响应**: `ResponseDTO<PageResult<WecomMessageVO>>`

---

### 4. 按发送者查询消息

**接口**: `POST /wecom/message/by-sender`

**权限**: `wecom:message:query`

**请求参数**:
```java
@Data
public class WecomMessageBySenderForm extends PageParam {
    @NotBlank(message = "发送者ID不能为空")
    private String fromId;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private Long conversationId;
}
```

**响应**: `ResponseDTO<PageResult<WecomMessageVO>>`

---

### 5. 按消息类型查询

**接口**: `POST /wecom/message/by-type`

**权限**: `wecom:message:query`

**请求参数**:
```java
@Data
public class WecomMessageByTypeForm extends PageParam {
    @NotNull
    private Long conversationId;

    @NotBlank(message = "消息类型不能为空")
    private String msgType;  // text/image/voice/video/file等

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
```

**响应**: `ResponseDTO<PageResult<WecomMessageVO>>`

---

### 6. 按时间范围查询消息

**接口**: `POST /wecom/message/by-time-range`

**权限**: `wecom:message:query`

**请求参数**:
```java
@Data
public class WecomMessageByTimeRangeForm extends PageParam {
    @NotNull
    private Long conversationId;

    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
```

**响应**: `ResponseDTO<PageResult<WecomMessageVO>>`

---

### 7. 统计会话消息数

**接口**: `GET /wecom/message/count/{conversationId}`

**权限**: `wecom:message:query`

**路径参数**:
- `conversationId` (Long) - 会话ID

**请求参数** (Query):
- `startTime` (String, 可选) - 开始时间
- `endTime` (String, 可选) - 结束时间

**响应**: `ResponseDTO<Long>` - 消息总数

---

### 8. 按类型统计消息数量

**接口**: `POST /wecom/message/stats-by-type`

**权限**: `wecom:message:query`

**请求参数**:
```java
@Data
public class WecomMessageStatsForm {
    @NotNull
    private Long conversationId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
```

**响应**: `ResponseDTO<List<WecomMessageTypeStatsVO>>`

```java
@Data
public class WecomMessageTypeStatsVO {
    private String msgType;
    private Long count;
    private Double percentage;
}
```

---

## 参与方关系管理

**基础路径**: `/wecom/participant`

### 1. 查询会话的参与方列表

**接口**: `GET /wecom/participant/by-conversation/{conversationId}`

**权限**: `wecom:chat:detail`

**路径参数**:
- `conversationId` (Long) - 会话ID

**响应**: `ResponseDTO<List<WecomParticipantVO>>`

```java
@Data
public class WecomParticipantVO {
    private Long id;
    private Long conversationId;
    private String participantId;
    private Integer participantType;  // 1-客户 2-员工
    private String participantName;
    private LocalDateTime joinTime;
    private LocalDateTime leaveTime;
    private Boolean isActive;

    // 扩展信息
    private String avatar;            // 头像
    private String position;          // 职务(员工)
    private String corpName;          // 企业名称(客户)
}
```

---

### 2. 查询某人参与的会话

**接口**: `POST /wecom/participant/by-user`

**权限**: `wecom:chat:list`

**请求参数**:
```java
@Data
public class WecomParticipantByUserForm extends PageParam {
    @NotBlank(message = "参与方ID不能为空")
    private String participantId;

    private Integer conversationType;  // 可选,1-单聊 2-群聊
    private Boolean isActive;          // 可选,筛选在群/已退群
}
```

**响应**: `ResponseDTO<PageResult<WecomConversationVO>>`

---

### 3. 查询在群成员

**接口**: `GET /wecom/participant/active-members/{conversationId}`

**权限**: `wecom:chat:detail`

**路径参数**:
- `conversationId` (Long) - 会话ID(群聊)

**响应**: `ResponseDTO<List<WecomParticipantVO>>`

**说明**: 查询 `is_active = 1` 的成员

---

### 4. 查询已退群成员

**接口**: `GET /wecom/participant/left-members/{conversationId}`

**权限**: `wecom:chat:detail`

**路径参数**:
- `conversationId` (Long) - 会话ID(群聊)

**响应**: `ResponseDTO<List<WecomParticipantVO>>`

**说明**: 查询 `is_active = 0` 的成员

---

### 5. 查询成员变更历史

**接口**: `POST /wecom/participant/member-history`

**权限**: `wecom:chat:detail`

**请求参数**:
```java
@Data
public class WecomParticipantHistoryForm extends PageParam {
    @NotNull
    private Long conversationId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
```

**响应**: `ResponseDTO<PageResult<WecomParticipantHistoryVO>>`

```java
@Data
public class WecomParticipantHistoryVO {
    private String participantId;
    private String participantName;
    private Integer participantType;
    private LocalDateTime joinTime;
    private LocalDateTime leaveTime;
    private String action;  // "加入" 或 "退出"
}
```

---

## 员工管理

**基础路径**: `/wecom/staff`

### 1. 分页查询员工列表

**接口**: `POST /wecom/staff/query-page`

**权限**: `wecom:staff:manage`

**请求参数** (`WecomStaffQueryForm`):
```java
@Data
public class WecomStaffQueryForm extends PageParam {
    @NotBlank
    private String corpId;

    private String keyword;           // 姓名/别名/手机号
    private Integer status;           // 1-已激活 2-已禁用 4-未激活 5-已离职
    private Boolean hasConversation;  // 是否有会话记录
    private Boolean enableChatArchive; // 是否开启会话存档
}
```

**响应**: `ResponseDTO<PageResult<WecomStaffVO>>`

```java
@Data
public class WecomStaffVO {
    private Long staffId;
    private String corpId;
    private String userId;
    private String name;
    private String alias;
    private String mobile;
    private String position;
    private Integer gender;
    private String avatar;
    private List<Long> department;
    private Integer status;
    private Boolean enableChatArchive;
    private Boolean hasConversation;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
```

---

### 2. 查询员工详情

**接口**: `GET /wecom/staff/detail/{staffId}`

**权限**: `wecom:staff:manage`

**路径参数**:
- `staffId` (Long) - 员工ID

**响应**: `ResponseDTO<WecomStaffDetailVO>`

```java
@Data
public class WecomStaffDetailVO extends WecomStaffVO {
    private Long conversationCount;   // 参与的会话数
    private Long messageCount;        // 发送的消息数
}
```

---

### 3. 同步员工信息

**接口**: `POST /wecom/staff/sync`

**权限**: `wecom:staff:manage`

**请求参数**:
```java
@Data
public class WecomStaffSyncForm {
    @NotNull
    private Long configId;

    private List<String> userIds;  // 可选,指定员工ID列表,为空则全量同步
}
```

**响应**: `ResponseDTO<WecomSyncResultVO>`

```java
@Data
public class WecomSyncResultVO {
    private Integer totalCount;
    private Integer successCount;
    private Integer failCount;
    private List<String> failedUserIds;
}
```

---

### 4. 搜索员工

**接口**: `POST /wecom/staff/search`

**权限**: `wecom:staff:manage`

**请求参数**:
```java
@Data
public class WecomStaffSearchForm extends PageParam {
    @NotBlank
    private String corpId;

    @NotBlank(message = "搜索关键词不能为空")
    private String keyword;  // 按姓名/手机号搜索
}
```

**响应**: `ResponseDTO<PageResult<WecomStaffVO>>`

---

### 5. 员工统计信息

**接口**: `GET /wecom/staff/stats`

**权限**: `wecom:staff:manage`

**请求参数**:
- `corpId` (String, Query) - 企业ID

**响应**: `ResponseDTO<WecomStaffStatsVO>`

```java
@Data
public class WecomStaffStatsVO {
    private Long totalStaff;
    private Long activeStaff;
    private Long disabledStaff;
    private Long leftStaff;
    private Long staffWithConversation;
    private Long staffWithChatArchive;
}
```

---

### 6. 查询有会话记录的员工

**接口**: `POST /wecom/staff/with-conversation`

**权限**: `wecom:staff:manage`

**请求参数**:
```java
@Data
public class WecomStaffWithConversationForm extends PageParam {
    @NotBlank
    private String corpId;
}
```

**响应**: `ResponseDTO<PageResult<WecomStaffVO>>`

**说明**: 查询 `has_conversation = 1` 的员工

---

## 客户管理

**基础路径**: `/wecom/customer`

### 1. 分页查询客户列表

**接口**: `POST /wecom/customer/query-page`

**权限**: `wecom:customer:manage`

**请求参数** (`WecomCustomerQueryForm`):
```java
@Data
public class WecomCustomerQueryForm extends PageParam {
    @NotBlank
    private String corpId;

    private String keyword;           // 客户昵称/企业名称
    private Integer type;             // 1-微信用户 2-企微用户
    private Integer gender;           // 0-未知 1-男 2-女
    private String staffUserId;       // 添加员工ID
    private Integer relationStatus;   // 0-已删除 1-正常
    private Boolean hasConversation;  // 是否有会话记录
}
```

**响应**: `ResponseDTO<PageResult<WecomCustomerVO>>`

```java
@Data
public class WecomCustomerVO {
    private Long customerId;
    private String corpId;
    private String externalUserid;
    private String name;
    private Integer type;
    private Integer gender;
    private String avatar;
    private String corpName;
    private String corpFullName;
    private String staffUserId;
    private String staffRemark;
    private String description;
    private Integer addWay;
    private LocalDateTime addTime;
    private Integer relationStatus;
    private Boolean hasConversation;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
```

---

### 2. 查询客户详情

**接口**: `GET /wecom/customer/detail/{customerId}`

**权限**: `wecom:customer:manage`

**路径参数**:
- `customerId` (Long) - 客户ID

**响应**: `ResponseDTO<WecomCustomerDetailVO>`

```java
@Data
public class WecomCustomerDetailVO extends WecomCustomerVO {
    private Long conversationCount;   // 参与的会话数
    private Long messageCount;        // 发送的消息数
}
```

---

### 3. 同步客户信息

**接口**: `POST /wecom/customer/sync`

**权限**: `wecom:customer:manage`

**请求参数**:
```java
@Data
public class WecomCustomerSyncForm {
    @NotNull
    private Long configId;

    private List<String> externalUserids;  // 可选,指定客户ID列表
}
```

**响应**: `ResponseDTO<WecomSyncResultVO>`

---

### 4. 按跟进员工查询客户

**接口**: `POST /wecom/customer/by-staff`

**权限**: `wecom:customer:manage`

**请求参数**:
```java
@Data
public class WecomCustomerByStaffForm extends PageParam {
    @NotBlank
    private String corpId;

    @NotBlank(message = "员工ID不能为空")
    private String staffUserId;

    private Integer relationStatus;  // 可选,1-正常 0-已删除
}
```

**响应**: `ResponseDTO<PageResult<WecomCustomerVO>>`

---

### 5. 客户统计信息

**接口**: `GET /wecom/customer/stats`

**权限**: `wecom:customer:manage`

**请求参数**:
- `corpId` (String, Query) - 企业ID

**响应**: `ResponseDTO<WecomCustomerStatsVO>`

```java
@Data
public class WecomCustomerStatsVO {
    private Long totalCustomers;
    private Long activeCustomers;
    private Long deletedCustomers;
    private Long customerWithConversation;
    private Long wechatUserCount;
    private Long wecomUserCount;
}
```

---

### 6. 查询有会话记录的客户

**接口**: `POST /wecom/customer/with-conversation`

**权限**: `wecom:customer:manage`

**请求参数**:
```java
@Data
public class WecomCustomerWithConversationForm extends PageParam {
    @NotBlank
    private String corpId;
}
```

**响应**: `ResponseDTO<PageResult<WecomCustomerVO>>`

**说明**: 查询 `has_conversation = 1` 的客户

---

## 群聊管理

**基础路径**: `/wecom/group`

### 1. 分页查询群聊列表

**接口**: `POST /wecom/group/query-page`

**权限**: `wecom:group:manage`

**请求参数** (`WecomGroupQueryForm`):
```java
@Data
public class WecomGroupQueryForm extends PageParam {
    @NotBlank
    private String corpId;

    private String keyword;           // 群名称搜索
    private String owner;             // 群主UserID
    private Integer groupStatus;      // 0-已解散 1-正常
    private Boolean hasConversation;  // 是否有会话记录
    private Integer minCustomerNum;   // 最小客户数(筛选客户群)
    private Integer maxCustomerNum;   // 最大客户数(筛选内部群)
}
```

**响应**: `ResponseDTO<PageResult<WecomGroupVO>>`

```java
@Data
public class WecomGroupVO {
    private Long groupId;
    private String corpId;
    private String chatId;
    private String name;
    private String owner;
    private LocalDateTime groupCreateTime;
    private String notice;
    private String memberVersion;
    private Integer totalMember;
    private Integer staffNum;
    private Integer customerNum;
    private Integer groupStatus;
    private Boolean hasConversation;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
```

---

### 2. 查询群聊详情

**接口**: `GET /wecom/group/detail/{groupId}`

**权限**: `wecom:group:manage`

**路径参数**:
- `groupId` (Long) - 群聊ID

**响应**: `ResponseDTO<WecomGroupDetailVO>`

```java
@Data
public class WecomGroupDetailVO extends WecomGroupVO {
    private List<String> memberList;   // 成员ID列表
    private List<String> adminList;    // 管理员ID列表
    private Long conversationId;       // 关联的会话ID
    private Long messageCount;         // 消息数
}
```

---

### 3. 同步群聊信息

**接口**: `POST /wecom/group/sync`

**权限**: `wecom:group:manage`

**请求参数**:
```java
@Data
public class WecomGroupSyncForm {
    @NotNull
    private Long configId;

    private List<String> chatIds;  // 可选,指定群聊ID列表
}
```

**响应**: `ResponseDTO<WecomSyncResultVO>`

---

### 4. 按群主查询群聊

**接口**: `POST /wecom/group/by-owner`

**权限**: `wecom:group:manage`

**请求参数**:
```java
@Data
public class WecomGroupByOwnerForm extends PageParam {
    @NotBlank
    private String corpId;

    @NotBlank(message = "群主ID不能为空")
    private String owner;

    private Integer groupStatus;  // 可选,0-已解散 1-正常
}
```

**响应**: `ResponseDTO<PageResult<WecomGroupVO>>`

---

### 5. 查询客户群

**接口**: `POST /wecom/group/customer-groups`

**权限**: `wecom:group:manage`

**请求参数**:
```java
@Data
public class WecomGroupCustomerForm extends PageParam {
    @NotBlank
    private String corpId;

    private Integer minCustomerNum = 1;  // 默认至少1个客户
}
```

**响应**: `ResponseDTO<PageResult<WecomGroupVO>>`

**说明**: 查询 `customer_num > 0` 的群聊

---

### 6. 查询内部群

**接口**: `POST /wecom/group/internal-groups`

**权限**: `wecom:group:manage`

**请求参数**:
```java
@Data
public class WecomGroupInternalForm extends PageParam {
    @NotBlank
    private String corpId;
}
```

**响应**: `ResponseDTO<PageResult<WecomGroupVO>>`

**说明**: 查询 `customer_num = 0` 的群聊

---

### 7. 查询某人所在的群

**接口**: `POST /wecom/group/by-member`

**权限**: `wecom:group:manage`

**请求参数**:
```java
@Data
public class WecomGroupByMemberForm extends PageParam {
    @NotBlank
    private String corpId;

    @NotBlank(message = "成员ID不能为空")
    private String memberId;

    private Boolean isActive;  // 可选,true-在群 false-已退群
}
```

**响应**: `ResponseDTO<PageResult<WecomGroupVO>>`

---

### 8. 群聊统计信息

**接口**: `GET /wecom/group/stats`

**权限**: `wecom:group:manage`

**请求参数**:
- `corpId` (String, Query) - 企业ID

**响应**: `ResponseDTO<WecomGroupStatsVO>`

```java
@Data
public class WecomGroupStatsVO {
    private Long totalGroups;
    private Long activeGroups;
    private Long dissolvedGroups;
    private Long groupWithConversation;
    private Long customerGroups;      // customer_num > 0
    private Long internalGroups;      // customer_num = 0
    private Long avgMemberCount;      // 平均成员数
}
```

---

### 9. 群成员统计

**接口**: `GET /wecom/group/member-stats/{groupId}`

**权限**: `wecom:group:manage`

**路径参数**:
- `groupId` (Long) - 群聊ID

**响应**: `ResponseDTO<WecomGroupMemberStatsVO>`

```java
@Data
public class WecomGroupMemberStatsVO {
    private Long groupId;
    private String groupName;
    private Integer totalMember;
    private Integer activeMember;
    private Integer leftMember;
    private Integer staffNum;
    private Integer customerNum;
}
```

---

## 消息导出

**基础路径**: `/wecom/export`

### 1. 创建导出任务

**接口**: `POST /wecom/export/create`

**权限**: `wecom:export:create`

**请求参数** (`WecomExportTaskForm`):
```java
@Data
public class WecomExportTaskForm {
    @NotBlank(message = "任务名称不能为空")
    @Length(max = 255)
    private String taskName;

    @NotNull(message = "导出类型不能为空")
    private Integer exportType;  // 1-HTML 2-PDF 3-Excel 4-JSON

    @NotNull(message = "导出范围不能为空")
    private Integer exportScope;  // 1-单个会话 2-多个会话 3-按条件

    @NotNull(message = "筛选条件不能为空")
    private FilterCondition filterCondition;
}

@Data
public class FilterCondition {
    private TimeRange timeRange;
    private List<Long> conversationIds;
    private List<String> participantIds;
    private List<String> msgTypes;
}

@Data
public class TimeRange {
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
```

**响应**: `ResponseDTO<Long>` - 返回任务ID

---

### 2. 查询任务详情

**接口**: `GET /wecom/export/detail/{taskId}`

**权限**: `wecom:export:create`

**路径参数**:
- `taskId` (Long) - 任务ID

**响应**: `ResponseDTO<WecomExportTaskVO>`

```java
@Data
public class WecomExportTaskVO {
    private Long taskId;
    private String corpId;
    private String taskName;
    private Integer exportType;
    private Integer exportScope;
    private JSONObject filterCondition;
    private Integer taskStatus;  // 1-待处理 2-处理中 3-已完成 4-失败
    private Integer progressPercent;
    private Integer totalMsgCount;
    private Integer processedMsgCount;
    private String filePath;
    private String fileUrl;
    private Long fileSize;
    private String errorMsg;
    private LocalDateTime expireTime;
    private Long createUserId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
```

---

### 3. 查询导出任务列表

**接口**: `POST /wecom/export/query-page`

**权限**: `wecom:export:create`

**请求参数**:
```java
@Data
public class WecomExportTaskQueryForm extends PageParam {
    @NotBlank
    private String corpId;

    private Integer taskStatus;
    private Integer exportType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
```

**响应**: `ResponseDTO<PageResult<WecomExportTaskVO>>`

---

### 4. 取消任务

**接口**: `POST /wecom/export/cancel/{taskId}`

**权限**: `wecom:export:create`

**路径参数**:
- `taskId` (Long) - 任务ID

**响应**: `ResponseDTO<Void>`

**说明**: 仅可取消状态为 `1-待处理` 或 `2-处理中` 的任务

---

### 5. 删除任务

**接口**: `POST /wecom/export/delete/{taskId}`

**权限**: `wecom:export:create`

**路径参数**:
- `taskId` (Long) - 任务ID

**响应**: `ResponseDTO<Void>`

**说明**: 删除任务及关联的导出文件

---

### 6. 下载导出文件

**接口**: `GET /wecom/export/download/{taskId}`

**权限**: `wecom:export:download`

**路径参数**:
- `taskId` (Long) - 任务ID

**响应**: 文件流 (application/octet-stream)

**说明**:
- 检查任务状态是否为 `3-已完成`
- 检查文件是否过期 (`expire_time`)
- 返回文件流供下载

---

## 统计分析

**基础路径**: `/wecom/stats`

### 1. 综合统计概览

**接口**: `GET /wecom/stats/overview`

**权限**: `wecom:stats:view`

**请求参数**:
- `corpId` (String, Query) - 企业ID

**响应**: `ResponseDTO<WecomOverviewStatsVO>`

```java
@Data
public class WecomOverviewStatsVO {
    // 会话统计
    private Long totalConversations;
    private Long singleChatCount;
    private Long groupChatCount;
    private Long externalConversations;

    // 消息统计
    private Long totalMessages;
    private Long todayMessages;
    private Long weekMessages;
    private Long monthMessages;

    // 参与方统计
    private Long totalStaff;
    private Long totalCustomers;
    private Long totalGroups;

    // 活跃度统计
    private Long activeStaffCount;      // 近7天有消息的员工
    private Long activeCustomerCount;   // 近7天有消息的客户
}
```

---

### 2. 消息趋势分析

**接口**: `POST /wecom/stats/message-trend`

**权限**: `wecom:stats:view`

**请求参数**:
```java
@Data
public class WecomMessageTrendForm {
    @NotBlank
    private String corpId;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @NotNull
    private String dimension;  // "hour", "day", "week", "month"
}
```

**响应**: `ResponseDTO<List<WecomMessageTrendVO>>`

```java
@Data
public class WecomMessageTrendVO {
    private String timeDimension;  // 时间维度值(如:2025-10-08)
    private Long messageCount;
    private Long conversationCount;
}
```

---

### 3. 员工活跃度排行

**接口**: `POST /wecom/stats/staff-activity-rank`

**权限**: `wecom:stats:view`

**请求参数**:
```java
@Data
public class WecomStaffActivityForm {
    @NotBlank
    private String corpId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    private Integer topN = 20;  // 默认前20名
}
```

**响应**: `ResponseDTO<List<WecomStaffActivityVO>>`

```java
@Data
public class WecomStaffActivityVO {
    private String userId;
    private String userName;
    private Long messageCount;
    private Long conversationCount;
    private Long customerCount;
}
```

---

## 权限定义

### 权限码清单

```java
public class WecomPermissionCode {
    // 企业配置管理
    public static final String CONFIG_MANAGE = "wecom:config:manage";
    public static final String CONFIG_PULL = "wecom:config:pull";

    // 会话管理
    public static final String CHAT_LIST = "wecom:chat:list";
    public static final String CHAT_DETAIL = "wecom:chat:detail";
    public static final String CHAT_FAVORITE = "wecom:chat:favorite";

    // 消息管理
    public static final String MESSAGE_QUERY = "wecom:message:query";
    public static final String MESSAGE_SEARCH = "wecom:message:search";

    // 参与方管理
    public static final String PARTICIPANT_QUERY = "wecom:participant:query";

    // 员工管理
    public static final String STAFF_MANAGE = "wecom:staff:manage";
    public static final String STAFF_SYNC = "wecom:staff:sync";

    // 客户管理
    public static final String CUSTOMER_MANAGE = "wecom:customer:manage";
    public static final String CUSTOMER_SYNC = "wecom:customer:sync";

    // 群聊管理
    public static final String GROUP_MANAGE = "wecom:group:manage";
    public static final String GROUP_SYNC = "wecom:group:sync";

    // 导出功能
    public static final String EXPORT_CREATE = "wecom:export:create";
    public static final String EXPORT_DOWNLOAD = "wecom:export:download";

    // 统计分析
    public static final String STATS_VIEW = "wecom:stats:view";
}
```

### 权限分配建议

| 角色 | 权限组合 | 说明 |
|------|---------|------|
| **系统管理员** | 全部权限 | 完整管理权限 |
| **企微管理员** | `config:*`, `pull:*`, `sync:*` | 配置和同步管理 |
| **销售主管** | `chat:*`, `message:*`, `stats:*` | 查看和分析权限 |
| **普通销售** | `chat:list`, `chat:detail`, `message:query` | 基础查看权限 |
| **数据分析师** | `stats:*`, `export:*` | 统计和导出权限 |

---

## 接口使用示例

### 示例1: 查询某销售的客户会话列表

```java
// 1. 查询销售参与的外部会话
WecomConversationExternalForm form = new WecomConversationExternalForm();
form.setCorpId("test_corp");
form.setParticipantId("sales001");
form.setPageNum(1);
form.setPageSize(20);

ResponseDTO<PageResult<WecomConversationVO>> response =
    conversationController.queryExternalConversations(form);

// 2. 获取会话详情和消息
Long conversationId = response.getData().getList().get(0).getConversationId();

WecomMessageQueryForm msgForm = new WecomMessageQueryForm();
msgForm.setConversationId(conversationId);
msgForm.setStartTime(LocalDateTime.now().minusDays(7));
msgForm.setEndTime(LocalDateTime.now());
msgForm.setPageNum(1);
msgForm.setPageSize(50);

ResponseDTO<PageResult<WecomMessageVO>> messages =
    messageController.queryByConversation(msgForm);
```

---

### 示例2: 导出某客户的聊天记录

```java
// 1. 查询客户的会话
WecomConversationByCustomerForm queryForm = new WecomConversationByCustomerForm();
queryForm.setCorpId("test_corp");
queryForm.setCustomerUserId("customer_external_001");

ResponseDTO<PageResult<WecomConversationVO>> conversations =
    conversationController.queryByCustomer(queryForm);

// 2. 创建导出任务
WecomExportTaskForm exportForm = new WecomExportTaskForm();
exportForm.setTaskName("客户XXX聊天记录");
exportForm.setExportType(1);  // HTML
exportForm.setExportScope(2);  // 多个会话

FilterCondition filter = new FilterCondition();
filter.setConversationIds(
    conversations.getData().getList().stream()
        .map(WecomConversationVO::getConversationId)
        .collect(Collectors.toList())
);

TimeRange timeRange = new TimeRange();
timeRange.setStartTime(LocalDateTime.now().minusMonths(1));
timeRange.setEndTime(LocalDateTime.now());
filter.setTimeRange(timeRange);

exportForm.setFilterCondition(filter);

ResponseDTO<Long> taskId = exportController.createExportTask(exportForm);

// 3. 轮询任务状态
while (true) {
    ResponseDTO<WecomExportTaskVO> task =
        exportController.getTaskDetail(taskId.getData());

    if (task.getData().getTaskStatus() == 3) {  // 已完成
        // 下载文件
        exportController.downloadFile(taskId.getData());
        break;
    } else if (task.getData().getTaskStatus() == 4) {  // 失败
        throw new BusinessException("导出失败: " + task.getData().getErrorMsg());
    }

    Thread.sleep(2000);  // 等待2秒后重试
}
```

---

## 总结

### 接口设计特点

1. ✅ **统一规范**: 遵循 RESTful 风格,使用统一的 `ResponseDTO` 响应格式
2. ✅ **权限完善**: 所有接口均需权限验证,支持细粒度权限控制
3. ✅ **分页支持**: 列表查询统一使用分页,避免大数据量查询
4. ✅ **参数验证**: 使用 `@Valid` 注解,确保参数有效性
5. ✅ **错误处理**: 统一错误码,便于前端处理和问题定位
6. ✅ **查询优化**: 关键查询参数(如时间范围)必填,利用数据库分区
7. ✅ **扩展性强**: 预留统计分析接口,支持后续数据分析需求

### 核心接口汇总

| 功能模块 | 核心接口 | 备注 |
|---------|---------|------|
| **会话列表** | `POST /wecom/conversation/query-page` | 支持多条件筛选 |
| **消息查询** | `POST /wecom/message/query-by-conversation` | 必须带时间范围 |
| **参与方查询** | `GET /wecom/participant/by-conversation/{id}` | 查询会话成员 |
| **员工同步** | `POST /wecom/staff/sync` | 调用企微API同步 |
| **客户同步** | `POST /wecom/customer/sync` | 调用企微API同步 |
| **消息导出** | `POST /wecom/export/create` | 异步任务导出 |
| **统计概览** | `GET /wecom/stats/overview` | 综合统计数据 |

### 下一步工作

1. ✅ API 接口设计文档完成
2. ⏳ 编写 Entity/Form/VO 类
3. ⏳ 实现 Service 业务逻辑
4. ⏳ 开发 Controller 接口
5. ⏳ 编写接口单元测试
6. ⏳ Swagger 文档生成

---

**文档版本**: v1.0
**最后更新**: 2025-10-08
**维护人**: wangxiao
**变更说明**: 初版发布,定义企微会话存档 API 接口体系