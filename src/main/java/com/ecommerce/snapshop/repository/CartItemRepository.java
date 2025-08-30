package com.ecommerce.snapshop.repository;

import com.ecommerce.snapshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci where ci.cart.cartId=?1 and ci.product.id=?2")
    CartItem findCartItemByProductIdAndCartId(Long cartId, Long productId);
}
