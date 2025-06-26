package model;

import java.lang.reflect.Parameter;

public class Stock
{
    private String name;
    private double price;
    private double lastPrice;
    private double primePrice;

    public Stock(String name, double price)
    {
        this.name = name;
        this.price = price;
        this.primePrice = price;
        this.lastPrice = 0;
    }

    public String getName()
    {
        return name;
    }

    public double getPrice()
    {
        return price;
    }
    public double getPrimePrice() { return primePrice; }

    public void setPrice(double price)
    {
        lastPrice = this.price;
        this.price = price;
    }

    @Override
    public String toString()
    {
        return name + ": " + price + "원 / (변동 전: " + lastPrice + ") / (변동가: " + (price - lastPrice) + ")";

    }
}
