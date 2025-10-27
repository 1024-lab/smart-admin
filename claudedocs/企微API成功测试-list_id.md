---
title: 企业微信 list_id API 成功测试报告
author: wangxiao
company: 子午线高科智能科技
date: 2025-10-14
permalink: claudedocs/企微-api-成功测试-list-id
---

# 企业微信 list_id API 成功测试报告

## 🎉 重大突破

### ✅ 通讯录Secret可以使用 list_id API!

虽然通讯录同步助手Secret不支持 `user/simplelist` 和 `user/get` API,但**可以使用 `user/list_id` API**!

---

## 📊 测试结果

### API信息

**API路径**: `/cgi-bin/user/list_id`
**请求方式**: POST
**Content-Type**: application/json
**请求Body**: `{}`

### 测试响应

```json
{
    "errcode": 0,
    "errmsg": "ok",
    "dept_user": [
        {"userid": "YunEr", "department": 1},
        {"userid": "LuFeiXueCheng-amandali", "department": 1},
        {"userid": "ShaoNianLang", "department": 1},
        ... (共约350+个成员)
    ]
}
```

### 获取到的数据

**成员总数**: **350+** 个成员
**数据字段**:
- `userid`: 成员UserID
- `department`: 所属部门ID

**部门覆盖**: 1, 2, 3, 7, 8, 10, 11, 12, 14, 15, 16, 17, 18, 19, 22, 23, 25, 26, 27, 28, 29, 30, 31, 32, 35, 36, 37, 38, 39, 40, 41, 42, 43

---

## 💡 API特点分析

### 与其他API的对比

| API | 通讯录同步助手支持 | 返回数据 |
|-----|------------------|----------|
| `/user/simplelist` | ❌ 不支持 (48009) | userid, name, department |
| `/user/list` | ❌ 不支持 (48009) | 完整成员信息 |
| `/user/get` | ❌ 不支持 (48009) | 单个成员详情 |
| **`/user/list_id`** | **✅ 支持** | **userid, department** |

### list_id API 的优势

1. **✅ 通讯录同步助手可用**
   - 不需要创建自建应用
   - 现有Secret即可使用

2. **✅ 获取所有成员UserID**
   - 一次性获取全部成员
   - 包含部门归属信息

3. **✅ 数据量大**
   - 获取到350+个成员
   - 覆盖多个部门

### list_id API 的限制

1. **⚠️ 无成员姓名**
   - 只有UserID,没有name字段
   - 无法直接显示姓名

2. **⚠️ 无详细信息**
   - 没有手机号、头像等
   - 只有最基础的ID和部门

3. **⚠️ 需要二次查询**
   - 要获取姓名需要调用其他API
   - 或从会话消息中关联

---

## 🎯 实际应用价值

### 场景1: 与会话存档配合

**流程**:
1. 使用 `list_id` 获取所有成员UserID和部门
2. 使用会话存档API拉取消息
3. 从消息中匹配UserID
4. 知道该UserID属于哪个部门

**价值**:
- ✅ 可以按部门筛选会话
- ✅ 可以统计部门的消息量
- ✅ 可以建立UserID到部门的映射

### 场景2: 建立UserID映射表

**数据库设计**:
```sql
CREATE TABLE t_wecom_staff_simple (
    staff_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    corp_id VARCHAR(64) NOT NULL,
    wecom_user_id VARCHAR(64) NOT NULL,  -- 来自list_id
    department_ids TEXT,                  -- 部门列表(JSON)
    name VARCHAR(128),                    -- 暂时为空,后续填充
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_corp_user (corp_id, wecom_user_id)
);
```

**数据填充策略**:
1. 先用 `list_id` 填充 `wecom_user_id` 和 `department_ids`
2. 后续从会话消息中提取姓名 (如果消息体包含)
3. 或者等有自建应用Secret后再填充姓名

### 场景3: 部门统计分析

基于 `list_id` 返回的部门信息:
- 统计各部门人数
- 分析组织结构
- 按部门筛选会话

---

## 🚀 推荐实施方案

### 方案: 混合使用 list_id + 会话消息

#### 第一步: 同步UserID和部门

```java
/**
 * 同步员工UserID和部门信息
 * @author wangxiao
 */
public void syncStaffListId() {
    // 1. 调用list_id API
    String accessToken = getAccessToken(CONTACTS_SECRET);
    WecomListIdResponse response = wecomApiService.getUserListId(accessToken);

    // 2. 批量插入数据库
    for (DeptUser user : response.getDeptUser()) {
        WecomStaffSimple staff = new WecomStaffSimple();
        staff.setCorpId(CORP_ID);
        staff.setWecomUserId(user.getUserid());
        staff.setDepartmentIds(JSON.toJSONString(Collections.singletonList(user.getDepartment())));
        // name 暂时为空

        staffSimpleDao.insertOrUpdate(staff);
    }
}
```

