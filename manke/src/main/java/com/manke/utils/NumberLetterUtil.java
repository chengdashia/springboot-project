package com.manke.utils;

import java.util.List;

/**
 * @author 成大事
 * @date 2022/2/18 21:37
 */
public class NumberLetterUtil {

//    //数字转字母 1-26 ： A-Z
//    public static String numberToCapitalLetter(int num) {
//        if (num <= 0) {
//            return null;
//        }
//        String letter = "";
//        num--;
//        do {
//            if (letter.length() > 0) {
//                num--;
//            }
//            letter = ((char) (num % 26 + (int) 'A')) + letter;
//            num = (int) ((num - num % 26) / 26);
//        } while (num > 0);
//
//        return letter;
//    }
//
//    //数字转字母 1-26 ： A-Z
//    public static String numberToLowerLetter(int num) {
//        if (num <= 0) {
//            return null;
//        }
//        String letter = "";
//        num--;
//        do {
//            if (letter.length() > 0) {
//                num--;
//            }
//            letter = ((char) (num % 26 + (int) 'a')) + letter;
//            num = (int) ((num - num % 26) / 26);
//        } while (num > 0);
//
//        return letter;
//    }
//
//    //字母转数字  A-Z ：1-26
//    public static int capitalLetterToNumber(String letter) {
//        int length = letter.length();
//        int num = 0;
//        int number = 0;
//        for(int i = 0; i < length; i++) {
//            char ch = letter.charAt(length - i - 1);
//            num = (int)(ch - 'A' + 1) ;
//            num *= Math.pow(26, i);
//            number += num;
//        }
//        return number;
//    }
//
//    //字母转数字  A-Z ：1-26
//    public static int lowerLetterToNumber(String letter) {
//        int length = letter.length();
//        int num = 0;
//        int number = 0;
//        for(int i = 0; i < length; i++) {
//            char ch = letter.charAt(length - i - 1);
//            num = (int)(ch - 'a' + 1) ;
//            num *= Math.pow(26, i);
//            number += num;
//        }
//        return number;
//    }

    //数字转字母 1-26 ： A-Z  a-z
    public static String numberToLetter(int num, boolean isCapital) {
        if (num <= 0) {
            return null;
        }
        String letter = "";
        num--;
        do {
            if (letter.length() > 0) {
                num--;
            }
            if (isCapital) {
                letter = ((char) (num % 26 + (int) 'A')) + letter;
            } else {
                letter = ((char) (num % 26 + (int) 'a')) + letter;
            }

            num = (int) ((num - num % 26) / 26);
        } while (num > 0);

        return letter;
    }

    //字母转数字  a-z  A-Z ：1-26
    public static int letterToNumber(String letter) {
        int length = letter.length();
        int num = 0;
        int number = 0;
        int isCapital = (int) (letter.charAt(0) - 'a');
        if (isCapital >= 0) {
            for (int i = 0; i < length; i++) {
                char ch = letter.charAt(length - i - 1);
                num = (int) (ch - 'a' + 1);
                num *= Math.pow(26, i);
                number += num;
            }
        } else {
            for (int i = 0; i < length; i++) {
                char ch = letter.charAt(length - i - 1);
                num = (int) (ch - 'A' + 1);
                num *= Math.pow(26, i);
                number += num;
            }
        }

        return number;
    }

    //获取正确答案的字符串形式
    public static String getRightKey(List<Integer> list) {
        StringBuilder result = new StringBuilder("");
        for (Integer i : list) {
            String s = numberToLetter(i, true);
            result.append(s);
        }
        return new String(result);
    }
}