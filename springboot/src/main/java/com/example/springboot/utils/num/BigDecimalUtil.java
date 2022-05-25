package com.example.springboot.utils.num;

import java.math.BigDecimal;

/**
 * @author 成大事
 * @since 2022/4/2 15:24
 */
public class BigDecimalUtil {
/**
 *                    BigDecimal.ROUND_HALF_UP     四舍五入
 *                    BigDecimal.ROUND_HALF_DOWN   五舍六入
 *                    BigDecimal.ROUND_HALF_EVEN   四舍六入，五分两种情况，前一位为奇数，则入位，否则舍去。
 *
 *                    BigDecimal.ROUND_DOWN        直接舍弃      ----此舍入模式始终不会增加计算值的大小
 *                                                 -3.559->-3.55   -3.551->-3.55  3.559->3.55   3.551->3.55
 *
 *                    BigDecimal.ROUND_UP          非零直接进位  ----此舍入模式始终不会减少计算值的大小
 *                                                 -3.550->-3.55  -3.559->-3.56   -3.551->-3.56    3.550->3.55  3.559->3.56   3.551->3.56
 *
 *                    BigDecimal.ROUND_CEILING     正--BigDecimal.ROUND_UP，负数--BigDecimal.ROUND_DOWN  ----此舍入模式始终不会减少计算值
 *                                                 -3.550->-3.55  -3.559->-3.55   -3.551->-3.55    3.550->3.55  3.559->3.56   3.551->3.56
 *
 *                    BigDecimal.ROUND_FLOOR       正--BigDecimal.ROUND_DOWN，负数--BigDecimal.ROUND_UP  ----此舍入模式始终不会增加计算值
 *                                                 -3.550->-3.55  -3.559->-3.56   -3.551->-3.56    3.550->3.55  3.559->3.55   3.551->3.55
 *
 *                    BigDecimal.ROUND_UNNECESSARY 断言请求的操作具有精确的结果，因此不需要舍入。如果对获得精确结果的操作指定此舍入模式，则抛出ArithmeticException。【一般不用】
 *
 */

    /**
     * 金额简单处理       分转元
     *
     * @param n1        待处理金额
     * @param scale     保留小数位数
     * @param remainder 尾数处理方式
     * @return
     */
    public static BigDecimal getY2F(String n1,int scale,int remainder){
        if(null==n1){
            return null;
        }
        BigDecimal num1=new BigDecimal(n1);
        return num1.multiply(new BigDecimal("100.00")).setScale(scale,remainder);
    }

    /**
     * 金额简单处理       分转元
     *
     * @param n1        待处理金额
     * @param scale     保留小数位数
     * @param remainder 尾数处理方式
     * @return
     */
    public static BigDecimal getF2Y(String n1, int scale, int remainder) {
        if (null == n1) {
            return null;
        }
        BigDecimal num1 = new BigDecimal(n1);
        return num1.divide(new BigDecimal("100.00"), scale, remainder);
    }

    /**
     * 求变化率
     *
     * @param n1        除数
     * @param n2        被除数
     * @param scale     保留小数位数
     * @param remainder 小数尾数处理方法
     * @return
     */
    public static BigDecimal getChangeRatio(double n1, double n2, int scale, int remainder) {
        String num1 = Double.toString(n1);
        String num2 = Double.toString(n2);
        return getChangeRatio(num1, num2, scale, remainder);
    }

    /**
     * 求变化率
     *
     * @param n1        除数
     * @param n2        被除数
     * @param scale     保留小数位数
     * @param remainder 小数尾数处理方法
     * @return
     */
    public static BigDecimal getChangeRatio(long n1, long n2, int scale, int remainder) {
        String num1 = Long.toString(n1);
        String num2 = Long.toString(n2);
        return getChangeRatio(num1, num2, scale, remainder);
    }

    /**
     * 求变化率
     *
     * @param n1        除数
     * @param n2        被除数
     * @param scale     保留小数位数
     * @param remainder 小数尾数处理方法
     * @return
     */
    public static BigDecimal getChangeRatio(int n1, int n2, int scale, int remainder) {
        String num1 = Integer.toString(n1);
        String num2 = Integer.toString(n2);
        return getChangeRatio(num1, num2, scale, remainder);
    }

    /**
     * 求变化率
     *
     * @param n1        除数
     * @param n2        被除数
     * @param scale     保留小数位数
     * @param remainder 小数尾数处理方法
     * @return
     */
    public static BigDecimal getChangeRatio(String n1, String n2, int scale, int remainder) {
        if (null == n1 || null == n2) {
            return null;
        }
        BigDecimal v1 = new BigDecimal(n1);
        BigDecimal v2 = new BigDecimal(n2);
        return getChangeRatio(v1, v2, scale, remainder);
    }

    /**
     * 求变化率
     *
     * @param n1        除数
     * @param n2        被除数
     * @param scale     保留小数位数
     * @param remainder 小数尾数处理方法
     * @return
     */
    public static BigDecimal getChangeRatio(BigDecimal n1, BigDecimal n2, int scale, int remainder) {
        if (null == n1 || null == n2) {
            return null;
        }
        BigDecimal v1 = n1.subtract(n2);
        return getRatio(v1, n2, scale, remainder);
    }

