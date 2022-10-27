package net.lab1024.smartadmin.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

/**
 * @author zhuoda
 */
public class SmartFileUtil extends FileUtils {

    public static boolean isXmlFile(File file) {
        return "xml".equalsIgnoreCase(getFileExtension(file.getName()));
    }

    /**
     * 文件后缀名
     *
     * @param fullName
     * @return
     */
    public static String getFileExtension(String fullName) {
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    /**
     * 不带后缀名的文件名
     *
     * @param file
     * @return
     */
    public static String getNameWithoutExtension(String file) {
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 验证文件是否存在，如果不存在则抛出异常
     *
     * @param filePath
     * @throws IOException
     */
    public static void isFileExistThrowException(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        }
    }

    public static BufferedReader newBufferedReader(File file, Charset charset) throws FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
    }

    public static BufferedWriter newBufferedWriter(File file, Charset charset) throws FileNotFoundException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset));
    }

    public static boolean createParentDirs(File file) throws IOException {
        File parent = file.getCanonicalFile().getParentFile();
        if (parent == null) {
            return false;
        }
        return parent.mkdirs();
    }

    public static boolean createNotExistParentDirFile(File file) throws IOException {
        boolean createParentDirsRes = createParentDirs(file);
        if (!createParentDirsRes) {
            throw new IOException("cannot create parent Directory of " + file.getName());
        }
        return file.createNewFile();
    }

}
