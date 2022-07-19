package dev.cheerfun.deepix.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cheerfun.deepix.domain.Predictions;
import dev.cheerfun.deepix.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.datavec.image.loader.NativeImageLoader;
import org.nd4j.linalg.api.buffer.DataType;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.api.ops.impl.broadcast.BroadcastSubOp;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2022/7/18 18:49
 * @description NativeImageLoaderService
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageFeatureGenerateService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    @Value("${imageFeatureGenerate.TFServingServer}")
    private String TFServingServer;


    @Value("${imageFeatureGenerate.imageHeight}")
    private Integer imageHeight;
    @Value("${imageFeatureGenerate.imageWidth}")
    private Integer imageWidth;
    @Value("${imageFeatureGenerate.i2iModelName}")
    private String i2iModelName;

    @Value("${imageFeatureGenerate.isVGG}")
    private Boolean isVGG;


    @PostConstruct
    public void init() throws IOException, InterruptedException {




    }

    public Float[] generateImageFeatureForReverseImageSearch(InputStream inputStream) throws IOException, InterruptedException {
        return generateImageFeature(loadImage(inputStream,isVGG),i2iModelName);
    }


    public Float[] generateImageFeature(INDArray image,String modelName) throws IOException, InterruptedException {
        long l = System.currentTimeMillis();
        URI uri;
        uri = URI.create("http://" + TFServingServer + "/v1/models/"+modelName+":predict");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri).POST(HttpRequest.BodyPublishers.ofString("{\"instances\":" + image.toStringFull() + "}")).build();
        String body = httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        Predictions predictions = objectMapper.readValue(body, Predictions.class);
        log.info("本次抽取耗时" + (System.currentTimeMillis() - l) / 1000F + "秒");
        return predictions.getPredictions()[0];
    }

    public INDArray loadImage(InputStream inputStream,boolean isVGG) {
        try {
            INDArray image = new NativeImageLoader(imageHeight, imageWidth, 3).asMatrix(inputStream, false);
            if(isVGG){
                Nd4j.getExecutioner().execAndReturn(new BroadcastSubOp(image.dup(), Nd4j.create(new double[]{103.939, 116.779, 123.68}).castTo(DataType.FLOAT), image, 3));
            }
            return image;
        } catch (IOException e) {
            throw new BaseException(HttpStatus.BAD_REQUEST, "加载图片出错");
        } finally {
            try {
                inputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

}
