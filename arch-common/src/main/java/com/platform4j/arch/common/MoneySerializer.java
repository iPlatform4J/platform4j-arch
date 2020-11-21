package com.platform4j.arch.common;

import com.platform4j.arch.domain.Money;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

public class MoneySerializer extends JsonSerializer<Money> {
    public MoneySerializer(){
    }

    @Override
    public void serialize(Money value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        jgen.writeStartObject();
        jgen.writeStringField("amount", value.getAmount().toString());
        jgen.writeStringField("currencyCode", value.getCurrencyCode());
        jgen.writeEndObject();
    }


}
