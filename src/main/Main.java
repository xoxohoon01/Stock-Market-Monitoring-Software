package main;

import handler.MenuRouter;
import io.InputProvider;
import io.JavaConsoleInputProvider;
import io.JavaConsoleOutputRenderer;
import io.OutputRenderer;
import market.MarketSimulator;
import user.UserData;

public class Main
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
}

