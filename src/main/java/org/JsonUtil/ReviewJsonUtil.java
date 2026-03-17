package org.JsonUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.DTO.Review;

import java.util.List;

public class ReviewJsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();
    public  static String  reviewToJson(Review review)
    {
        try
        {
            return mapper.writeValueAsString(review);
        }
        catch (JsonProcessingException e)
        {
           throw new RuntimeException( "Error converting review to json", e);
        }
    }
    public static Review reviewFromJson(String reviewJson)
    {
        try
        {
            return mapper.readValue(reviewJson, Review.class);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException( "Error converting review to json", e);
        }
    }
    public static String reviewListToJson(List<Review> reviews)
    {
        try
        {
            return mapper.writeValueAsString(reviews);
        }
        catch (JsonProcessingException e)
            {
            throw new RuntimeException( "Error converting review list to json", e);
            }
    }
}
