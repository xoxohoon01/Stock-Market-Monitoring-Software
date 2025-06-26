package market;

import model.Holding;
import model.Stock;
import main.MonitoringMain;
import strategy.Reservation;
import system.MessageBox;

import java.util.*;

public class MarketSimulator
{
    public final List<Stock> marketData;
    private final Random random;
    public boolean isOpenned;

    // 시장 개장 및 폐장
    public void openMarket()
    {
        isOpenned = true;
        System.out.print("\n주식 시장이 개장되었습니다.\n");

        MessageBox.showMenuMessage();

        run();

    }
    public void closeMarket()
    {
        isOpenned = false;
        System.out.print("\n주식 시장이 폐장되었습니다.\n");
        System.out.print("\n10초 뒤, 다시 개장합니다.\n");

        MessageBox.showMenuMessage();
    }

    // 종목 생성 및 삭제
    public void createNewStock()
    {
        System.out.println("신규 상장");

        MessageBox.showMenuMessage();
    }
    public void deleteNewStock()
    {
        System.out.println("상장 폐지");

        MessageBox.showMenuMessage();
    }

    // 시뮬레이션 시작
    public void run()
    {
        for (int i = 0; i < 50; i++) { // 예시: 10번 반복
            simulatePriceChanges();
            checkReservations();
            sleep(500); // 0.5초 대기
        }

        closeMarket();
        sleep(10000); // 10초 대기
        openMarket();
    }

    // 주가 변동
    private void simulatePriceChanges()
    {
        for (Stock stock : marketData)
        {
            double changePercent = ((random.nextDouble() * 0.5) - 0.25) * 0.1;// -2.5% ~ +2.5%
            double newPrice = stock.getPrice() * (1 + changePercent);
            stock.setPrice(Math.round(newPrice)); // 반올림
        }
    }

    // 변동 대기
    private void sleep(long millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException ignored)
        {
        }
    }

    // 시장 변동사항 확인
    public void printMarket()
    {
        System.out.print("\033[H"); // 커서를 맨 위로 이동
        System.out.println("📊 현재 주가:");
        for (Stock stock : marketData)
        {
            System.out.println(stock);
        }
        System.out.println("--------------------------------------------------");
    }

    // 구매
    public static boolean buyStock(Stock stock, int quantity) {
        if (quantity <= 0) {
            System.out.println("잘못된 수량입니다.");
            return false;
        }

        double totalCost = stock.getPrice() * quantity;
        if (MonitoringMain.user.deposit < totalCost) {
            System.out.println("잔액이 부족합니다.");
            return false;
        }

        // 구매 처리
        MonitoringMain.user.deposit -= totalCost;
        MonitoringMain.user.stockData.compute(stock, (k, v) -> {
            if (v == null) {
                return new Holding(quantity, stock.getPrice());
            } else {
                int newQuantity = v.getQuantity() + quantity;
                double newAvgPrice = (v.getAveragePrice() * v.getQuantity() + stock.getPrice() * quantity) / newQuantity;
                v.setQuantity(newQuantity);
                v.setAveragePrice(newAvgPrice);
                return v;
            }
        });

        System.out.printf("%s 주식 %d주를 %.2f원에 구매했습니다. (총액: %.2f원)\n",
                stock.getName(), quantity, stock.getPrice(), totalCost);
        return true;
    }


    // 판매
    public boolean sellStock(Stock stock, int quantity)
    {
        Holding holding = MonitoringMain.user.stockData.get(stock);

        if (holding == null || holding.getQuantity() < quantity) {
            System.out.println("보유 수량이 부족합니다.");
            return false;
        }

        // 보유 수량 감소
        holding.setQuantity(holding.getQuantity() - quantity);

        // 예치금 증가
        double totalAmount = stock.getPrice() * quantity;
        MonitoringMain.user.deposit += totalAmount;

        // 수량이 0이면 제거
        if (holding.getQuantity() == 0) {
            MonitoringMain.user.stockData.remove(stock);
        }

        System.out.printf("%s 주식 %d주를 매도하였습니다. (총액: %.2f원)\n", stock.getName(), quantity, totalAmount);
        return true;
    }

    // 예약 대기
    private void checkReservations() {
        Iterator<Reservation> iter = MonitoringMain.user.reservations.iterator();

        while (iter.hasNext()) {
            Reservation r = iter.next();
            double currentPrice = r.getStock().getPrice();

            if (r.getType() == Reservation.Type.BUY && currentPrice <= r.getTargetPrice()) {
                buyStock(r.getStock(), r.getQuantity()); // 예약된 수량만큼 매수
                iter.remove();
            } else if (r.getType() == Reservation.Type.SELL && currentPrice >= r.getTargetPrice()) {
                sellStock(r.getStock(), r.getQuantity()); // 예약된 수량만큼 매도
                iter.remove();
            }
        }
    }


    // 초기화
    private void initialize()
    {
        marketData.add(new Stock("삼성", 20000));
        marketData.add(new Stock("카카오", 17500));
        marketData.add(new Stock("대전대학교", 10000));
    }

    // 생성자
    public MarketSimulator()
    {
        this.marketData = new ArrayList<>();
        this.random = new Random();
        initialize();
    }

    public List<Stock> getMarketData() {
        return marketData;
    }
}
