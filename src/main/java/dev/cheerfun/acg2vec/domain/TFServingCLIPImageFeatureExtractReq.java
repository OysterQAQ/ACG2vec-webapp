package dev.cheerfun.acg2vec.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.cheerfun.acg2vec.constant.TFServingModelInfo;
import lombok.Data;
import org.buildobjects.process.ProcBuilder;
import org.buildobjects.process.ProcResult;
import org.buildobjects.process.TimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2023/1/31 10:31
 * @description TFServingTagFeatureExractReq
 */

@Component
@Data
class ClipImagePreprocessProperty {

    public ClipImagePreprocessProperty() {

    }

    @Value("${clip.preprocess.commend}")
    public void setCommend(String commend) {
        TFServingCLIPImageFeatureExtractReq.pythonScriptCommend = commend;
    }
}

@Data
public class TFServingCLIPImageFeatureExtractReq implements TFServingReq {
    private final static String MODEL_NAME = TFServingModelInfo.DCLIP_IMAGE;
    private String imagePath;
    static String pythonScriptCommend;

    public TFServingCLIPImageFeatureExtractReq(String imagePath) {
        this.imagePath = imagePath;
    }

    private final static String pre = "{\"instances\": ";
    private final static String pos = "}";

    @Override
    public String getModelName() {
        return MODEL_NAME;
    }

    @Override
    public String getReqBody() {
        //如果是http先保存到本地
        if (imagePath.startsWith("http")) {

        }

        //调用python脚本增量训练模型
        ProcBuilder builder = new ProcBuilder("bash", "-c", pythonScriptCommend + " " + imagePath)
                .withTimeoutMillis(5000);
        try {
            final ProcResult procResult = builder.run();
            return pre + procResult.getOutputString() + pos;

        } catch (TimeoutException ex) {
            return null;
        }

    }

    @Override
    public TypeReference getRespType() {
        return new TypeReference<Predictions<Float[]>>() {
        };
    }

    @Value("${clip.preprocess.commend}")
    public static void setPythonScriptPath(String commend) {
        TFServingCLIPImageFeatureExtractReq.pythonScriptCommend = commend;
    }
}
