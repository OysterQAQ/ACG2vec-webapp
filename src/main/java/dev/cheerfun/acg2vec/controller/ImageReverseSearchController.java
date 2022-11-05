package dev.cheerfun.acg2vec.controller;

import dev.cheerfun.acg2vec.domain.ImageReverseSearchItem;
import dev.cheerfun.acg2vec.domain.Result;
import dev.cheerfun.acg2vec.service.FeatureExtractService;
import dev.cheerfun.acg2vec.service.ImageReverseSearchItemService;
import dev.cheerfun.acg2vec.service.ReverseImageSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2022/7/19 09:28
 * @description ImageReverseSearchController
 */
@RestController
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageReverseSearchController {
    private final ImageReverseSearchItemService imageReverseSearchItemService;
    private final ReverseImageSearchService imageSearchService;
    private final FeatureExtractService featureExtractService;

    @PostMapping("/images/{imageId}")
    public ResponseEntity<Result<Boolean>> insertImageItem(@PathVariable("imageId") Integer imageId, @RequestParam("file") MultipartFile file, @RequestParam("extend") Map<String, Object> extendInfo) throws IOException, InterruptedException {
        imageSearchService.insertImage(imageId, file.getInputStream(), extendInfo);
        return ResponseEntity.ok().body(new Result<>("插入以图搜图条目成功", true));
    }

    @PutMapping("/images/{imageId}")
    public ResponseEntity<Result<Boolean>> updateImageItem(@PathVariable("imageId") Integer imageId, @RequestParam("file") MultipartFile file, @RequestParam("extend") Map<String, Object> extendInfo) throws IOException, InterruptedException {
        imageSearchService.updateImageItem(imageId, file.getInputStream(), extendInfo);
        return ResponseEntity.ok().body(new Result<>("更新以图搜图条目成功", true));
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Result<Boolean>> deleteImageItem(@PathVariable("imageId") Integer imageId) {
        imageSearchService.deleteImageItem(imageId);
        return ResponseEntity.ok().body(new Result<>("删除以图搜图条目成功", true));
    }

    @PostMapping("/images/similarImages")
    public ResponseEntity<Result<List<ImageReverseSearchItem>>> queryTopKSimilarImage(@RequestParam("file") MultipartFile file, @RequestParam Integer k) throws IOException, InterruptedException {
        return ResponseEntity.ok().body(new Result<>("获取以图搜图结果成功", imageSearchService.queryTopKSimilarImage(file.getInputStream(), k)));
    }

}
