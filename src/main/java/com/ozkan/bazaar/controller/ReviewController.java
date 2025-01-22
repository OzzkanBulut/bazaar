package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.model.Product;
import com.ozkan.bazaar.model.Review;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.request.CreateReviewRequest;
import com.ozkan.bazaar.response.ApiResponse;
import com.ozkan.bazaar.service.IProductService;
import com.ozkan.bazaar.service.IReviewService;
import com.ozkan.bazaar.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final IReviewService reviewService;
    private final IUserService userService;
    private final IProductService productService;

    @GetMapping("/products/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId) {

        List<Review> reviews = reviewService.getReviewByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/products/{productId}/reviews")
    public ResponseEntity<Review> writeReview(@RequestBody CreateReviewRequest createReviewRequest,
                                              @PathVariable Long productId,
                                              @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(productId);
        Review review = reviewService.createReview(createReviewRequest, user, product);

        return ResponseEntity.ok(review);
    }

    @PatchMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long reviewId,
            @RequestBody CreateReviewRequest request,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(reviewService.updateReview(
                reviewId,
                request.getReviewText(),
                request.getReviewRating(),
                user.getId()));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        reviewService.deleteReview(reviewId,user.getId());

        ApiResponse response = new ApiResponse();
        response.setMessage("Review Deleted");

        return ResponseEntity.ok(response);
    }

}
