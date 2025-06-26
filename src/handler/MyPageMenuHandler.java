package handler;

import io.InputProvider;
import io.OutputRenderer;
import main.MonitoringMain;
import main.SelectedMenu;
import market.MarketSimulator;
import system.MessageBox;

public class MyPageMenuHandler implements MenuHandler
{
    private final MarketSimulator simulator;
    private final InputProvider input;
    private final OutputRenderer output;

    public MyPageMenuHandler(MarketSimulator simulator, InputProvider input, OutputRenderer output)
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
            MessageBox.showMenuMessage();

            String command = input.readLine().trim();

            switch (command)
            {
                case "0":
                    MonitoringMain.selectedMenu = SelectedMenu.MainMenu;
                    return;
                case "1":
                    MessageBox.showAccount(simulator);
                    break;
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }
}
