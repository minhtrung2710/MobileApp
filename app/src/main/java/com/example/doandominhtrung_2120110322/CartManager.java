package com.example.doandominhtrung_2120110322;

import android.util.Log;
import java.util.ArrayList;

public class CartManager {
    private static CartManager instance;
    private ArrayList<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Product product, int quantity) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getName().equals(product.getName())) {
                item.increaseQuantity(quantity); // Nếu đã có, tăng số lượng
                return;
            }
        }
        cartItems.add(new CartItem(product, quantity)); // Nếu chưa có, thêm mới
    }

    public void removeFromCart(Product product) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getProduct().getName().equals(product.getName())) {
                cartItems.remove(i);
                return;
            }
        }
        Log.w("CartManager", "Product not found in cart: " + product.getName());
    }

    public ArrayList<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public void clearCart() {
        cartItems.clear();
    }
}
