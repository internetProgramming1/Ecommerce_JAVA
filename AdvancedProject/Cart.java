package Ecommerce_JAVA.AdvancedProject;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Cart {
    private List<CartItem> items = new ArrayList<>();

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

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public void addToCart(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            throw new IllegalArgumentException("Product cannot be null and quantity must be positive");
        }

        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        items.add(new CartItem(product, quantity));
    }

    public void removeFromCart(Product product) {
        if (product == null)
            return;
        items.removeIf(item -> item.getProduct().getId() == product.getId());
    }

    public void updateQuantity(Product product, int newQuantity) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        if (newQuantity <= 0) {
            removeFromCart(product);
            return;
        }

        for (CartItem item : items) {
            if (item.getProduct().getId() == product.getId()) {
                item.setQuantity(newQuantity);
                return;
            }
        }

        throw new IllegalArgumentException("Product not found in cart");
    }

    public void clear() {
        items.clear();
    }

    public List<CartItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public int getTotalItems() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public boolean containsProduct(Product product) {
        if (product == null)
            return false;
        return items.stream()
                .anyMatch(item -> item.getProduct().getId() == product.getId());
    }

    public int getProductQuantity(Product product) {
        if (product == null)
            return 0;
        return items.stream()
                .filter(item -> item.getProduct().getId() == product.getId())
                .findFirst()
                .map(CartItem::getQuantity)
                .orElse(0);
    }
}