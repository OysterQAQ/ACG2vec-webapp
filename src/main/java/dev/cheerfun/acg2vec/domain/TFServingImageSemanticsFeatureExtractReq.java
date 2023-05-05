package dev.cheerfun.acg2vec.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Base64;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2023/1/31 14:21
 * @description TFServingImageFeatureExtractReq
 */
@Data
@AllArgsConstructor
public class TFServingImageSemanticsFeatureExtractReq implements TFServingReq {
    private final static String MODEL_NAME = "img_semantics_feature_extract_model";

    private byte[] image;

    @Override
    public String getModelName() {
        return MODEL_NAME;
    }

    @Override
    public String getReqBody() {
        return "{\"instances\":[{\"b64_input_bytes\":\"" + Base64.getUrlEncoder().withoutPadding().encodeToString(image) + "\"}]}";
    }

    @Override
    public TypeReference getRespType() {
        return new TypeReference<Predictions<Float[]>>() {
        };
    }
}
