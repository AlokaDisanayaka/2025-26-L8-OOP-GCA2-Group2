package org.Interface;

import com.fasterxml.jackson.databind.ObjectMapper;
// shared ObjectMapper
public class ObjectMapperProvider {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper getMapper() {
        return mapper;
    }
}