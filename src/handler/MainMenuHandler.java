package handler;

import io.InputProvider;
import io.OutputRenderer;
import main.MonitoringMain;
import main.SelectedMenu;
import market.MarketSimulator;
import system.MessageBox;

public class MainMenuHandler implements MenuHandler
{
    private final MarketSimulator simulator;
    private final InputProvider input;
    private final OutputRenderer output;

    public MainMenuHandler(MarketSimulator simulator, InputProvider input, OutputRenderer output)
    {
        this.simulator = simulator;
        this.input = input;
        this.output = output;
    }

    @Override
    public void handle() throws Exception
    {
        MessageBox.showMenuMessage();

        String command = input.readLine().trim();

        switch(command)
        {
            case "0":
                System.out.println("프로그램을 종료합니다.");
                System.exit(0);
                break;
            case "1":
                MonitoringMain.selectedMenu = SelectedMenu.Monitoring;
                break;
            case "2":
                MonitoringMain.selectedMenu = SelectedMenu.TradeMainMenu;
                break;
            case "3":
                MonitoringMain.selectedMenu = SelectedMenu.BookingMainMenu;
                break;
            case "4":
                MonitoringMain.selectedMenu = SelectedMenu.MyPage;
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                break;
        }
    }
}
