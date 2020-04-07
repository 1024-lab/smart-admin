package net.lab1024.smartadmin.common.reload.domain;

import net.lab1024.smartadmin.common.reload.domain.entity.ReloadItem;
import net.lab1024.smartadmin.common.reload.domain.entity.SmartReloadResult;

import java.io.PrintWriter;
import java.io.StringWriter;
/**
 * AbstractSmartReloadObject 处理程序的抽象类
 *
 * @author zhuoda
 */
public abstract class AbstractSmartReloadObject {

    protected String getStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    /**
     * 通过reloadItem参数reload，获得结果
     *
     * @param reloadItem
     * @return boolean
     * @author zhuokongming
     * @date 2016年10月21日 下午2:09:44
     */
    public abstract SmartReloadResult reload(ReloadItem reloadItem);
}
