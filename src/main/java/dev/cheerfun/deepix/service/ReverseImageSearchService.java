package dev.cheerfun.deepix.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.*;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2021/5/6 6:14 PM
 * @description 反向图片搜索服务
 */
@Slf4j
//@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReverseImageSearchService {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final MilvusService milvusService;

    private final FeatureExtractService featureExtractService;




    public List<Integer> searchTopKIllustId(MultipartFile file) throws IOException, InterruptedException {
        return searchTopKIllustIdFromMilvus(featureExtractService.generateImageFeatureForReverseImageSearch(file.getInputStream()), 10);
    }


    //从Milvus中检索topk相似向量
    public List<Integer> searchTopKIllustIdFromMilvus(Float[] imageFeature, Integer k) {
        return milvusService.search(Arrays.asList(imageFeature), k);
    }






    private void saveImage(INDArray indArray) throws IOException {

        File outputfile = new File("/Users/oysterqaq/Desktop/image.jpg");
        ImageIO.write(imageFromINDArray(indArray), "jpg", outputfile);
    }

    private BufferedImage imageFromINDArray(INDArray array) {
        long[] shape = array.shape();

        long height = shape[2];
        long width = shape[3];
        BufferedImage image = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int red = array.getInt(0, 2, y, x);
                int green = array.getInt(0, 1, y, x);
                int blue = array.getInt(0, 0, y, x);

                //handle out of bounds pixel values
                red = Math.min(red, 255);
                green = Math.min(green, 255);
                blue = Math.min(blue, 255);

                red = Math.max(red, 0);
                green = Math.max(green, 0);
                blue = Math.max(blue, 0);
                image.setRGB(x, y, new Color(red, green, blue).getRGB());
            }
        }
        return image;
    }

}
