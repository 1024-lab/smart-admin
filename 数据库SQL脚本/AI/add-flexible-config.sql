-- AI Chat 统一接口 - 智能体平台灵活配置系统数据库变更脚本
-- 执行日期: 2025-12-24
-- 功能: 添加平台级和应用级配置字段，支持 Dify 等智能体平台

-- ================================================================================
-- 1. 添加平台扩展配置字段
-- ================================================================================
ALTER TABLE t_ai_app_provider
ADD COLUMN config_json TEXT COMMENT '平台扩展配置(JSON格式)' AFTER `key`;

-- 配置示例说明:
-- Dify: null 或 {} (Dify 不需要平台级配置)
-- RAGFlow: {"timeout": 30000, "retryCount": 3} 等
-- 自定义: {"authType": "bearer", "customHeaders": {...}} 等

-- ================================================================================
-- 2. 添加应用级配置字段
-- ================================================================================
ALTER TABLE t_ai_app
ADD COLUMN app_config_json TEXT COMMENT '应用级别配置(JSON格式)' AFTER app_provider_id;

-- 配置示例说明:
-- Dify 应用配置: {"apiKey": "app-xxx123456"} - 每个应用有独立的 API Key
-- RAGFlow 应用配置: {"appId": "rag-app-001"} - 如果使用统一认证
-- 自定义应用配置: {} 或 null - 如果使用平台级配置

-- ================================================================================
-- 3. 数据迁移 - 将现有 Dify API Key 迁移到应用级别配置
-- ================================================================================

-- 3.1 备份现有数据 (可选但强烈建议)
CREATE TABLE IF NOT EXISTS t_ai_app_provider_backup_20251224 AS SELECT * FROM t_ai_app_provider;
CREATE TABLE IF NOT EXISTS t_ai_app_backup_20251224 AS SELECT * FROM t_ai_app;

-- 3.2 迁移 Dify API Key 从 provider.key 到 app.app_config_json
UPDATE t_ai_app a
INNER JOIN t_ai_app_provider p ON a.app_provider_id = p.id
SET a.app_config_json = JSON_OBJECT('apiKey', p.`key`)
WHERE p.name = 'dify' AND p.`key` IS NOT NULL;

-- 3.3 清空 Dify 供应商的 key 字段 (因为已迁移到应用级别)
UPDATE t_ai_app_provider
SET `key` = NULL
WHERE name = 'dify';

-- ================================================================================
-- 执行完成提示
-- ================================================================================
-- 请执行以下 SQL 验证变更是否成功:
-- 1. 验证字段添加: DESC t_ai_app_provider; DESC t_ai_app;
-- 2. 查看 Dify 配置:
--    SELECT a.id, a.name, p.name as platform, p.base_url, p.key,
--           p.config_json, a.app_config_json
--    FROM t_ai_app a
--    JOIN t_ai_app_provider p ON a.app_provider_id = p.id
--    WHERE p.name = 'dify';
