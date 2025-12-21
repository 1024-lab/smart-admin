-- AI Chat 测试数据脚本
-- 使用说明：
-- 1. 替换 'your-dify-api-key' 为你的实际 Dify API Key
-- 2. 替换 'https://api.dify.ai' 为你的实际 Dify API 基础 URL
-- 3. 执行此脚本插入测试数据

-- 1. 插入 Dify 供应商配置
INSERT INTO t_ai_app_provider (id, name, base_url, `key`, create_at, create_by, create_by_name, update_at, update_by, update_by_name)
VALUES (
    'provider001',
    'dify',
    'https://api.dify.ai',
    'your-dify-api-key',
    NOW(),
    'admin',
    '管理员',
    NOW(),
    'admin',
    '管理员'
);

-- 2. 插入测试应用
INSERT INTO t_ai_app (id, name, description, app_provider_id, create_at, create_by, create_by_name, update_at, update_by, update_by_name)
VALUES (
    'app001',
    'Dify测试应用',
    '用于测试 Dify Chat 接口的应用',
    'provider001',
    NOW(),
    'admin',
    '管理员',
    NOW(),
    'admin',
    '管理员'
);

-- 查看插入结果
SELECT * FROM t_ai_app_provider WHERE id = 'provider001';
SELECT * FROM t_ai_app WHERE id = 'app001';
