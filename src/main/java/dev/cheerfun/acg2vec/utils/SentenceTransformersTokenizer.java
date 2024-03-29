package dev.cheerfun.acg2vec.utils;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2022/11/5 22:05
 * @description SentenceTransformersTokenizer
 */
@Component
public class SentenceTransformersTokenizer {
    HuggingFaceTokenizer tokenizer;

    @PostConstruct
    public void init() throws IOException {
        //HuggingFaceTokenizer.builder().
        tokenizer = HuggingFaceTokenizer.newInstance(new ClassPathResource("tokenizer/SentenceTransformersTokenizer.json").getInputStream(), null);
    }

    public Encoding encode(String sentence) {
        return tokenizer.encode(sentence);
    }

    public Encoding[] encodeList(List<String> sentence) {
        return tokenizer.batchEncode(sentence);
    }

}
