package main;

import handler.MenuRouter;
import io.InputProvider;
import io.JavaConsoleInputProvider;
import io.JavaConsoleOutputRenderer;
import io.OutputRenderer;
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
    public static UserData user = new UserData();
    public static SelectedMenu selectedMenu = SelectedMenu.MainMenu;


    public static void main(String[] args) throws Exception
    {
        // io 시스템 추상화
        InputProvider input = new JavaConsoleInputProvider();
        OutputRenderer output = new JavaConsoleOutputRenderer();

        // 시뮬레이터를 별도 스레드에서 실행
        Thread simulatorThread = new Thread(marketSimulator::openMarket);
        simulatorThread.start();

        // UI 핸들러
        MenuRouter router = new MenuRouter(marketSimulator, input, output);
        while (true)
        {
            router.route(selectedMenu);
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
                default:
                    System.out.println("잘못된 입력입니다.");
                    break;
            }
        }
    }
}

