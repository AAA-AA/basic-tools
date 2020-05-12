package com.github.tools.pub;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.github.tools.annotation.HideAnn;
import com.github.tools.annotation.HideCollection;
import com.github.tools.annotation.HideImg;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 日志格式化工具类，可用于隐藏base64字符串，隐藏password等一些敏感信息输出
 * @Author: renhongqiang
 * @Date: 2020/5/12 3:51 下午
 **/
public final class LogFormats {

    private LogFormats() {}

    private static final String HIDE_PWD = "*******";
    private static final String UNKNOWN = "unknown";
    private static final String SERIAL_VERSION_UID = "serialVersionUID";

    /**
     * 过滤obj中敏感属性
     */
    public static String formatLog(Object obj) {
        if (Checks.isEmpty(obj)) {
            return "null";
        }
        //隐藏注解项
        JSONObject jsonObject = handleObjectJSON(obj);
        if (jsonObject != null) {
            return jsonObject.toJSONString();
        }
        //else
        return JSONObject.toJSONString(obj);

    }

    /**
     * 处理object 返回json对象
     */
    private static JSONObject handleObjectJSON(Object target) {
        try {
            Object jsObj = JSONObject.toJSON(target);
            //数组json
            if (jsObj instanceof JSONArray) {
                ObjectBean objectBean = new ObjectBean(target);
                return handleObjectJSON(objectBean);
            }
            //对象json
            if (jsObj instanceof JSONObject) {
                JSONObject objCopy = (JSONObject) (jsObj);
                List<Field> objFields = getFieldListByClass(target.getClass());
                for (Field field : objFields) {
                    field.setAccessible(true);
                    Object fieldVal = field.get(target);
                    hideProperties(objCopy, field, fieldVal);//敏感信息隐藏
                }
                return objCopy;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //else
        return null;
    }


    /**
     * 敏感信息隐藏
     */
    private static void hideProperties(JSONObject objCopy, Field field, Object fieldVal) {
        if (isBasicType(fieldVal)) {
            //基本类型
            handleBasicType(objCopy, field, fieldVal);
        } else if (fieldVal instanceof Collection) {
            //Collection类型
            handleCollectionType(objCopy, field, fieldVal);
        } else if (fieldVal instanceof Map) {
            //Map类型
            handleMapType(objCopy, field, fieldVal);
        } else if (Checks.isArray(fieldVal)) {
            //数组类型
            handleArrayType(objCopy, field, fieldVal);
        } else {
            String fieldName = null;
            if (hasJSONField(field)) {
                fieldName = getJSONFieldName(field);
            } else {
                fieldName = field.getName();
            }
            //对象类型
            JSONObject jsonObject = handleObjectJSON(fieldVal);
            objCopy.put(fieldName, jsonObject);
        }
    }


    /**
     * 处理数组类型
     */
    private static void handleArrayType(JSONObject objCopy, Field field, Object fieldVal) {
        Object[] objects = (Object[]) fieldVal;
        String fieldName = null;
        if (hasJSONField(field)) {
            fieldName = getJSONFieldName(field);
        } else {
            fieldName = field.getName();
        }
        if (hasHideCollection(field)) {
            JSONArray array = new JSONArray();
            int length = objects.length;
            if (length > 0) {
                //取一个
                array.add(handleType(objects[0]));
                array.add(hideArrays(length));
            }
            objCopy.put(fieldName, array);
        } else {
            JSONArray array = new JSONArray();
            for (Object item : objects) {
                array.add(handleType(item));
            }
            objCopy.put(fieldName, array);
        }
    }


    /**
     * 处理Map 类型
     */
    private static void handleMapType(JSONObject objCopy, Field field, Object fieldVal) {
        Map map = (Map) fieldVal;
        String fieldName = null;
        if (hasJSONField(field)) {
            fieldName = getJSONFieldName(field);
        } else {
            fieldName = field.getName();
        }
        if (hasHideCollection(field)) {
            int size = map.size();
            JSONObject jsonObj = hideArrays(size);
            if (size > 0) {
                //取一个
                Map.Entry<Object, Object> next = (Map.Entry<Object, Object>) map.entrySet().iterator().next();
                Object key = next.getKey();
                Object item = next.getValue();
                jsonObj.put(key.toString(), handleType(item));
            }
            objCopy.put(fieldName, jsonObj);
        } else {
            JSONObject jsonObj = new JSONObject();
            for (Map.Entry<Object, Object> objectEntry : (Set<Map.Entry<Object, Object>>) (map.entrySet())) {
                Object key = objectEntry.getKey();
                Object item = objectEntry.getValue();
                jsonObj.put(key.toString(), handleType(item));
            }
            objCopy.put(fieldName, jsonObj);
        }
    }

    /**
     * 处理集合类型
     */
    private static void handleCollectionType(JSONObject objCopy, Field field, Object fieldVal) {
        Collection objCollection = (Collection) fieldVal;
        String fieldName = null;
        if (hasJSONField(field)) {
            fieldName = getJSONFieldName(field);
        } else {
            fieldName = field.getName();
        }

        if (hasHideCollection(field)) {
            int size = objCollection.size();
            JSONArray array = new JSONArray();
            if (size > 0) {
                //取一个
                Object next = objCollection.iterator().next();
                array.add(handleType(next));
                array.add(hideArrays(size));
            }
            objCopy.put(fieldName, array);
        } else {
            JSONArray array = new JSONArray();
            for (Object item : objCollection) {
                array.add(handleType(item));
            }
            objCopy.put(fieldName, array);
        }
    }


    /**
     * 处理基本类型
     */
    private static void handleBasicType(JSONObject objCopy, Field field, Object fieldVal) {

        //如果是序列化ID
        if(field.getName().equals(SERIAL_VERSION_UID)&& Modifier.isStatic(field.getModifiers())&& Modifier.isFinal(field.getModifiers())&&field.getType().getTypeName().equalsIgnoreCase("long")){
            return;
        }
        //基本类型
        int length = fieldVal.toString().length();
        String fieldName = null;
        if (hasJSONField(field)) {
            fieldName = getJSONFieldName(field);
        } else {
            fieldName = field.getName();
        }
        if ((hasHideImg(field))) {
            String front = Strings.substring((fieldVal.toString()), 0, 23);
            String after = Strings.substring((fieldVal.toString()), length - 11, length - 1);
            objCopy.put(fieldName, front + HIDE_PWD + after);
        } else if ((hasHideAn(field))) {
            objCopy.put(fieldName, HIDE_PWD);
        } else {
            objCopy.put(fieldName, fieldVal);
        }
    }


    /**
     * 隐藏集合对象，只显示第一条内容
     */
    private static JSONObject hideArrays(int size) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "collection total size is " + size);
        return jsonObject;
    }

