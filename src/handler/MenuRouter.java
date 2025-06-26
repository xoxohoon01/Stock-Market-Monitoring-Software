package handler;

import io.InputProvider;
import io.OutputRenderer;
import market.MarketSimulator;
import main.SelectedMenu;

public class MenuRouter
{
    private final MarketSimulator simulator;
    private final InputProvider input;
    private final OutputRenderer output;

    public MenuRouter(MarketSimulator simulator, InputProvider input, OutputRenderer output)
    {
        this.simulator = simulator;
        this.input = input;
        this.output = output;
    }

    public void route(SelectedMenu selectedMenu) throws Exception
    {
        MenuHandler handler;
        switch (selectedMenu) {
            case MainMenu -> handler = new MainMenuHandler(simulator, input, output);

            case TradeMainMenu -> handler = new TradeMenuHandler(simulator, input, output);
            case TradeBuy -> handler = new TradeBuyHandler(simulator, input, output);
            case TradeSell -> handler = new TradeSellHandler(simulator, input, output);

            case BookingMainMenu -> handler = new BookingMenuHandler(simulator, input, output);
            case BookingBuy -> handler = new BookingBuyHandler(simulator, input, output);
            case BookingSell -> handler = new BookingSellHandler(simulator, input, output);
            case BookingList -> handler = new BookingListHandler(simulator, input, output);

            case MyPage -> handler = new MyPageMenuHandler(simulator, input, output);
            case Monitoring -> handler = new MonitoringHandler(simulator, input, output);
            default -> throw new IllegalStateException("알 수 없는 메뉴입니다: " + selectedMenu);
        }
        handler.handle();
    }
}
