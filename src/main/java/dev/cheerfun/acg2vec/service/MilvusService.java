package dev.cheerfun.acg2vec.service;

import com.google.gson.JsonObject;

import dev.cheerfun.acg2vec.constant.MilvusInfo;
import dev.cheerfun.acg2vec.exception.BaseException;
import io.milvus.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
            check(MilvusInfo.IMAGE_SEMANTIC_FEATURE, IndexType.IVF_SQ8, (int) (Math.sqrt(6000000) * 4), 1024);
            check(MilvusInfo.HOT_1000000_TAG_SEMANTIC_FEATURE, IndexType.IVF_SQ8, (int) (Math.sqrt(100000) * 4), 512);


            log.info("milvus服务初始化成功");
        } catch (Exception e) {
            log.error("milvus服务初始化失败");
            e.printStackTrace();
        }

    }

    public void check(String collectionName, IndexType indexType, Integer nlist, Integer dimension) {
        if (!hasCollection(collectionName)) {
            log.info("milvus " + collectionName + " 向量索引进行首次创建");
            createCollection(collectionName, dimension, 2048);
            createIndexForCollection(collectionName, indexType, nlist);
            log.info("milvus " + collectionName + " 向量索引首次创建成功");
        } else {
            log.info("milvus " + collectionName + " 集合内实体总数为：" + client.countEntities(collectionName).getCollectionEntityCount());
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

    public List<Long> search(String collectionName, List<Float> vector, Integer k, Integer nprobe) {
        JsonObject searchParamsJson = new JsonObject();
        searchParamsJson.addProperty("nprobe", nprobe);
        SearchParam searchParam =
                new SearchParam.Builder(collectionName)
                        .withFloatVectors(Collections.singletonList(normalizeVector(vector)))
                        .withTopK(k)
                        .withParamsInJson(searchParamsJson.toString())
                        .build();
        SearchResponse searchResponse = client.search(searchParam);
        if (searchResponse.ok()) {
            List<List<SearchResponse.QueryResult>> queryResultsList =
                    searchResponse.getQueryResultsList();
            List<SearchResponse.QueryResult> queryResults = queryResultsList.get(0);
            return queryResults.stream().mapToLong(e -> Math.toIntExact(e.getVectorId())).boxed().collect(Collectors.toList());
        } else {
            throw new BaseException(HttpStatus.BAD_REQUEST, "向量搜索出错");
        }
    }

    //将特征向量存入Milvus
    public Boolean saveFeatureToMilvus(String collectionName, Long id, Float[] feature) {
        List<Float> featureAfterL2N = normalizeVector(List.of(feature));
        InsertParam.Builder builder = new InsertParam.Builder(collectionName).withFloatVectors(Collections.singletonList(featureAfterL2N));
        builder = builder.withVectorIds(Collections.singletonList(id));
        InsertParam insertParam = builder.build();
        client.insert(insertParam);
        return true;
    }

    public Boolean saveFeatureToMilvus(String collectionName, Long id, List<Float> feature) {
        List<Float> featureAfterL2N = normalizeVector(feature);
        InsertParam.Builder builder = new InsertParam.Builder(collectionName).withFloatVectors(Collections.singletonList(featureAfterL2N));
        builder = builder.withVectorIds(Collections.singletonList(id));
        InsertParam insertParam = builder.build();
        client.insert(insertParam);
        return true;
    }

    public List<Float> normalizeVector(List<Float> vector) {
        float squareSum = vector.stream().map(x -> x * x).reduce((float) 0, Float::sum);
        final float norm = (float) Math.sqrt(squareSum);
        vector = vector.stream().map(x -> x / norm).collect(Collectors.toList());
        return vector;
    }

    public void deleteFeature(String index, Long id) {
        Response deleteByIdsResponse =
                client.deleteEntityByID(index, "", Collections.singletonList(id));

    }
}
