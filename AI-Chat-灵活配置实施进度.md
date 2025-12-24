# AI Chat 灵活配置系统 - 实施进度报告

## 已完成的工作

### 1. 数据库变更脚本 ✅
**文件**: `数据库SQL脚本/AI/add-flexible-config.sql`

- 添加 `t_ai_app_provider.config_json` 字段（平台级配置）
- 添加 `t_ai_app.app_config_json` 字段（应用级配置）
- 包含数据备份和迁移脚本
- 将现有 Dify 的 `provider.key` 迁移到 `app.app_config_json`

### 2. 后端配置模型类 ✅
已创建以下文件：

- **ProviderConfigBase.java** - 配置基类
- **DifyProviderConfig.java** - Dify 平台配置（目前为空）
- **DifyAppConfig.java** - Dify 应用配置（包含 apiKey 字段）
- **OpenAiProviderConfig.java** - OpenAI 配置（预留扩展）
- **ProviderConfigFactory.java** - 配置工厂，负责解析和验证

### 3. 实体类更新 ✅
- **AiAppProviderEntity.java** - 添加 `configJson` 字段
- **AiAppEntity.java** - 添加 `appConfigJson` 字段

### 4. 服务层更新 ✅
- **AiChatContextDTO.java** - 添加 `providerConfig` 和 `appConfigJson` 字段
- **AiChatService.java** - 注入 `ProviderConfigFactory`，更新 `buildChatContext` 方法
- **DifyAiChatProvider.java** - 添加 `getDifyApiKey` 方法，优先使用应用级 API Key

### 5. 核心逻辑
Dify API Key 获取逻辑（DifyAiChatProvider:106-124）：
1. 优先使用 `app.app_config_json` 中的 `apiKey`
2. 降级使用 `provider.key`（向后兼容）
3. 如果都为空则抛出异常

---

## 待完成的工作

### 1. 后端 API 和 VO 类（剩余）

#### 需要修改的文件：

**AiAppProviderController.java**
添加查询所有供应商的接口：
```java
@GetMapping("/queryAll")
@SaCheckPermission("aiAppProvider:query")
public ResponseDTO<List<AiAppProviderVO>> queryAll() {
    List<AiAppProviderEntity> list = aiAppProviderService.aiAppProviderDao.selectList(null);
    List<AiAppProviderVO> voList = SmartBeanUtil.copyList(list, AiAppProviderVO.class);
    return ResponseDTO.ok(voList);
}
```

**AiAppProviderVO.java**
添加字段：
```java
@Schema(description = "平台扩展配置")
private String configJson;
```

**AiAppVO.java**
添加字段：
```java
@Schema(description = "应用级别配置")
private String appConfigJson;
```

### 2. 前端实现

#### 2.1 创建供应商下拉组件
**文件**: `smart-admin-web-javascript/src/components/ai-app-provider-select/index.vue`

主要功能：
- 下拉选择智能体平台
- 支持搜索过滤
- 显示平台标签（Dify / 其他）

#### 2.2 更新 API 方法
**文件**: `smart-admin-web-javascript/src/api/business/ai/ai-app-provider-api.js`

添加方法：
```javascript
export function queryAll() {
  return getRequest('/aiAppProvider/queryAll');
}
```

#### 2.3 更新 AI 应用表单
**文件**: `smart-admin-web-javascript/src/views/business/ai-app/ai-app-form.vue`

主要修改：
- 将供应商输入框改为下拉选择组件
- 当选择 Dify 时，显示 API Key 输入框
- 自动将 API Key 转换为 JSON 格式存储

---

## 下一步操作

### 步骤1：执行数据库变更
```bash
mysql -u root -p smart_admin_v3 < 数据库SQL脚本/AI/add-flexible-config.sql
```

验证：
```sql
DESC t_ai_app_provider;
DESC t_ai_app;

-- 查看 Dify 配置
SELECT a.id, a.name, p.name as platform, p.base_url, p.key,
       p.config_json, a.app_config_json
FROM t_ai_app a
JOIN t_ai_app_provider p ON a.app_provider_id = p.id
WHERE p.name = 'dify';
```

### 步骤2：编译后端代码
```bash
cd smart-admin-api-java17-springboot3
mvn clean compile
```

如果遇到编译错误，请检查：
- 所有导入语句是否正确
- FastJSON 版本是否支持 Java 17

### 步骤3：完成剩余的后端代码
参考"待完成的工作"部分，添加：
- Controller 的 queryAll 接口
- VO 类的新字段

### 步骤4：实现前端代码
按照计划文档创建：
- 下拉组件
- API 方法
- 更新表单

### 步骤5：端到端测试
```bash
# 启动后端
cd smart-admin-api-java17-springboot3/sa-admin
mvn spring-boot:run

# 测试接口
curl -X POST http://localhost:8080/ai/chat/messages \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "appId": "app001",
    "query": "你好",
    "responseMode": "streaming",
    "user": "test"
  }'
```

---

## 设计要点

### Dify 配置设计
- **平台级（t_ai_app_provider）**：
  - `baseUrl`: https://api.dify.ai
  - `key`: NULL（Dify 不需要平台级 Key）
  - `config_json`: NULL 或 {}

- **应用级（t_ai_app）**：
  - 应用A：`app_config_json = {"apiKey": "app-aaa123"}`
  - 应用B：`app_config_json = {"apiKey": "app-bbb456"}`

每个 Dify 应用使用自己独立的 API Key 调用 Dify API。

### 扩展性
完成 Dify 后，添加 RAGFlow 或自定义 Agent 只需：
1. 创建对应的配置类（RagflowProviderConfig/RagflowAppConfig）
2. 在 ProviderConfigFactory 添加 case 分支
3. 实现对应的 Provider（RagflowAiChatProvider）
4. 前端添加对应的配置表单逻辑

---

## 参考文档
- 详细计划：`C:\Users\Mission\.claude\plans\glimmering-scribbling-octopus.md`
- Dify API 文档：`dify-document/chat-message.md`
