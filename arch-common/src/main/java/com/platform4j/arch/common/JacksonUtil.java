package com.platform4j.arch.common;

import com.platform4j.arch.domain.Money;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.module.SimpleModule;

import java.io.IOException;
import java.io.StringWriter;

public class JacksonUtil {

    private static Logger logger = Logger.getLogger(JacksonUtil.class);

    public static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        SimpleModule module = new SimpleModule("money", Version.unknownVersion());
        module.addSerializer(Money.class, new MoneySerializer());
        module.addDeserializer(Money.class, new MoneyDeserializer());

        mapper.registerModule(module);
    }

    public static String toJson(Object obj) {
        StringWriter writer = new StringWriter();
        JsonGenerator gen;
        try {
            gen = new JsonFactory().createJsonGenerator(writer);
            mapper.writeValue(gen, obj);
            gen.close();
            String json = writer.toString();
            writer.close();
            return json;
        } catch (IOException e) {
            logger.warn(e.getCause());
        }

        return null;
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        Object object;
        try {
            object = mapper.readValue(json, classOfT);
            return (T) object;
        } catch (JsonParseException e) {
            logger.warn(e.getCause());
        } catch (JsonMappingException e) {
            logger.warn(e.getCause());
        } catch (IOException e) {
            logger.warn(e.getCause());
        }
        return null;
    }
}
