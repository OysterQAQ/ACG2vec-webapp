package dev.cheerfun.deepix.service;

import dev.cheerfun.deepix.utils.ImageLoadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2022/7/18 18:49
 * @description 图片特征抽取服务
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FeatureExtractService {

    private final TFServingService tfServingService;
    private final ImageLoadUtil imageLoadUtil;

    public Float[] generateImageFeature(InputStream inputStream) throws IOException, InterruptedException {
        return tfServingService.requestForFeatureExtract(imageLoadUtil.loadImage(inputStream));
    }

}
