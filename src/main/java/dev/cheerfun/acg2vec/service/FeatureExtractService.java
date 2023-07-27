package dev.cheerfun.acg2vec.service;

import dev.cheerfun.acg2vec.domain.*;
import dev.cheerfun.acg2vec.utils.CLIPTokenizer;
import dev.cheerfun.acg2vec.utils.SentenceTransformersTokenizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2023/7/27 09:33
 * @description FeatureExtractService
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FeatureExtractService {

    private final TFServingService tfServingService;
    private final SentenceTransformersTokenizer sentenceTransformersTokenizer;
    private final CLIPTokenizer clipTokenizer;

    public Float[] extractImageSemanticsFeature(byte[] image) {
        final Predictions predictions = tfServingService.request(new TFServingImageSemanticsFeatureExtractReq(image));
        if (predictions.getPredictions() == null) {
            return null;
        }
        return (Float[]) predictions.getPredictions().get(0);

    }

//    public Float[] extractImageSemanticsFeatureByDCLIP(byte[] image) {
//        final Predictions predictions = tfServingService.request(new TFServingCLIPImageFeatureExtractReq(image));
//        if (predictions.getPredictions() == null) {
//            return null;
//        }
//        return (Float[]) predictions.getPredictions().get(0);
//
//    }



    public Float[] extractTagFeature(String sentence) {
        return (Float[]) tfServingService.request(new TFServingTagFeatureExtractReq(false, sentenceTransformersTokenizer.encode(sentence))).getPredictions().get(0);

    }

    public Float[] extractTextFeatureByDCLIP(String sentence) {
        return (Float[]) tfServingService.request(new TFServingCLIPTextFeatureExtractReq(false, clipTokenizer.encode(sentence))).getPredictions().get(0);

    }

    public List<Float[]> extractTagListFeature(List<String> sentence) {
        return (List<Float[]>) tfServingService.request(new TFServingTagFeatureExtractReq(true, sentenceTransformersTokenizer.encodeList(sentence))).getPredictions();

    }

    public Float[] extractMultiTagAvgPoolingFeature(List<String> tagList) {
        final List<Float[]> tagFeatureList = extractTagListFeature(tagList);
        return avgPooling(tagFeatureList);
    }

    public Float[] avgPooling(List<Float[]> tagFeatureList) {
        Float[] tagFeature = new Float[tagFeatureList.get(0).length];
        final int size = tagFeatureList.size();
        for (int i = 0; i < tagFeature.length; i++) {
            Float temp = 0F;
            for (int j = 0; j < size; j++) {
                temp += tagFeatureList.get(j)[i];
            }

            tagFeature[i] = temp / size;
        }
        return tagFeature;
    }

}
