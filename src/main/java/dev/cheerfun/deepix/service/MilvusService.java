package dev.cheerfun.deepix.service;

import com.google.gson.JsonObject;
import dev.cheerfun.deepix.constant.MilvusInfo;
import dev.cheerfun.deepix.domain.ImageReverseSearchItem;
import dev.cheerfun.deepix.exception.BaseException;
import io.milvus.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2021/5/6 11:02 PM
 * @description Milvus初始化、检测、交互
 */
//@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MilvusService {
    private final MilvusClient client;

    @PostConstruct
    public void init() {
        try {
            if (!hasCollection(MilvusInfo.DEEPIX_COLLECTION_NAME)) {
                log.info("milvus向量索引进行首次创建");
                createCollection(MilvusInfo.DEEPIX_COLLECTION_NAME, 1536, 2048);
                createIndexForCollection(MilvusInfo.DEEPIX_COLLECTION_NAME, IndexType.IVF_SQ8, 4000 * 4145 * 4);
                log.info("milvus向量索引首次创建成功");
            }
            log.info("当前集合内实体总数为：" + client.countEntities(MilvusInfo.DEEPIX_COLLECTION_NAME).getCollectionEntityCount());
            Response getCollectionStatsResponse = client.getCollectionStats(MilvusInfo.DEEPIX_COLLECTION_NAME);
            if (getCollectionStatsResponse.ok()) {
                log.info("Collection Stats:" + getCollectionStatsResponse.getMessage());
                log.info("Index Info:" + client.getIndexInfo(MilvusInfo.DEEPIX_COLLECTION_NAME).getIndex().get());
            }
            log.info("milvus服务初始化成功");
        } catch (Exception e) {
            log.error("milvus服务初始化失败");
            e.printStackTrace();
        }

    }

    public Boolean hasCollection(String collectionName) {
        return client.hasCollection(collectionName).hasCollection();
    }

    public Boolean createCollection(String collectionName, Integer dimension, Integer indexFileSize) {
        final MetricType metricType = MetricType.L2;
        CollectionMapping collectionMapping =
                new CollectionMapping.Builder(collectionName, dimension)
                        .withIndexFileSize(indexFileSize)
                        .withMetricType(metricType)
                        .build();
        Response createCollectionResponse = client.createCollection(collectionMapping);
        return createCollectionResponse.ok();
    }

    public void createIndexForCollection(String collectionName, IndexType indexType, Integer nlist) {
        JsonObject indexParamsJson = new JsonObject();
        indexParamsJson.addProperty("nlist", nlist);
        Index index =
                new Index.Builder(collectionName, indexType)
                        .withParamsInJson(indexParamsJson.toString())
                        .build();
        Response createIndexResponse = client.createIndex(index);
    }

    public void flush(String collectionName) {
        client.flush(collectionName);
    }

    public List<Integer> search(List<Float> vector, Integer k) {
        JsonObject searchParamsJson = new JsonObject();
        searchParamsJson.addProperty("nprobe", 256);
        SearchParam searchParam =
                new SearchParam.Builder(MilvusInfo.DEEPIX_COLLECTION_NAME)
                        .withFloatVectors(Collections.singletonList(normalizeVector(vector)))
                        .withTopK(k)
                        .withParamsInJson(searchParamsJson.toString())
                        .build();
        SearchResponse searchResponse = client.search(searchParam);
        if (searchResponse.ok()) {
            List<List<SearchResponse.QueryResult>> queryResultsList =
                    searchResponse.getQueryResultsList();
            List<SearchResponse.QueryResult> queryResults = queryResultsList.get(0);
            return queryResults.stream().mapToInt(e -> Math.toIntExact(e.getVectorId())).boxed().collect(Collectors.toList());
        } else {
            throw new BaseException(HttpStatus.BAD_REQUEST, "以图搜图出错");
        }
    }

    //将特征向量存入Milvus
    public Boolean saveFeatureToMilvus(ImageReverseSearchItem imageReverseSearchItem) {
        imageReverseSearchItem.setFeature(normalizeVector(imageReverseSearchItem.getFeature()));
         InsertParam.Builder builder = new InsertParam.Builder(MilvusInfo.DEEPIX_COLLECTION_NAME).withFloatVectors(Collections.singletonList(imageReverseSearchItem.getFeature()));
        if(imageReverseSearchItem.getItemId()!=null){
          builder = builder.withVectorIds(Collections.singletonList((imageReverseSearchItem.getItemId().longValue())));
        }
        InsertParam insertParam = builder.build();
        client.insert(insertParam);
        return true;
    }

    private List<Float> normalizeVector(List<Float> vector) {
        float squareSum = vector.stream().map(x -> x * x).reduce((float) 0, Float::sum);
        final float norm = (float) Math.sqrt(squareSum);
        vector = vector.stream().map(x -> x / norm).collect(Collectors.toList());
        return vector;
    }

    public void deleteFeature(Integer imageId) {
        Response deleteByIdsResponse =
                client.deleteEntityByID(MilvusInfo.DEEPIX_COLLECTION_NAME, "",Collections.singletonList(imageId.longValue()));
        client.flush(MilvusInfo.DEEPIX_COLLECTION_NAME);
    }
}
