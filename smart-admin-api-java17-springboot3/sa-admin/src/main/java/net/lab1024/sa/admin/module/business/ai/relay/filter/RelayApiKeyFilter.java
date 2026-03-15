package net.lab1024.sa.admin.module.business.ai.relay.filter;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.relay.context.RelayRequestContext;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayTokenDao;
import net.lab1024.sa.admin.module.business.ai.relay.dao.RelayUserAccountDao;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayTokenEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayUserAccountEntity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class RelayApiKeyFilter implements Filter {

    private final RelayTokenDao relayTokenDao;
    private final RelayUserAccountDao relayUserAccountDao;

    public RelayApiKeyFilter(RelayTokenDao relayTokenDao, RelayUserAccountDao relayUserAccountDao) {
        this.relayTokenDao = relayTokenDao;
        this.relayUserAccountDao = relayUserAccountDao;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            String authorization = request.getHeader("Authorization");
            if (authorization == null || !authorization.startsWith("Bearer ")) {
                writeError(response, 401, "invalid_api_key", "缺少Authorization头或格式错误");
                return;
            }

            String plainToken = authorization.substring(7).trim();
            if (plainToken.isEmpty()) {
                writeError(response, 401, "invalid_api_key", "API Key不能为空");
                return;
            }

            String tokenHash = sha256(plainToken);
            RelayTokenEntity token = relayTokenDao.selectByTokenHash(tokenHash);
            if (token == null) {
                writeError(response, 401, "invalid_api_key", "无效的API Key");
                return;
            }

            if (token.getDisabledFlag() == 1) {
                writeError(response, 403, "api_key_disabled", "API Key已禁用");
                return;
            }

            if (token.getExpiresTime() != null && token.getExpiresTime().isBefore(LocalDateTime.now())) {
                writeError(response, 403, "api_key_expired", "API Key已过期");
                return;
            }

            RelayUserAccountEntity account = relayUserAccountDao.selectById(token.getEmployeeId());
            if (account == null) {
                writeError(response, 403, "account_not_found", "用户账户不存在");
                return;
            }

            if (account.getDisabledFlag() == 1) {
                writeError(response, 403, "account_disabled", "账户已冻结");
                return;
            }

            // 设置请求上下文
            RelayRequestContext ctx = new RelayRequestContext();
            ctx.setRequestId(UUID.randomUUID().toString());
            ctx.setEmployeeId(token.getEmployeeId());
            ctx.setTokenId(token.getTokenId());
            ctx.setToken(token);
            ctx.setAccount(account);
            RelayRequestContext.set(ctx);

            chain.doFilter(request, response);
        } finally {
            RelayRequestContext.clear();
        }
    }

    private void writeError(HttpServletResponse response, int httpStatus, String code, String message) throws IOException {
        response.setStatus(httpStatus);
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("message", message);
        error.put("type", "invalid_request_error");
        error.put("code", code);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", error);
        response.getWriter().write(JSON.toJSONString(body));
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("SHA-256计算失败", e);
        }
    }
}
