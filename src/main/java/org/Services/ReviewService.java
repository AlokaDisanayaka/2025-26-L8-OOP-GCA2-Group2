package org.Services;

import org.DAO.ReviewDao;
import org.DTO.Review;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ReviewService {

    private final ReviewDao reviewDao;

    public ReviewService(ReviewDao reviewDao) {
        this.reviewDao = reviewDao;
    }

    public List<Review> getallReviews() {
        return reviewDao.getallReviews();
    }

    public Optional<Review> getreviewbyid(int id) {
        return reviewDao.getreviewbyid(id);
    }

    public Review insertReview(Review review) {
        return reviewDao.insertReview(review);
    }

    public Review updateReview(int id, Review review) {
        return reviewDao.updateReview(id, review);
    }

    public boolean deleteReview(int id) {
        return reviewDao.deleteReview(id);
    }

    public List<Review> findReviewsByfilter(Predicate<Review> filter) {
        return reviewDao.findReviewsByfilter(filter);
    }
}