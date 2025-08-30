package com.ecommerce.snapshop.service;

import com.ecommerce.snapshop.exceptions.APIException;
import com.ecommerce.snapshop.exceptions.ResourceNotFoundException;
import com.ecommerce.snapshop.model.Cart;
import com.ecommerce.snapshop.model.CartItem;
import com.ecommerce.snapshop.model.Product;
import com.ecommerce.snapshop.payload.CartDTO;
import com.ecommerce.snapshop.payload.ProductDTORequest;
import com.ecommerce.snapshop.repository.CartItemRepository;
import com.ecommerce.snapshop.repository.CartRepository;
import com.ecommerce.snapshop.repository.ProductRepository;
import com.ecommerce.snapshop.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDTO addProductToCart(Long productId, int quantity) {
        Cart cart = createCart();
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product","product Id",productId));
        CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(),productId);
        if(cartItem!=null){
            throw new APIException("Product "+product.getName()+" already exists");
        }
        if(product.getQuantity()==0){
            throw new APIException(product.getProductPrice()+" is not available");
        }
        if(product.getQuantity()<quantity){
            throw new APIException("Please make an order of "+product.getName()
                    +"less than "+product.getQuantity());
        }

        CartItem cartItemNew=new CartItem();
        cartItemNew.setProduct(product);
        cartItemNew.setCart(cart);
        cartItemNew.setQuantity(quantity);
        cartItem.setProductPrice(product.getProductPrice());
        cartItem.setDiscount(product.getDiscount());
        cartItemRepository.save(cartItemNew);

        product.setQuantity(product.getQuantity()-quantity);
        productRepository.save(product);

        cart.setTotalPrice(cart.getTotalPrice()+(quantity*product.getProductPrice()));
        cartRepository.save(cart);

        List<CartItem> cartItemList=cart.getItems();
        Stream<ProductDTORequest> productDTORequestStream=cartItemList.stream().map(
            item -> {
                ProductDTORequest productDTOMap = modelMapper.map(cartItem.getProduct(), ProductDTORequest.class);
                productDTOMap.setQuantity(cartItem.getQuantity());
                return productDTOMap;
            });
        CartDTO cartDTO = modelMapper.map(cartItemList,CartDTO.class);
        cartDTO.setProductDTORequestList(productDTORequestStream.toList());

    return cartDTO;
    }

    @Override
    public List<CartDTO> getAllCarts() {
        List<Cart> carts=cartRepository.findAll();
        if(carts.size()==0){
            throw new APIException("No cart exists");
        }
        List<CartDTO> cartDTOS=carts.stream().map(
                cart ->{
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
                    List<ProductDTORequest> productDTORequests=cart.getItems()
                            .stream().map(product -> modelMapper.map(product.getProduct(),ProductDTORequest.class))
                            .collect(Collectors.toList());
                    cartDTO.setProductDTORequestList(productDTORequests);
                    return cartDTO;
                }).collect(Collectors.toList());
        return cartDTOS;
    }

    @Override
    public CartDTO getCart(String emailId, Long cartId) {
        Cart cart=cartRepository.fingByCartEmailAndCartId(emailId,cartId);
        if(cart==null){
            throw new ResourceNotFoundException("Cart","cartId",cartId);
        }
        CartDTO cartDTO=modelMapper.map(cart,CartDTO.class);
        cart.getItems().forEach(item->item.getProduct().setQuantity(item.getQuantity()));
        List<ProductDTORequest> productDTORequests=cart.getItems()
                .stream().map(product->modelMapper.map(product.getProduct(),ProductDTORequest.class))
                .collect(Collectors.toList());
        cartDTO.setProductDTORequestList(productDTORequests);
        return cartDTO;
    }

    private Cart createCart() {
        Cart cart=cartRepository.fingByCartEmail(authUtil.loggedInEmail());
        if(cart!=null){
            return cart;
        }
        Cart newCart= new Cart();
        newCart.setTotalPrice(0.0);
        newCart.setUser(authUtil.loggedInUser());
        return cartRepository.save(newCart);
    }
}
