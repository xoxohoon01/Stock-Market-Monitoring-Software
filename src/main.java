import java.io.LineNumberReader;
import java.util.Scanner;

public class main
{
    public static void clearScreen()
    {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printScreen()
    {
        clearScreen();
        System.out.print("\033[2J");
        System.out.println("출력 중");
    }
    public static void main(String[] args)
    {

        Thread outputThread = new Thread(() ->
        {
            while (true)
            {
                try
                {
                    printScreen();
                    Thread.sleep(500);
                } catch (InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread inputThread = new Thread(() ->
        {
            Scanner scanner = new Scanner(System.in);
            while (true)
            {
                System.out.print("\033");
                String input = scanner.nextLine();
            }
        });

        outputThread.start();
        inputThread.start();
    }
}
