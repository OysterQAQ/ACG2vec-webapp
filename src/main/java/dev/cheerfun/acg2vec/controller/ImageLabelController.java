package dev.cheerfun.acg2vec.controller;

import dev.cheerfun.acg2vec.domain.Result;
import dev.cheerfun.acg2vec.service.DeepDanbooruService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2023/7/27 17:16
 * @description ImageLabelController
 */
@RestController
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageLabelController {
    private final DeepDanbooruService deepDanbooruService;


    @PostMapping("/images/labelsByDeepDanbooru")
    public ResponseEntity<Result<List<String>>> queryImageLabelByDeepDanbooru(@RequestParam("image") MultipartFile file) throws IOException {
        return ResponseEntity.ok().body(new Result<>("获取文本特征向量成功", deepDanbooruService.queryImageLabelByDeepDanbooru(file.getInputStream().readAllBytes())));

    }
}
