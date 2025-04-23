package com.ozkan.bazaar.controller;

import com.ozkan.bazaar.model.Cart;
import com.ozkan.bazaar.model.CartItem;
import com.ozkan.bazaar.model.Product;
import com.ozkan.bazaar.model.User;
import com.ozkan.bazaar.request.AddItemRequest;
import com.ozkan.bazaar.response.ApiResponse;
import com.ozkan.bazaar.service.ICartItemService;
import com.ozkan.bazaar.service.ICartService;
import com.ozkan.bazaar.service.IProductService;
import com.ozkan.bazaar.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final ICartService cartService;
    private final ICartItemService cartItemService;
    private final IUserService userService;
    private final IProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = cartService.findUserCart(user);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest req,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(req.getProductId());

        CartItem item = cartService.addCartItem(user, product, req.getSize(), req.getQuantity());

        ApiResponse res = new ApiResponse("Item added to cart successfully");

        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {

        User user = userService.findUserByJwtToken(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        return new ResponseEntity<>(new ApiResponse("Item removed from cart"), HttpStatus.OK);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItemHandler(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        CartItem updatedCartItem = null;

        if(cartItem.getQuantity()>0){
            updatedCartItem = cartItemService.updateCartItem(user.getId(), cartItemId, cartItem);
        }

        return ResponseEntity.ok(updatedCartItem);


    }

}
