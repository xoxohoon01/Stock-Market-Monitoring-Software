package strategy;

import model.Stock;

public class Reservation
{
    public enum Type { BUY, SELL }

    private Type type;
    private Stock stock;
    private double targetPrice;
    private int quantity;

    public Reservation(Type type, Stock stock, double targetPrice, int quantity) {
        this.type = type;
        this.stock = stock;
        this.targetPrice = targetPrice;
        this.quantity = quantity;
    }

    public Type getType() { return type; }
    public Stock getStock() { return stock; }
    public double getTargetPrice() { return targetPrice; }
    public int getQuantity() { return quantity; }
}
