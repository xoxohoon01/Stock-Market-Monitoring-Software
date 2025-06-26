package handler;

import io.InputProvider;
import io.OutputRenderer;
import main.MonitoringMain;
import main.SelectedMenu;
import market.MarketSimulator;
import system.MessageBox;

public class MonitoringHandler implements MenuHandler
{
    private final MarketSimulator simulator;
    private final InputProvider input;
    private final OutputRenderer output;

    public MonitoringHandler(MarketSimulator simulator, InputProvider input, OutputRenderer output)
    {
        this.simulator = simulator;
        this.input = input;
        this.output = output;
    }

    @Override
    public void handle() throws Exception
    {
        Thread monitorThread = new Thread(() ->
        {
            try {
                boolean isShownMessage = false;
                while (!Thread.currentThread().isInterrupted())
                {
                    if (simulator.isOpenned)
                    {
                        isShownMessage = false;
                        simulator.printMarket();

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
        input.readLine();

        monitorThread.interrupt();
        monitorThread.join(); // 쓰레드 종료 대기

        MonitoringMain.selectedMenu = SelectedMenu.MainMenu;
    }
}
