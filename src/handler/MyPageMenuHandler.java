package handler;

import io.InputProvider;
import io.OutputRenderer;
import market.MarketSimulator;

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

    }
}