    /**
     * 求变化率
     *
     * @param n1        除数
     * @param n2        被除数
     * @param scale     保留小数位数
     * @param remainder 小数尾数处理方法
     * @return
     */
    public static BigDecimal getRatio(long n1, long n2, int scale, int remainder) {
        String str1 = Long.toString(n1);
        String str2 = Long.toString(n2);
        return getRatio(str1, str2, scale, remainder);
    }

    /**
     * 求变化率
     *
     * @param n1        除数
     * @param n2        被除数
     * @param scale     保留小数位数
     * @param remainder 小数尾数处理方法
     * @return
     */
    public static BigDecimal getRatio(int n1, int n2, int scale, int remainder) {
        String str1 = Integer.toString(n1);
        String str2 = Integer.toString(n2);
        return getRatio(str1, str2, scale, remainder);
    }

    /**
     * @param d1        除数
     * @param d2        被除数
     * @param scale     保留小数位数
     * @param remainder 小数尾数处理方法
     * @return
     */
    public static BigDecimal getRatio(double d1, double d2, int scale, int remainder) {
        String str1 = Double.toString(d1);
        String str2 = Double.toString(d2);
        return getRatio(str1, str2, scale, remainder);
    }

    /**
     * 求百分比
     *
     * @param n1        除数
     * @param n2        被除数
     * @param scale     保留小数位数
     * @param remainder 小数尾数处理方法
     * @return
     */
    public static BigDecimal getRatio(String n1, String n2, int scale, int remainder) {
        if (null == n1 || null == n2) {
            return null;
        }
        BigDecimal v1 = new BigDecimal(n1);
        BigDecimal v2 = new BigDecimal(n2);
        return getRatio(v1, v2, scale, remainder);
    }

    /**
     * 求百分比
     *
     * @param n1        除数
     * @param n2        被除数
     * @param scale     保留小数位数
     * @param remainder 小数尾数处理方法
     * @return
     */
    public static BigDecimal getRatio(BigDecimal n1, BigDecimal n2, int scale, int remainder) {
        if (null == n1 || null == n2) {
            return null;
        }
        if (0 == n2.compareTo(BigDecimal.ZERO)) {
            return new BigDecimal(1).setScale(scale);
        }
        return n1.divide(n2, scale, remainder);
    }

    /**
     * 求两数和
     *
     * @param n1        加数1
     * @param n2        加数2
     * @param scale     保留小数位数
     * @param remainder 小数位数处理方法
     * @return
     */
    public static BigDecimal add(String n1, String n2, int scale, int remainder) {
        if (null == n1 || null == n2) {
            return null;
        }
        BigDecimal num1 = new BigDecimal(n1);
        BigDecimal num2 = new BigDecimal(n2);
        return (num1.add(num2)).setScale(scale, remainder);
    }

    /**
     * 求两数和
     *
     * @param n1        加数1
     * @param n2        加数2
     * @return
     */
    public static BigDecimal add(String n1, String n2) {
        if (null == n1 || null == n2) {
            return null;
        }
        BigDecimal num1 = new BigDecimal(n1);
        BigDecimal num2 = new BigDecimal(n2);
        return (num1.add(num2)).setScale(2, 2);
    }

    /**
     * 求两数差
     *
     * @param n1        减数
     * @param n2        被减数
     * @param scale     保留小数位数
     * @param remainder 小数位数处理方法
     * @return
     */
    public static BigDecimal sub(String n1, String n2, int scale, int remainder) {
        if (null == n1 || null == n2) {
            return null;
        }
        BigDecimal num1 = new BigDecimal(n1);
        BigDecimal num2 = new BigDecimal(n2);
        return num1.subtract(num2).setScale(scale, remainder);
    }


    /**
     * 求两数差
     *
     * @param n1        减数
     * @param n2        被减数
     * @return
     */
    public static BigDecimal sub(String n1, String n2) {
        if (null == n1 || null == n2) {
            return new BigDecimal(0);
        }
        BigDecimal num1 = new BigDecimal(n1);
        BigDecimal num2 = new BigDecimal(n2);
        return num1.subtract(num2).setScale(2, 2);
    }

    public static BigDecimal sub(BigDecimal n1, BigDecimal n2) {
        if (null == n1 || null == n2) {
            return new BigDecimal(0);
        }
        return n1.subtract(n2).setScale(2, 2);
    }

    /**
     * 求两数积
     *
     * @param n1        乘数1
     * @param n2        乘数2
     * @param scale     保留小数位数
     * @param remainder 小数位数处理方法
     * @return
     */
    public static BigDecimal mul(String n1, String n2, int scale, int remainder) {
        if (null == n1 || null == n2) {
            return null;
        }
        BigDecimal num1 = new BigDecimal(n1);
        BigDecimal num2 = new BigDecimal(n2);
        return num1.multiply(num2).setScale(scale, remainder);
    }

