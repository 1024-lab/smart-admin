package net.lab1024.smartadmin.common.reload.domain.entity;
/**
 * ReloadItem 类
 *
 * @author zhuoda
 */
public class ReloadItem {

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

    public ReloadItem() {

    }
    public ReloadItem(String tag, String identification, String args) {
        this.tag = tag;
        this.identification = identification;
        this.args = args;
    }

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getIdentification() {
        return identification;
    }
    public void setIdentification(String identification) {
        this.identification = identification;
    }
    public String getArgs() {
        return args;
    }
    public void setArgs(String args) {
        this.args = args;
    }
    @Override
    public String toString() {
        return "ReloadItem{" + "tag='" + tag + '\'' + ", identification='" + identification + '\'' + ", args='" + args + '\'' + '}';
    }
}
