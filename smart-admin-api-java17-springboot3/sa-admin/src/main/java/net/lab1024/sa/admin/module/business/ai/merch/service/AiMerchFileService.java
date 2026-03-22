package net.lab1024.sa.admin.module.business.ai.merch.service;

import jakarta.annotation.Resource;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.util.SmartRequestUtil;
import net.lab1024.sa.base.module.support.file.dao.FileDao;
import net.lab1024.sa.base.module.support.file.domain.vo.FileUploadVO;
import net.lab1024.sa.base.module.support.file.domain.vo.FileVO;
import net.lab1024.sa.base.module.support.file.service.FileService;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class AiMerchFileService {

    @Resource
    private FileDao fileDao;

    @Resource
    private FileService fileService;

    private final HttpClient httpClient = HttpClient.newBuilder().build();

    public FileVO getFileByKey(String fileKey) {
        if (StringUtils.isBlank(fileKey)) {
            return null;
        }
        FileVO fileVO = fileDao.getByFileKey(fileKey);
        if (fileVO == null) {
            return null;
        }
        ResponseDTO<String> urlResult = fileService.getFileUrl(fileKey);
        if (urlResult.getOk()) {
            fileVO.setFileUrl(urlResult.getData());
        }
        return fileVO;
    }

    public FileUploadVO importRemoteFile(String remoteUrl, Integer folderType, String fallbackPrefix) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(remoteUrl)).GET().build();
            HttpResponse<byte[]> response = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException("下载远程文件失败，HTTP状态码：" + response.statusCode());
            }

            String fileName = resolveFileName(
                    response.headers().firstValue("content-disposition").orElse(null),
                    remoteUrl,
                    response.headers().firstValue("content-type").orElse(null),
                    fallbackPrefix
            );

            MultipartFile multipartFile = new ByteArrayMultipartFile(
                    fileName,
                    response.headers().firstValue("content-type").orElse("application/octet-stream"),
                    response.body()
            );
            ResponseDTO<FileUploadVO> uploadResult = fileService.fileUpload(multipartFile, folderType, SmartRequestUtil.getRequestUser());
            if (!uploadResult.getOk()) {
                throw new RuntimeException(uploadResult.getMsg());
            }
            return uploadResult.getData();
        } catch (Exception e) {
            throw new RuntimeException("导入远程文件失败：" + e.getMessage(), e);
        }
    }

    private String resolveFileName(String contentDisposition, String remoteUrl, String contentType, String fallbackPrefix) {
        String fileName = parseDispositionFileName(contentDisposition);
        if (StringUtils.isBlank(fileName)) {
            String path = URI.create(remoteUrl).getPath();
            fileName = FilenameUtils.getName(path);
        }
        if (StringUtils.isBlank(fileName)) {
            fileName = fallbackPrefix;
        }
        if (!fileName.contains(".")) {
            fileName = fileName + "." + detectExtension(contentType, remoteUrl);
        }
        return fileName;
    }

    private String parseDispositionFileName(String contentDisposition) {
        if (StringUtils.isBlank(contentDisposition)) {
            return null;
        }
        for (String token : contentDisposition.split(";")) {
            String item = token.trim();
            if (item.startsWith("filename=") || item.startsWith("fileName=")) {
                return item.substring(item.indexOf('=') + 1).replace("\"", "");
            }
        }
        return null;
    }

    private String detectExtension(String contentType, String remoteUrl) {
        if (StringUtils.isNotBlank(contentType)) {
            String lowerContentType = contentType.toLowerCase();
            if (lowerContentType.contains("png")) {
                return "png";
            }
            if (lowerContentType.contains("jpeg") || lowerContentType.contains("jpg")) {
                return "jpg";
            }
            if (lowerContentType.contains("gif")) {
                return "gif";
            }
            if (lowerContentType.contains("webp")) {
                return "webp";
            }
            if (lowerContentType.contains("mp4")) {
                return "mp4";
            }
            if (lowerContentType.contains("quicktime")) {
                return "mov";
            }
            if (lowerContentType.contains("webm")) {
                return "webm";
            }
        }
        String extension = FilenameUtils.getExtension(URI.create(remoteUrl).getPath());
        return StringUtils.isBlank(extension) ? "bin" : extension;
    }

    private static class ByteArrayMultipartFile implements MultipartFile {

        private final String fileName;
        private final String contentType;
        private final byte[] bytes;

        private ByteArrayMultipartFile(String fileName, String contentType, byte[] bytes) {
            this.fileName = fileName;
            this.contentType = contentType;
            this.bytes = Optional.ofNullable(bytes).orElse(new byte[0]);
        }

        @Override
        public String getName() {
            return fileName;
        }

        @Override
        public String getOriginalFilename() {
            return fileName;
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public boolean isEmpty() {
            return bytes.length == 0;
        }

        @Override
        public long getSize() {
            return bytes.length;
        }

        @Override
        public byte[] getBytes() {
            return bytes;
        }

        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(bytes);
        }

        @Override
        public void transferTo(File dest) throws IOException {
            Files.write(dest.toPath(), bytes);
        }
    }
}
