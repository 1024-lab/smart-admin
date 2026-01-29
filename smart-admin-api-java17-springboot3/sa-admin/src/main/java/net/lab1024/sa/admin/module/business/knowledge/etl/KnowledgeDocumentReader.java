package net.lab1024.sa.admin.module.business.knowledge.etl;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.knowledge.dao.AiDocumentsDao;
import net.lab1024.sa.admin.module.business.knowledge.domain.entity.AiDocumentsEntity;
import net.lab1024.sa.base.module.support.file.dao.FileDao;
import net.lab1024.sa.base.module.support.file.domain.entity.FileEntity;
import net.lab1024.sa.base.module.support.file.service.IFileStorageService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.module.support.file.domain.vo.FileDownloadVO;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文档读取器
 * 从文件系统读取文档并转换为 Spring AI Document
 *
 * @Author Mission
 * @Date 2026-01-29
 * @Copyright Mission
 */
@Slf4j
@Component
public class KnowledgeDocumentReader {

    @Resource
    private AiDocumentsDao aiDocumentsDao;

    @Resource
    private FileDao fileDao;

    @Resource
    private IFileStorageService fileStorageService;

    /**
     * 读取文档
     *
     * @param documentEntity 文档实体
     * @return Spring AI Document 列表
     */
    public List<Document> read(AiDocumentsEntity documentEntity) {
        try {
            // 1. 获取文件信息
            FileEntity fileEntity = fileDao.selectById(documentEntity.getFileId());
            if (fileEntity == null) {
                log.error("File not found for document: {}", documentEntity.getId());
                return new ArrayList<>();
            }

            // 2. 通过 fileKey 下载文件内容
            String fileKey = fileEntity.getFileKey();
            ResponseDTO<FileDownloadVO> downloadResult = fileStorageService.download(fileKey);
            if (!downloadResult.getOk() || downloadResult.getData() == null) {
                log.error("Failed to download file: {}", fileKey);
                return new ArrayList<>();
            }

            // 3. 获取文件字节数据
            byte[] fileData = downloadResult.getData().getData();
            if (fileData == null || fileData.length == 0) {
                log.error("File data is empty: {}", fileKey);
                return new ArrayList<>();
            }

            // 4. 使用 Tika 读取文档
            ByteArrayResource resource = new ByteArrayResource(fileData);
            TikaDocumentReader reader = new TikaDocumentReader(resource);
            List<Document> documents = reader.get();

            // 5. 添加元数据
            for (Document doc : documents) {
                Map<String, Object> metadata = new HashMap<>(doc.getMetadata());
                metadata.put("document_id", documentEntity.getId());
                metadata.put("knowledge_id", documentEntity.getKnowledgeId());
                metadata.put("document_name", documentEntity.getName());
                metadata.put("file_id", documentEntity.getFileId());
                doc.getMetadata().putAll(metadata);
            }

            log.info("Successfully read document: {}, segments: {}", documentEntity.getName(), documents.size());
            return documents;

        } catch (Exception e) {
            log.error("Error reading document: {}", documentEntity.getId(), e);
            return new ArrayList<>();
        }
    }
}
