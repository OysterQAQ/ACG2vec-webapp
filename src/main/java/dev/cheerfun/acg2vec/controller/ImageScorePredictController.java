package dev.cheerfun.acg2vec.controller;

import dev.cheerfun.acg2vec.domain.ImageScorePrediction;
import dev.cheerfun.acg2vec.domain.Result;
import dev.cheerfun.acg2vec.service.ImageScoreService;
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
 * @date 2023/5/4 20:27
 * @description ImageScorePredictController
 */
@RestController
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageScorePredictController {
    private final ImageScoreService imageScoreService;

    //预测插图分数
    @PostMapping("/images/socresByPix2Score")
    public ResponseEntity<Result<ImageScorePrediction>> queryImageScoreByPix2Score(@RequestParam("image") MultipartFile file) throws IOException {
        return ResponseEntity.ok().body(new Result<>("获取插画预测分数成功", imageScoreService.extractImageScore(file.getInputStream().readAllBytes())));
    }


}
