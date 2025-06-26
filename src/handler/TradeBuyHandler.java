package handler;

import io.InputProvider;
import io.OutputRenderer;
import main.MonitoringMain;
import main.SelectedMenu;
import market.MarketSimulator;
import model.Stock;
import system.MessageBox;

import java.util.List;

public class TradeBuyHandler implements MenuHandler {
    private final MarketSimulator simulator;
    private final InputProvider input;
    private final OutputRenderer output;

    public TradeBuyHandler(MarketSimulator simulator, InputProvider input, OutputRenderer output) {
        this.simulator = simulator;
        this.input = input;
        this.output = output;
    }

    @Override
    public void handle() throws Exception
    {
        List<Stock> marketData = simulator.getMarketData();

        while (true) {
            showAllStocks(marketData);

            output.print("구매할 주식 번호 선택 (0: 뒤로가기) > ");
            String command = input.readLine().trim();

            if (command.equals("0")) {
                MonitoringMain.selectedMenu = SelectedMenu.TradeMainMenu;
                return;
            }

            int index;
            try {
                index = Integer.parseInt(command) - 1;
                if (index < 0 || index >= marketData.size()) {
                    output.println("❌ 잘못된 번호입니다.");
                    continue;
                }
            } catch (NumberFormatException e) {
                output.println("❌ 숫자를 입력해주세요.");
                continue;
            }

            Stock selectedStock = marketData.get(index);
            output.printf("[%s] 주식을 선택하셨습니다.\n", selectedStock.getName());
            output.print("매수 수량 입력 > ");

            try {
                int quantity = Integer.parseInt(input.readLine().trim());
                simulator.buyStock(selectedStock, quantity);
            } catch (NumberFormatException e) {
                output.println("❌ 유효한 수량을 입력해주세요.");
            }
        }
    }

    private void showAllStocks(List<Stock> stocks) {
        output.println("\n📃 현재 상장된 주식 목록:");
        for (int i = 0; i < stocks.size(); i++) {
            Stock stock = stocks.get(i);
            output.printf("%d. %s | 가격: %.2f원\n", i + 1, stock.getName(), stock.getPrice());
        }
    }
}
