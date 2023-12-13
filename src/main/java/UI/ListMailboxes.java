package UI;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import Filter.Mailbox;

public class ListMailboxes extends UI {
    private String username = "example@fit.hcmus.edu.vn"; // TODO: get from JSON
    List<Mailbox> mailboxes = new ArrayList<Mailbox>(
            Arrays.asList(new Mailbox("Inbox"), new Mailbox("Sent"), new Mailbox("Spam")));

    public void list() throws IOException {
        clearConsole();

        System.out.println("Mailboxes for " + username + ":");
        for (int i = 0; i < mailboxes.size(); i++) {
            System.out.printf("[%d] %s\n", i + 1, mailboxes.get(i).getMailboxName());
        }

        String[][] options = {
                { "#", "Open mailbox #" },
                { "C", "New mailbox" },
                { "Q", "Quit" }
        };
        showOptions(options);

        String choice = readConsole.nextLine();
        if (choice.equalsIgnoreCase("Q")) {
            return;
        } else if (choice.equalsIgnoreCase("C")) {
            System.out.print("Mailbox name: ");
            String newMailboxName = readConsole.nextLine();
            Mailbox newMailbox = new Mailbox(newMailboxName);
            mailboxes.add(newMailbox);
            System.out.println("Mailbox " + newMailboxName + " created.");
            list();
        } else {
            int mailboxIndex = Integer.parseInt(choice) - 1;
            Mailbox selectedMailbox = mailboxes.get(mailboxIndex);
            System.out.println("Selected mailbox: " + selectedMailbox.getMailboxName());
            ListEmails listEmails = new ListEmails();
            listEmails.list(selectedMailbox);
            list();
        }
    }

    public static void main(String[] args) throws IOException {
        ListMailboxes listMailboxes = new ListMailboxes();
        listMailboxes.list();
    }
}