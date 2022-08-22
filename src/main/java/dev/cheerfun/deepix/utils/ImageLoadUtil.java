package dev.cheerfun.deepix.utils;

import dev.cheerfun.deepix.exception.BaseException;
import org.datavec.image.loader.NativeImageLoader;
import org.datavec.image.transform.ColorConversionTransform;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

import static org.opencv.imgproc.Imgproc.COLOR_BGR2RGB;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2022/8/21 18:53
 * @description ImageLoadUtil
 */
@Component
public class ImageLoadUtil {

    public INDArray loadImage(InputStream inputStream) {
        try {
            INDArray image = new NativeImageLoader(299, 299, 3, new ColorConversionTransform(COLOR_BGR2RGB)).asMatrix(inputStream, false).div(255);
            return image;
        } catch (IOException e) {
            throw new BaseException(HttpStatus.BAD_REQUEST, "加载图片出错");
        } finally {
            try {
                inputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}
