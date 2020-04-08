package net.lab1024.smartadmin.common.reload.domain.entity;
/**
 * t_reload_result 表 实体类
 *
 * @author zhuoda
 */
public class SmartReloadResult {

    /**
     * 项名称
     */
    private String tag;

    /**
     * 参数
     */
    private String args;

    /**
     * 标识
     */
    private String identification;

    /**
     * 处理结果
     */
    private boolean result;

    /**
     * 异常说明
     */
    private String exception;

    public SmartReloadResult() {
    }

    public SmartReloadResult(String tag, String args, boolean result, String exception) {
        this.tag = tag;
        this.args = args;
        this.result = result;
        this.exception = exception;
    }

    public SmartReloadResult(String tag, String args, String identification, boolean result, String exception) {
        this.tag = tag;
        this.args = args;
        this.identification = identification;
        this.result = result;
        this.exception = exception;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getTag() {
        return tag;
    }

    public String getArgs() {
        return args;
    }

    public String getIdentification() {
        return identification;
    }

    public boolean isResult() {
        return result;
    }

    public String getException() {
        return exception;
    }

    @Override
    public String toString() {
        return "SmartReloadResult{" +
                "tag='" + tag + '\'' +
                ", args='" + args + '\'' +
                ", identification='" + identification + '\'' +
                ", result=" + result +
                ", exception='" + exception + '\'' +
                '}';
    }
}
