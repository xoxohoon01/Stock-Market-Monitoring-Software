package model;

import java.math.BigDecimal;

public class Holding {
    public Stock stock;
    public int quantity;
    public double averagePrice;

    public Holding(int quantity, double averagePrice) {
        this.quantity = quantity;
        this.averagePrice = averagePrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setQuantity(int newQuantity)
    {
        this.quantity = newQuantity;
    }

    public void setAveragePrice(double newAveragePrice)
    {
        this.averagePrice = newAveragePrice;
    }
}
