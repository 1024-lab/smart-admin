package net.lab1024.smartadmin.util;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 字符串操作类，包括分割，转换，大写首字母
 *
 * @author jiaozi
 */
public class SmartStringUtil extends StringUtils {

    // ===============split =======================

    public static Set<String> splitConvertToSet(String str, String split) {
        if (isEmpty(str)) {
            return new HashSet<String>();
        }
        String[] splitArr = str.split(split);
        HashSet<String> set = new HashSet<String>(splitArr.length);
        for (String string : splitArr) {
            set.add(string);
        }
        return set;
    }

    public static List<String> splitConvertToList(String str, String split) {
        if (isEmpty(str)) {
            return new ArrayList<String>();
        }
        String[] splitArr = str.split(split);
        ArrayList<String> list = new ArrayList<String>(splitArr.length);
        for (String string : splitArr) {
            list.add(string);
        }
        return list;
    }

    // ===============split Integer=======================

    public static List<Integer> splitConverToIntList(String str, String split, int defaultVal) {
        if (isEmpty(str)) {
            return new ArrayList<Integer>();
        }
        String[] strArr = str.split(split);
        List<Integer> list = new ArrayList<Integer>(strArr.length);
        for (int i = 0; i < strArr.length; i++) {
            try {
                int parseInt = Integer.parseInt(strArr[i]);
                list.add(parseInt);
            } catch (NumberFormatException e) {
                list.add(defaultVal);
                continue;
            }
        }
        return list;
    }

    public static Set<Integer> splitConverToIntSet(String str, String split, int defaultVal) {
        if (isEmpty(str)) {
            return new HashSet<Integer>();
        }
        String[] strArr = str.split(split);
        HashSet<Integer> set = new HashSet<Integer>(strArr.length);
        for (int i = 0; i < strArr.length; i++) {
            try {
                int parseInt = Integer.parseInt(strArr[i]);
                set.add(parseInt);
            } catch (NumberFormatException e) {
                set.add(defaultVal);
                continue;
            }
        }
        return set;
    }

    public static Set<Integer> splitConverToIntSet(String str, String split) {
        return splitConverToIntSet(str, split, 0);
    }

    public static List<Integer> splitConverToIntList(String str, String split) {
        return splitConverToIntList(str, split, 0);
    }

    public static int[] splitConvertToIntArray(String str, String split, int defaultVal) {
        if (isEmpty(str)) {
            return new int[0];
        }
        String[] strArr = str.split(split);
        int[] result = new int[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            try {
                result[i] = Integer.parseInt(strArr[i]);
            } catch (NumberFormatException e) {
                result[i] = defaultVal;
                continue;
            }
        }
        return result;
    }

    public static int[] splitConvertToIntArray(String str, String split) {
        return splitConvertToIntArray(str, split, 0);
    }

    // ===============split 2 Long=======================

    public static List<Long> splitConverToLongList(String str, String split, long defaultVal) {
        if (isEmpty(str)) {
            return new ArrayList<Long>();
        }
        String[] strArr = str.split(split);
        List<Long> list = new ArrayList<Long>(strArr.length);
        for (int i = 0; i < strArr.length; i++) {
            try {
                long parseLong = Long.parseLong(strArr[i]);
                list.add(parseLong);
            } catch (NumberFormatException e) {
                list.add(defaultVal);
                continue;
            }
        }
        return list;
    }

    public static List<Long> splitConverToLongList(String str, String split) {
        return splitConverToLongList(str, split, 0L);
    }

    public static long[] splitConvertToLongArray(String str, String split, long defaultVal) {
        if (isEmpty(str)) {
            return new long[0];
        }
        String[] strArr = str.split(split);
        long[] result = new long[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            try {
                result[i] = Long.parseLong(strArr[i]);
            } catch (NumberFormatException e) {
                result[i] = defaultVal;
                continue;
            }
        }
        return result;
    }

    public static long[] splitConvertToLongArray(String str, String split) {
        return splitConvertToLongArray(str, split, 0L);
    }

    // ===============split convert byte=======================

