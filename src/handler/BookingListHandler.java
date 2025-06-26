package handler;

import io.InputProvider;
import io.OutputRenderer;
import main.Main;
import main.SelectedMenu;
import market.MarketSimulator;
import strategy.Reservation;

import java.util.List;

public class BookingListHandler implements MenuHandler
{
    private final MarketSimulator simulator;
    private final InputProvider input;
    private final OutputRenderer output;

    public BookingListHandler(MarketSimulator simulator, InputProvider input, OutputRenderer output)
    {
        this.simulator = simulator;
        this.input = input;
        this.output = output;
    }

    @Override
    public void handle() throws Exception
    {
        List<Reservation> reservations = Main.user.reservations;

        if (reservations.isEmpty()) {
            System.out.println("\n📭 현재 등록된 예약이 없습니다.");
            Main.selectedMenu = SelectedMenu.BookingMainMenu;
            return;
        }

        while (true) {
            System.out.println("\n📌 예약 목록:");
            for (int i = 0; i < reservations.size(); i++) {
                Reservation r = reservations.get(i);
                System.out.printf("%d. [%s] %s - 수량: %d | 목표가: %.2f원\n",
                        i + 1,
                        r.getType(),
                        r.getStock().getName(),
                        r.getQuantity(),
                        r.getTargetPrice());
            }

            System.out.println("\n0. 뒤로가기");
            System.out.print("취소할 예약 번호를 입력하세요: ");

            String command = input.readLine().trim();

            if (command.equals("0")) {
                Main.selectedMenu = SelectedMenu.BookingMainMenu;
                return;
            }

            try {
                int index = Integer.parseInt(command) - 1;
                if (index < 0 || index >= reservations.size()) {
                    System.out.println("❌ 잘못된 번호입니다.");
                } else {
                    Reservation removed = reservations.remove(index);
                    System.out.printf("✅ [%s] %s 예약이 취소되었습니다.\n",
                            removed.getType(), removed.getStock().getName());
                    Main.selectedMenu = SelectedMenu.BookingMainMenu;
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
    }
}
