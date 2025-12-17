# AI Chat 统一接口集成实施计划

## 需求概述

集成统一的 AI Chat 接口，支持多供应商扩展：
- 第一个供应商：**Dify**
- 响应模式：**流式响应（Streaming/SSE）** 为主
- 供应商配置存储在 `ai_app_provider` 表
- 通过 `app_id` → `app_provider_id` → 调用对应供应商实例
- **MVP 范围**：先实现核心 chat 接口，暂不考虑会话管理

## 技术栈

- Spring Boot 3.5.4
- MyBatis Plus 3.5.12
- RestClient (HTTP 客户端)
- SSE (Server-Sent Events) 流式响应

## 实施步骤

### 阶段 1: 基础架构搭建（优先级：最高）

#### 1.1 创建目录结构
```
sa-admin/src/main/java/net/lab1024/sa/admin/module/business/ai/
├── constant/        # 常量和枚举
├── controller/      # API 控制器
├── dao/            # MyBatis DAO
├── domain/         # 数据模型
│   ├── entity/     # 数据库实体
│   ├── form/       # 请求表单
│   ├── vo/         # 响应对象
│   └── dto/        # 内部传输对象
├── provider/       # 供应商策略层
│   └── impl/       # 供应商实现
└── service/        # 业务服务层
```

#### 1.2 常量枚举类（3 个文件）

**文件 1**: `constant/AiProviderTypeEnum.java`
```java
供应商类型枚举：
- DIFY("dify", "Dify")
- 预留：OPENAI, AZURE_OPENAI, ANTHROPIC
```

**文件 2**: `constant/AiResponseModeEnum.java`
```java
响应模式枚举：
- STREAMING("streaming", "流式响应")
- BLOCKING("blocking", "阻塞响应")
```

**文件 3**: `constant/AiFileTypeEnum.java`
```java
文件类型枚举：
- DOCUMENT, IMAGE, AUDIO, VIDEO, CUSTOM
文件传输方式：
- REMOTE_URL, LOCAL_FILE
```

### 阶段 2: 数据层实现（优先级：高）

#### 2.1 实体类（4 个文件）

**文件 1**: `domain/entity/AiAppEntity.java`
```java
表名：ai_app
字段：id, name, description, appProviderId, createBy, createByName,
      createAt, updateBy, updateByName, updateAt
注意：
- @TableId 主键
- 时间字段使用 LocalDateTime
- 参考 CategoryEntity 的写法
```

**文件 2**: `domain/entity/AiAppProviderEntity.java`
```java
表名：ai_app_provider
字段：id, name(供应商类型), baseUrl, key(API密钥),
      createAt, createBy, createByName, updateAt, updateBy, updateByName
```

**文件 3**: `domain/entity/AiConversationEntity.java`
```java
表名：ai_conversations
字段：id, appId, name, inputs(JSON), summary,
      createAt, createBy, createByName, updateAt, updateBy, updateByName
说明：本阶段暂不使用，为未来扩展预留
```

**文件 4**: `domain/entity/AiMessageEntity.java`
```java
表名：ai_messages
字段：id, conversatonId(注意拼写), query, message, inputs, answer,
      createBy, createByName, createAt, updateBy, updateByName, updateAt
说明：本阶段暂不使用，为未来扩展预留
```

#### 2.2 DAO 层（2 个文件）

**文件 1**: `dao/AiAppDao.java`
```java
@Mapper
public interface AiAppDao extends BaseMapper<AiAppEntity> {
    // 基础的 CRUD 由 MyBatis Plus 提供
}
```

**文件 2**: `dao/AiAppProviderDao.java`
```java
@Mapper
public interface AiAppProviderDao extends BaseMapper<AiAppProviderEntity> {
    // 基础的 CRUD 由 MyBatis Plus 提供
}
```

### 阶段 3: 请求响应模型（优先级：高）

#### 3.1 请求表单（2 个文件）

