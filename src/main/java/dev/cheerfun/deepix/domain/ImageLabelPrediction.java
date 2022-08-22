package dev.cheerfun.deepix.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2022/8/22 14:58
 * @description ImageLabelPrediction
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageLabelPrediction {
    @JsonAlias({"bookmark_predict"})
    Float[] bookmarkPredict;
    @JsonAlias({"view_predict"})
    Float[] viewPredict;
    @JsonAlias({"sanity_predict"})
    Float[] sanityPredict;
    @JsonAlias({"restrict_predict"})
    Float[] restrictPredict;
    @JsonAlias({"x_restrict_predict"})
    Float[] xRestrictPredict;
    @JsonAlias({"tag_predict"})
    Float[] tagPredict;
}
