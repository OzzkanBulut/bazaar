package com.ozkan.bazaar.service.impl;

import com.ozkan.bazaar.model.Product;
import com.ozkan.bazaar.model.Review;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.repository.IReviewRepository;
import com.ozkan.bazaar.request.CreateReviewRequest;
import com.ozkan.bazaar.service.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final IReviewRepository reviewRepository;

    @Override
    public Review createReview(CreateReviewRequest req, User user, Product product) {
        Review review = new Review();
        review.setReviewText(req.getReviewText());
        review.setRating(req.getReviewRating());
        review.setProduct(product);
        review.setUser(user);
        review.setProductImages(req.getProductImages());

        product.getReviews().add(review);

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception {
        Review review = getReviewById(reviewId);

        if (review.getUser().getId().equals(userId)) {
            review.setReviewText(reviewText);
            review.setRating(rating);
            return reviewRepository.save(review);

        }

        throw new Exception("You cannot update this review");
    }


    @Override
    public void deleteReview(Long reviewId, Long userId) throws Exception {

        Review review = getReviewById(reviewId);
        if (!review.getUser().getId().equals(userId)) {
            throw new Exception("You can't delete this review");
        }
        reviewRepository.delete(review);
    }

    @Override
    public Review getReviewById(Long reviewId) throws Exception {
        return reviewRepository.findById(reviewId).orElseThrow(
                () -> new Exception("Cannot find review with id: " + reviewId)
        );
    }
}
