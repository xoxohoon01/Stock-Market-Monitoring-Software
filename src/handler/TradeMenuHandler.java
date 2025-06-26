package handler;

import io.InputProvider;
import io.OutputRenderer;
import main.Main;
import main.SelectedMenu;
import market.MarketSimulator;
import system.MessageBox;

public class TradeMenuHandler implements MenuHandler
{
    private final MarketSimulator simulator;
    private final InputProvider input;
    private final OutputRenderer output;

    public TradeMenuHandler(MarketSimulator simulator, InputProvider input, OutputRenderer output)
    {
        this.simulator = simulator;
        this.input = input;
        this.output = output;
    }

    @Override
    public void handle() throws Exception
    {
        while (true)
        {
            // 시스템 메시지 출력
            MessageBox.showMenuMessage();

            String command = input.readLine().trim();

            switch (command)
            {
                case "0":
                    Main.selectedMenu = SelectedMenu.MainMenu;
                    return;
                case "1":
                    Main.selectedMenu = SelectedMenu.TradeBuy;
                    return;
                case "2":
                    Main.selectedMenu = SelectedMenu.TradeSell;
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }
}
