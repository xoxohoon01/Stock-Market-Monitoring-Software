package handler;

import io.InputProvider;
import io.OutputRenderer;
import main.MonitoringMain;
import main.SelectedMenu;
import market.MarketSimulator;
import model.Holding;
import model.Stock;
import strategy.Reservation;

import java.util.List;
import java.util.Map;

public class BookingSellHandler implements MenuHandler
{
    private final MarketSimulator simulator;
    private final InputProvider input;
    private final OutputRenderer output;

    public BookingSellHandler(MarketSimulator simulator, InputProvider input, OutputRenderer output)
    {
        this.simulator = simulator;
        this.input = input;
        this.output = output;
    }

    @Override
    public void handle() throws Exception
    {
        List<Map.Entry<Stock, Holding>> holdings = MonitoringMain.user.stockData.entrySet()
                .stream().filter(e -> e.getValue().getQuantity() > 0).toList();

        if (holdings.isEmpty()) {
            System.out.print("\n보유한 주식이 없습니다.\n");
            MonitoringMain.selectedMenu = SelectedMenu.BookingMainMenu;
            return;
        }

        System.out.print("\n📤 매도 예약할 주식 선택 (0: 뒤로가기)\n");
        for (int i = 0; i < holdings.size(); i++) {
            Stock stock = holdings.get(i).getKey();
            System.out.printf("%d. %s | 보유량: %d주 | 현재가: %.2f원\n", i + 1, stock.getName(), holdings.get(i).getValue().getQuantity(), stock.getPrice());
        }

        System.out.print("> ");
        int select = Integer.parseInt(input.readLine().trim());
        if (select == 0) {
            MonitoringMain.selectedMenu = SelectedMenu.BookingMainMenu;
            return;
        }

        if (select < 1 || select > holdings.size()) {
            System.out.println("잘못된 선택입니다.");
            return;
        }

        Stock selectedStock = holdings.get(select - 1).getKey();

        System.out.print("예약 매도 수량 입력: ");
        int quantity = Integer.parseInt(input.readLine().trim());

        System.out.print("매도 희망가 입력: ");
        double targetPrice = Double.parseDouble(input.readLine().trim());

        Reservation reservation = new Reservation(Reservation.Type.SELL, selectedStock, targetPrice, quantity);
        MonitoringMain.user.reservations.add(reservation);
        System.out.println("✅ 매도 예약이 등록되었습니다.");
    }
}
