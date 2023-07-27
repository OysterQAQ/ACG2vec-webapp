package dev.cheerfun.acg2vec.controller;

import dev.cheerfun.acg2vec.domain.Result;
import dev.cheerfun.acg2vec.service.FeatureExtractService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2022/7/19 09:28
 * @description FeatureExtractController
 */
@RestController
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FeatureExtractController {
    private final FeatureExtractService featureExtractService;

    @PostMapping("/models/illust2vec/feature")
    public ResponseEntity<Result<Float[]>> queryImageFeatureByIllust2Vec(@RequestParam("image") MultipartFile file) throws IOException {
        return ResponseEntity.ok().body(new Result<>("获取illust2vec图片特征向量成功", featureExtractService.extractImageSemanticsFeature(file.getInputStream().readAllBytes())));
    }

    @PostMapping("/models/acgvoc2vec/feature")
    public ResponseEntity<Result<Float[]>> queryTextFeatureByAcgVoc2Vec(@RequestParam("text") String text) throws IOException, InterruptedException {
        return ResponseEntity.ok().body(new Result<>("获取acgvoc2vec文本特征向量成功", featureExtractService.extractTagFeature(text)));
    }

    @PostMapping("/models/dclip_text/feature")
    public ResponseEntity<Result<Float[]>> queryTextFeatureByDclipText(@RequestParam("text") String text) throws IOException, InterruptedException {
        return ResponseEntity.ok().body(new Result<>("获取clip文本特征向量成功", featureExtractService.extractTextFeatureByDCLIP(text)));
    }


    @PostMapping("/models/dclip_image/feature")
    public ResponseEntity<Result<Float[]>> queryImageFeatureByDclipText(@RequestParam("image") MultipartFile file) throws IOException, InterruptedException {
       // return ResponseEntity.ok().body(new Result<>("获取文本特征向量成功", tagFeatureExtractService.extractTagFeature(text)));
        return null;
    }


}