    public static List<Byte> splitConverToByteList(String str, String split, byte defaultVal) {
        if (isEmpty(str)) {
            return new ArrayList<Byte>();
        }
        String[] strArr = str.split(split);
        List<Byte> list = new ArrayList<Byte>(strArr.length);
        for (int i = 0; i < strArr.length; i++) {
            try {
                byte parseByte = Byte.parseByte(strArr[i]);
                list.add(parseByte);
            } catch (NumberFormatException e) {
                list.add(defaultVal);
                continue;
            }
        }
        return list;
    }

    public static List<Byte> splitConverToByteList(String str, String split) {
        return splitConverToByteList(str, split, (byte) 0);
    }

    public static byte[] splitConvertToByteArray(String str, String split, byte defaultVal) {
        if (isEmpty(str)) {
            return new byte[0];
        }
        String[] strArr = str.split(split);
        byte[] result = new byte[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            try {
                result[i] = Byte.parseByte(strArr[i]);
            } catch (NumberFormatException e) {
                result[i] = defaultVal;
                continue;
            }
        }
        return result;
    }

    public static byte[] splitConvertToByteArray(String str, String split) {
        return splitConvertToByteArray(str, split, (byte) 0);
    }

    // ===============split convert double=======================

    public static List<Double> splitConverToDoubleList(String str, String split, double defaultVal) {
        if (isEmpty(str)) {
            return new ArrayList<Double>();
        }
        String[] strArr = str.split(split);
        List<Double> list = new ArrayList<Double>(strArr.length);
        for (int i = 0; i < strArr.length; i++) {
            try {
                double parseByte = Double.parseDouble(strArr[i]);
                list.add(parseByte);
            } catch (NumberFormatException e) {
                list.add(defaultVal);
                continue;
            }
        }
        return list;
    }

    public static List<Double> splitConverToDoubleList(String str, String split) {
        return splitConverToDoubleList(str, split, 0);
    }

    public static double[] splitConvertToDoubleArray(String str, String split, double defaultVal) {
        if (isEmpty(str)) {
            return new double[0];
        }
        String[] strArr = str.split(split);
        double[] result = new double[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            try {
                result[i] = Double.parseDouble(strArr[i]);
            } catch (NumberFormatException e) {
                result[i] = defaultVal;
                continue;
            }
        }
        return result;
    }

    public static double[] splitConvertToDoubleArray(String str, String split) {
        return splitConvertToDoubleArray(str, split, 0);
    }

    // ===============solit convert float=======================

    public static List<Float> splitConverToFloatList(String str, String split, float defaultVal) {
        if (isEmpty(str)) {
            return new ArrayList<Float>();
        }
        String[] strArr = str.split(split);
        List<Float> list = new ArrayList<Float>(strArr.length);
        for (int i = 0; i < strArr.length; i++) {
            try {
                float parseByte = Float.parseFloat(strArr[i]);
                list.add(parseByte);
            } catch (NumberFormatException e) {
                list.add(defaultVal);
                continue;
            }
        }
        return list;
    }

    public static List<Float> splitConverToFloatList(String str, String split) {
        return splitConverToFloatList(str, split, 0f);
    }

    public static float[] splitConvertToFloatArray(String str, String split, float defaultVal) {
        if (isEmpty(str)) {
            return new float[0];
        }
        String[] strArr = str.split(split);
        float[] result = new float[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            try {
                result[i] = Float.parseFloat(strArr[i]);
            } catch (NumberFormatException e) {
                result[i] = defaultVal;
                continue;
            }
        }
        return result;
    }

    public static float[] splitConvertToFloatArray(String str, String split) {
        return splitConvertToFloatArray(str, split, 0f);
    }

    // ===============upperCase=======================

    /**
     * 将首字母大写
     *
     * @param str
     * @return
     */
    public static String upperCaseFirstChar(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        char firstChar = str.charAt(0);
        if (Character.isUpperCase(firstChar)) {
            return str;
        }
        char[] values = str.toCharArray();
        values[0] = Character.toUpperCase(firstChar);
        return new String(values);
    }

}
