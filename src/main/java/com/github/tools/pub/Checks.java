package com.github.tools.pub;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 验证工具类
 */
public class Checks {

        private static final String[] CAUSE_METHOD_NAMES = new String[]{"getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable"};
        private static final Random RANDOM = new Random();
        static Class<?>[] BASIC = new Class[]{String.class, Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class};

        private Checks() {
        }

        public static boolean isEmpty(Map<?, ?> parameters) {
            return null == parameters || parameters.isEmpty();
        }

        public static boolean isEmpty(Collection<?> parameters) {
            return null == parameters || parameters.isEmpty();
        }

        public static boolean isNotEmpty(Collection<?> parameters) {
            return !isEmpty(parameters);
        }

        public static <T> boolean isEmpty(T[] parameters) {
            return null == parameters || parameters.length < 1;
        }

        public static boolean isBlank(CharSequence cs) {
            int strLen;
            if (cs != null && (strLen = cs.length()) != 0) {
                for(int i = 0; i < strLen; ++i) {
                    if (!Character.isWhitespace(cs.charAt(i))) {
                        return false;
                    }
                }

                return true;
            } else {
                return true;
            }
        }

        public static boolean isNotBlank(CharSequence str) {
            return !isBlank(str);
        }

        public static String randomNumeric(int size) {
            char[] chars = new char[size];

            for(int i = 0; i < size; ++i) {
                chars[i] = (char)(48 + RANDOM.nextInt(10));
            }

            return new String(chars);
        }

        public static boolean isNumeric(final CharSequence cs) {
            if (isBlank(cs)) {
                return false;
            } else {
                int sz = cs.length();

                for(int i = 0; i < sz; ++i) {
                    if (!Character.isDigit(cs.charAt(i))) {
                        return false;
                    }
                }

                return true;
            }
        }

        public static String toString(byte[] bytes, String charset) {
            return new String(bytes, charset == null ? Charset.defaultCharset() : Charset.forName(charset));
        }


        public static int indexOfThrowable(Throwable e, Class<? extends Throwable> clazz) {
            if (null != e && null != clazz) {
                if (clazz.isAssignableFrom(e.getClass())) {
                    return 0;
                } else {
                    int count = 0;

                    for(Throwable te = getTargetCause(e); null != te; te = getTargetCause(te)) {
                        ++count;
                        if (clazz.isAssignableFrom(te.getClass())) {
                            return count;
                        }
                    }

                    return -1;
                }
            } else {
                return -1;
            }
        }

        private static Throwable getTargetCause(Throwable e) {
            String[] var1 = CAUSE_METHOD_NAMES;
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                String methodName = var1[var3];

                try {
                    Method method = e.getClass().getMethod(methodName);
                    if (null != method && Throwable.class.isAssignableFrom(method.getReturnType())) {
                        return (Throwable)method.invoke(e);
                    }
                } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException var6) {
                    ;
                }
            }

            return null;
        }

        public static LocalDateTime convert(long millis) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), TimeZone.getDefault().toZoneId());
        }

        public static LocalDateTime parse(String text) {
            return LocalDateTime.parse(standardDateTime(text), DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.CHINA));
        }

        private static String standardDateTime(String text) {
            String standard = text.replaceAll("\\D", "");
            StringBuilder bu = new StringBuilder(standard);

            while(true) {
                while(bu.length() < 14) {
                    if (bu.length() == 5 && bu.charAt(4) == '0' || bu.length() == 7 && bu.charAt(6) == '0') {
                        bu.append('1');
                    } else {
                        bu.append('0');
                    }
                }

                standard = bu.toString();
                if (standard.length() > 14) {
                    standard = standard.substring(0, 14);
                }

                return standard;
            }
        }

        private static BigDecimal removePercent(String text) {
            if (text.contains("%")) {
                return (new BigDecimal(text.replace("%", ""))).movePointLeft(2);
            } else {
                return text.contains("‰") ? (new BigDecimal(text.replace("‰", ""))).movePointLeft(3) : new BigDecimal(text);
            }
        }

        public static String format(Object value, String format) {
            if (null == value) {
                return "";
            } else if (isBlank(format)) {
                return String.valueOf(value);
            } else {
                Class<?> c = value.getClass();
                if (c == Date.class) {
                    return (new SimpleDateFormat(format)).format(value);
                } else if (c == LocalDateTime.class) {
                    if ("millisecond".equals(format)) {
                        return String.valueOf(((LocalDateTime)value).toInstant(OffsetDateTime.now().getOffset()).toEpochMilli());
                    } else {
                        return "second".equals(format) ? String.valueOf(((LocalDateTime)value).toInstant(OffsetDateTime.now().getOffset()).getEpochSecond()) : ((LocalDateTime)value).format(DateTimeFormatter.ofPattern(format));
                    }
                } else if (c == LocalDate.class) {
                    return ((LocalDate)value).format(DateTimeFormatter.ofPattern(format));
                } else if (c == LocalTime.class) {
                    return ((LocalTime)value).format(DateTimeFormatter.ofPattern(format));
                } else {
                    return Number.class.isAssignableFrom(c) ? (new DecimalFormat(format)).format(value) : String.valueOf(value);
                }
            }
        }

    public synchronized static String cleanSpecialChar(String str){
        //过滤掉非字母，数字，中文的符号
        str = str.replaceAll("[^(a-zA-Z0-9\\u4e00-\\u9fa5)]", "");
        return str;
    }

        public static String join(List<Object> os, String se) {
            if (isEmpty((Collection)os)) {
                return "";
            } else {
                StringBuilder bu = new StringBuilder();
                Iterator var3 = os.iterator();

                while(var3.hasNext()) {
                    Object o = var3.next();
                    bu.append(se).append(String.valueOf(o));
                }

                return bu.toString().substring(1);
            }
        }

        public static boolean isBasicType(Class<?> clazz) {
            if (clazz.isPrimitive()) {
                return true;
            } else {
                Class[] var1 = BASIC;
                int var2 = var1.length;

                for(int var3 = 0; var3 < var2; ++var3) {
                    Class<?> b = var1[var3];
                    if (b.isAssignableFrom(clazz)) {
                        return true;
                    }
                }

                return false;
            }
        }



        public static <T extends Annotation> T findAnnotation(Field field, Method method, Class<T> a) {
            return field.isAnnotationPresent(a) ? field.getAnnotation(a) : method.getAnnotation(a);
        }

        public static boolean isProxyClass(Class<?> clazz) {
            return isJDKProxy(clazz) || isCGLIBProxy(clazz);
        }

        public static boolean isJDKProxy(Class<?> clazz) {
            return clazz != null && Proxy.isProxyClass(clazz);
        }

        public static boolean isCGLIBProxy(Class<?> clazz) {
            return clazz != null && null != clazz.getName() && clazz.getName().contains("$$");
        }


        public static void main(String[] args) {
            System.out.println(Strings.lineToHump("a_v_b_N_d"));

        }

}
