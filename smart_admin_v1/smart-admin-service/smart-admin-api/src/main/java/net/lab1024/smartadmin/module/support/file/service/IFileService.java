package net.lab1024.smartadmin.module.support.file.service;

import net.lab1024.smartadmin.common.domain.ResponseDTO;
import net.lab1024.smartadmin.module.support.file.domain.vo.UploadVO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 文件服务接口
 *
 * @author yandanyang
 * @version 1.0
 * @company 1024lab.net
 * @copyright (c) 2018 1024lab.netInc. All rights reserved.
 * @date 2019/5/11 0011 下午 16:42
 * @since JDK1.8
 */
public interface IFileService {

    /**
     * 文件上传
     *
     * @param multipartFile
     * @param path
     * @return
     */
    ResponseDTO<UploadVO> fileUpload(MultipartFile multipartFile, String path);

    /**
     * 获取文件url
     *
     * @param path
     * @return
     */
    ResponseDTO<String> getFileUrl(String path);

    /**
     * 文件下载
     *
     * @param key
     * @param fileName
     * @param request
     * @return
     */
    ResponseEntity<byte[]> fileDownload(String key, String fileName, HttpServletRequest request);

    /**
     * 生成文件名字
     * 当前年月日时分秒 +32位 uuid + 文件格式后缀
     *
     * @param originalFileName
     * @return String
     */
    default String generateFileName(String originalFileName) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddhhmms"));
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String fileType = originalFileName.substring(originalFileName.lastIndexOf("."));
        return time + uuid + fileType;
    }

    /**
     * 获取文件类型
     *
     * @param fileExt
     * @return
     */
    default String getContentType(String fileExt) {
        // 文件的后缀名
        if ("bmp".equalsIgnoreCase(fileExt)) {
            return "image/bmp";
        }
        if ("gif".equalsIgnoreCase(fileExt)) {
            return "image/gif";
        }
        if ("jpeg".equalsIgnoreCase(fileExt) || "jpg".equalsIgnoreCase(fileExt) || ".png".equalsIgnoreCase(fileExt)) {
            return "image/jpeg";
        }
        if ("png".equalsIgnoreCase(fileExt)) {
            return "image/png";
        }
        if ("html".equalsIgnoreCase(fileExt)) {
            return "text/html";
        }
        if ("txt".equalsIgnoreCase(fileExt)) {
            return "text/plain";
        }
        if ("vsd".equalsIgnoreCase(fileExt)) {
            return "application/vnd.visio";
        }
        if ("ppt".equalsIgnoreCase(fileExt) || "pptx".equalsIgnoreCase(fileExt)) {
            return "application/vnd.ms-powerpoint";
        }
        if ("doc".equalsIgnoreCase(fileExt) || "docx".equalsIgnoreCase(fileExt)) {
            return "application/msword";
        }
        if ("xml".equalsIgnoreCase(fileExt)) {
            return "text/xml";
        }
        return "";
    }

    default ResponseEntity<byte[]> downloadMethod(File file, HttpServletRequest request) {
        HttpHeaders heads = new HttpHeaders();
        heads.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream; charset=utf-8");
        String fileName = file.getName();
        try {
            if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0) {
                // firefox浏览器
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            } else if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {
                // IE浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else if (request.getHeader("User-Agent").toUpperCase().indexOf("EDGE") > 0) {
                // WIN10浏览器
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } else if (request.getHeader("User-Agent").toUpperCase().indexOf("CHROME") > 0) {
                // 谷歌
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
            } else {
                //万能乱码问题解决
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            }
        } catch (UnsupportedEncodingException e) {
            // log.error("", e);
        }
        heads.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
        try {
            InputStream in = new FileInputStream(file);
            // 输入流转换为字节流
            byte[] buffer = FileCopyUtils.copyToByteArray(in);
            ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(buffer, heads, HttpStatus.OK);
            //file.delete();
            return responseEntity;
        } catch (Exception e) {
            // log.error("", e);
        }
        return null;
    }
}
