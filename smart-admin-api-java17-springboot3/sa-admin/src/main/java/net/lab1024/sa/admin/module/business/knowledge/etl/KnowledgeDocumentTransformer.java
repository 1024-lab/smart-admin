package net.lab1024.sa.admin.module.business.knowledge.etl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 文档转换器
 * 将文档分割成更小的片段
 *
 * @Author Mission
 * @Date 2026-01-29
 * @Copyright Mission
 */
@Slf4j
@Component
public class KnowledgeDocumentTransformer {

    /**
     * 转换文档 - 分割成小片段
     *
     * @param documents 原始文档列表
     * @return 分割后的文档列表
     */
    public List<Document> transform(List<Document> documents) {
        try {
            // 使用 TokenTextSplitter 进行文档分割
            // defaultChunkSize: 800 tokens
            // minChunkSizeChars: 350 characters
            // minChunkLengthToEmbed: 5 characters
            // maxNumChunks: 10000
            TokenTextSplitter splitter = new TokenTextSplitter();

            List<Document> splitDocuments = splitter.apply(documents);

            log.info("Document transformation completed. Original: {}, Split: {}",
                    documents.size(), splitDocuments.size());

            return splitDocuments;

        } catch (Exception e) {
            log.error("Error transforming documents", e);
            return documents;
        }
    }

    /**
     * 自定义分割参数的转换方法
     *
     * @param documents 原始文档列表
     * @param chunkSize 每个片段的大小（token数）
     * @param chunkOverlap 片段之间的重叠（token数）
     * @return 分割后的文档列表
     */
    public List<Document> transform(List<Document> documents, int chunkSize, int chunkOverlap) {
        try {
            TokenTextSplitter splitter = new TokenTextSplitter(chunkSize, chunkOverlap, 5, 10000, true);
            List<Document> splitDocuments = splitter.apply(documents);

            log.info("Document transformation completed with custom params. Original: {}, Split: {}",
                    documents.size(), splitDocuments.size());

            return splitDocuments;

        } catch (Exception e) {
            log.error("Error transforming documents with custom params", e);
            return documents;
        }
    }
}
