package UI;

import java.io.IOException;
import java.util.Scanner;

// import javax.imageio.stream.ImageOutputStreamImpl;
// import javax.sound.sampled.SourceDataLine;

// import Config.Config;
// import Message.Message;
// import Socket.SMTPSocket;
// import Envelope.Envelope;
// import UI.SendFunction;

public class UI {
    private final String ANSI_TEXT_BLACK = "\u001B[30m";
    private final String ANSI_TEXT_RED = "\u001B[31m";
    private final String ANSI_TEXT_GREEN = "\u001B[32m";
    private final String ANSI_TEXT_YELLOW = "\u001B[33m";
    private final String ANSI_TEXT_BLUE = "\u001B[34m";
    private final String ANSI_TEXT_PURPLE = "\u001B[35m";
    private final String ANSI_TEXT_CYAN = "\u001B[36m";
    private final String ANSI_TEXT_WHITE = "\u001B[37m";
    private final String ANSI_RESET = "\u001B[0m";
    private final String ANSI_BACKGROUND_BLACK = "\u001B[40m";
    private final String ANSI_BACKGROUND_RED = "\u001B[41m";
    private final String ANSI_BACKGROUND_GREEN = "\u001B[42m";
    private final String ANSI_BACKGROUND_YELLOW = "\u001B[43m";
    private final String ANSI_BACKGROUND_BLUE = "\u001B[44m";
    private final String ANSI_BACKGROUND_PURPLE = "\u001B[45m";
    private final String ANSI_BACKGROUND_CYAN = "\u001B[46m";
    private final String ANSI_BACKGROUND_WHITE = "\u001B[47m";

    // public String username = new GetUserLoginInfomation().getUsername(); // TODO:
    // JSON
    public String username;
    public String password; // = new GetUserLoginInfomation().getPassword();

    protected Scanner readConsole;

    public UI() {
        this.readConsole = new Scanner(System.in);
    }

    protected void clearConsole() {
        System.out.print("\033[H\033[2J");
    }

    protected void showOption() throws IOException {
        System.out.println("Welcome back, " + username);
        String[][] options = {
                { "1", "Send new email" },
                { "2", "Read email" },
                { "3", "Quit" }
        };
        showOptions(options);
        System.out.print("Please choose your option: ");
        int option = readConsole.nextInt();
        clearConsole();
        switch (option) {
            case 1:
                new SendEmails().send();
                break;
            case 2:
                // new ListEmails().list();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option! Aborting...");
                System.exit(0);
        }
    }

    // This start the UI in console
    public void start() throws IOException {
        while (true)
            showOption();
    }

    protected void showOptions(String[][] options) {
        // Calculate the maximum length of the trigger keys and triggered commands
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
            if ((i + 1) % 3 == 0)
                System.out.println();
        }
    }

    // public static void main(String[] args) throws IOException {
    // UI ui = new UI();
    // ui.start();
    // }

    // private boolean verifyEmail(String email) {
    // return email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    // }
}
