package net.lab1024.sa.admin.module.business.ai.relay.context;

import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayTokenEntity;
import net.lab1024.sa.admin.module.business.ai.relay.domain.entity.RelayUserAccountEntity;

/**
 * Relay 请求上下文，通过 ThreadLocal 在 Filter → Controller → Service 间传递鉴权信息
 */
public class RelayRequestContext {

    private static final ThreadLocal<RelayRequestContext> HOLDER = new ThreadLocal<>();

    private String requestId;
    private Long employeeId;
    private Long tokenId;
    private RelayTokenEntity token;
    private RelayUserAccountEntity account;

    public static void set(RelayRequestContext ctx) {
        HOLDER.set(ctx);
    }

    public static RelayRequestContext get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public RelayTokenEntity getToken() {
        return token;
    }

    public void setToken(RelayTokenEntity token) {
        this.token = token;
    }

    public RelayUserAccountEntity getAccount() {
        return account;
    }

    public void setAccount(RelayUserAccountEntity account) {
        this.account = account;
    }
}
