package com.ozkan.bazaar.service;

import com.ozkan.bazaar.model.Product;
import com.ozkan.bazaar.model.Review;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.request.CreateReviewRequest;

import java.util.List;

public interface IReviewService {

    Review createReview(CreateReviewRequest req, User user, Product product);
    List<Review> getReviewByProductId(Long productId);
    Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception;
    Review getReviewById(Long reviewId) throws Exception;
    void deleteReview(Long reviewId, Long userId) throws Exception;



}
