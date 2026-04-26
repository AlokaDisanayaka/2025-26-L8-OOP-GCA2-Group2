package org.DAO_JDBC;

import org.DAO.ReviewDao;
import org.DB.DBConnection;
import org.DTO.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class JdbcReviewDao implements ReviewDao {

    @Override
    public List<Review> getallReviews() {
        List<Review> reviews = new ArrayList<>();

        String sql = "SELECT review_id, customer_id, product_id, rating, comment, review_date, file_name, content_type, file_size FROM review";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reviews.add(new Review(
                        rs.getInt("review_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("product_id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getTimestamp("review_date"),
                        null,
                        rs.getString("file_name"),
                        rs.getString("content_type"),
                        rs.getInt("file_size")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving reviews", e);
        }

        return reviews;
    }

    @Override
    public Optional<Review> getreviewbyid(int id) {
        String sql = "SELECT review_id, customer_id, product_id, rating, comment, review_date, file_data, file_name, content_type, file_size FROM review WHERE review_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Review(
                            rs.getInt("review_id"),
                            rs.getInt("customer_id"),
                            rs.getInt("product_id"),
                            rs.getInt("rating"),
                            rs.getString("comment"),
                            rs.getTimestamp("review_date"),
                            rs.getBytes("file_data"),
                            rs.getString("file_name"),
                            rs.getString("content_type"),
                            rs.getInt("file_size")
                    ));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving review by ID", e);
        }

        return Optional.empty();
    }

    @Override
    public Review insertReview(Review review) {
        String sql = "INSERT INTO review (customer_id, product_id, rating, comment, review_date, file_data, file_name, content_type, file_size) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, review.getCustomerId());
            ps.setInt(2, review.getProductId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());
            ps.setTimestamp(5, review.getReviewDate());
            ps.setBytes(6, review.getFileData());
            ps.setString(7, review.getFileName());
            ps.setString(8, review.getContentType());
            ps.setInt(9, review.getFileSize());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Review(
                            keys.getInt(1),
                            review.getCustomerId(),
                            review.getProductId(),
                            review.getRating(),
                            review.getComment(),
                            review.getReviewDate(),
                            review.getFileData(),
                            review.getFileName(),
                            review.getContentType(),
                            review.getFileSize()
                    );
                }
            }

            throw new RuntimeException("Insert successful but no ID returned");

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting review", e);
        }
    }

    @Override
    public Review updateReview(int id, Review review) {
        String sql = "UPDATE review SET customer_id = ?, product_id = ?, rating = ?, comment = ?, review_date = ?, file_data = ?, file_name = ?, content_type = ?, file_size = ? WHERE review_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, review.getCustomerId());
            ps.setInt(2, review.getProductId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());
            ps.setTimestamp(5, review.getReviewDate());
            ps.setBytes(6, review.getFileData());
            ps.setString(7, review.getFileName());
            ps.setString(8, review.getContentType());
            ps.setInt(9, review.getFileSize());
            ps.setInt(10, id);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new RuntimeException("No review found with ID " + id);
            }

            return new Review(
                    id,
                    review.getCustomerId(),
                    review.getProductId(),
                    review.getRating(),
                    review.getComment(),
                    review.getReviewDate(),
                    review.getFileData(),
                    review.getFileName(),
                    review.getContentType(),
                    review.getFileSize()
            );

        } catch (SQLException e) {
            throw new RuntimeException("Error updating review", e);
        }
    }

    @Override
    public Optional<Review> getReviewMetadataById(int id) {
        String sql = "SELECT review_id, file_name, content_type, file_size FROM review WHERE review_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Review review = new Review();
                    review.setReviewId(rs.getInt("review_id"));
                    review.setFileName(rs.getString("file_name"));
                    review.setContentType(rs.getString("content_type"));
                    review.setFileSize(rs.getInt("file_size"));
                    return Optional.of(review);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving review metadata", e);
        }

        return Optional.empty();
    }

    @Override
    public boolean deleteReview(int id) {
        String sql = "DELETE FROM review WHERE review_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting review", e);
        }
    }

    @Override
    public List<Review> findReviewsByfilter(Predicate<Review> filter) {
        List<Review> matchingReviews = new ArrayList<>();

        for (Review review : getallReviews()) {
            if (filter.test(review)) {
                matchingReviews.add(review);
            }
        }

        return matchingReviews;
    }
}