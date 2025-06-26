package handler;

import io.InputProvider;
import io.OutputRenderer;
import main.Main;
import main.SelectedMenu;
import market.MarketSimulator;
import model.Stock;
import strategy.Reservation;

import java.util.List;

public class BookingBuyHandler implements MenuHandler
{
    private final MarketSimulator simulator;
    private final InputProvider input;
    private final OutputRenderer output;

    public BookingBuyHandler(MarketSimulator simulator, InputProvider input, OutputRenderer output)
    {
        this.simulator = simulator;
        this.input = input;
        this.output = output;
    }

    @Override
    public void handle() throws Exception
    {
        List<Stock> stocks = simulator.getMarketData();

        System.out.println("\n매수 예약할 주식 선택 (0: 뒤로가기)");
        for (int i = 0; i < stocks.size(); i++) {
            System.out.printf("%d. %s | 현재가: %.2f원\n", i + 1, stocks.get(i).getName(), stocks.get(i).getPrice());
        }

        System.out.print("> ");
        int select = Integer.parseInt(input.readLine().trim());
        if (select == 0) {
            Main.selectedMenu = SelectedMenu.BookingMainMenu;
            return;
        }

        if (select < 1 || select > stocks.size()) {
            System.out.println("잘못된 선택입니다.");
            return;
        }

        Stock selectedStock = stocks.get(select - 1);

        System.out.print("예약 매수 수량 입력: ");
        int quantity = Integer.parseInt(input.readLine().trim());

        System.out.print("매수 희망가 입력: ");
        double targetPrice = Double.parseDouble(input.readLine().trim());

        Reservation reservation = new Reservation(Reservation.Type.BUY, selectedStock, targetPrice, quantity);
        Main.user.reservations.add(reservation);
        System.out.println("✅ 매수 예약이 등록되었습니다.");
    }
}