#### 第二步: 从消息中提取姓名 (可选)

有些企业微信消息可能包含发送者姓名,可以尝试提取:

```java
/**
 * 从消息中更新员工姓名
 * @author wangxiao
 */
public void updateStaffNameFromMessage(WecomMessage message) {
    String fromId = message.getFromId();

    // 某些消息类型可能包含发送者姓名
    // 具体取决于企业微信的消息格式

    if (StringUtils.isNotEmpty(fromName)) {
        staffSimpleDao.updateName(fromId, fromName);
    }
}
```

#### 第三步: 前端显示策略

```vue
<template>
  <div class="chat-sender">
    <!-- 优先显示姓名,如果没有则显示UserID -->
    <span>{{ staff.name || staff.wecomUserId }}</span>
    <span class="department">部门{{ staff.departmentIds }}</span>
  </div>
</template>
```

---

## 📋 完整的测试脚本

### Bash脚本

```bash
#!/bin/bash

CORP_ID="ww7d5bca9c66c2e988"
CONTACTS_SECRET="URKzVx0vJRWJ7QPbjizd5WbrABSkqyq5EjKsNOeLhx4"

# 获取Token
TOKEN=$(curl -s "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=$CORP_ID&corpsecret=$CONTACTS_SECRET" | python3 -c "import sys, json; print(json.load(sys.stdin)['access_token'])")

echo "Access Token: ${TOKEN:0:30}..."
echo ""

# 调用list_id API
echo "调用 list_id API..."
curl -s -X POST "https://qyapi.weixin.qq.com/cgi-bin/user/list_id?access_token=$TOKEN" \
  -H "Content-Type: application/json" \
  -d '{}' | python3 -m json.tool
```

### Java示例

```java
/**
 * 企业微信list_id API调用
 * @author wangxiao
 */
@Service
public class WecomContactService {

    /**
     * 获取成员UserID列表
     */
    public WecomListIdResponse getUserListId(String accessToken) {
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list_id?access_token=" + accessToken;

        // 请求参数(空对象)
        Map<String, Object> params = new HashMap<>();

        // 发送POST请求
        String response = HttpUtil.post(url, JSON.toJSONString(params));

        // 解析响应
        WecomListIdResponse result = JSON.parseObject(response, WecomListIdResponse.class);

        if (result.getErrcode() != 0) {
            throw new BusinessException("获取成员列表失败: " + result.getErrmsg());
        }

        log.info("成功获取{}个成员", result.getDeptUser().size());
        return result;
    }
}

/**
 * 响应实体类
 */
@Data
public class WecomListIdResponse {
    private Integer errcode;
    private String errmsg;
    private List<DeptUser> deptUser;
}

@Data
public class DeptUser {
    private String userid;
    private Integer department;
}
```

---

## ✅ 结论

### 通讯录同步助手Secret的可用API

虽然通讯录同步助手不支持 `simplelist` 和 `get` API,但**可以使用 `list_id` API**!

### 实际应用价值

1. **✅ 获取所有成员UserID**
   - 350+个成员
   - 包含部门信息

2. **✅ 建立UserID到部门的映射**
   - 支持按部门筛选
   - 支持部门统计

3. **✅ 与会话存档配合**
   - 消息中的UserID可以关联到部门
   - 可以实现基础的员工识别

### 限制和解决方案

**限制**: 没有姓名等详细信息

**解决方案**:
1. **临时方案**: 前端显示UserID
2. **优化方案**: 后续创建自建应用填充姓名
3. **混合方案**: 优先显示姓名,回退到UserID

---

## 🎯 下一步行动

### 立即可以做 (使用list_id)

1. ✅ 实现 `list_id` API调用
2. ✅ 同步UserID和部门到数据库
3. ✅ 建立UserID到部门的映射
4. ✅ 会话列表按部门筛选
5. ✅ 前端显示UserID (临时方案)

### 后续优化 (创建自建应用)

1. ⏳ 创建自建应用获取新Secret
2. ⏳ 调用 `/user/get` 获取姓名
3. ⏳ 更新数据库中的姓名字段
4. ⏳ 前端显示姓名 (完整方案)

---

## 📚 相关文档

- [企业微信通讯录管理API](https://developer.work.weixin.qq.com/document/path/90201)
- [获取成员ID列表](https://developer.work.weixin.qq.com/document/path/96067)

---

**报告生成时间**: 2025-10-14
**测试状态**: ✅ 成功
**API状态**: ✅ 可用
**数据量**: 350+ 个成员