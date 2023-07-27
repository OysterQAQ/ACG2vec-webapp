package dev.cheerfun.acg2vec.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2023/4/6 20:31
 * @description ImageScore
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageScorePrediction {
    @JsonAlias({"pix2score"})
    Float[] bookmarkPredict;
    @JsonAlias({"pix2score_1"})
    Float[] viewPredict;
    @JsonAlias({"pix2score_2"})
    Float[] sanityPredict;
}
