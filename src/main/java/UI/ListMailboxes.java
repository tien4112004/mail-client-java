package UI;

import java.io.IOException;
import java.util.List;

import javax.imageio.spi.IIOServiceProvider;

import java.util.ArrayList;
import java.util.Arrays;

import Filter.Mailbox;

public class ListMailboxes extends UI {
    private String username; // TODO: get from JSON
    List<Mailbox> mailboxes = new ArrayList<Mailbox>(
            Arrays.asList(new Mailbox("Inbox"), new Mailbox("Sent"), new Mailbox("Spam")));

    private static final int MAILBOXES_PER_PAGE = 10;
    private final String PART_SPLITER = "========================================================================================\n";

    public int currentPage = 1;

    public ListMailboxes(String username, InputHandler inputHandler) {
        this.username = username;
        this.inputHandler = super.inputHandler;
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
        System.out.print(PART_SPLITER);

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
        int order = Integer.parseInt(userInput);
        int mailboxIndex = (currentPage - 1) * 10 + order;
        Mailbox mailbox = mailboxes.get(mailboxIndex - 1);
        ListEmails listEmails = new ListEmails(mailbox, inputHandler);
        listEmails.list();
        if (mailboxIndex > mailboxes.size() || order > 9 || order < 0) {
            System.out.println("Invalid mailbox number! Aborting...");
            System.exit(0);
        }
    }

    private void createNewMailbox() {
        String newMailboxName = inputHandler.dialog("New mailbox name: ");
        mailboxes.add(new Mailbox(newMailboxName));
        System.out.println(ANSI_TEXT_GREEN + "New mailbox created!" + ANSI_RESET);
        sleep(1500);
        list();
    }
}