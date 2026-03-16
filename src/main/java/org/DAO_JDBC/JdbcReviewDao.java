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
        String sql = "SELECT review_id, customer_id, product_id, rating, comment, review_date FROM review";

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
                        rs.getTimestamp("review_date")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving reviews", e);
        }

        return reviews;
    }

    @Override
    public Optional<Review> getreviewbyid(int id) {   // matches interface

        String sql = "SELECT review_id, customer_id, product_id, rating, comment, review_date FROM review WHERE review_id = ?";

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
                            rs.getTimestamp("review_date")
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

        String sql = "INSERT INTO review (customer_id, product_id, rating, comment, review_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, review.getCustomerId());
            ps.setInt(2, review.getProductId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());
            ps.setTimestamp(5, review.getReviewDate());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Review(
                            keys.getInt(1),
                            review.getCustomerId(),
                            review.getProductId(),
                            review.getRating(),
                            review.getComment(),
                            review.getReviewDate()
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

        String sql = "UPDATE review SET customer_id = ?, product_id = ?, rating = ?, comment = ?, review_date = ? WHERE review_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, review.getCustomerId());
            ps.setInt(2, review.getProductId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getComment());
            ps.setTimestamp(5, review.getReviewDate());
            ps.setInt(6, id);

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
                    review.getReviewDate()
            );

        } catch (SQLException e) {
            throw new RuntimeException("Error updating review", e);
        }
    }

    @Override
    public boolean deleteReview(int id) {   // matches interface

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
    public List<Review> findReviewsByfilter(Predicate<Review> filter) {   // matches interface

        List<Review> matchingReviews = new ArrayList<>();

        for (Review review : getallReviews()) {
            if (filter.test(review)) {
                matchingReviews.add(review);
            }
        }

        return
    }
}