/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.z.testUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import static javafx.scene.input.KeyCode.T;

/**
 *
 * @author MarianoLopez
 */
public class TestUtil {
    public static ObjectMapper mapper = new ObjectMapper();
    //Object to JSON
    public static String toJson(Object obj) throws JsonProcessingException{
            mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
            ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
            return ow.writeValueAsString(obj);
        }
    
    public static <T> T jsonToObj(String json, Class<T> clazz) throws IOException{
        T obj = mapper.readValue(json, clazz);
        return obj;
    }
}
