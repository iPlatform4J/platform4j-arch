package com.platform4j.arch.common;

import org.codehaus.jackson.map.ObjectMapper;

public class JacksonJsonMapperUtil {
    static volatile ObjectMapper objectMapper = null;
    private JacksonJsonMapperUtil() {
    }

    public static ObjectMapper getMapper() {
        if (objectMapper == null) {
            synchronized (ObjectMapper.class) {
                if (objectMapper == null) {
                    objectMapper = new ObjectMapper();
                }
            }
        }
        return objectMapper;
    }
}
