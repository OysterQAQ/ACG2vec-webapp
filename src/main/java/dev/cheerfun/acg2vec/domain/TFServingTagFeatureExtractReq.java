package dev.cheerfun.acg2vec.domain;

import ai.djl.huggingface.tokenizers.Encoding;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;

import java.util.Arrays;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2023/1/31 10:31
 * @description TFServingTagFeatureExractReq
 */
@Data
public class TFServingTagFeatureExtractReq implements TFServingReq {
    private final static String MODEL_NAME = "acgvoc2vec";

    private Encoding encode;

    private Boolean batching;

    private Encoding[] encodeArray;

    private final static String pre = "{\"instances\": [";
    private final static String pos = "]}";

    @Override
    public String getModelName() {
        return MODEL_NAME;
    }

    @Override
    public String getReqBody() {
        if (batching) {
            final StringBuilder body = new StringBuilder(pre);
            for (int i = 0; i < encodeArray.length; i++) {
                body.append("{\"input_ids\": " + Arrays.toString(paddingOrTruncate(encodeArray[i].getIds())) + ", \"attention_mask\": " + Arrays.toString(paddingOrTruncate(encodeArray[i].getAttentionMask())) + "},");
            }
            body.deleteCharAt(body.length() - 1);
            body.append(pos);

            return body.toString();
        }
        return "{\"instances\": [{\"input_ids\": " + Arrays.toString(paddingOrTruncate(encode.getIds())) + ", \"attention_mask\": " + Arrays.toString(paddingOrTruncate(encode.getAttentionMask())) + "}]}";
    }

    public long[] paddingOrTruncate(long[] input) {
        final long[] result = new long[128];
        if (input.length < 128) {
            System.arraycopy(input, 0, result, 0, input.length);
        } else {
            System.arraycopy(input, 0, result, 0, 128);
        }
        return result;
    }

    @Override
    public TypeReference getRespType() {
        return new TypeReference<Predictions<Float[]>>() {
        };
    }

    public TFServingTagFeatureExtractReq(Boolean batching, Encoding encode) {
        this.encode = encode;
        this.batching = batching;
    }

    public TFServingTagFeatureExtractReq(Boolean batching, Encoding[] encodeArray) {
        this.batching = batching;
        this.encodeArray = encodeArray;
    }
}
