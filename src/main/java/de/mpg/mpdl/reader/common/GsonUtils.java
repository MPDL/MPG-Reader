package de.mpg.mpdl.reader.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

/**
 * @author shidenghui@gmail.com
 * @date 2020/11/24
 */
public class GsonUtils {
    public static <T> T fromJson(String json, Class<T> clazz) {
        return new Gson().fromJson(json, clazz);
    }

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static String toJsonWithJackson(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public static <T> T fromJsonWithJackson(String json, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(json, clazz);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}