**文件 1**: `domain/form/AiChatMessageForm.java`
```java
@Data
public class AiChatMessageForm {
    @NotBlank(message = "应用ID不能为空")
    private String appId;           // 应用ID（必填）

    @NotBlank(message = "消息内容不能为空")
    private String query;           // 用户消息（必填）

    private Map<String, Object> inputs;  // 自定义变量（可选）

    @NotBlank(message = "响应模式不能为空")
    private String responseMode;    // streaming/blocking（必填）

    private String conversationId;  // 会话ID（可选，暂不使用）

    private String user;            // 用户标识（可选）

    private List<AiChatFileForm> files;  // 文件列表（可选）
}
```

**文件 2**: `domain/form/AiChatFileForm.java`
```java
@Data
public class AiChatFileForm {
    @NotBlank(message = "文件类型不能为空")
    private String type;            // document/image/audio/video

    @NotBlank(message = "传输方法不能为空")
    private String transferMethod;  // remote_url/local_file

    private String url;             // 文件URL（remote_url时必填）

    private String uploadFileId;    // 上传文件ID（local_file时必填）
}
```

#### 3.2 响应对象（1 个文件）

**文件**: `domain/vo/AiChatMessageVO.java`
```java
@Data
public class AiChatMessageVO {
    private String event;           // 事件类型：message/message_end/error
    private String messageId;       // 消息ID
    private String conversationId;  // 会话ID
    private String mode;            // chat
    private String answer;          // 回答内容
    private Long createdAt;         // 创建时间戳
    private Map<String, Object> metadata;  // 元数据（token使用量等）
    private String error;           // 错误信息
}
```

#### 3.3 内部传输对象（1 个文件）

**文件**: `domain/dto/AiChatContextDTO.java`
```java
@Data
public class AiChatContextDTO {
    private String baseUrl;         // 供应商API基础URL
    private String apiKey;          // API密钥
    private String query;           // 用户查询
    private Map<String, Object> inputs;  // 输入变量
    private String conversationId;  // 会话ID
    private String user;            // 用户标识
    private List<AiChatFileForm> files;  // 文件列表
}
```

### 阶段 4: 供应商策略层（优先级：核心）

#### 4.1 供应商接口（1 个文件）

**文件**: `provider/AiChatProvider.java`
```java
public interface AiChatProvider {
    /**
     * 获取供应商类型
     */
    String getProviderType();

    /**
     * 流式聊天（SSE）
     * @param context 聊天上下文
     * @param emitter SSE发射器
     */
    void chatStreaming(AiChatContextDTO context, SseEmitter emitter);

    /**
     * 阻塞式聊天（可选实现）
     */
    AiChatMessageVO chatBlocking(AiChatContextDTO context);
}
```

#### 4.2 供应商工厂（1 个文件）

**文件**: `provider/AiProviderFactory.java`
```java
@Component
public class AiProviderFactory {
    private final Map<String, AiChatProvider> providerMap = new ConcurrentHashMap<>();

    @Resource
    private List<AiChatProvider> providers;

    @PostConstruct
    public void init() {
        // 自动注册所有 @Component 的供应商
        for (AiChatProvider provider : providers) {
            providerMap.put(provider.getProviderType(), provider);
        }
    }

    public AiChatProvider getProvider(String providerType) {
        AiChatProvider provider = providerMap.get(providerType);
        if (provider == null) {
            throw new BusinessException("不支持的供应商: " + providerType);
        }
        return provider;
    }
}
```

#### 4.3 Dify 供应商实现（1 个文件，核心）

