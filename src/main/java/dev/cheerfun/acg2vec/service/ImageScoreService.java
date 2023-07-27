package dev.cheerfun.acg2vec.service;

import dev.cheerfun.acg2vec.domain.ImageScorePrediction;
import dev.cheerfun.acg2vec.domain.TFServingImageScoreFeatureExtractReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2023/7/27 18:32
 * @description ImageScoreService
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageScoreService {
    private final TFServingService tfServingService;


    public ImageScorePrediction extractImageScore(byte[] image) {
        return (ImageScorePrediction) tfServingService.request(new TFServingImageScoreFeatureExtractReq(image)).getPredictions().get(0);
    }
}
