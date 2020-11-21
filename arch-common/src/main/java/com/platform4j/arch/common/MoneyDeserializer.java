package com.platform4j.arch.common;

import com.platform4j.arch.domain.Money;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonDeserializer;

import java.io.IOException;
import java.util.Currency;

public class MoneyDeserializer extends JsonDeserializer<Money> {


    public MoneyDeserializer() {
    }

    private static String AMOUNT = "amount";
    private static String CURRENCY_CODE = "currencyCode";


    @Override
    public Money deserialize(org.codehaus.jackson.JsonParser jp, org.codehaus.jackson.map.DeserializationContext ctxt) throws IOException, org.codehaus.jackson.JsonProcessingException {
        JsonToken jt = jp.getCurrentToken();

        if(jt == JsonToken.VALUE_STRING){
            String name = jp.getText();
            return  new Money(name,"CNY");
        }

        if(jt == JsonToken.VALUE_NUMBER_INT){
            String name = jp.getText();
            return  new Money(name,"CNY");
        }

        if(jt == JsonToken.VALUE_NUMBER_FLOAT){
            float name = jp.getFloatValue();
            return  new Money(name,"CNY");
        }

        if(jt != JsonToken.START_OBJECT){
            return null;
        }

        String amount = null;
        String currency = null;

        while(jt != JsonToken.END_OBJECT){
            if(jt  == JsonToken.START_OBJECT){
                jt = jp.nextToken();
            }else if(jt == JsonToken.FIELD_NAME){
                String name = jp.getText();
                jt = jp.nextToken();
                String value = jp.getText();
                if(AMOUNT.equals(name)){
                    amount = value;
                }else if(CURRENCY_CODE.equals(name)){
                    currency = value;
                }
                jt = jp.nextToken();
            }
        }
        if(amount == null || currency == null){
            return null;
        }
        return new Money(amount, Currency.getInstance(currency));
    }

}
