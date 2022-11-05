package dev.cheerfun.acg2vec.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cheerfun.acg2vec.constant.TFServingInfo;
import dev.cheerfun.acg2vec.domain.ImageLabelPrediction;
import dev.cheerfun.acg2vec.domain.Predictions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2022/8/21 16:55
 * @description TFServing检测与交互
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TFServingService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    @Value("${TFServingServer}")
    private String TFServingServer;

    @PostConstruct
    public void init() {
        try {
            Thread.sleep(2*1000);
            checkStatus();
            log.info("tf-serving服务初始化成功");
        } catch (Exception e) {
            log.error("tf-serving服务初始化失败");
            e.printStackTrace();
        }

    }

    public void checkStatus() throws IOException, InterruptedException {
        URI uri = URI.create("http://" + TFServingServer + "/v1/models/" + TFServingInfo.FEATURE_EXTRACT_MODEL);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri).GET().build();
        String body = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        log.info(body);
    }

    public Predictions<Float[]> requestForFeatureExtract(INDArray image) throws IOException, InterruptedException {
        return objectMapper.readValue(request(image, TFServingInfo.FEATURE_EXTRACT_MODEL), new TypeReference<Predictions<Float[]>>() {
        });
    }

    public Predictions<ImageLabelPrediction> requestForLabelPredict(INDArray image) throws IOException, InterruptedException {
        return objectMapper.readValue( request(image, TFServingInfo.LABEL_PREDICT_MODEL), new TypeReference<Predictions<ImageLabelPrediction>>() {
        });
    }

    public String request(INDArray image, String modelName) throws IOException, InterruptedException {
        long l = System.currentTimeMillis();
        URI uri = URI.create("http://" + TFServingServer + "/v1/models/" + modelName + ":predict");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri).POST(HttpRequest.BodyPublishers.ofString("{\"instances\":" + image.toStringFull() + "}")).build();
        String body = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        log.info("本次抽取耗时" + (System.currentTimeMillis() - l) / 1000F + "秒");
        return body;
    }

}
