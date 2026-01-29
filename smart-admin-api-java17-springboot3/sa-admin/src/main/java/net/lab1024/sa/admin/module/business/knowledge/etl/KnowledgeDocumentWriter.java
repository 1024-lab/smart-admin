package net.lab1024.sa.admin.module.business.knowledge.etl;

import io.milvus.client.MilvusServiceClient;
import io.milvus.grpc.DataType;
import io.milvus.grpc.MutationResult;
import io.milvus.param.ConnectParam;
import io.milvus.param.R;
import io.milvus.param.collection.CreateCollectionParam;
import io.milvus.param.collection.FieldType;
import io.milvus.param.collection.HasCollectionParam;
import io.milvus.param.dml.InsertParam;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.business.ai.dao.AiModelConfigDao;
import net.lab1024.sa.admin.module.business.ai.domain.entity.AiModelConfigEntity;
import net.lab1024.sa.admin.module.business.knowledge.dao.AiDocumentSegmentsDao;
import net.lab1024.sa.admin.module.business.knowledge.domain.entity.AiDocumentSegmentsEntity;
import net.lab1024.sa.admin.module.business.knowledge.factory.DynamicModelFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 文档写入器
 * 将文档向量化后写入 Milvus 向量数据库
 *
 * @Author Mission
 * @Date 2026-01-29
 * @Copyright Mission
 */
@Slf4j
@Component
public class KnowledgeDocumentWriter {

    @Resource
    private AiModelConfigDao aiModelConfigDao;

    @Resource
    private DynamicModelFactory dynamicModelFactory;

    @Resource
    private AiDocumentSegmentsDao aiDocumentSegmentsDao;

    @Value("${milvus.host:localhost}")
    private String milvusHost;

    @Value("${milvus.port:19530}")
    private Integer milvusPort;

    private static final String COLLECTION_PREFIX = "knowledge_";

    /**
     * 写入文档到向量数据库
     *
     * @param documents 文档列表
     * @param knowledgeId 知识库ID
     * @param embeddingModelId 向量模型ID
     */
    public void write(List<Document> documents, Long knowledgeId, Long embeddingModelId) {
        if (documents == null || documents.isEmpty()) {
            log.warn("No documents to write for knowledge: {}", knowledgeId);
            return;
        }

        try {
            // 1. 获取向量模型配置
            AiModelConfigEntity modelConfig = aiModelConfigDao.selectById(embeddingModelId);
            if (modelConfig == null) {
                log.error("Embedding model not found: {}", embeddingModelId);
                return;
            }

            // 2. 创建 EmbeddingModel
            EmbeddingModel embeddingModel = dynamicModelFactory.createEmbeddingModel(modelConfig);

            // 3. 连接 Milvus
            MilvusServiceClient milvusClient = new MilvusServiceClient(
                    ConnectParam.newBuilder()
                            .withHost(milvusHost)
                            .withPort(milvusPort)
                            .build()
            );

            // 4. 确保集合存在
            String collectionName = COLLECTION_PREFIX + knowledgeId;
            ensureCollection(milvusClient, collectionName);

            // 5. 处理每个文档
            int position = 0;
            for (Document doc : documents) {
                try {
                    // 5.1 生成向量
                    String content = doc.getText();
                    EmbeddingResponse embeddingResponse = embeddingModel.embedForResponse(List.of(content));
                    float[] embeddingArray = embeddingResponse.getResults().get(0).getOutput();

                    // 转换为 List<Float>
                    List<Float> embedding = new ArrayList<>(embeddingArray.length);
                    for (float f : embeddingArray) {
                        embedding.add(f);
                    }

                    // 5.2 保存到数据库
                    AiDocumentSegmentsEntity segmentEntity = new AiDocumentSegmentsEntity();
                    segmentEntity.setKnowledgeId(knowledgeId);
                    segmentEntity.setDocumentId(Long.valueOf(doc.getMetadata().get("document_id").toString()));
                    segmentEntity.setType("DOCUMENT");
                    segmentEntity.setContent(content);
                    segmentEntity.setWordCount(content.length());
                    segmentEntity.setPosition(position++);
                    segmentEntity.setStatus("completed");
                    segmentEntity.setCreateTime(LocalDateTime.now());
                    segmentEntity.setUpdateTime(LocalDateTime.now());

                    aiDocumentSegmentsDao.insert(segmentEntity);

                    // 5.3 写入 Milvus
                    List<Long> ids = Arrays.asList(segmentEntity.getId());
                    List<List<Float>> vectors = Arrays.asList(embedding);
                    List<String> contents = Arrays.asList(content);

                    InsertParam insertParam = InsertParam.newBuilder()
                            .withCollectionName(collectionName)
                            .withFields(Arrays.asList(
                                    new InsertParam.Field("id", ids),
                                    new InsertParam.Field("vector", vectors),
                                    new InsertParam.Field("content", contents)
                            ))
                            .build();

                    R<MutationResult> insertResult = milvusClient.insert(insertParam);
                    if (insertResult.getStatus() != R.Status.Success.getCode()) {
                        log.error("Failed to insert vector to Milvus: {}", insertResult.getMessage());
                    }

                } catch (Exception e) {
                    log.error("Error processing document segment", e);
                }
            }

            milvusClient.close();
            log.info("Successfully wrote {} documents to knowledge: {}", documents.size(), knowledgeId);

        } catch (Exception e) {
            log.error("Error writing documents to vector database", e);
        }
    }

    /**
     * 确保集合存在，不存在则创建
     */
    private void ensureCollection(MilvusServiceClient client, String collectionName) {
        R<Boolean> hasCollection = client.hasCollection(
                HasCollectionParam.newBuilder()
                        .withCollectionName(collectionName)
                        .build()
        );

        if (hasCollection.getData() != null && hasCollection.getData()) {
            log.info("Collection already exists: {}", collectionName);
            return;
        }

        // 创建集合
        FieldType idField = FieldType.newBuilder()
                .withName("id")
                .withDataType(DataType.Int64)
                .withPrimaryKey(true)
                .withAutoID(false)
                .build();

        FieldType vectorField = FieldType.newBuilder()
                .withName("vector")
                .withDataType(DataType.FloatVector)
                .withDimension(1536) // 默认维度，根据实际模型调整
                .build();

        FieldType contentField = FieldType.newBuilder()
                .withName("content")
                .withDataType(DataType.VarChar)
                .withMaxLength(65535)
                .build();

        CreateCollectionParam createParam = CreateCollectionParam.newBuilder()
                .withCollectionName(collectionName)
                .withDescription("Knowledge base collection")
                .withFieldTypes(Arrays.asList(idField, vectorField, contentField))
                .build();

        R<?> createResult = client.createCollection(createParam);
        if (createResult.getStatus() == R.Status.Success.getCode()) {
            log.info("Successfully created collection: {}", collectionName);
        } else {
            log.error("Failed to create collection: {}", createResult.getMessage());
        }
    }

    /**
     * 将 Double 列表转换为 Float 列表
     */
    private List<Float> convertToFloatList(List<Double> doubleList) {
        List<Float> floatList = new ArrayList<>(doubleList.size());
        for (Double d : doubleList) {
            floatList.add(d.floatValue());
        }
        return floatList;
    }
}
