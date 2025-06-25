package model;

public class Stock
{
    private String name;
    private double price;
    private double lastPrice;

    public Stock(String name, double price)
    {
        this.name = name;
        this.price = price;
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
