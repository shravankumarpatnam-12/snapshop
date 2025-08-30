package com.ecommerce.snapshop.controller;

import com.ecommerce.snapshop.model.Cart;
import com.ecommerce.snapshop.payload.CartDTO;
import com.ecommerce.snapshop.repository.CartRepository;
import com.ecommerce.snapshop.service.CartService;
//import com.ecommerce.snapshop.util.AuthUtil;
import com.ecommerce.snapshop.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {
    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @PostMapping("/cart/product/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(Long productId,
                                                    int quantity){
        CartDTO cartDTO = cartService.addProductToCart(productId,quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);

    }

    @GetMapping("/carts")
    public  ResponseEntity<List<CartDTO>> getAllCart(){
        List<CartDTO> cartDTOS= cartService.getAllCarts();
        return new ResponseEntity<List<CartDTO>>(cartDTOS,HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCartById(Long id){
        String emailId= authUtil.loggedInEmail();
         Cart cart= cartRepository.fingByCartEmail(emailId);
         Long cartId=cart.getCartId();
        CartDTO cartDTO=cartService.getCart(emailId,cartId);
        return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.FOUND);

  }
}
