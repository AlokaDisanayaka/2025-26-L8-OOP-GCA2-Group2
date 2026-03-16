package org.DTO;

import java.sql.Timestamp;

public class Review {

    private int reviewId;
    private int customerId;
    private int productId;
    private int rating;
    private String comment;
    private Timestamp reviewDate;

    // Constructor used when reading from DB
    public Review(int reviewId, int customerId, int productId, int rating, String comment, Timestamp reviewDate)
    {
        this.reviewId = reviewId;
        this.customerId = customerId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    public Review()
    {

    }

    public Review(int customerId, int productId, int rating, String comment, Timestamp reviewDate)
    {
        this.reviewId = 0;
        this.customerId = customerId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    public int getReviewId()
    {
        return reviewId;
    }

    public void setReviewId(int reviewId)
    {
        if (reviewId < 0)
        {
            throw new IllegalArgumentException("Review ID cannot be negative");
        }
        this.reviewId = reviewId;
    }

    public int getCustomerId()
    {
        return customerId;
    }

    public void setCustomerId(int customerId)
    {
        if (customerId < 0)
        {
            throw new IllegalArgumentException("Customer ID cannot be negative");
        }
        this.customerId = customerId;
    }

    public int getProductId()
    {
        return productId;
    }

    public void setProductId(int productId)
    {
        if (productId < 0)
        {
            throw new IllegalArgumentException("Product ID cannot be negative");
        }
        this.productId = productId;
    }

    public int getRating()
    {
        return rating;
    }

    public void setRating(int rating)
    {
        if (rating < 1 || rating > 5)
        {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        this.rating = rating;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        if (comment == null || comment.trim().isEmpty())
        {
            throw new IllegalArgumentException("Comment cannot be blank");
        }
        this.comment = comment.trim();
    }

    public Timestamp getReviewDate()
    {
        return reviewDate;
    }

    public void setReviewDate(Timestamp reviewDate)
    {
        if (reviewDate == null)
        {
            throw new IllegalArgumentException("Review date cannot be null");
        }
        this.reviewDate = reviewDate;
    }
}