package com.platform4j.arch.common;

import com.platform4j.arch.domain.Money;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.module.SimpleModule;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

public class JsonUtil {
    private static Logger logger = Logger.getLogger(JsonUtil.class);
    public static final ObjectMapper OM = new ObjectMapper();
    static{
        OM.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        OM.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);   /*---money module---*/
        SimpleModule module = new SimpleModule("money", Version.unknownVersion());
        module.addSerializer(Money.class, new MoneySerializer());
        module.addDeserializer(Money.class, new MoneyDeserializer());
        OM.registerModule(module);

    }
    public static JavaType assignList(Class<? extends Collection> collection, Class<? extends Object> object) {
        return JsonUtil.OM.getTypeFactory().constructParametricType(collection, object);
    }
    public static <T> ArrayList<T> readValuesAsArrayList(String key, Class<T> object) {
        ArrayList<T> list = null;
        try {
            list = OM.readValue(key, assignList(ArrayList.class, object));
        } catch (JsonParseException e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
    public static String toJson(Object obj){
        try {
            return OM.writeValueAsString(obj);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    public static <T> T fromJson(String json, Class<T> clazz){
        try {
            return OM.readValue(json, clazz);
        } catch (JsonParseException e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            logger.warn(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
