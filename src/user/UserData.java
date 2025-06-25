package user;

import model.Holding;
import model.Stock;
import strategy.Reservation;

import java.util.*;

public class UserData
{
    public Map<Stock, Holding> stockData;
    public double deposit;
    public List<Reservation> reservations;  // 예약 주문 리스트

    public UserData()
    {
        deposit = 1000000;
        stockData = new HashMap<>();
        reservations = new ArrayList<>();
    }
}
