package dev.cheerfun.deepix.domain;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2022/8/21 16:52
 * @description 图片反向搜索项目实体类
 */
@Data
public class ImageReverseSearchItem {
    Integer itemId;
    List<Float> feature;
    Map<String, Object> extendInfo;

}