    /**
     * 非基本类型继续format
     */
    private static Object handleType(Object target) {
        if (!isBasicType(target)) {
            return handleObjectJSON(target);
        }
        return target;
    }

    /**
     * 获取类以及父类属性
     */
    private static List<Field> getFieldListByClass(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }

    private static boolean hasHideImg(Field field) {
        return field.getAnnotation(HideImg.class) != null;
    }

    private static boolean hasJSONField(Field field) {
        return field.getAnnotation(JSONField.class) != null;
    }

    private static String getJSONFieldName(Field field) {
        JSONField annotation = field.getAnnotation(JSONField.class);
        if(annotation!=null){
            return annotation.name();
        }
        return UNKNOWN;
    }

    private static boolean hasHideAn(Field field) {
        return field.getAnnotation(HideAnn.class) != null;
    }

    private static boolean hasHideCollection(Field field) {
        return field.getAnnotation(HideCollection.class) != null;
    }

    /**
     * 判断是否为基本类型
     */
    private static boolean isBasicType(Object obj) {
        return (obj instanceof CharSequence || obj instanceof Boolean || obj instanceof Number || obj instanceof Character);
    }

    public static class ObjectBean {
        private Object arrayObject;

        public ObjectBean() {
        }

        public ObjectBean(Object arrayObject) {
            this.arrayObject = arrayObject;
        }

        public Object getArrayObject() {
            return arrayObject;
        }

        public void setArrayObject(Object arrayObject) {
            this.arrayObject = arrayObject;
        }
    }

}
