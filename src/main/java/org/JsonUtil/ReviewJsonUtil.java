package org.JsonUtil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.DTO.Review;

import java.util.List;

public class ReviewJsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String reviewToJson(Review review) {
        try {
            return mapper.writeValueAsString(review);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Review to JSON", e);
        }
    }

    public static Review reviewFromJson(String json) {
        try {
            return mapper.readValue(json, Review.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to Review", e);
        }
    }

    public static String reviewListToJson(List<Review> reviews) {
        try {
            return mapper.writeValueAsString(reviews);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting Review list to JSON", e);
        }
    }
}