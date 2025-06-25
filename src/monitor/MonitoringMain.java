package monitor;

import market.MarketSimulator;
import model.Holding;
import model.Stock;
import strategy.Reservation;
import system.MessageBox;
import user.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MonitoringMain
{
    public static MarketSimulator marketSimulator = new MarketSimulator();

    public static SelectedMenu selectedMenu;

    public static UserData user;

    public static void main(String[] args) throws Exception
    {
        user = new UserData();

        // 시뮬레이터를 별도 스레드에서 실행
        Thread simulatorThread = new Thread(marketSimulator::openMarket);
        simulatorThread.start();

        selectedMenu = SelectedMenu.MainMenu;
        showMenu(marketSimulator);
    }

    // 주가 실시간 모니터링 메뉴
    private static void runMonitoringMode(MarketSimulator marketSimulator) throws Exception
    {
        Thread monitorThread = new Thread(() ->
        {
            try {
                boolean isShownMessage = false;
                while (!Thread.currentThread().isInterrupted())
                {
                    if (marketSimulator.isOpenned)
                    {
                        isShownMessage = false;
                        marketSimulator.printMarket();

                        System.out.print("\n(엔터를 누르면 종료)\n");
                    }

                    else if (!isShownMessage)
                    {
                        isShownMessage = true;
                        System.out.print("\n아직 개장 전입니다. 개장 후 자동으로 변동사항이 표시됩니다.\n");

                        MessageBox.showMenuMessage();
                    }


                    Thread.sleep(1000);
                }
            }
            catch (InterruptedException ignored)
            {
            }
        });

        monitorThread.start();

        // 엔터 입력 대기
        System.in.read();

        monitorThread.interrupt();
        monitorThread.join(); // 쓰레드 종료 대기
        selectedMenu = SelectedMenu.MainMenu;
    }

    // 메인 메뉴
    private static void showMenu(MarketSimulator marketSimulator) throws Exception
    {
        Scanner scanner = new Scanner(System.in);

        while (true)
        {
            MessageBox.showMenuMessage();

            int menuSelector;
            try
            {
                menuSelector = Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println("숫자를 입력해주세요.");
                continue;
            }

            switch (menuSelector)
            {
                case 0:
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                    break;
                case 1:
                    selectedMenu = SelectedMenu.Monitoring;
                    runMonitoringMode(marketSimulator);
                    break;
                case 2:
                    selectedMenu = SelectedMenu.TradeMainMenu;
                    showTrade(marketSimulator);
                    break;
                case 3:
                    selectedMenu = SelectedMenu.BookingMainMenu;
                    showBooking(marketSimulator);
                    break;
                case 4:
                    selectedMenu = SelectedMenu.MyPage;
                    showMyPage(marketSimulator);
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }

    // 거래 메뉴
    private static void showTrade(MarketSimulator marketSimulator)
    {
        Scanner scanner = new Scanner(System.in);

        while (true)
        {
            // 시스템 메시지 출력
            MessageBox.showMenuMessage();

            int menuSelector;
            try
            {
                menuSelector = Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println("숫자를 입력해주세요.");
                continue;
            }

            switch (menuSelector)
            {
                case 0:
                    selectedMenu = SelectedMenu.MainMenu;
                    return;
                case 1:
                    selectedMenu = SelectedMenu.TradeBuy;
                    showTradeBuy(marketSimulator);
                    break;
                case 2:
                    selectedMenu = SelectedMenu.TradeSell;
                    showTradeSell(marketSimulator);
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }

    private static void showTradeBuy(MarketSimulator marketSimulator)
    {
        Scanner scanner = new Scanner(System.in);

        int currentPage = 1;
        while (true)
        {
            MessageBox.showBuyPage(marketSimulator, currentPage);

            String input = scanner.nextLine().trim();

            switch (input)
            {
                case "0":
                    selectedMenu = SelectedMenu.TradeMainMenu;
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
                    int totalPages = (int) Math.ceil((double) marketSimulator.getMarketData().size() / 6);
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
                    int selection = Integer.parseInt(input);
                    int index = (currentPage - 1) * 6 + (selection - 1);
                    List<Stock> marketData = marketSimulator.getMarketData();

                    if (index >= marketData.size())
                    {
                        System.out.println("해당 번호에 주식이 없습니다.");
                        break;
                    }

                    Stock selectedStock = marketData.get(index);
                    // 👉 여기에 매수 로직 또는 매수 메뉴로 연결
                    System.out.println("\n[" + selectedStock.getName() + "] 주식을 선택하셨습니다.");

                    System.out.printf("%s의 매수 수량을 입력하세요:\n> ", selectedStock.getName());
                    int buyQuantity = Integer.parseInt(scanner.nextLine());
                    marketSimulator.buyStock(selectedStock, buyQuantity);
                    // 예시로만 출력하고 원래는 showBuyStockMenu(selectedStock); 이런 함수로 연결

                    break;
                default:
                    System.out.println("잘못된 입력입니다. 0, 1~6, 8, 9 중 선택해주세요.");
                    break;
            }
        }
    }

    private static void showTradeSell(MarketSimulator marketSimulator)
    {
        Scanner scanner = new Scanner(System.in);

        int currentPage = 1;
        while (true)
        {
            MessageBox.showSellPage(marketSimulator, currentPage);
            String input = scanner.nextLine();

            switch (input)
            {
                case "0":
                    selectedMenu = SelectedMenu.TradeMainMenu;
                    MessageBox.currentPage = -1;
                    return;
                case "8":
                    currentPage--;
                    break;
                case "9":
                    currentPage++;
                    break;
                default:
                    try
                    {
                        int selected = Integer.parseInt(input);
                        int index = (currentPage - 1) * 6 + (selected - 1);
                        List<Map.Entry<Stock, Holding>> holdings = new ArrayList<>(user.stockData.entrySet());

                        if (index >= 0 && index < holdings.size())
                        {
                            Stock stock = holdings.get(index).getKey();
                            Holding holding = holdings.get(index).getValue();

                            System.out.printf("%s의 매도 수량을 입력하세요 (보유 수량: %d):\n> ", stock.getName(), holding.getQuantity());
                            int sellQuantity = Integer.parseInt(scanner.nextLine());

                            if (sellQuantity <= 0 || sellQuantity > holding.getQuantity())
                            {
                                System.out.println("잘못된 수량입니다.");
                                break;
                            }

                            double proceeds = stock.getPrice() * sellQuantity;
                            holding.setQuantity(holding.getQuantity() - sellQuantity);
                            user.deposit += proceeds;

                            if (holding.getQuantity() == 0)
                                user.stockData.remove(stock);

                            System.out.printf("%.2f원을 획득했습니다.\n", proceeds);
                        }
                        else
                        {
                            System.out.println("잘못된 선택입니다.");
                        }
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("숫자를 입력해주세요.");
                    }
                    break;
            }
        }
    }

    // 예약 메뉴
    private static void showBooking(MarketSimulator marketSimulator)
    {
        Scanner scanner = new Scanner(System.in);

        while (true)
        {
            MessageBox.showMenuMessage();

            int menuSelector;
            try
            {
                menuSelector = Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println("숫자를 입력해주세요.");
                continue;
            }

            switch (menuSelector)
            {
                case 0:
                    selectedMenu = SelectedMenu.MainMenu;
                    return;
                case 1:
                    selectedMenu = SelectedMenu.BookingBuy;
                    showBookingBuy(marketSimulator);
                    break;
                case 2:
                    selectedMenu = SelectedMenu.BookingSell;
                    showBookingSell(marketSimulator);
                    break;
                case 3:
                    showBookingList(marketSimulator);
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }

    private static void showBookingBuy(MarketSimulator marketSimulator) {
        Scanner scanner = new Scanner(System.in);
        List<Stock> stocks = marketSimulator.getMarketData();

        System.out.println("\n매수 예약할 주식 선택 (0: 뒤로가기)");
        for (int i = 0; i < stocks.size(); i++) {
            System.out.printf("%d. %s | 현재가: %.2f원\n", i + 1, stocks.get(i).getName(), stocks.get(i).getPrice());
        }

        System.out.print("> ");
        int select = Integer.parseInt(scanner.nextLine());
        if (select == 0) {
            selectedMenu = SelectedMenu.BookingMainMenu;
            return;
        }

        if (select < 1 || select > stocks.size()) {
            System.out.println("잘못된 선택입니다.");
            return;
        }

        Stock selectedStock = stocks.get(select - 1);

        System.out.print("예약 매수 수량 입력: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        System.out.print("매수 희망가 입력: ");
        double targetPrice = Double.parseDouble(scanner.nextLine());

        Reservation reservation = new Reservation(Reservation.Type.BUY, selectedStock, targetPrice, quantity);
        MonitoringMain.user.reservations.add(reservation);
        System.out.println("✅ 매수 예약이 등록되었습니다.");
    }


    private static void showBookingSell(MarketSimulator marketSimulator)
    {
        Scanner scanner = new Scanner(System.in);

        List<Map.Entry<Stock, Holding>> holdings = MonitoringMain.user.stockData.entrySet()
                .stream().filter(e -> e.getValue().getQuantity() > 0).toList();

        if (holdings.isEmpty()) {
            System.out.println("보유한 주식이 없습니다.");
            return;
        }

        System.out.println("\n📤 매도 예약할 주식 선택 (0: 뒤로가기)");
        for (int i = 0; i < holdings.size(); i++) {
            Stock stock = holdings.get(i).getKey();
            System.out.printf("%d. %s | 보유량: %d주 | 현재가: %.2f원\n", i + 1, stock.getName(), holdings.get(i).getValue().getQuantity(), stock.getPrice());
        }

        System.out.print("> ");
        int select = Integer.parseInt(scanner.nextLine());
        if (select == 0) {
            selectedMenu = SelectedMenu.BookingMainMenu;
            return;
        }

        if (select < 1 || select > holdings.size()) {
            System.out.println("잘못된 선택입니다.");
            return;
        }

        Stock selectedStock = holdings.get(select - 1).getKey();

        System.out.print("예약 매도 수량 입력: ");
        int quantity = Integer.parseInt(scanner.nextLine());

        System.out.print("매도 희망가 입력: ");
        double targetPrice = Double.parseDouble(scanner.nextLine());

        Reservation reservation = new Reservation(Reservation.Type.SELL, selectedStock, targetPrice, quantity);
        MonitoringMain.user.reservations.add(reservation);
        System.out.println("✅ 매도 예약이 등록되었습니다.");
    }

    private static void showBookingList(MarketSimulator marketSimulator) {
        Scanner scanner = new Scanner(System.in);
        List<Reservation> reservations = MonitoringMain.user.reservations;

        if (reservations.isEmpty()) {
            System.out.println("\n📭 현재 등록된 예약이 없습니다.");
            return;
        }

        while (true) {
            System.out.println("\n📌 예약 목록:");
            for (int i = 0; i < reservations.size(); i++) {
                Reservation r = reservations.get(i);
                System.out.printf("%d. [%s] %s - 수량: %d | 목표가: %.2f원\n",
                        i + 1,
                        r.getType(),
                        r.getStock().getName(),
                        r.getQuantity(),
                        r.getTargetPrice());
            }

            System.out.println("\n0. 뒤로가기");
            System.out.print("취소할 예약 번호를 입력하세요: ");

            String input = scanner.nextLine().trim();

            if (input.equals("0")) {
                return;
            }

            try {
                int index = Integer.parseInt(input) - 1;
                if (index < 0 || index >= reservations.size()) {
                    System.out.println("❌ 잘못된 번호입니다.");
                } else {
                    Reservation removed = reservations.remove(index);
                    System.out.printf("✅ [%s] %s 예약이 취소되었습니다.\n",
                            removed.getType(), removed.getStock().getName());
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
    }



    private static void showMyPage(MarketSimulator marketSimulator)
    {
        Scanner scanner = new Scanner(System.in);

        while (true)
        {
            MessageBox.showMenuMessage();

            int menuSelector;
            try
            {
                menuSelector = Integer.parseInt(scanner.nextLine());
            }
            catch (NumberFormatException e)
            {
                System.out.println("숫자를 입력해주세요.");
                continue;
            }

            switch (menuSelector)
            {
                case 0:
                    selectedMenu = SelectedMenu.MainMenu;
                    return;
                case 1:
                    MessageBox.showAccount(marketSimulator);
                    break;
                case 2:
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }
}

