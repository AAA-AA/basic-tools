package com.github.tools.pub;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

/**
 * @Author: renhongqiang
 * @Date: 2020/5/19 3:35 下午
 **/
public final class Randoms {


    /**包含字符*/
    public static final char[] CHARS_ALL = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()[]{}=_+".toCharArray();
    /**仅包含数字*/
//    public static final char[] CHARS_D = "1234567890".toCharArray();
    /**仅包含字母*/
//    public static final char[] CHARS_A = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    /**包含数字及字母*/
//    public static final char[] CHARS_DA = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * 随机一个包含n个数字的串
     * @param n
     * @return
     */
    public static String randomOfD(int n) {
        return RandomStringUtils.randomNumeric(n);
//        return random(CHARS_D, n, n);
    }

    /**
     * 随机一个包含[minLen, maxLen]个数字的串
     * @param minLen
     * @param maxLen
     * @return
     */
    public static String randomOfD(int minLen, int maxLen) {
        return RandomStringUtils.randomNumeric(minLen,maxLen);
//        return random(CHARS_D, minLen, maxLen);
    }

    /**
     * 随机一个包含n个字母的串
     * @param n
     * @return
     */
    public static String randomOfA(int n) {
        return RandomStringUtils.randomAlphabetic(n);
//        return random(CHARS_A, n, n);
    }
    /**
     * 随机一个包含[minLen, maxLen]个字母的串
     * @param minLen
     * @param maxLen
     * @return
     */
    public static String randomOfA(int minLen, int maxLen) {
        return RandomStringUtils.randomAlphabetic(minLen,maxLen);
//        return random(CHARS_A, minLen, maxLen);
    }

    /**
     * 随机包含n个字符（数字和字母）的串
     * @param n
     * @return
     */
    public static String randomOfDA(int n) {
        return RandomStringUtils.randomAlphanumeric(n);
//        return random(CHARS_DA, n, n);
    }
    /**
     * 随机包含[minLen, maxLen]个字符（数字和字母）的串
     * @param minLen
     * @param maxLen
     * @return
     */
    public static String randomOfDA(int minLen, int maxLen) {
        return RandomStringUtils.randomAlphanumeric(minLen,maxLen);
//        return random(CHARS_DA, minLen, maxLen);
    }

    /**
     * 随机n个字符的串
     * @param n
     * @return
     */
    public static String random(int n) {
        return random(CHARS_ALL, n, n);
    }
    /**
     * 随机[minLen, maxLen]个字符的串
     * @param minLen
     * @param maxLen
     * @return
     */
    public static String random(int minLen, int maxLen) {
        return random(CHARS_ALL, minLen, maxLen);
    }

    /**
     * 随机密码串
     * @param originalChars
     * @param minLen
     * @param maxLen
     * @return
     */
    public static String random(char[] originalChars, int minLen, int maxLen) {
        int len = originalChars.length;
        Random r = new Random();
        int n = minLen;
        if(minLen < maxLen) {
            n += r.nextInt(maxLen - minLen);
        }
        char[] ret = new char[n];
        while(--n >= 0) {
            ret[n] = originalChars[r.nextInt(len)];
        }
        return new String(ret);
    }



    /**
     * 生成8-16位随机字符串，范围大小写以及数字,没有校验
     * @return
     */
    public static String getRandomString(){
        return RandomStringUtils.randomAlphanumeric(8,16);
    }

    /**
     * 校验正则8-16位含数字、小写和大写字母
     */
    private static String regexForRandomString ="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";
    /**
     * 生成8-16位含数字、小写和大写字母的随机密码，有校验
     * @return
     */
    public static String checkAndGetResultString(){
        String str = getRandomString();
        int count = 0;
        while(true){
            if(str.matches(regexForRandomString)){
                return str;
            }
            str = getRandomString();
            count++;
            if(count>100){
                return str;
            }
        }

    }

    private static Random r= new Random();
    /**
     * probability/capacity 为概率值，例如2/5，即40%的概率
     * @param probability
     * @param capacity
     * @return
     */
    public static boolean isScoreAHit(int probability,int capacity) {
        if(capacity<=probability) {
            return true;
        }
        int temp =r.nextInt(capacity);
        return temp<probability?true:false;
    }



    /**
     * max 如果是5 返回[0,1,2,3,4]的随机组合
     * @param max
     * @return
     */
    public static int [] getRandomArray(int max){
        //循环遍历队列，这个地方不能每次都按照从0 到 ConstantValue.MemberShipPassivityListCount 大小循环
        //万一数据多了，0 1 2这几个队列没关系，后边的队列会一直处于饥饿状态的，所以每次随机一下
        int[] array = new int[max];
        for(int i = 0;i < max;i++){
            array[i] = i;
        }
        ArrayUtils.shuffle(array);
        return array;
    }


}