    /**
     * 求两数积
     *
     * @param n1        乘数1
     * @param n2        乘数2
     * @return
     */
    public static BigDecimal mul(String n1, String n2) {
        if (null == n1 || null == n2) {
            return null;
        }
        BigDecimal num1 = new BigDecimal(n1);
        BigDecimal num2 = new BigDecimal(n2);
        return num1.multiply(num2).setScale(2, 2);
    }

    /**
     * @param n1        除数
     * @param n2        被除数
     * @param scale     保留小数位数
     * @param remainder 小数位数处理方法
     * @return
     */
    public static BigDecimal div(String n1, String n2, int scale, int remainder) {
        if (null == n1 || null == n2) {
            return null;
        }
        BigDecimal num1 = new BigDecimal(n1);
        BigDecimal num2 = new BigDecimal(n2);
        if (0 == num2.compareTo(BigDecimal.ZERO)) {
            throw new RuntimeException("除数不能为零");
        }
        return num1.divide(num2, scale, remainder);
    }

    /**
     * @param n1        除数
     * @param n2        被除数
     * @return
     */
    public static BigDecimal div(String n1, String n2) {
        if (null == n1 || null == n2) {
            return null;
        }
        BigDecimal num1 = new BigDecimal(n1);
        BigDecimal num2 = new BigDecimal(n2);
        if (0 == num2.compareTo(BigDecimal.ZERO)) {
            throw new RuntimeException("除数不能为零");
        }
        return num1.divide(num2, 2, 2);
    }

    /**
     * 设置参数小数位数
     *
     * @param n1        参数
     * @param scale     小数点位数
     * @param remainder 尾数处理方式
     * @return
     */
    public static BigDecimal setPoint(String n1, int scale, int remainder) {
        if (null == n1) {
            return null;
        }
        BigDecimal num1 = new BigDecimal(n1);
        return num1.setScale(scale, remainder);
    }

    /**
     * 设置参数小数位数
     *
     * @param n1        参数
     * @param scale     小数点位数
     * @param remainder 尾数处理方式
     * @return
     */
    public static BigDecimal setPoint(int n1, int scale, int remainder) {
        String num1 = Integer.toString(n1);
        return setPoint(num1, scale, remainder);
    }

    /**
     * 设置参数小数位数
     *
     * @param n1        参数
     * @param scale     小数点位数
     * @param remainder 尾数处理方式
     * @return
     */
    public static BigDecimal setPoint(long n1, int scale, int remainder) {
        String num1 = Long.toString(n1);
        return setPoint(num1, scale, remainder);
    }

    /**
     * 设置参数小数位数
     *
     * @param n1        参数
     * @param scale     小数点位数
     * @param remainder 尾数处理方式
     * @return
     */
    public static BigDecimal setPoint(double n1, int scale, int remainder) {
        String num1 = Double.toString(n1);
        return setPoint(num1, scale, remainder);
    }

    /**
     * 比较大小，flag取值：==:1  <:2   >:3   <=:4   >=:5   !=:6
     *
     * @param n1
     * @param n2
     * @param flag
     * @return
     */
    public static boolean compare(String n1, String n2, int flag) {
        if (null == n1 || null == n2 || n1.equals("") || n2.equals("")) {
            return false;
        }
        BigDecimal num1Bcm1 = new BigDecimal(n1);
        BigDecimal num2Bcm2 = new BigDecimal(n2);
        if (1 == flag) {
            return num1Bcm1.compareTo(num2Bcm2) == 0;
        }
        if (2 == flag) {
            return num1Bcm1.compareTo(num2Bcm2) == -1;
        }
        if (3 == flag) {
            return num1Bcm1.compareTo(num2Bcm2) == 1;
        }
        if (4 == flag) {
            return num1Bcm1.compareTo(num2Bcm2) <= 0;
        }
        if (5 == flag) {
            return num1Bcm1.compareTo(num2Bcm2) >= 0;
        }
        if (6 == flag) {
            return !(num1Bcm1.compareTo(num2Bcm2) == 0);
        }
        return false;
    }


    /**
     * 比较大小，flag取值：==:1  <:2   >:3   <=:4   >=:5   !=:6
     *
     * @param n1
     * @param n2
     * @param flag
     * @return
     */
    public static boolean compare(BigDecimal n1, BigDecimal n2, int flag) {
        if (null == n1 || null == n2 || n1.equals("") || n2.equals("")) {
            return false;
        }
        if (1 == flag) {
            return n1.compareTo(n2) == 0;
        }
        if (2 == flag) {
            return n1.compareTo(n2) == -1;
        }
        if (3 == flag) {
            return n1.compareTo(n2) == 1;
        }
        if (4 == flag) {
            return n1.compareTo(n2) <= 0;
        }
        if (5 == flag) {
            return n1.compareTo(n2) >= 0;
        }
        if (6 == flag) {
            return !(n1.compareTo(n2) == 0);
        }
        return false;
    }

}
