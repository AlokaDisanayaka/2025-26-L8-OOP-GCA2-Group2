package org.Interface;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * @author ALoka (primary)
 * @author Adhira Pillai (contributor)
 **/

public class ObjectMapperProvider
{
    private static final ObjectMapper mapper = createMapper();

    private static ObjectMapper createMapper()
    {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }

    public static ObjectMapper getMapper()
    {
        return mapper;
    }
}