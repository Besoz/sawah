package com.sawah.sawah.Handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by root on 02/02/17.
 */
public class JacksonHelper {
    private static JacksonHelper ourInstance = new JacksonHelper();

    private ObjectMapper mapper;

    public static JacksonHelper getInstance() {
        return ourInstance;
    }

    private JacksonHelper() {
        mapper = new ObjectMapper();
    }

    public String convertToJSON(Object[] ObjectArray) throws JsonProcessingException {
        return mapper.writeValueAsString(ObjectArray);
    }

    public <T> T[] convertToArray(String response, Class<T> c) throws IOException {

        T[] result = (T[]) mapper.readValue(response, mapper.getTypeFactory().constructArrayType(c));

//        mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, c));
//        List<T> myObjects = mapper.readValue(response.toString(), mapper.getTypeFactory().constructCollectionType(List.class, c));
        return (T[]) result;
    }
}
