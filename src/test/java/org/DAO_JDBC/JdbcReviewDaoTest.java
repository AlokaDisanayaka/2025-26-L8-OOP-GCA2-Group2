package org.DAO_JDBC;

import org.DTO.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test class tests the behaviour of the JdbcReviewDao
 * The purpose is to ensure that SQL CRUD operations work correctly:
 *
 * - Read operations - getById, getAll
 * - Insert operation with auto generated ID
 * - Update operation to confirm data is modified correctly
 * - Delete operation to confirm records are removed
 *
 * Using @BeforeEach makes sure each test runs independently to create a clean DAO instance
 */

public class JdbcReviewDaoTest
{
    private JdbcReviewDao dao;

    @BeforeEach
    void setUp()
    {
        dao = new JdbcReviewDao();
    }

    //Read
    @Test
    void getReviewById_returnsEmpty_whenIdDoesNotExist()
    {
        Optional<Review> result = dao.getreviewbyid(-99);
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllReviews_returnsList()
    {
        List<Review> reviews = dao.getallReviews();
        assertNotNull(reviews);
    }

    //Insert
    @Test
    void insertReview_returnsGeneratedId()
    {
        Review review = new Review(1, 1, 5, "DAO insert test review", new Timestamp(System.currentTimeMillis()));
        Review inserted = dao.insertReview(review);

        assertNotNull(inserted);
        assertTrue(inserted.getReviewId() > 0);
    }

    //Update
    @Test
    void updateReview_returnsUpdatedReview()
    {
        Review review = dao.insertReview(new Review(1, 1, 5, "Original review", new Timestamp(System.currentTimeMillis())));

        Review update = new Review(1, 1, 3, "Updated review", new Timestamp(System.currentTimeMillis()));
        Review result = dao.updateReview(review.getReviewId(), update);

        assertEquals(3, result.getRating());
        assertEquals("Updated review", result.getComment());
    }

    //Delete
    @Test
    void deleteReview_returnsTrue_whenExists()
    {
        Review review = dao.insertReview(new Review(1, 1, 4, "Delete test review", new Timestamp(System.currentTimeMillis())));
        boolean deleted = dao.deleteReview(review.getReviewId());

        assertTrue(deleted);
    }

    //Metadata
    @Test
    void getReviewMetadataById_returnsMetadataOnly()
    {
        Review review = new Review(1, 1, 5, "Metadata test review", new Timestamp(System.currentTimeMillis()));
        review.setFileData("test data".getBytes());
        review.setFileName("review-test.txt");
        review.setContentType("text/plain");
        review.setFileSize(review.getFileData().length);

        Review inserted = dao.insertReview(review);
        Optional<Review> result = dao.getReviewMetadataById(inserted.getReviewId());

        assertTrue(result.isPresent());
        assertEquals("review-test.txt", result.get().getFileName());
        assertEquals("text/plain", result.get().getContentType());
        assertEquals(review.getFileSize(), result.get().getFileSize());
        assertNull(result.get().getFileData());
    }
}