package org.DAO;

import org.DTO.Review;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface ReviewDao {

    // return all reviews
    List<Review> getallReviews();

    // return one review by id
    Optional<Review> getreviewbyid(int id);

    // insert a new review and return it with generated id
    Review insertReview(Review review);

    // update review by id
    Review updateReview(int id, Review review);

    // delete review by id
    boolean deleteReview(int id);

    // filter reviews using a predicate
    List<Review> findReviewsByfilter(Predicate<Review> filter);

    // STAGE 3: get metadata only (same style as Orders)
    Optional<Review> getReviewMetadataById(int id);
}