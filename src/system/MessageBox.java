package system;

import market.MarketSimulator;
import model.Holding;
import model.Stock;
import monitor.MonitoringMain;
import monitor.SelectedMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MessageBox
{
    public static int currentPage = -1; // 페이지 선택 화면 이용 시 기본 페이지, -1일 경우 첫번째 페이지부터 시작하도록 설계할 것
    public static void showMenuMessage()
    {
        switch (MonitoringMain.selectedMenu)
        {
            case SelectedMenu.MainMenu:
                System.out.println("\n명령을 입력하세요.");
                System.out.println("1. 실시간 주가 모니터링");
                System.out.println("2. 실시간 거래");
                System.out.println("3. 거래 예약");
                System.out.println("4. 마이 페이지");
                System.out.println("0. 종료");
                System.out.print("> ");
                break;
            case SelectedMenu.Monitoring:
                System.out.println("\n(엔터를 누르면 종료)");
                break;
            case SelectedMenu.TradeMainMenu:
                System.out.println("\n명령을 입력하세요.");
                System.out.println("0. 뒤로가기");
                System.out.println("1. 매수하기");
                System.out.println("2. 매도하기");
                System.out.print("> ");
                break;
            case SelectedMenu.TradeBuy:
                if (currentPage == -1) currentPage = 0;
                showBuyPage(MonitoringMain.marketSimulator, currentPage);
                break;
            case SelectedMenu.TradeSell:
                if (currentPage == -1) currentPage = 0;
                showSellPage(MonitoringMain.marketSimulator, currentPage);
                break;
            case SelectedMenu.BookingMainMenu:
                System.out.println("\n명령을 입력하세요.");
                System.out.println("0. 뒤로가기");
                System.out.println("1. 매수 예약");
                System.out.println("2. 매도 예약");
                System.out.println("3. 예약 확인");
                System.out.print("> ");
                break;
            case SelectedMenu.BookingBuy:
                System.out.println("\n명령을 입력하세요.");
                System.out.println("0. 뒤로가기");
                System.out.print("> ");
                break;
            case SelectedMenu.BookingSell:
                System.out.println("\n명령을 입력하세요.");
                System.out.println("0. 뒤로가기");
                System.out.print("> ");
                break;
            case SelectedMenu.MyPage:
                System.out.println("\n명령을 입력하세요.");
                System.out.println("0. 뒤로가기");
                System.out.println("1. 보유 자산");
                System.out.println("2. 분석");
                System.out.print("> ");
                break;
            default:
                break;
        }
    }

    public static void showAccount(MarketSimulator marketSimulator)
    {
        for (Map.Entry<Stock, Holding> entry : MonitoringMain.user.stockData.entrySet())
        {
            Stock stock = entry.getKey();
            Holding holding = entry.getValue();

            System.out.printf("\n[%s]\n현재 가격: %f원\n보유 수량: %d주\n평균 매수가: %f원\n", stock.getName(), stock.getPrice(), holding.getQuantity(), holding.getAveragePrice());
        }
        System.out.printf("\n보유금액: %f원\n", MonitoringMain.user.deposit);
    }

    public static void showBuyPage(MarketSimulator marketSimulator, int page)
    {
        currentPage = page;
        final int PAGE_SIZE = 6;
        List<Stock> marketData = marketSimulator.getMarketData();
        int totalStocks = marketData.size();
        int totalPages = (int) Math.ceil((double) totalStocks / PAGE_SIZE);

        if (page < 1 || page > totalPages)
        {
            System.out.println("잘못된 페이지 번호입니다. (1 ~ " + totalPages + ")");
            currentPage = -1;
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

    public static void showSellPage(MarketSimulator marketSimulator, int page)
    {
        currentPage = page;
        final int PAGE_SIZE = 6;
        List<Map.Entry<Stock, Holding>> holdings = new ArrayList<>(MonitoringMain.user.stockData.entrySet());
        int totalHoldings = holdings.size();
        int totalPages = (int) Math.ceil((double) totalHoldings / PAGE_SIZE);

        if (totalHoldings == 0) {
            System.out.println("\n보유한 주식이 없습니다.");
            System.out.println("0. 뒤로가기");
            System.out.print("> ");
            return;
        }

        if (page < 1 || page > totalPages) {
            System.out.println("잘못된 페이지입니다.");
            currentPage = -1;
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
