package com.example.springboot.utils.num;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 成大事
 * @date 2022/2/18 21:37
 */
public class NumberUtil {

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

    //通过字符串类型的正确答案。得到List<Integer>的格式
    public static List<Integer> getRightKeyList(String rightKey){
        List<Integer> list = new LinkedList<>();
        for (int i = 0; i < rightKey.length(); i++) {
            int result = letterToNumber(String.valueOf(rightKey.charAt(i)));
            list.add(result);
        }
        return list;
    }


    //数字转中文
    public static String digitCapital(double n){
        String fraction[] = {"角", "分"};
        String digit[] = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
        String unit[][] = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};

        String head = n < 0 ? "负" : "";
        // 如果是负数取绝对值
        n = Math.abs(n);
        String s = "";
        BigDecimal bigDecimal = new BigDecimal(Double.valueOf(n).toString());
        String nStr = bigDecimal.toString();
        // 小数部分
        String[] split = nStr.split("\\.");
        if (split.length > 1) {
            // 小数点为特殊符号，在分割时需进行转义
            String decimalStr = split[1];
            if (decimalStr.length() > 2) {
                decimalStr = decimalStr.substring(0, 2);
            }
            // 将小数部分转换为整数
            Integer integer = Integer.valueOf(decimalStr);
            String p = "";
            for (int i = 0; i < decimalStr.length() && i < fraction.length; i++) {
                p = digit[integer % 10] + fraction[decimalStr.length() - i - 1] + p;
                integer = integer / 10;
            }
            s = p.replaceAll("(零.)+", "") + s;
        }
        if (s.length() < 1) {
            s = "整";
        }
        int integerPart = (int)Math.floor(n);
        // 整数部分
        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p = "";
            for (int j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[integerPart % 10] + unit[1][j] + p;
                integerPart = integerPart / 10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }
}