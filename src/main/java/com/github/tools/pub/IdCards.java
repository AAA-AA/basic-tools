package com.github.tools.pub;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 身份证工具类
 * @Author: renhongqiang
 * @Date: 2020/5/5 9:01 下午
 **/
@Slf4j
public final class IdCards {
    private IdCards() {}

    public static String trans15To18(String cid) {
        String[] input = cid.split("");
        String[] result = new String[18];
        for (int i = 0; i < input.length; i++) {
            if (i <= 5) {
                result[i] = input[i];
            } else {
                result[i + 2] = input[i];
            }
        }
        //年份最后两位小于17,年份为20XX，否则为19XX
        if (Integer.valueOf(input[6]) <= 1 && Integer.valueOf(input[7]) <= 7) {
            result[6] = "2";
            result[7] = "0";
        } else {
            result[6] = "1";
            result[7] = "9";
        }
        //计算最后一位
        String[] xs = {"7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8",
                "4", "2"};
        //前十七位乘以系数[7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2],
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += Integer.valueOf(result[i]) * Integer.valueOf(xs[i]);
        }
        //对11求余，的余数 0 - 10
        int rod = sum % 11;
        //所得余数映射到对应数字即可
        switch (rod) {
            case 0:
                result[17] = "1";
                break;
            case 1:
                result[17] = "0";
                break;
            case 2:
                result[17] = "X";
                break;
            case 3:
                result[17] = "9";
                break;
            case 4:
                result[17] = "8";
                break;
            case 5:
                result[17] = "7";
                break;
            case 6:
                result[17] = "6";
                break;
            case 7:
                result[17] = "5";
                break;
            case 8:
                result[17] = "4";
                break;
            case 9:
                result[17] = "3";
                break;
            case 10:
                result[17] = "2";
                break;
            default:
                break;
        }
        StringBuilder card = new StringBuilder();

        for (String str : result) {
            card.append(str);
        }

        return card.toString();
    }

    public static String to18(String cid) {
        String[] input = cid.split("");
        if (input.length == 15) {
            return trans15To18(cid);
        }
        return cid;
    }

    /**
     * 方法名：parseAge
     * 详述：根据身份证号码，返回年龄
     *
     * @return 说明返回值含义
     * @throw 说明发生此异常的条件
     */
    public static int parseAge(String cid) {
        cid = to18(cid);
        int age = 0;
        String birthDayStr = cid.substring(6, 14);
        Date birthDay = null;
        try {
            birthDay = new SimpleDateFormat("yyyyMMdd").parse(birthDayStr);
        } catch (ParseException e) {
            log.error("", e);
        }
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("param error");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayBirth = cal.get(Calendar.DAY_OF_MONTH);
        age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth && dayNow < dayBirth) {
                age--;
            }
        } else {
            age--;
        }
        return age;
    }

    /**
     * 方法名：parseBirthday
     * 详述：根据身份证号截取出生日期
     *
     * @return 说明返回值含义
     * @throw 说明发生此异常的条件
     */
    public static String parseBirthday(String cid) {
        try {
            cid = to18(cid);
            //通过身份证号来读取出生日期
            String birthday = "";
            //如果没有身份证，那么不进行字符串截取工作。
            if (checkCardId(cid)) {
                String year = cid.substring(6, 10);
                String month = cid.substring(10, 12);
                String day = cid.substring(12, 14);
                birthday = year + "-" + month + "-" + day;
            }
            return birthday;
        } catch (Exception e) {
            log.error("parse birthday error {}", e);
        }
        return "";
    }

    /**
     * 方法名：checkCardId
     * 详述：检测身份证号是否符合规则
     *
     * @param cid
     * @return boolean说明返回值含义
     */
    public static boolean checkCardId(String cid) {
        cid = to18(cid);
        boolean flag = false;
        int len = cid.length();
        int kx = 0;
        //身份证号第一位到第十七位的系数装入到一个整型数组
        int[] weight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        //需要进行运算的是身份证前17位
        for (int i = 0; i < len - 1; i++) {
            //把身份证的数字分拆成一个个数字
            int x = Integer.parseInt(String.valueOf(cid.charAt(i)));
            //然后相加起来
            kx += weight[i] * x;
        }
        //用加出来和模以11，看余数是多少？
        int mod = kx % 11;
        //最后一位身份证的号码的对应号码,一一对应
        //(0,1,2,3,4,5,6,7,8,9,10)
        //(1,0,X,9,8,7,6,5,4,3,2)
        Character[] checkMods = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        //获取身份证最后的一个验证码
        Character lastCode = cid.charAt(len - 1);
        //判断是否对应
        String idNumber = lastCode.toString().toLowerCase();
        String checkMods2 = checkMods[mod].toString().toLowerCase();
        if (checkMods2.equals(idNumber)) {
            flag = true;
        }
        return flag;
    }

    /**
     * 方法名：parseGender
     * 详述：根据所传身份证号解析其性别
     *
     * @return 说明返回值含义
     * @throw 说明发生此异常的条件
     */
    public static GenderEnum parseGender(String cid) {
        GenderEnum gender = GenderEnum.UNKNOWN;
        try {
            cid = to18(cid);
            char c = cid.charAt(cid.length() - 2);
            int sex = Integer.parseInt(String.valueOf(c));
            if (sex % 2 == 0) {
                gender = GenderEnum.FEMALE;
            } else {
                gender = GenderEnum.MALE;
            }
        } catch (Exception e) {
            log.error("parse gender error {}", e);
        }
        return gender;
    }

   public enum  GenderEnum {
       MALE(1, "male"),
       FEMALE(2, "female"),
       UNKNOWN(9, "unknown");

       private Integer code;
       private String desc;

       public Integer getCode() {
           return code;
       }

       public String getDesc() {
           return desc;
       }

       GenderEnum(Integer code, String desc) {
           this.code = code;
           this.desc = desc;
       }
   }
}
