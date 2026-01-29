package net.lab1024.sa.admin.module.business.knowledge.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.knowledge.dao.AiDocumentsDao;
import net.lab1024.sa.admin.module.business.knowledge.domain.entity.AiDocumentsEntity;
import net.lab1024.sa.admin.module.business.knowledge.domain.entity.AiKnowledgeEntity;
import net.lab1024.sa.admin.module.business.knowledge.dao.AiKnowledgeDao;
import net.lab1024.sa.admin.module.business.knowledge.etl.KnowledgeDocumentReader;
import net.lab1024.sa.admin.module.business.knowledge.etl.KnowledgeDocumentTransformer;
import net.lab1024.sa.admin.module.business.knowledge.etl.KnowledgeDocumentWriter;
import org.springframework.ai.document.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文档处理定时任务
 * 定时处理待处理的文档，进行 RAG 化处理
 *
 * @Author Mission
 * @Date 2026-01-29
 * @Copyright Mission
 */
@Slf4j
@Service
public class DocumentProcessingScheduler {

    @Resource
    private AiDocumentsDao aiDocumentsDao;

    @Resource
    private AiKnowledgeDao aiKnowledgeDao;

    @Resource
    private KnowledgeDocumentReader documentReader;

    @Resource
    private KnowledgeDocumentTransformer documentTransformer;

    @Resource
    private KnowledgeDocumentWriter documentWriter;

    /**
     * 定时处理待处理的文档
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processDocuments() {
        log.info("Starting document processing task");

        try {
            // 1. 查询待处理的文档
            List<AiDocumentsEntity> pendingDocuments = aiDocumentsDao.selectList(
                    new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<AiDocumentsEntity>()
                            .eq(AiDocumentsEntity::getStatus, "pending")
                            .orderByAsc(AiDocumentsEntity::getCreateTime)
                            .last("LIMIT 10") // 每次处理10个
            );

            if (pendingDocuments.isEmpty()) {
                log.debug("No pending documents to process");
                return;
            }

            log.info("Found {} pending documents to process", pendingDocuments.size());

            // 2. 处理每个文档
            for (AiDocumentsEntity document : pendingDocuments) {
                processDocument(document);
            }

        } catch (Exception e) {
            log.error("Error in document processing task", e);
        }
    }

    /**
     * 处理单个文档
     */
    private void processDocument(AiDocumentsEntity document) {
        log.info("Processing document: {}", document.getId());

        try {
            // 1. 更新状态为处理中
            document.setStatus("processing");
            document.setUpdateTime(LocalDateTime.now());
            aiDocumentsDao.updateById(document);

            // 2. 获取知识库信息
            AiKnowledgeEntity knowledge = aiKnowledgeDao.selectById(document.getKnowledgeId());
            if (knowledge == null) {
                log.error("Knowledge not found: {}", document.getKnowledgeId());
                updateDocumentStatus(document, "fail");
                return;
            }

            // 3. 读取文档
            List<Document> documents = documentReader.read(document);
            if (documents.isEmpty()) {
                log.error("Failed to read document: {}", document.getId());
                updateDocumentStatus(document, "fail");
                return;
            }

            // 4. 转换文档（分割）
            List<Document> transformedDocuments = documentTransformer.transform(documents);

            // 5. 写入向量数据库
            documentWriter.write(transformedDocuments, knowledge.getId(), knowledge.getEmbeddingModel());

            // 6. 更新文档状态为成功
            document.setWordCount(calculateTotalWordCount(transformedDocuments));
            updateDocumentStatus(document, "success");

            log.info("Successfully processed document: {}", document.getId());

        } catch (Exception e) {
            log.error("Error processing document: {}", document.getId(), e);
            updateDocumentStatus(document, "fail");
        }
    }

    /**
     * 更新文档状态
     */
    private void updateDocumentStatus(AiDocumentsEntity document, String status) {
        document.setStatus(status);
        document.setUpdateTime(LocalDateTime.now());
        aiDocumentsDao.updateById(document);
    }

    /**
     * 计算总字数
     */
    private int calculateTotalWordCount(List<Document> documents) {
        return documents.stream()
                .mapToInt(doc -> doc.getText().length())
                .sum();
    }
}
