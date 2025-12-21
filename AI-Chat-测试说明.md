# AI Chat 接口测试说明

## 前置准备

### 1. 执行测试数据脚本
```bash
mysql -u root -p your_database < 数据库SQL脚本/AI/test-data.sql
```

### 2. 修改测试数据
编辑 `test-data.sql` 中的以下内容：
- 将 `your-dify-api-key` 替换为实际的 Dify API Key
- 将 `https://api.dify.ai` 替换为实际的 Dify API 基础 URL

## 接口测试

### Postman 测试流式响应

**请求配置**：
```
Method: POST
URL: http://localhost:8080/ai/chat/messages
Headers:
  Content-Type: application/json
  Authorization: Bearer {your-satoken}
```

**请求体（流式模式）**：
```json
{
  "appId": "app001",
  "query": "你好，请介绍一下你自己",
  "responseMode": "streaming",
  "user": "test-user"
}
```

**预期响应**：
- Content-Type: `text/event-stream`
- 响应格式：SSE 事件流
- 事件示例：
```
data: {"event":"message","answer":"你好！","messageId":"xxx","conversationId":"xxx"}

data: {"event":"message_end","messageId":"xxx","conversationId":"xxx"}
```

### cURL 测试

```bash
curl -X POST http://localhost:8080/ai/chat/messages \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "appId": "app001",
    "query": "你好",
    "responseMode": "streaming",
    "user": "test-user"
  }'
```

### 前端 EventSource 测试

```javascript
// 注意：POST 请求不能直接使用 EventSource，需要使用 fetch + EventSource 结合
const response = await fetch('/ai/chat/messages', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': 'Bearer YOUR_TOKEN'
  },
  body: JSON.stringify({
    appId: 'app001',
    query: '你好',
    responseMode: 'streaming',
    user: 'test-user'
  })
});

const reader = response.body.getReader();
const decoder = new TextDecoder();

while (true) {
  const {done, value} = await reader.read();
  if (done) break;

  const chunk = decoder.decode(value);
  const lines = chunk.split('\n');

  for (const line of lines) {
    if (line.startsWith('data: ')) {
      const data = line.substring(6);
      if (data !== '[DONE]' && data.trim()) {
        const event = JSON.parse(data);
        console.log('收到消息:', event);
      }
    }
  }
}
```

## 权限配置

需要在权限管理中添加 `ai:chat` 权限点，或者临时移除 Controller 中的 `@SaCheckPermission("ai:chat")` 注解进行测试。

## 常见问题

### 1. 401 Unauthorized
- 检查是否携带了正确的 Authorization Token
- 检查权限配置是否正确

### 2. 应用不存在
- 检查数据库中是否成功插入了测试数据
- 检查 appId 是否正确（app001）

### 3. 供应商不存在
- 检查 t_ai_app 的 app_provider_id 是否正确关联到 t_ai_app_provider
- 检查 provider 的 name 字段是否为 "dify"

### 4. Dify API 调用失败
- 检查 base_url 是否正确
- 检查 API Key 是否有效
- 检查网络连接是否正常
- 查看后端日志了解详细错误信息

### 5. SSE 连接超时
- 检查是否在 5 分钟内有响应
- 检查 Dify API 是否正常工作
- 查看后端日志

## 下一步

测试通过后，可以：
1. 实现会话管理功能
2. 实现消息历史存储
3. 添加更多供应商（OpenAI、Azure OpenAI等）
4. 添加停止生成功能
5. 添加消息反馈功能
