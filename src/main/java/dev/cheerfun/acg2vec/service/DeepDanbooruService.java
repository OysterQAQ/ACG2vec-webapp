package dev.cheerfun.acg2vec.service;

import dev.cheerfun.acg2vec.domain.Predictions;
import dev.cheerfun.acg2vec.domain.TFServingImageLabelPredictReq;
import dev.cheerfun.acg2vec.domain.TFServingImageSemanticsFeatureExtractReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2023/7/27 17:19
 * @description DeepDanbooruService
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeepDanbooruService {
    private final TFServingService tfServingService;

    List<String> tagList;

    @PostConstruct
    public void init() throws IOException {
        tagList=new ArrayList<>();
        //构建标签列表
        Scanner scanner = new Scanner(new ClassPathResource("deepdanbooru/tags.txt").getFile());
        while (scanner.hasNextLine()) {
            tagList.add(scanner.nextLine());
        }
        scanner.close();




    }


    public  List<String> queryImageLabelByDeepDanbooru(byte[] image) {
        final Predictions predictions = tfServingService.request(new TFServingImageLabelPredictReq(image));
        if (predictions.getPredictions() == null) {
            return null;
        }
        Float[] socre= (Float[]) predictions.getPredictions().get(0);
        List<String> imageTagList=new ArrayList<>();
        //阈值为0.5
        for (int i = 0; i < socre.length; i++) {
            if(socre[i]>=0.5){
                imageTagList.add(tagList.get(i));
            }
        }
        return imageTagList;
    }

}
