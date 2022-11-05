package dev.cheerfun.acg2vec.domain;

import lombok.Data;

import java.util.List;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2022/8/21 17:01
 * @description ImageLabelPredictItem
 */
@Data
public class ImageLabelPredictResult {
    String bookmark;
    String view;
    String sanity;
    String restrict;
    String xRestrict;
    List<String> tagList;

}