**文件**: `provider/impl/DifyAiChatProvider.java`
```java
@Component
@Slf4j
public class DifyAiChatProvider implements AiChatProvider {

    @Override
    public String getProviderType() {
        return AiProviderTypeEnum.DIFY.getValue();
    }

    @Override
    public void chatStreaming(AiChatContextDTO context, SseEmitter emitter) {
        try {
            // 1. 构建 Dify 请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("query", context.getQuery());
            requestBody.put("response_mode", "streaming");
            requestBody.put("user", context.getUser() != null ? context.getUser() : "default");

            if (context.getInputs() != null) {
                requestBody.put("inputs", context.getInputs());
            }
            if (context.getConversationId() != null) {
                requestBody.put("conversation_id", context.getConversationId());
            }
            if (context.getFiles() != null && !context.getFiles().isEmpty()) {
                requestBody.put("files", context.getFiles());
            }

            // 2. 创建 HTTP 连接（使用 HttpURLConnection 支持流式读取）
            String url = context.getBaseUrl() + "/v1/chat-messages";
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + context.getApiKey());
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setReadTimeout(300000); // 5分钟超时

            // 3. 发送请求
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = JSON.toJSONString(requestBody).getBytes(StandardCharsets.UTF_8);
                os.write(input);
            }

            // 4. 读取流式响应
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("data: ")) {
                        String data = line.substring(6).trim();

                        // 检查是否结束
                        if ("[DONE]".equals(data) || data.isEmpty()) {
                            emitter.complete();
                            break;
                        }

                        // 解析并发送 SSE 事件
                        try {
                            AiChatMessageVO eventVO = JSON.parseObject(data, AiChatMessageVO.class);
                            emitter.send(SseEmitter.event()
                                .name(eventVO.getEvent() != null ? eventVO.getEvent() : "message")
                                .data(eventVO));
                        } catch (Exception e) {
                            log.error("解析 SSE 数据失败: {}", data, e);
                        }
                    }
                }
            }

        } catch (IOException e) {
            log.error("Dify 流式 API 调用失败", e);
            emitter.completeWithError(e);
        }
    }

    @Override
    public AiChatMessageVO chatBlocking(AiChatContextDTO context) {
        // 暂不实现，抛出异常或返回 null
        throw new BusinessException("暂不支持阻塞模式");
    }
}
```

### 阶段 5: 业务服务层（优先级：核心）

#### 5.1 供应商配置服务（1 个文件）

**文件**: `service/AiProviderService.java`
```java
@Service
public class AiProviderService {

    @Resource
    private AiAppDao aiAppDao;

    @Resource
    private AiAppProviderDao aiAppProviderDao;

    /**
     * 根据应用ID获取应用信息
     */
    public AiAppEntity getApp(String appId) {
        AiAppEntity app = aiAppDao.selectById(appId);
        if (app == null) {
            throw new BusinessException("应用不存在: " + appId);
        }
        return app;
    }

    /**
     * 根据供应商ID获取供应商配置
     */
    public AiAppProviderEntity getProvider(String providerId) {
        AiAppProviderEntity provider = aiAppProviderDao.selectById(providerId);
        if (provider == null) {
            throw new BusinessException("供应商不存在: " + providerId);
        }
        return provider;
    }
}
```

#### 5.2 Chat 核心服务（1 个文件，核心）

**文件**: `service/AiChatService.java`
```java
@Service
@Slf4j
public class AiChatService {

    @Resource
    private AiProviderFactory providerFactory;

    @Resource
    private AiProviderService providerService;

    /**
     * 流式聊天（异步处理）
     */
    @Async
    public void chatStreaming(AiChatMessageForm form, SseEmitter emitter) {
        try {
            // 1. 根据 appId 获取应用配置
            AiAppEntity app = providerService.getApp(form.getAppId());

            // 2. 根据 appProviderId 获取供应商配置
            AiAppProviderEntity provider = providerService.getProvider(app.getAppProviderId());

            // 3. 构建聊天上下文
            AiChatContextDTO context = new AiChatContextDTO();
            context.setBaseUrl(provider.getBaseUrl());
            context.setApiKey(provider.getKey());
            context.setQuery(form.getQuery());
            context.setInputs(form.getInputs());
            context.setConversationId(form.getConversationId());
            context.setUser(form.getUser());
            context.setFiles(form.getFiles());

            // 4. 获取供应商实例并调用
            AiChatProvider chatProvider = providerFactory.getProvider(provider.getName());
            chatProvider.chatStreaming(context, emitter);

        } catch (Exception e) {
            log.error("流式聊天失败", e);
            emitter.completeWithError(e);
        }
    }

    /**
     * 阻塞式聊天（可选）
     */
    public AiChatMessageVO chatBlocking(AiChatMessageForm form) {
        // 1. 获取配置
        AiAppEntity app = providerService.getApp(form.getAppId());
        AiAppProviderEntity provider = providerService.getProvider(app.getAppProviderId());

        // 2. 构建上下文
        AiChatContextDTO context = new AiChatContextDTO();
        context.setBaseUrl(provider.getBaseUrl());
        context.setApiKey(provider.getKey());
        context.setQuery(form.getQuery());
        context.setInputs(form.getInputs());
        context.setUser(form.getUser());
        context.setFiles(form.getFiles());

        // 3. 调用供应商
        AiChatProvider chatProvider = providerFactory.getProvider(provider.getName());
        return chatProvider.chatBlocking(context);
    }
}
```

