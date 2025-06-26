package handler;

import main.MonitoringMain;
import main.SelectedMenu;
import market.MarketSimulator;
import io.InputProvider;
import io.OutputRenderer;
import model.Holding;
import model.Stock;
import system.MessageBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TradeBuyHandler implements MenuHandler
{
    private final MarketSimulator simulator;
    private final InputProvider input;
    private final OutputRenderer output;

    public TradeBuyHandler(MarketSimulator simulator, InputProvider input, OutputRenderer output)
    {
        this.simulator = simulator;
        this.input = input;
        this.output = output;
    }

    @Override
    public void handle() throws Exception
    {
        int currentPage = 1;
        final int PAGE_SIZE = 6;

        while (true)
        {

            showBuyPage(simulator, currentPage);

            // 상장 중인 주식 로드
            int totalPages = (int) Math.ceil((double) simulator.getMarketData().size() / 6);

            String command = input.readLine().trim();

            switch (command)
            {
                case "0":
                    MonitoringMain.selectedMenu = SelectedMenu.TradeMainMenu;
                    MessageBox.currentPage = -1;
                    return;
                case "8":
                    if (currentPage > 1)
                    {
                        currentPage--;
                    }
                    else
                    {
                        System.out.println("첫 페이지입니다.");
                    }
                    break;
                case "9":
                    if (currentPage < totalPages)
                    {
                        currentPage++;
                    }
                    else
                    {
                        System.out.println("마지막 페이지입니다.");
                    }
                    break;
                case "1": case "2": case "3": case "4": case "5": case "6":
                int selection = Integer.parseInt(command);
                int index = (currentPage - 1) * 6 + (selection - 1);
                List<Stock> marketData = simulator.getMarketData();

                if (index >= marketData.size())
                {
                    System.out.println("해당 번호에 주식이 없습니다.");
                    break;
                }

                Stock selectedStock = marketData.get(index);
                // 👉 여기에 매수 로직 또는 매수 메뉴로 연결
                System.out.println("\n[" + selectedStock.getName() + "] 주식을 선택하셨습니다.");

                System.out.printf("%s의 매수 수량을 입력하세요:\n> ", selectedStock.getName());
                int buyQuantity = Integer.parseInt(input.readLine().trim());
                simulator.buyStock(selectedStock, buyQuantity);
                // 예시로만 출력하고 원래는 showBuyStockMenu(selectedStock); 이런 함수로 연결

                break;
                default:
                    System.out.println("잘못된 입력입니다. 0, 1~6, 8, 9 중 선택해주세요.");
                    break;
            }
        }
    }

    public static void showBuyPage(MarketSimulator marketSimulator, int page)
    {
        final int PAGE_SIZE = 6;
        List<Stock> marketData = marketSimulator.getMarketData();
        int totalStocks = marketData.size();
        int totalPages = (int) Math.ceil((double) totalStocks / PAGE_SIZE);

        if (page < 1 || page > totalPages)
        {
            System.out.println("잘못된 페이지 번호입니다. (1 ~ " + totalPages + ")");
            MonitoringMain.selectedMenu = SelectedMenu.TradeMainMenu;
            return;
        }

        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, totalStocks);

        System.out.println("\n📃 현재 상장된 주식 목록 (" + page + "/" + totalPages + ")");
        for (int i = startIndex; i < endIndex; i++)
        {
            Stock stock = marketData.get(i);
            int displayNumber = i - startIndex + 1; // 1~6
            System.out.printf("%d. %s | 가격: %.2f원\n", displayNumber, stock.getName(), stock.getPrice());
        }

        System.out.println("\n0. 뒤로가기");
        System.out.println("8. 이전 페이지 | 9. 다음 페이지");
        System.out.println("1~6: 해당 번호 주식 선택");
        System.out.print("> ");
    }
}
