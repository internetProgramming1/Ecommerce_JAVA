package Ecommerce_JAVA.AdvancedProject;

import java.util.Date;

public class Order {
    private int id;
    private String customerName;
    private Date orderDate;
    private double amount;
    private String status;

    public Order(int id, String customerName, Date orderDate, double amount, String status) {
        this.id = id;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.amount = amount;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public double getAmount() {
        return amount;
    }

    public String getStatus() {
        return status;
    }
}