package io;

public class JavaConsoleOutputRenderer implements OutputRenderer
{

    @Override
    public void print(String message)
    {
        System.out.print(message);
    }

    @Override
    public void println(String message)
    {
        System.out.println(message);
    }
}
