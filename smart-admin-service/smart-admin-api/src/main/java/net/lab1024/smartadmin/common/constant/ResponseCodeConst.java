package net.lab1024.smartadmin.common.constant;

import net.lab1024.smartadmin.module.system.department.DepartmentResponseCodeConst;
import net.lab1024.smartadmin.module.system.employee.constant.EmployeeResponseCodeConst;
import net.lab1024.smartadmin.module.support.file.constant.FileResponseCodeConst;
import net.lab1024.smartadmin.module.business.log.orderoperatelog.constant.OrderOperateLogOperateTypeConst;
import net.lab1024.smartadmin.module.system.login.LoginResponseCodeConst;
import net.lab1024.smartadmin.module.system.position.PositionResponseCodeConst;
import net.lab1024.smartadmin.module.system.privilege.constant.PrivilegeResponseCodeConst;
import net.lab1024.smartadmin.module.system.role.basic.RoleResponseCodeConst;
import net.lab1024.smartadmin.module.system.systemconfig.constant.SystemConfigResponseCodeConst;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 每个业务，100个范围值就够了.
 */
@Slf4j
public class ResponseCodeConst {

    // 范围声明
    static {
        // 系统功能，从0开始，step=1000
        ResponseCodeContainer.register(ResponseCodeConst.class, 0, 1000);
        ResponseCodeContainer.register(LoginResponseCodeConst.class, 1001, 1999);
        ResponseCodeContainer.register(DepartmentResponseCodeConst.class, 2001, 2999);
        ResponseCodeContainer.register(EmployeeResponseCodeConst.class, 3001, 3999);
        ResponseCodeContainer.register(FileResponseCodeConst.class, 4001, 4999);
        ResponseCodeContainer.register(SystemConfigResponseCodeConst.class, 5001, 5999);
        ResponseCodeContainer.register(RoleResponseCodeConst.class, 6001, 6999);
        ResponseCodeContainer.register(PrivilegeResponseCodeConst.class, 7001, 7999);
        ResponseCodeContainer.register(OrderOperateLogOperateTypeConst.class, 8001, 8999);
        ResponseCodeContainer.register(PositionResponseCodeConst.class, 13000, 13999);

    }

    public static final ResponseCodeConst SUCCESS = new ResponseCodeConst(1, "操作成功!", true);

    public static final ResponseCodeConst ERROR_PARAM = new ResponseCodeConst(101, "参数异常！");

    public static final ResponseCodeConst ERROR_PARAM_ANY = new ResponseCodeConst(102, "%s参数异常！");

    public static final ResponseCodeConst SYSTEM_ERROR = new ResponseCodeConst(111, "系统错误");

    public static final ResponseCodeConst DEVELOPMENT = new ResponseCodeConst(112, "此功能正在开发中");

    public static final ResponseCodeConst NOT_EXISTS = new ResponseCodeConst(113, "数据不存在");

    public static ResponseCodeConst REQUEST_METHOD_ERROR = new ResponseCodeConst(114, "请求方式错误");

    public static ResponseCodeConst JSON_FORMAT_ERROR = new ResponseCodeConst(115, "JSON格式错误");

    protected int code;

    protected String msg;

    protected boolean success;

    public ResponseCodeConst() {
    }

    protected ResponseCodeConst(int code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
        ResponseCodeContainer.put(this);
    }

    protected ResponseCodeConst(int code, String msg, boolean success) {
        super();
        this.code = code;
        this.msg = msg;
        this.success = success;
        ResponseCodeContainer.put(this);
    }

    protected ResponseCodeConst(int code) {
        super();
        this.code = code;
        ResponseCodeContainer.put(this);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public static void init() {
        log.info("ResponseCodeConst init....");
    }

    // =======================分割=======================

    /**
     * 内部类，用于检测code范围
     *
     * @author Anders
     */
    @Slf4j
    private static class ResponseCodeContainer {

        private static final Map<Integer, ResponseCodeConst> RESPONSE_CODE_MAP = new HashMap<>();

        private static final Map<Class<? extends ResponseCodeConst>, int[]> RESPONSE_CODE_RANGE_MAP = new HashMap<>();

        /**
         * id的范围：[start, end]左闭右闭
         *
         * @param clazz
         * @param start
         * @param end
         */
        private static void register(Class<? extends ResponseCodeConst> clazz, int start, int end) {
            if (start > end) {
                throw new IllegalArgumentException("<ResponseDTO> start > end!");
            }

            if (RESPONSE_CODE_RANGE_MAP.containsKey(clazz)) {
                throw new IllegalArgumentException(String.format("<ResponseDTO> Class:%s already exist!", clazz.getSimpleName()));
            }
            RESPONSE_CODE_RANGE_MAP.forEach((k, v) -> {
                if ((start >= v[0] && start <= v[1]) || (end >= v[0] && end <= v[1])) {
                    throw new IllegalArgumentException(String.format("<ResponseDTO> Class:%s 's id range[%d,%d] has " + "intersection with " + "class:%s", clazz.getSimpleName(), start, end,
                        k.getSimpleName()));
                }
            });

            RESPONSE_CODE_RANGE_MAP.put(clazz, new int[]{start, end});

            // 提前初始化static变量，进行范围检测
            Field[] fields = clazz.getFields();
            if (fields.length != 0) {
                try {
                    fields[0].get(clazz);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    log.error("", e);
                }
            }
        }

        public static void put(ResponseCodeConst codeConst) {
            int[] idRange = RESPONSE_CODE_RANGE_MAP.get(codeConst.getClass());
            if (idRange == null) {
                throw new IllegalArgumentException(String.format("<ResponseDTO> Class:%s has not been registered!", codeConst.getClass().getSimpleName()));
            }
            int code = codeConst.code;
            if (code < idRange[0] || code > idRange[1]) {
                throw new IllegalArgumentException(String.format("<ResponseDTO> Id(%d) out of range[%d,%d], " + "class:%s", code, idRange[0], idRange[1], codeConst.getClass().getSimpleName()));
            }
            if (RESPONSE_CODE_MAP.keySet().contains(code)) {
                log.error(String.format("<ResponseDTO> Id(%d) out of range[%d,%d], " + "class:%s  code is repeat!", code, idRange[0], idRange[1], codeConst.getClass().getSimpleName()));
                System.exit(0);
            }
            RESPONSE_CODE_MAP.put(code, codeConst);
        }

    }

}
