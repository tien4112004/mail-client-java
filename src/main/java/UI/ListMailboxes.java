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
        clearConsole();

        int start = (currentPage - 1) * MAILBOXES_PER_PAGE;
        int end = Math.min(start + MAILBOXES_PER_PAGE, mailboxes.size());
        int pageCount = mailboxes.size() / MAILBOXES_PER_PAGE + 1;

        System.out.printf("Mailboxes for %s%s%s (Page %d/%d): \n", ANSI_TEXT_BLUE, username, ANSI_RESET, currentPage,
                pageCount);
        System.out.print(PART_SPLITER);

        for (int i = start; i < end; i++) {
            System.out.printf("[%d] %s\n", i + 1, mailboxes.get(i).getMailboxName());
        }

        String[][] options = {
                { ">", "Next page" },
                { "<", "Previous page" },
                { "#", "Open mailbox #" },
                { "N", "New mailbox" },
                { "Q", "Quit" }
        };
        showOptions(options);
        String userInput = inputHandler.dialog(EMPTY_PROMPT);
        switch (userInput) {
            case ">":
                nextPage();
                break;
            case "<":
                previousPage();
                break;
            case "N":
                createNewMailbox();
                break;
            case "Q":
                return;
            default:
                int order = Integer.parseInt(userInput);
                int mailboxOrder = (currentPage - 1) * 10 + Integer.parseInt(userInput);
                Mailbox mailbox = mailboxes.get(mailboxOrder - 1);
                ListEmails listEmails = new ListEmails(mailbox, inputHandler);
                listEmails.list();
                if (mailboxOrder > mailboxes.size() || order > 9 || order < 0) {
                    System.out.println("Invalid mailbox number! Aborting...");
                    System.exit(0);
                }
        }
    }

    private void nextPage() {
        currentPage = Math.min(currentPage + 1, mailboxes.size() / MAILBOXES_PER_PAGE + 1);
        list();
    }

    private void previousPage() {
        currentPage = Math.max(currentPage - 1, 1);
        list();
    }

    private void createNewMailbox() {
        String newMailboxName = inputHandler.dialog("New mailbox name: ");
        mailboxes.add(new Mailbox(newMailboxName));
        System.out.println(ANSI_TEXT_GREEN + "New mailbox created!" + ANSI_RESET);
        sleep(1500);
        list();
    }

    public static void main(String[] args) throws IOException {
        ListMailboxes listMailboxes = new ListMailboxes("example@fit.hcmus.edu.vn", new InputHandler());
        listMailboxes.list();
    }
}