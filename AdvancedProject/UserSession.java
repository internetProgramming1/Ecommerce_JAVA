package Ecommerce_JAVA.AdvancedProject;

public class UserSession {
    private static UserSession instance;
    private boolean loggedIn;
    private String username;
    private Cart cart;

    private UserSession() {
        this.loggedIn = false;
        this.cart = new Cart();
    }

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void login(String username) {
        this.loggedIn = true;
        this.username = username;
        this.cart = new Cart(); // reset cart on login
    }

    public void logout() {
        this.loggedIn = false;
        this.username = null;
        this.cart.clear();
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getUsername() {
        return username;
    }

    public Cart getCart() {
        return cart;
    }
}
