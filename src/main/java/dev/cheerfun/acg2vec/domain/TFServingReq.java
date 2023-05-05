package dev.cheerfun.acg2vec.domain;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2023/1/31 10:19
 * @description TFSeringReq
 */
public interface TFServingReq {
    String getModelName();

    String getReqBody();

    TypeReference getRespType();
}
