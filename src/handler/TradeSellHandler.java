package handler;

import io.InputProvider;
import io.OutputRenderer;
import main.Main;
import main.SelectedMenu;
import market.MarketSimulator;
import model.Holding;
import model.Stock;
import system.MessageBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TradeSellHandler implements MenuHandler
{
    private final MarketSimulator simulator;
    private final InputProvider input;
    private final OutputRenderer output;

    public TradeSellHandler(MarketSimulator simulator, InputProvider input, OutputRenderer output)
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
            // 보유중인 주식 로드
            List<Map.Entry<Stock, Holding>> holdings = new ArrayList<>(Main.user.stockData.entrySet());
            int totalPages = (int) Math.ceil((double) holdings.size() / PAGE_SIZE);

            showSellPage(simulator, currentPage);
            String command = input.readLine().trim();

            switch (command)
            {
                case "0":
                    Main.selectedMenu = SelectedMenu.TradeMainMenu;
                    MessageBox.currentPage = -1;
                    return;
                case "8":
                    if (currentPage > 1) currentPage--;
                    else System.out.println("첫번째 페이지입니다.");
                    break;
                case "9":
                    if (currentPage < totalPages) currentPage++;
                    else System.out.println("마지막 페이지입니다.");
                    break;
                default:
                    try
                    {
                        int selected = Integer.parseInt(command);
                        int index = (currentPage - 1) * 6 + (selected - 1);

                        if (index < 0 || index >= holdings.size())
                        {
                            output.println("잘못된 번호입니다.");
                            continue;
                        }

                        Stock stock = holdings.get(index).getKey();
                        Holding holding = holdings.get(index).getValue();

                        System.out.printf("%s의 매도 수량을 입력하세요 (보유 수량: %d):\n> ", stock.getName(), holding.getQuantity());
                        int sellQuantity = Integer.parseInt(input.readLine().trim());

                        if (sellQuantity <= 0 || sellQuantity > holding.getQuantity())
                        {
                            System.out.println("잘못된 수량입니다.");
                            break;
                        }

                        simulator.sellStock(stock, sellQuantity);

                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("숫자를 입력해주세요.");
                    }
                    break;
            }
        }
    }

    public static void showSellPage(MarketSimulator marketSimulator, int page)
    {
        final int PAGE_SIZE = 6;
        List<Map.Entry<Stock, Holding>> holdings = new ArrayList<>(Main.user.stockData.entrySet());
        int totalHoldings = holdings.size();
        int totalPages = (int) Math.ceil((double) totalHoldings / PAGE_SIZE);

        if (totalHoldings == 0) {
            System.out.println("\n보유한 주식이 없습니다.");
            System.out.println("0. 뒤로가기");
            System.out.print("> ");
            Main.selectedMenu = SelectedMenu.TradeMainMenu;
            return;
        }

        if (page < 1 || page > totalPages) {
            System.out.println("잘못된 페이지입니다.");
            return;
        }

        int startIndex = (page - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, totalHoldings);

        System.out.println("\n보유 주식 목록 (" + page + "/" + totalPages + ")");
        for (int i = startIndex; i < endIndex; i++) {
            Stock stock = holdings.get(i).getKey();
            Holding holding = holdings.get(i).getValue();
            System.out.printf("%d. %s | 수량: %d | 평균 매수가: %.2f | 현재가: %.2f\n",
                    i - startIndex + 1, stock.getName(), holding.getQuantity(),
                    holding.getAveragePrice(), stock.getPrice());
        }

        System.out.println("\n선택: 1 ~ " + (endIndex - startIndex));
        if (page > 1) System.out.println("8. 이전 페이지");
        if (page < totalPages) System.out.println("9. 다음 페이지");
        System.out.println("0. 뒤로가기");
        System.out.print("> ");
    }


}
