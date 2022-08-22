package dev.cheerfun.deepix.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author OysterQAQ
 * @version 1.0
 * @date 2020/11/30 11:54 PM
 * @description Predictions
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Predictions<T> {

    List<T> predictions;

}