package UI;

public class UI {
    protected final String ANSI_TEXT_BLACK = "\u001B[30m";
    protected final String ANSI_TEXT_RED = "\u001B[31m";
    protected final String ANSI_TEXT_GREEN = "\u001B[32m";
    protected final String ANSI_TEXT_YELLOW = "\u001B[33m";
    protected final String ANSI_TEXT_BLUE = "\u001B[34m";
    protected final String ANSI_TEXT_PURPLE = "\u001B[35m";
    protected final String ANSI_TEXT_CYAN = "\u001B[36m";
    protected final String ANSI_TEXT_WHITE = "\u001B[37m";
    protected final String ANSI_RESET = "\u001B[0m";
    protected final String ANSI_BACKGROUND_BLACK = "\u001B[40m";
    protected final String ANSI_BACKGROUND_RED = "\u001B[41m";
    protected final String ANSI_BACKGROUND_GREEN = "\u001B[42m";
    protected final String ANSI_BACKGROUND_YELLOW = "\u001B[43m";
    protected final String ANSI_BACKGROUND_BLUE = "\u001B[44m";
    protected final String ANSI_BACKGROUND_PURPLE = "\u001B[45m";
    protected final String ANSI_BACKGROUND_CYAN = "\u001B[46m";
    protected final String ANSI_BACKGROUND_WHITE = "\u001B[47m";

    protected final String EMPTY_PROMPT = "";

    protected final int TIME_2_5_SECONDS = 2500;
    protected final int TIME_2_SECONDS = 2000;

    protected final String DEFAULT_WORKING_DIRECTORY = System.getProperty("./");

    protected InputHandler inputHandler;

    protected void clearConsole() {
        System.out.print("\033[H\033[2J");
    }

    protected void showOptions(String[][] options) {
        int maxKeyLength = 0;
        int maxCommandLength = 0;
        for (String[] option : options) {
            maxKeyLength = Math.max(maxKeyLength, option[0].length());
            maxCommandLength = Math.max(maxCommandLength, option[1].length());
        }

        String formatString = "%s[%-" + (maxKeyLength) + "s]%s %-" + maxCommandLength + "s\t";

        System.out.println();
        for (int i = 0; i < options.length; i++) {
            System.out.printf(formatString,
                    ANSI_BACKGROUND_WHITE + ANSI_TEXT_BLACK, options[i][0], ANSI_RESET,
                    options[i][1]);
            if ((i + 1) % 3 == 0 && i != options.length - 1)
                System.out.println();
        }
        System.out.println();
    }

    protected void printList(String prompt, String[] list) {
        if (list == null) {
            return;
        }
        System.out.print(prompt);
        System.out.printf("%s ", list[0]);
        for (int i = 1; i < list.length; i++) {
            System.out.printf(", %s ", list[i]);
        }
        System.out.println();
    }

    protected boolean isInvalidOptions(String userInput) {
        if (userInput.length() > 1 || !userInput.matches("[0-9]+")) {
            displayErrorMessage(userInput + " is not a valid option!");
            return true;
        }
        return false;
    }

    protected void sleep(int milisecond) {
        try {
            Thread.sleep(milisecond);
        } catch (InterruptedException e) {
            displayErrorMessage("Keyboard Interrupted!");
            e.printStackTrace();
        }
    }

    protected void displayErrorMessage(String message) {
        System.out.printf("%s[ERROR] %s%s\n", ANSI_TEXT_RED, message, ANSI_RESET);
        sleep(TIME_2_SECONDS);
    }

    protected void displaySuccessMessage(String message) {
        System.out.printf("%s[INFO] %s%s\n", ANSI_TEXT_GREEN, message, ANSI_RESET);
        sleep(TIME_2_SECONDS);
    }

    protected void displayStatusMessage(String message, int delaySeconds) {
        System.out.printf("%s%s%s\n", ANSI_TEXT_YELLOW, message, ANSI_RESET);
        sleep(delaySeconds * 1000);
    }

    protected void displayStatusMessage(String message) {
        displayStatusMessage(message, 0);
    }

}
