package com.manageinfo.service;

import com.manageinfo.dto.CartDTO;
import com.manageinfo.dto.CartItemDTO;
import com.manageinfo.exception.ResourceNotFoundException;
import com.manageinfo.model.Cart;
import com.manageinfo.model.CartItem;
import com.manageinfo.model.Product;
import com.manageinfo.model.User;
import com.manageinfo.repository.CartRepository;
import com.manageinfo.repository.ProductRepository;
import com.manageinfo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Cart getOrCreateCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Optional<Cart> existingCart = cartRepository.findByUser(user);

        if (existingCart.isPresent()) {
            return existingCart.get();
        } else {
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        }
    }

    @Transactional
    public void addItemToCart(Long userId, int productId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItem existingItem = null;
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getId() == productId) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.addItem(newItem);
        }
        cartRepository.save(cart);
    }

    @Transactional
    public void removeItemFromCart(Long userId, int productId) {
        Cart cart = getOrCreateCart(userId);
        List<CartItem> items = cart.getItems();

        CartItem itemToRemove = null;
        for (CartItem item : items) {
            if (item.getProduct().getId() == productId) {
                itemToRemove = item;
                break;
            }
        }

        if (itemToRemove != null) {
            cart.removeItem(itemToRemove);
        }

        cartRepository.save(cart);
    }

    public CartDTO getCartDetails(Long userId) {
        Cart cart = getOrCreateCart(userId);
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());

        List<CartItemDTO> itemDtos = new ArrayList<>();
        double total = 0;

        for (CartItem item : cart.getItems()) {
            CartItemDTO itemDto = new CartItemDTO();
            itemDto.setId(item.getId());
            itemDto.setProductId(item.getProduct().getId());
            itemDto.setProductName(item.getProduct().getName());
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getProduct().getPrice());

            itemDtos.add(itemDto);
            total += item.getProduct().getPrice() * item.getQuantity();
        }

        dto.setItems(itemDtos);
        dto.setTotalAmount(total);

        return dto;
    }

    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
