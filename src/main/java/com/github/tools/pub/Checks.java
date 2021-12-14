package com.github.tools.pub;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 验证工具类
 */
public class Checks {

    private static final String[] CAUSE_METHOD_NAMES = new String[]{"getCause", "getNextException", "getTargetException", "getException", "getSourceException", "getRootCause", "getCausedByException", "getNested", "getLinkedException", "getNestedException", "getLinkedCause", "getThrowable"};
    private static final Random RANDOM = new Random();
    static Class<?>[] BASIC = new Class[]{String.class, Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class};

    private Checks() {
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        // else
        return false;
    }

    public static boolean isNull(Object obj) {
        return null == obj;
    }

    public static boolean isNotNull(Object obj) {
        return !isNull(obj);
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
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

        for (int i = 0; i < size; ++i) {
            chars[i] = (char) (48 + RANDOM.nextInt(10));
        }

        return new String(chars);
    }

    public static boolean isNumeric(final CharSequence cs) {
        if (isBlank(cs)) {
            return false;
        } else {
            int sz = cs.length();

            for (int i = 0; i < sz; ++i) {
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

                for (Throwable te = getTargetCause(e); null != te; te = getTargetCause(te)) {
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

        for (int var3 = 0; var3 < var2; ++var3) {
            String methodName = var1[var3];

            try {
                Method method = e.getClass().getMethod(methodName);
                if (null != method && Throwable.class.isAssignableFrom(method.getReturnType())) {
                    return (Throwable) method.invoke(e);
                }
            } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException var6) {
                ;
            }
        }

        return null;
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
                    return String.valueOf(((LocalDateTime) value).toInstant(OffsetDateTime.now().getOffset()).toEpochMilli());
                } else {
                    return "second".equals(format) ? String.valueOf(((LocalDateTime) value).toInstant(OffsetDateTime.now().getOffset()).getEpochSecond()) : ((LocalDateTime) value).format(DateTimeFormatter.ofPattern(format));
                }
            } else if (c == LocalDate.class) {
                return ((LocalDate) value).format(DateTimeFormatter.ofPattern(format));
            } else if (c == LocalTime.class) {
                return ((LocalTime) value).format(DateTimeFormatter.ofPattern(format));
            } else {
                return Number.class.isAssignableFrom(c) ? (new DecimalFormat(format)).format(value) : String.valueOf(value);
            }
        }
    }

    public static String join(List<Object> os, String se) {
        if (isEmpty((Collection) os)) {
            return "";
        } else {
            StringBuilder bu = new StringBuilder();
            Iterator var3 = os.iterator();

            while (var3.hasNext()) {
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

            for (int var3 = 0; var3 < var2; ++var3) {
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


    /**
     * Determine whether the given object is an array:
     * either an Object array or a primitive array.
     * @param obj the object to check
     */
    public static boolean isArray(Object obj) {
        return (obj != null && obj.getClass().isArray());
    }

    public static void main(String[] args) {
        System.out.println(Strings.lineToHump("a_v_b_N_d"));

    }
}
