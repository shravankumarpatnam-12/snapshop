package com.ecommerce.snapshop.repository;

import com.ecommerce.snapshop.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository  extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c where c.user.email=?1")
    Cart fingByCartEmail(String email);

    @Query("SELECT c FROM Cart c where c.user.email=?1 and c.cartId=?2")
    Cart fingByCartEmailAndCartId(String emailId, Long cartId);
}