### 阶段 6: API 控制器（优先级：核心）

**文件**: `controller/AiChatController.java`
```java
@RestController
@RequestMapping("/ai/chat")
@Tag(name = "AI Chat")
@Slf4j
public class AiChatController {

    @Resource
    private AiChatService aiChatService;

    /**
     * 统一 Chat 消息接口
     * 支持 streaming 和 blocking 两种模式
     */
    @Operation(summary = "发送Chat消息 @author Mission")
    @PostMapping("/messages")
    public Object chatMessages(@RequestBody @Valid AiChatMessageForm form) {

        // 根据响应模式选择处理方式
        if (AiResponseModeEnum.STREAMING.getValue().equals(form.getResponseMode())) {
            // 流式响应：返回 SseEmitter
            SseEmitter emitter = new SseEmitter(300000L); // 5分钟超时

            // 设置完成和错误回调
            emitter.onCompletion(() -> log.info("SSE 连接完成"));
            emitter.onTimeout(() -> log.warn("SSE 连接超时"));
            emitter.onError(e -> log.error("SSE 连接错误", e));

            // 异步处理
            aiChatService.chatStreaming(form, emitter);

            return emitter;
        } else {
            // 阻塞响应：返回 ResponseDTO
            AiChatMessageVO result = aiChatService.chatBlocking(form);
            return ResponseDTO.ok(result);
        }
    }
}
```

### 阶段 7: 配置和依赖（优先级：中）

#### 7.1 异步配置

在项目中需要启用异步支持，检查 `AdminApplication.java` 是否有 `@EnableAsync` 注解。

如果没有，需要添加：
```java
@EnableAsync
@SpringBootApplication
public class AdminApplication {
    // ...
}
```

#### 7.2 线程池配置（可选）

在 `sa-base.yaml` 中添加：
```yaml
spring:
  task:
    execution:
      pool:
        core-size: 4
        max-size: 10
        queue-capacity: 100
        thread-name-prefix: ai-chat-
```

## 关键文件清单

### 必须创建的文件（按优先级排序）

#### 高优先级（核心功能）
1. `constant/AiProviderTypeEnum.java` - 供应商类型枚举
2. `constant/AiResponseModeEnum.java` - 响应模式枚举
3. `domain/entity/AiAppEntity.java` - 应用实体
4. `domain/entity/AiAppProviderEntity.java` - 供应商实体
5. `dao/AiAppDao.java` - 应用 DAO
6. `dao/AiAppProviderDao.java` - 供应商 DAO
7. `domain/form/AiChatMessageForm.java` - Chat 请求表单
8. `domain/vo/AiChatMessageVO.java` - Chat 响应 VO
9. `domain/dto/AiChatContextDTO.java` - Chat 上下文 DTO
10. `provider/AiChatProvider.java` - 供应商接口
11. `provider/AiProviderFactory.java` - 供应商工厂
12. `provider/impl/DifyAiChatProvider.java` - **Dify 实现（最核心）**
13. `service/AiProviderService.java` - 供应商配置服务
14. `service/AiChatService.java` - **Chat 服务（最核心）**
15. `controller/AiChatController.java` - **Chat 控制器（最核心）**

#### 中优先级（扩展功能）
16. `constant/AiFileTypeEnum.java` - 文件类型枚举
17. `domain/form/AiChatFileForm.java` - 文件表单

#### 低优先级（未来扩展）
18. `domain/entity/AiConversationEntity.java` - 会话实体（暂不使用）
19. `domain/entity/AiMessageEntity.java` - 消息实体（暂不使用）
20. `dao/AiConversationDao.java` - 会话 DAO（暂不使用）
21. `dao/AiMessageDao.java` - 消息 DAO（暂不使用）

## 完整文件路径参考

