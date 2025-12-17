# 发送对话消息
  
  > 创建会话消息。
  
  ## OpenAPI
    
    ````yaml zh/api-reference/openapi_chatflow.json post /chat-messages
  openapi: 3.0.1
  info:
    title: 工作流编排对话型应用 API (Chatflow API)
    description: 对话应用支持会话持久化，可将之前的聊天记录作为上下文进行回答，可适用于聊天/客服 AI 等。
    version: 1.0.0
  servers:
    - url: '{api_base_url}'
      description: API 的基础 URL。请将 {api_base_url} 替换为你的应用提供的实际 API 基础 URL。
      variables:
        api_base_url:
          default: https://api.dify.ai/v1
          description: 实际的 API 基础 URL
  security:
    - ApiKeyAuth: []
  tags:
    - name: 对话消息
      description: 与对话消息和交互相关的操作。
    - name: 文件操作
      description: 文件上传和预览相关的操作。
    - name: 消息反馈
      description: 用户对消息的反馈操作。
    - name: 会话管理
      description: 管理对话会话相关的操作。
    - name: 语音与文字转换
      description: 文本转语音和语音转文本操作。
    - name: 应用配置
      description: 获取应用设置和信息相关的操作。
    - name: 标注管理
      description: 管理用于直接回复的标注数据。
  paths:
    /chat-messages:
      post:
        tags:
          - 对话消息
        summary: 发送对话消息
        description: 创建会话消息。
        operationId: sendChatMessageCn
        requestBody:
          description: 发送对话消息的请求体。
          required: true
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/ChatRequestCn'
              examples:
                streaming_with_file:
                  summary: 包含文件和自定义输入的流式请求示例
                  value:
                    inputs:
                      name: dify
                    query: iPhone 13 Pro Max 的规格是什么？
                    response_mode: streaming
                    conversation_id: 101b4c97-fc2e-463c-90b1-5261a4cdcafb
                    user: abc-123
                    files:
                      - type: image
                        transfer_method: remote_url
                        url: https://cloud.dify.ai/logo/logo-site.png
        responses:
          '200':
            description: >-
              请求成功。响应的内容类型和结构取决于请求中的 `response_mode` 参数。
              
              - 当 `response_mode` 为 `blocking` 时，返回 `application/json` 格式的
              `ChatCompletionResponseCn` 对象。
              
              - 当 `response_mode` 为 `streaming` 时，返回 `text/event-stream` 格式的
              `ChunkChatEventCn` 对象流式序列。
            content:
              application/json:
                schema:
                  $ref: '#/components/schemas/ChatCompletionResponseCn'
              text/event-stream:
                schema:
                  type: string
                  description: >-
                    SSE 事件流。每个事件以 'data: ' 开头，以 '\n\n' 结尾。具体结构请参见
                    `ChunkChatEventCn`。
          '400':
            $ref: '#/components/responses/BadRequestGenericCn'
          '404':
            $ref: '#/components/responses/ConversationNotFoundCn'
          '500':
            $ref: '#/components/responses/InternalServerErrorCn'
  components:
    schemas:
      ChatRequestCn:
        type: object
        required:
          - query
          - user
        properties:
          query:
            type: string
            description: 用户输入/提问内容。
          inputs:
            type: object
            description: 允许传入 App 定义的各变量值。如果变量是文件类型，请指定一个 InputFileObjectCn 对象。
            additionalProperties:
              oneOf:
                - type: string
                - type: number
                - type: boolean
                - $ref: '#/components/schemas/InputFileObjectCn'
            default: {}
          response_mode:
            type: string
            enum:
              - streaming
              - blocking
            default: streaming
            description: 响应模式。streaming (推荐) 基于 SSE；blocking 等待执行完毕后返回 (Cloudflare 100秒超时限制)。
          user:
            type: string
            description: >-
              用户标识，应用内唯一。**重要说明**: Service API 不共享 WebApp 创建的对话。通过 API 创建的对话与
              WebApp 界面中创建的对话是相互隔离的。
          conversation_id:
            type: string
            format: uuid
            description: （选填）会话 ID，用于继续之前的对话。
          files:
            type: array
            items:
              $ref: '#/components/schemas/InputFileObjectCn'
            description: （选填）文件列表，仅当模型支持 Vision 能力时可用。
          auto_generate_name:
            type: boolean
            default: true
            description: （选填）自动生成会话标题，默认 true。
      ChatCompletionResponseCn:
        type: object
        description: 阻塞模式下的完整 App 结果。
        properties:
          event:
            type: string
            example: message
            description: 事件类型，固定为 `message`。
          task_id:
            type: string
            format: uuid
            description: 任务 ID。
          id:
            type: string
            format: uuid
            description: 唯一ID。
          message_id:
            type: string
            format: uuid
            description: 消息唯一 ID。
          conversation_id:
            type: string
            format: uuid
            description: 会话 ID。
          mode:
            type: string
            example: chat
            description: App 模式，固定为 `chat`。
          answer:
            type: string
            description: 完整回复内容。
          metadata:
            $ref: '#/components/schemas/ResponseMetadataCn'
          created_at:
            type: integer
            format: int64
            description: 消息创建时间戳。
      InputFileObjectCn:
        type: object
        required:
          - type
          - transfer_method
        properties:
          type:
            type: string
            enum:
              - document
              - image
              - audio
              - video
              - custom
            description: >-
              文件类型。document: TXT,MD,PDF等; image: JPG,PNG等; audio: MP3,WAV等; video:
              MP4,MOV等; custom: 其他。
          transfer_method:
            type: string
            enum:
              - remote_url
              - local_file
            description: 传递方式，remote_url 用于图片 URL / local_file 用于文件上传
          url:
            type: string
            format: url
            description: 图片地址（当传递方式为 remote_url 时）
          upload_file_id:
            type: string
            format: uuid
            description: 上传文件 ID，必须通过事先上传文件接口获得（当传递方式为 local_file 时）
        anyOf:
          - properties:
              transfer_method:
                enum:
                  - remote_url
              url:
                type: string
                format: url
            required:
              - url
            not:
              required:
                - upload_file_id
          - properties:
              transfer_method:
                enum:
                  - local_file
              upload_file_id:
                type: string
                format: uuid
            required:
              - upload_file_id
            not:
              required:
                - url
      ResponseMetadataCn:
        type: object
        description: 元数据。
        properties:
          usage:
            $ref: '#/components/schemas/UsageCn'
          retriever_resources:
            type: array
            items:
              $ref: '#/components/schemas/RetrieverResourceCn'
            description: 引用和归属分段列表。
      ErrorResponseCn:
        type: object
        description: 错误响应。
        properties:
          status:
            type: integer
            nullable: true
            description: HTTP 状态码。
          code:
            type: string
            nullable: true
            description: 错误码。
          message:
            type: string
            description: 错误消息。
      UsageCn:
        type: object
        description: 模型用量信息。
        properties:
          prompt_tokens:
            type: integer
          prompt_unit_price:
            type: string
          prompt_price_unit:
            type: string
          prompt_price:
            type: string
          completion_tokens:
            type: integer
          completion_unit_price:
            type: string
          completion_price_unit:
            type: string
          completion_price:
            type: string
          total_tokens:
            type: integer
          total_price:
            type: string
          currency:
            type: string
          latency:
            type: number
            format: double
      RetrieverResourceCn:
        type: object
        description: 引用和归属分段信息。
        properties:
          position:
            type: integer
          dataset_id:
            type: string
            format: uuid
          dataset_name:
            type: string
          document_id:
            type: string
            format: uuid
          document_name:
            type: string
          segment_id:
            type: string
            format: uuid
          score:
            type: number
            format: float
          content:
            type: string
    responses:
      BadRequestGenericCn:
        description: >-
          请求参数错误。可能原因：invalid_param, app_unavailable, provider_not_initialize,
          provider_quota_exceeded, model_currently_not_support,
          completion_request_error。
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ErrorResponseCn'
      ConversationNotFoundCn:
        description: 对话不存在。
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ErrorResponseCn'
      InternalServerErrorCn:
        description: 服务内部异常。
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/ErrorResponseCn'
    securitySchemes:
      ApiKeyAuth:
        type: http
        scheme: bearer
        bearerFormat: API_KEY
        description: >-
          API-Key 鉴权。所有 API 请求都应在 Authorization HTTP Header 中包含你的
          API-Key，格式为：Bearer {API_KEY}。强烈建议开发者把 API-Key 放在后端存储，而非客户端，以免泄露。
    
    ````

---
  
  > To find navigation and other pages in this documentation, fetch the llms.txt file at: https://docs.dify.ai/llms.txt