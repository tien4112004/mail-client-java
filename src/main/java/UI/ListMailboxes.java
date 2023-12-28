package UI;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import Filter.Mailbox;

public class ListMailboxes extends UI {
    private String username;
    List<Mailbox> mailboxes = null;

    private static final int MAILBOXES_PER_PAGE = 10;
    private final String UI_SPLITER = "========================================================================================\n";

    public int currentPage = 1;

    public ListMailboxes(String username, InputHandler inputHandler, List<Mailbox> mailboxes) {
        this.username = username;
        this.inputHandler = inputHandler;
        this.mailboxes = mailboxes;
    }

    public void list() {
        boolean keepRunning = true;
        while (keepRunning) {
            clearConsole();

            displayMailboxes();
            displayOptions();
            keepRunning = handleUserInput();
        }
    }

    private void nextPage() {
        currentPage = Math.min(currentPage + 1, mailboxes.size() / MAILBOXES_PER_PAGE + 1);
    }

    private void previousPage() {
        currentPage = Math.max(currentPage - 1, 1);
    }

    private void displayMailboxes() {
        int start = (currentPage - 1) * MAILBOXES_PER_PAGE;
        int end = Math.min(start + MAILBOXES_PER_PAGE, mailboxes.size());
        int pageCount = mailboxes.size() / MAILBOXES_PER_PAGE + 1;

        System.out.printf("Mailboxes for %s%s%s (Page %d/%d): \n", ANSI_TEXT_BLUE, username, ANSI_RESET, currentPage,
                pageCount);
        System.out.print(UI_SPLITER);

        for (int i = start; i < end; i++) {
            System.out.printf("[%d] %s\n", i + 1, mailboxes.get(i).getMailboxName());
        }
    }

    private void displayOptions() {
        String[][] options = {
                { "<", "Previous page" },
                { ">", "Next page" },
                { "#", "Open mailbox #" },
                { "N", "New mailbox" },
                { "Q", "Quit" }
        };
        showOptions(options);
    }

    private boolean handleUserInput() {
        String userInput = inputHandler.dialog(EMPTY_PROMPT);
        switch (userInput) {
            case ">":
                nextPage();
                return true;
            case "<":
                previousPage();
                return true;
            case "N":
                createNewMailbox();
                return true;
            case "Q":
                return false;
            default:
                handleMailboxSelection(userInput);
                return true;
        }
    }

    private void handleMailboxSelection(String userInput) {
        if (isInvalidOptions(userInput))
            return;

        int order = Integer.parseInt(userInput);
        int mailboxIndex = (currentPage - 1) * 10 + order;

        if (mailboxIndex > mailboxes.size() || order > 9 || order < 0) {
            System.out.printf("%s[ERROR] Invalid mailbox number!%s\n", ANSI_TEXT_RED, ANSI_RESET);
            sleep(TIME_2_SECONDS);
            return;
        }

        Mailbox mailbox = mailboxes.get(mailboxIndex - 1);
        ListEmails listEmails = new ListEmails(mailbox, inputHandler);
        listEmails.list();
    }

    private boolean isInvalidOptions(String userInput) {
        if (userInput.length() > 1 || !userInput.matches("[1-9]+")) {
            System.out.printf("%s[ERROR] Invalid option!%s\n", ANSI_TEXT_RED, ANSI_RESET);
            sleep(TIME_2_SECONDS);
            return true;
        }
        return false;
    }

    private void createNewMailbox() {
        String newMailboxName = inputHandler.dialog("New mailbox name: ");
        mailboxes.add(new Mailbox(newMailboxName));
        System.out.println(ANSI_TEXT_GREEN + "New mailbox created!" + ANSI_RESET);
        sleep(1500);
    }
}