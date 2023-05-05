package dev.cheerfun.acg2vec.service;


import dev.cheerfun.acg2vec.domain.ImageScorePrediction;
import dev.cheerfun.acg2vec.domain.Predictions;
import dev.cheerfun.acg2vec.domain.TFServingImageScoreFeatureExtractReq;
import dev.cheerfun.acg2vec.domain.TFServingImageSemanticsFeatureExtractReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2023/1/31 14:25
 * @description ImageFeatureExtractService
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageFeatureExtractService {
    private final TFServingService tfServingService;
    //private final ImageLoadUtil imageLoadUtil;

    public Float[] extractImageSemanticsFeature(byte[] image) {
        final Predictions predictions = tfServingService.request(new TFServingImageSemanticsFeatureExtractReq(image));
        if (predictions.getPredictions() == null) {
            return null;
        }

        return (Float[]) predictions.getPredictions().get(0);

    }

    public ImageScorePrediction extractImageScore(byte[] image) {

        return (ImageScorePrediction) tfServingService.request(new TFServingImageScoreFeatureExtractReq(image)).getPredictions().get(0);

    }


}
