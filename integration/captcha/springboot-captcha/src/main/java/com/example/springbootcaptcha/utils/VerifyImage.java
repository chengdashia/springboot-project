package com.example.springbootcaptcha.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.BufferedImage;

/**
 * @author 成大事
 * @since 2022/5/2 11:34
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyImage {

    //    原图
    BufferedImage srcImage;
    //    抠图后的图
    BufferedImage cutImage;
    //    滑块坐标点
    Integer XPosition;

    Integer YPosition;

}
