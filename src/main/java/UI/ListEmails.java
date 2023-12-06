package UI;

import java.io.IOException;

public class ListEmails extends UI {

    protected void list() throws IOException {
        System.out.print("This function is in development! Want to goback? (Y/N) ");
        do {
            String answer = readConsole.nextLine();
            if (answer.equalsIgnoreCase("Y")) {
                clearConsole();
                showOption();
            }
            if (answer.equalsIgnoreCase("N")) {
                System.exit(0);
            }
        } while (true);
    }
}
