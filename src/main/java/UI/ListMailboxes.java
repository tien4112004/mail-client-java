package UI;

import java.io.IOException;
import java.util.List;
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

    public ListMailboxes(String username) {
        this.username = username;
    }

    public void list() throws IOException {
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
    }

    public Mailbox handleMailboxInput(String input, List<Mailbox> mailboxes, int currentPage) {
        switch (input) {
            case ">":
                currentPage = Math.min(currentPage + 1, mailboxes.size() / MAILBOXES_PER_PAGE + 1);
                break;
            case "<":
                currentPage = Math.max(currentPage - 1, 1);
                break;
            case "Q":
                System.exit(0);
                break;
            default:
                int order = Integer.parseInt(input);
                int mailboxOrder = (currentPage - 1) * 10 + Integer.parseInt(input);
                if (mailboxOrder > mailboxes.size() || order > 9 || order < 0) {
                    System.out.println("Invalid mailbox number! Aborting...");
                    System.exit(0);
                }
                return mailboxes.get(mailboxOrder - 1);
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        ListMailboxes listMailboxes = new ListMailboxes("example@fit.hcmus.edu.vn");
        listMailboxes.list();
    }
}