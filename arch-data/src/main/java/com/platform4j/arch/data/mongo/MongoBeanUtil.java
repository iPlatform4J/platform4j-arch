package com.platform4j.arch.data.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.platform4j.arch.common.JsonUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.type.JavaType;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class MongoBeanUtil {
    private static Logger logger = Logger.getLogger(MongoBeanUtil.class);

    /**
     * 把实体bean对象转换成DBObject
     *
     * @param bean
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> DBObject bean2DBObject(T bean) throws IllegalArgumentException, IllegalAccessException {
        if (bean == null) {
            return null;
        }
        DBObject dbObject = new BasicDBObject();
        // 获取对象对应类中的所有属性域
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 获取属性名
            String varName = field.getName();
            // 修改访问控制权限
            boolean accessFlag = field.isAccessible();
            if (!accessFlag) {
                field.setAccessible(true);
            }
            Object param = field.get(bean);
            if (param == null) {
                continue;
            } else if (param instanceof Integer) {// 判断变量的类型
                int value = ((Integer) param).intValue();
                dbObject.put(varName, value);
            } else if (param instanceof String) {
                String value = (String) param;
                dbObject.put(varName, value);
            } else if (param instanceof Double) {
                double value = ((Double) param).doubleValue();
                dbObject.put(varName, value);
            } else if (param instanceof Float) {
                float value = ((Float) param).floatValue();
                dbObject.put(varName, value);
            } else if (param instanceof Long) {
                long value = ((Long) param).longValue();
                dbObject.put(varName, value);
            } else if (param instanceof Boolean) {
                boolean value = ((Boolean) param).booleanValue();
                dbObject.put(varName, value);
            } else if (param instanceof Date) {
                Date value = (Date) param;
                dbObject.put(varName, value);
            }
            // 恢复访问控制权限
            field.setAccessible(accessFlag);
        }
        return dbObject;
    }

    /**
     * 把DBObject转换成bean对象
     *
     * @param dbObject
     * @param bean
     * @return
     */
    public static <T> T dbObject2Bean(DBObject dbObject, T bean) {
        return handle(dbObject, (Class<T>) bean.getClass(), bean);
    }

    public static <T> T handle(DBObject dbObject, Class<T> clazz, T bean) {
        if (!clazz.getName().equals("java.lang.Object")) {
            handle(dbObject, clazz.getSuperclass(), bean);
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                Object fieldValue = dbObject.get(fieldName);
                if (fieldValue != null) {
                    if (field.getType().isPrimitive() || field.getType().getName().equals("java.lang.String")) {// 如果是基本数据类型或字符串
                        try {
                            BeanUtils.setProperty(bean, fieldName, fieldValue);
                        } catch (IllegalAccessException e) {
                            logger.warn(e.getMessage());
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            logger.warn(e.getMessage());
                            e.printStackTrace();
                        }

                    } else {// 否则应当按字符串取出并 转为 实体bean 再set到 返回的实例里

                        if (field.getType().getName().equals("java.util.List")) {// 如果是List
                            Type genericType = field.getGenericType();
                            ParameterizedTypeImpl ty = (ParameterizedTypeImpl) genericType;
                            Type[] actualType = ty.getActualTypeArguments();
                            if(actualType.length>0){
                                try {
                                    JavaType javaType = JsonUtil.OM.getTypeFactory().constructType(actualType[0]);
                                    JavaType javaTypeInArrayList = JsonUtil.OM.getTypeFactory().constructParametricType(ArrayList.class, javaType);
                                    Object readValue = JsonUtil.OM.readValue(fieldValue.toString(), javaTypeInArrayList);
                                    BeanUtils.setProperty(bean, fieldName, readValue);
                                } catch (IOException e) {
                                    logger.warn(e.getMessage());
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    logger.warn(e.getMessage());
                                    e.printStackTrace();
                                } catch (InvocationTargetException e) {
                                    logger.warn(e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        } else {// 否则按实体去转换 JSON
                            Type type = field.getType();
                            try {
                                JavaType javaType = JsonUtil.OM.getTypeFactory().constructType(type);
                                Object readValue = JsonUtil.OM.readValue(fieldValue.toString(), javaType);
                                BeanUtils.setProperty(bean, fieldName, readValue);
                            } catch (JsonParseException e) {
                                logger.warn(e.getMessage());
                                e.printStackTrace();
                            } catch (JsonMappingException e) {
                                logger.warn(e.getMessage());
                                e.printStackTrace();
                            } catch (IOException e) {
                                logger.warn(e.getMessage());
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                logger.warn(e.getMessage());
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                logger.warn(e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return bean;
    }

    public static String upperCaseFirstLetter(String value) {
        if (value != null && value.length() > 0) {
            value = value.substring(0, 1).toUpperCase() + value.substring(1);
        }
        return value;
    }
}
