package dev.cheerfun.deepix.service;

import dev.cheerfun.deepix.domain.ImageLabelPredictResult;
import dev.cheerfun.deepix.utils.ImageLoadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2022/8/21 17:04
 * @description 图片标签预测服务，需要初始化标签
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageLabelPredictService {
    private final TFServingService tfServingService;
    private final ImageLoadUtil imageLoadUtil;
    private List<String> tagList;
    private List<String> bookmarkLevelList;
    private List<String> viewLevelList;
    private List<String> sanityLevelList;
    private List<String> restrictLevelList;
    private List<String> xRestrictLevelList;

    @PostConstruct
    public void init() {
        try {
            tagList = loadTextAsList("labelDict/tagList.txt");
            bookmarkLevelList = loadTextAsList("labelDict/bookmarkLevelList.txt");
            viewLevelList = loadTextAsList("labelDict/viewLevelList.txt");
            sanityLevelList = loadTextAsList("labelDict/sanityLevelList.txt");
            restrictLevelList = loadTextAsList("labelDict/restrictLevelList.txt");
            xRestrictLevelList = loadTextAsList("labelDict/xRestrictLevelList.txt");
            log.info("图片标签预测服务初始化成功");
        } catch (Exception e) {
            log.error("图片标签预测服务初始化失败");
            e.printStackTrace();
        }
    }

    public ImageLabelPredictResult predict(InputStream inputStream) throws IOException, InterruptedException {
        final Float[][] floats = tfServingService.requestForLabelPredict(imageLoadUtil.loadImage(inputStream));
        final ImageLabelPredictResult imageLabelPredictResult = new ImageLabelPredictResult();
        imageLabelPredictResult.setBookmark(bookmarkLevelList.get(findMaxIndex(floats[0])));
        imageLabelPredictResult.setView(viewLevelList.get(findMaxIndex(floats[1])));
        imageLabelPredictResult.setSanity(sanityLevelList.get(findMaxIndex(floats[2])));
        imageLabelPredictResult.setRestrict(restrictLevelList.get(findMaxIndex(floats[3])));
        imageLabelPredictResult.setXRestrict(xRestrictLevelList.get(findMaxIndex(floats[4])));
        imageLabelPredictResult.setTagList(findIndexAbove(floats[5], 0.5F).stream().map(i -> tagList.get(i)).collect(Collectors.toList()));
        return imageLabelPredictResult;

    }

    public Integer findMaxIndex(Float[] array) {
        if (array == null || array.length == 0) {
            return -1;
        }

        int largest = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[largest]) {
                largest = i;
            }
        }
        return largest;
    }

    int[] findTopKIndex(Float[] orig, int nummax) {
        Float[] copy = Arrays.copyOf(orig, orig.length);
        Arrays.sort(copy);
        Float[] honey = Arrays.copyOfRange(copy, copy.length - nummax, copy.length);
        int[] result = new int[nummax];
        int resultPos = 0;
        for (int i = 0; i < orig.length; i++) {
            Float onTrial = orig[i];
            int index = Arrays.binarySearch(honey, onTrial);
            if (index < 0) {
                continue;
            }
            result[resultPos++] = i;
        }
        return result;
    }

    List<Integer> findIndexAbove(Float[] array, Float num) {
        final ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            if (array[i] >= num) {
                result.add(i);
            }
        }
        return result;
    }

    public List<String> loadTextAsList(String path) throws IOException {
        final InputStream inputStream = new ClassPathResource(path).getInputStream();
        final ArrayList<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
