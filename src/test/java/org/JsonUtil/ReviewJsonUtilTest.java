package org.JsonUtil;

import org.DTO.Review;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class tests JSON serialisation and deserialisation
 *
 * - JSON round trip: object -> JSON -> object
 * - Serialisation of a single object to JSON
 * - Serialisation of a list of objects to JSON
 */

public class ReviewJsonUtilTest
{
    @Test
    void review_jsonRoundTrip_preservesAllFields()
    {
        Review original = new Review(
                1, 1, 1, 5,
                "Great product",
                new Timestamp(System.currentTimeMillis())
        );

        original.setFileName("review.txt");
        original.setContentType("text/plain");
        original.setFileSize(100);
        original.setFileData("test".getBytes());

        String json = ReviewJsonUtil.reviewToJson(original);
        Review result = ReviewJsonUtil.reviewFromJson(json);

        assertEquals(original.getCustomerId(), result.getCustomerId());
        assertEquals(original.getProductId(), result.getProductId());
        assertEquals(original.getRating(), result.getRating());
        assertEquals(original.getComment(), result.getComment());

        assertEquals(original.getFileName(), result.getFileName());
        assertEquals(original.getContentType(), result.getContentType());
        assertEquals(original.getFileSize(), result.getFileSize());
        assertArrayEquals(original.getFileData(), result.getFileData());
    }

    @Test
    void review_toJson_returnsValidJson()
    {
        Review review = new Review(
                1, 1, 1, 4,
                "Nice",
                new Timestamp(System.currentTimeMillis())
        );

        String json = ReviewJsonUtil.reviewToJson(review);

        assertNotNull(json);
        assertTrue(json.contains("reviewId"));
    }

    @Test
    void reviewList_toJson_returnsJsonArray()
    {
        List<Review> reviews = List.of(
                new Review(1, 1, 1, 5, "A", new Timestamp(System.currentTimeMillis())),
                new Review(2, 2, 2, 4, "B", new Timestamp(System.currentTimeMillis()))
        );

        String json = ReviewJsonUtil.reviewListToJson(reviews);

        assertNotNull(json);
        assertTrue(json.startsWith("["));
    }
}