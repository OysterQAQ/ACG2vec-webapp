package dev.cheerfun.acg2vec.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.cheerfun.acg2vec.domain.ImageReverseSearchItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2022/8/21 16:57
 * @description ImageReverseSearchItem增删改查
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageReverseSearchItemService {
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public void addImageReverseSearchItem(ImageReverseSearchItem imageReverseSearchItem) throws JsonProcessingException {
        imageReverseSearchItem.setFeature(null);
        stringRedisTemplate.opsForValue().set(String.valueOf(imageReverseSearchItem.getItemId()), objectMapper.writeValueAsString(imageReverseSearchItem));
    }

    public void deleteImageReverseSearchItemById(Integer id) {
        stringRedisTemplate.delete(String.valueOf(id));
    }

    public void updateImageReverseSearchItemById(ImageReverseSearchItem imageReverseSearchItem) throws JsonProcessingException {
        imageReverseSearchItem.setFeature(null);
        stringRedisTemplate.opsForValue().set(String.valueOf(imageReverseSearchItem.getItemId()), objectMapper.writeValueAsString(imageReverseSearchItem));
    }

    //@Cacheable("IRSItem")
    public ImageReverseSearchItem getImageReverseSearchItemById(Integer id) throws JsonProcessingException {
        final String entries = stringRedisTemplate.opsForValue().get(id);
        return objectMapper.readValue(entries, ImageReverseSearchItem.class);
    }

    public Boolean checkImageReverseSearchItemById(Integer id) {
        return stringRedisTemplate.hasKey(String.valueOf(id));
    }

/*
    public void addImageReverseSearchItem(ImageReverseSearchItem imageReverseSearchItem){
        imageReverseSearchItem.setFeature(null);
        stringRedisTemplate.opsForHash().putAll(imageReverseSearchItem.getItemId(),objectMapper.convertValue(imageReverseSearchItem,Map.class));
    }


    public void deleteImageReverseSearchItemById(String id){
        stringRedisTemplate.delete(id);
    }

    public void updateImageReverseSearchItemById(ImageReverseSearchItem imageReverseSearchItem){
        imageReverseSearchItem.setFeature(null);
        stringRedisTemplate.opsForHash().putAll(imageReverseSearchItem.getItemId(),objectMapper.convertValue(imageReverseSearchItem,Map.class));
    }


    //@Cacheable("IRSItem")
    public ImageReverseSearchItem getImageReverseSearchItemById(String id){
        final Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(id);
        return objectMapper.convertValue(entries,ImageReverseSearchItem.class);
    }

    public Boolean checkImageReverseSearchItemById(String id) {
        return stringRedisTemplate.hasKey(id);
    }*/

}
