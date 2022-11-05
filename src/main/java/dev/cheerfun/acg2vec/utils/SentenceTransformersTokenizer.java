package dev.cheerfun.acg2vec.utils;

import ai.djl.huggingface.tokenizers.Encoding;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2022/11/5 22:05
 * @description SentenceTransformersTokenizer
 */
@Component
public class SentenceTransformersTokenizer {
    HuggingFaceTokenizer tokenizer ;
    @PostConstruct
    public void init() throws IOException {
        tokenizer = HuggingFaceTokenizer.newInstance(new ClassPathResource("tokenizer/tokenizer.json").getInputStream(),null);
    }


    public Encoding encode(String sentence){
        return tokenizer.encode(sentence);
    }


}
