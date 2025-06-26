package handler;

import io.InputProvider;
import io.OutputRenderer;
import main.Main;
import main.SelectedMenu;
import market.MarketSimulator;
import system.MessageBox;

public class BookingMenuHandler implements MenuHandler
{
    private final MarketSimulator simulator;
    private final InputProvider input;
    private final OutputRenderer output;

    public BookingMenuHandler(MarketSimulator simulator, InputProvider input, OutputRenderer output)
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

        switch (command)
        {
            case "0":
                Main.selectedMenu = SelectedMenu.MainMenu;
                return;
            case "1":
                Main.selectedMenu = SelectedMenu.BookingBuy;
                break;
            case "2":
                Main.selectedMenu = SelectedMenu.BookingSell;
                break;
            case "3":
                Main.selectedMenu = SelectedMenu.BookingList;
                break;
            default:
                System.out.println("잘못된 입력입니다.");
                break;
        }
    }
}
