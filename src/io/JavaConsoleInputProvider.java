package io;

import java.util.Scanner;

public class JavaConsoleInputProvider implements InputProvider
{
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String readLine()
    {
        return scanner.nextLine();
    }
}
