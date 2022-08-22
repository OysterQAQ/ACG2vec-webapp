package dev.cheerfun.deepix.controller;

import dev.cheerfun.deepix.domain.Result;
import dev.cheerfun.deepix.service.FeatureExtractService;
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

    @PostMapping("/images/features")
    public ResponseEntity<Result<Float[]>> generateImageFeature(@RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
        return ResponseEntity.ok().body(new Result<>("获取图片特征向量成功", featureExtractService.generateImageFeature(file.getInputStream())));
    }

}
