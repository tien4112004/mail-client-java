package UI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import Filter.Mailbox;
import Message.Message;

// import javax.imageio.stream.ImageOutputStreamImpl;
// import javax.sound.sampled.SourceDataLine;

// import Config.Config;
// import Socket.SMTPSocket;

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

    // public String username = new GetUserLoginInfomation().getUsername(); // TODO:
    // JSON
    public String username;
    public String password; // = new GetUserLoginInfomation().getPassword();
    List<Mailbox> mailboxes;
    ListMailboxes listMailboxesUI;
    Mailbox currentMailbox;
    ListEmails listEmailsUI;
    String currentEmailDirectory;

    protected InputHandler inputHandler;

    public UI() {
        this.inputHandler = new InputHandler();
        // this.username = inputHandler.getUsername();
        // this.password = inputHandler.getPassword();
        // get mailboxes from JSON
    }

    protected void clearConsole() {
        System.out.print("\033[H\033[2J");
    }

    protected void showOption() throws IOException {
        clearConsole();
        System.out.printf("Welcome back, %s%s%s!\n", ANSI_TEXT_BLUE, username, ANSI_RESET);
        String[][] options = {
                { "1", "Send new email\n" },
                { "2", "Open mailboxes\n" },
                { "3", "Quit\n" }
        };
        showOptions(options);
        int option = inputHandler.getMenuOption();
        clearConsole();
        switch (option) {
            case 1:
                new SendEmails().send();
                break;
            case 2:
                listMailboxesUI = new ListMailboxes(username);
                listMailboxesUI.list();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option! Aborting...");
                System.exit(0);
        }
    }

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
        System.out.println();
    }

    // protected void mailboxInputHandler() {
    // String choice = inputHandler.dialog(EMPTY_PROMPT);
    // switch (choice) {
    // case "Q":
    // return;
    // case "C":
    // String newMailboxName = inputHandler.dialog("Name for new mailbox:");
    // Mailbox newMailbox = new Mailbox(newMailboxName);
    // mailboxes.add(newMailbox);
    // System.out.println("Mailbox " + newMailboxName + " created.");
    // // show the mailbox list again
    // break;
    // case ">":
    // // currentPage = Math.min(currentPage + 1, pageCount);
    // // list();
    // break;
    // case "<":
    // // currentPage = Math.max(currentPage - 1, 1);
    // // list();
    // break;
    // default:
    // // int mailboxIndex = Integer.parseInt(choice) - 1;
    // // Mailbox selectedMailbox = mailboxes.get(mailboxIndex);
    // // System.out.println("Selected mailbox: " +
    // // selectedMailbox.getMailboxName());
    // // ListEmails listEmails = new ListEmails();
    // // listEmails.list(selectedMailbox);
    // // list();
    // break;
    // }
    // }

    public static void main(String[] args) throws IOException {
        UI ui = new UI();
        ui.start();
    }

    // private boolean verifyEmail(String email) {
    // return email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    // }
}
