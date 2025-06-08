package Ecommerce_JAVA.AdvancedProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    private Map<Product, Integer> items;

    public Cart() {
        items = new HashMap<>();
    }

    public void addToCart(Product product, int quantity) {
        if (product == null)
            return;

        if (items.containsKey(product)) {
            int currentQuantity = items.get(product);
            items.put(product, currentQuantity + quantity);
        } else {
            items.put(product, quantity);
        }

        product.decreaseStock(quantity);
    }

    public void removeFromCart(Product product) {
        if (product == null || !items.containsKey(product))
            return;

        // Restore stock when removing from cart
        int quantity = items.get(product);
        product.increaseStock(quantity);

        items.remove(product);
    }

    public void updateQuantity(Product product, int newQuantity) {
        if (product == null || !items.containsKey(product))
            return;

        int currentQuantity = items.get(product);
        int difference = newQuantity - currentQuantity;

        if (newQuantity <= 0) {
            removeFromCart(product);
            return;
        }

        // Check stock availability
        if (difference > 0 && product.getStockQuantity() < difference) {
            throw new IllegalArgumentException("Not enough stock available");
        }

        // Update quantity and adjust stock
        product.decreaseStock(difference);
        items.put(product, newQuantity);
    }

    public void clear() {
        // Restore all stock when clearing cart
        items.forEach((product, quantity) -> {
            product.increaseStock(quantity);
        });
        items.clear();
    }

    public List<CartItem> getItems() {
        List<CartItem> cartItems = new ArrayList<>();
        items.forEach((product, quantity) -> {
            cartItems.add(new CartItem(product, quantity));
        });
        return cartItems;
    }

    public double getTotal() {
        return items.entrySet().stream()
                .mapToDouble(e -> e.getKey().getPrice() * e.getValue())
                .sum();
    }

    public int getTotalItems() {
        return items.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    // Inner class to represent cart items
    public static class CartItem {
        private Product product;
        private int quantity;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}