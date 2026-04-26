package org.Services;

import org.DAO_JDBC.JdbcReviewDao;
import org.DTO.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewServiceTest
{
    private ReviewService reviewService;

    @BeforeEach
    void setUp()
    {
        reviewService = new ReviewService(new JdbcReviewDao());
    }

    @Test
    void insertReview_returnsGeneratedId()
    {
        Review review = new Review(1, 1, 5, "Service insert test", new Timestamp(System.currentTimeMillis()));

        Review inserted = reviewService.insertReview(review);

        assertNotNull(inserted);
        assertTrue(inserted.getReviewId() > 0);

        reviewService.deleteReview(inserted.getReviewId());
    }

    @Test
    void getReviewById_returnsReview_whenExists()
    {
        Review review = new Review(1, 1, 4, "Service get test", new Timestamp(System.currentTimeMillis()));
        Review inserted = reviewService.insertReview(review);

        Optional<Review> result = reviewService.getreviewbyid(inserted.getReviewId());

        assertTrue(result.isPresent());
        assertEquals(inserted.getReviewId(), result.get().getReviewId());

        reviewService.deleteReview(inserted.getReviewId());
    }

    @Test
    void deleteReview_returnsTrue_whenExists()
    {
        Review review = new Review(1, 1, 3, "Service delete test", new Timestamp(System.currentTimeMillis()));
        Review inserted = reviewService.insertReview(review);

        boolean deleted = reviewService.deleteReview(inserted.getReviewId());

        assertTrue(deleted);
    }
}