所有文件的完整路径前缀：
```
c:\Users\Mission\Desktop\demo\smart-admin\smart-admin-api-java17-springboot3\sa-admin\src\main\java\net\lab1024\sa\admin\module\business\ai\
```

例如：
- 枚举类：`constant/AiProviderTypeEnum.java`
- 实体类：`domain/entity/AiAppEntity.java`
- DAO：`dao/AiAppDao.java`
- 控制器：`controller/AiChatController.java`

## 测试计划

### 单元测试
1. 测试供应商工厂注册和获取
2. 测试供应商配置查询
3. 测试 Dify API 调用（Mock）

### 集成测试
1. 准备测试数据：
   - 在 `ai_app_provider` 表插入 Dify 配置
   - 在 `ai_app` 表插入测试应用
2. 测试流式响应：
   - 使用 Postman 或 curl 调用接口
   - 验证 SSE 事件流
3. 测试错误处理：
   - 无效的 appId
   - 无效的供应商配置
   - 网络超时

### 前端测试
使用 EventSource API 测试 SSE：
```javascript
const eventSource = new EventSource('/ai/chat/messages', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    appId: 'xxx',
    query: 'Hello',
    responseMode: 'streaming'
  })
});

eventSource.onmessage = (event) => {
  console.log('收到消息:', event.data);
};

eventSource.onerror = (error) => {
  console.error('SSE 错误:', error);
  eventSource.close();
};
```

## 技术要点和注意事项

### SSE 实现关键点
1. **超时设置**：SseEmitter 默认 30 秒，需设置为 300000ms（5 分钟）
2. **异步处理**：使用 `@Async` 避免阻塞 Tomcat 线程
3. **错误处理**：正确调用 `emitter.completeWithError(e)`
4. **连接管理**：客户端断开时自动清理资源
5. **编码问题**：使用 UTF-8 编码读取响应

### 流式读取实现
```java
// 不能使用 RestClient，因为它不支持流式读取
// 必须使用 HttpURLConnection 或 OkHttp
HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
connection.setReadTimeout(300000); // 5分钟
```

### 数据库字段映射
- 实体类字段使用驼峰命名：`createAt`
- 数据库字段使用下划线：`create_at`
- MyBatis Plus 自动转换
- 注意：`ai_messages.conversaton_id` 有拼写错误，保持与数据库一致

### 供应商扩展
添加新供应商步骤：
1. 在 `AiProviderTypeEnum` 添加枚举值
2. 创建实现类实现 `AiChatProvider`
3. 添加 `@Component` 注解自动注册
4. 在数据库配置供应商信息

## 实施建议

### 开发顺序
1. **第 1 天**：创建基础架构（枚举、实体、DAO）
2. **第 2 天**：实现供应商策略层（接口、工厂、Dify 实现）
3. **第 3 天**：实现业务服务层和控制器
4. **第 4 天**：测试和调试
5. **第 5 天**：优化和文档

### 风险点
1. **SSE 跨域问题**：前端可能需要配置 CORS
2. **网络超时**：Dify API 响应慢时需要合理设置超时
3. **并发问题**：多个用户同时使用时的线程池配置
4. **错误处理**：Dify API 错误格式需要正确解析

### 优化建议
1. 添加 Redis 缓存供应商配置
2. 添加请求日志记录
3. 添加性能监控（响应时间、token 使用量）
4. 考虑添加限流机制

## 未来扩展

### 会话管理（第二阶段）
- 实现会话创建和查询
- 实现消息历史记录
- 实现会话标题自动生成

### 多供应商支持（第三阶段）
- 添加 OpenAI 供应商
- 添加 Azure OpenAI 供应商
- 添加国内大模型（通义千问、文心一言等）

### 高级功能（第四阶段）
- 停止生成功能
- 消息反馈功能
- 工具调用支持
- 知识库集成

## 总结

本实施计划专注于 **MVP（最小可行产品）**：
- ✅ 实现统一 Chat 接口
- ✅ 支持 Dify 供应商
- ✅ 支持流式响应（SSE）
- ✅ 可扩展的供应商架构
- ⏳ 暂不实现会话管理（后续迭代）
- ⏳ 暂不实现消息存储（后续迭代）

核心文件数量：**15 个必须文件**
预计开发时间：**3-5 天**
