package org.Services;

import org.DAO.ReviewDao;
import org.DTO.Review;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;


public class ReviewService {
    private final ReviewDao reviewDao;

    public ReviewService(ReviewDao reviewDao)
    {
        this.reviewDao = reviewDao;
    }
    public List <Review> getAllReviews()
    {
        return reviewDao.getallReviews();
    }
    public Optional<Review> getReviewById(int id)
    {
        return reviewDao.getreviewbyid(id);
    }
    public Review addReview(Review review)
    {
        return reviewDao.insertReview(review);
    }
    public Review updateReview( int id, Review review)
    {
        return reviewDao.updateReview(id, review);
    }
    public boolean deleteReview(int id)
    {
         return reviewDao.deleteReview(id);
    }
    public List<Review> findReviewByFilter(Predicate<Review> filter)
    {
        return reviewDao.findReviewsByfilter(filter);
    }

}
