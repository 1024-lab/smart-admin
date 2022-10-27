package net.lab1024.smartadmin.common.heartbeat;

import java.util.Iterator;

/**
 * [  ]
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/8/8 0008 下午 16:27
 * @since JDK1.8
 */
public class StringUtil {



    public static String join(Iterable<?> iterable, String separator) {
        return iterable == null ? null : join(iterable.iterator(), separator);
    }


    public static String join(Iterator<?> iterator, String separator) {
        if (iterator == null) {
            return null;
        } else if (!iterator.hasNext()) {
            return "";
        } else {
            Object first = iterator.next();
            if (!iterator.hasNext()) {
                String result = toString(first);
                return result;
            } else {
                StringBuilder buf = new StringBuilder(256);
                if (first != null) {
                    buf.append(first);
                }

                while(iterator.hasNext()) {
                    if (separator != null) {
                        buf.append(separator);
                    }

                    Object obj = iterator.next();
                    if (obj != null) {
                        buf.append(obj);
                    }
                }

                return buf.toString();
            }
        }
    }

    public static String toString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

}