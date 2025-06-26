package system;

import market.MarketSimulator;
import model.Holding;
import model.Stock;
import main.Main;
import main.SelectedMenu;

import java.util.Map;

public class MessageBox
{
    public static int currentPage = -1; // 페이지 선택 화면 이용 시 기본 페이지, -1일 경우 첫번째 페이지부터 시작하도록 설계할 것
    public static void showMenuMessage()
    {
        switch (Main.selectedMenu)
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
                break;
            case SelectedMenu.TradeSell:
                if (currentPage == -1) currentPage = 0;
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
                System.out.print("> ");
                break;
            default:
                break;
        }
    }

    public static void showAccount(MarketSimulator marketSimulator)
    {
        for (Map.Entry<Stock, Holding> entry : Main.user.stockData.entrySet())
        {
            Stock stock = entry.getKey();
            Holding holding = entry.getValue();

            System.out.printf("\n[%s]\n현재 가격: %f원\n보유 수량: %d주\n평균 매수가: %f원\n", stock.getName(), stock.getPrice(), holding.getQuantity(), holding.getAveragePrice());
        }
        System.out.printf("\n보유금액: %f원\n", Main.user.deposit);
    }

}
