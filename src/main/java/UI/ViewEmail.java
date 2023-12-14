package UI;

import Message.MessageParser;

import java.util.List;

import Filter.Mailbox;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class ViewEmail extends UI {
    private final String LONG_DAY_DISPLAY_FORMAT = "EEE, d MMM yyyy HH:mm:ss";

    private String emailDirectory;
    private List<String> mailList;
    private List<Mailbox> mailboxes;

    private BackToPreviousCallback backToListCallback;

    private int mailOrder;

    public ViewEmail(String emailDirectory, List<String> mailList, int mailOrder, List<Mailbox> mailboxes,
            InputHandler inputHandler, BackToPreviousCallback backToListCallback) {
        this.emailDirectory = emailDirectory;
        this.mailList = mailList;
        this.mailOrder = mailOrder;
        this.mailboxes = mailboxes;
        this.inputHandler = inputHandler;
        this.backToListCallback = backToListCallback;
    }

    public void showEmail() {
        clearConsole();
        Path emailPath = Paths.get(emailDirectory);
        String rawMessage;
        try {
            rawMessage = new String(java.nio.file.Files.readAllBytes(emailPath));
        } catch (IOException e) {
            System.out.println(ANSI_TEXT_RED + "[ERROR] Error in reading email." + ANSI_RESET);
            e.printStackTrace();
            return;
        }

        MessageParser parser = new MessageParser();
        parser.parse(rawMessage);
        System.out.println("Date: " + parser.getDate(LONG_DAY_DISPLAY_FORMAT));
        System.out.println("From: " + parser.getSender());
        printList("To: ", parser.getRecipientsTo());
        printList("Cc: ", parser.getRecipientsCc());
        System.out.println("Subject: " + parser.getSubject());
        System.out.println("Content: " + parser.getContent());

        String[] attachmentDirectories = parser.getAttachmentDirectories();
        attachmentDirectories = resolveAttachmentDirectories(attachmentDirectories);
        System.out.println("Attachments: ");
        for (int i = 0; i < attachmentDirectories.length; i++) {
            System.out.printf("[%d] %s\n", i + 1, attachmentDirectories[i]);
        }

        String[][] options = {
                { "Q", "Back to list" },
                { "#", "Download attachment #" },
                { "A", "Dowmload all attachments" },
                { "D", "Delete email" },
                { "M", "Move to mailbox" }
        };
        showOptions(options);
        String userInput = inputHandler.dialog(EMPTY_PROMPT);
        handleUserInput(userInput);
    }

    private void printList(String prompt, String[] list) {
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

    private String[] resolveAttachmentDirectories(String[] attachmentDirectories) {
        String[] resolvedAttachmentDirectories = new String[attachmentDirectories.length];
        for (int i = 0; i < attachmentDirectories.length; i++) {
            Path attachmentPath = Paths.get(attachmentDirectories[i]);
            resolvedAttachmentDirectories[i] = attachmentPath.toAbsolutePath().toString();
        }
        return resolvedAttachmentDirectories;
    }

    public void handleUserInput(String userInput) {
        if (userInput.equals("Q")) {
            backToListCallback.backToList();
        } else if (userInput.equals("A")) {

        } else if (userInput.equals("D")) {
            deleteEmailHandler();
        } else if (userInput == "M") {
            for (int i = 0; i < mailboxes.size(); i++) {
                System.out.printf("[%d] %s\n", i + 1, mailboxes.get(i).getMailboxName());
            }
            String[][] options = {
                    { ".", "Back to list" },
                    { "#", "Move to mailbox #" },
                    { "N", "New mailbox" },
                    { "C", "Cancel" }
            };
            showOptions(options);
            userInput = inputHandler.dialog("Move to: ");

            Mailbox destination;
            switch (userInput) {
                case ".":
                    return;
                case "C":
                    return;
                case "N":
                    String newMailboxName = inputHandler.dialog("New mailbox name: ");
                    mailboxes.add(new Mailbox(newMailboxName));
                    System.out.println(ANSI_TEXT_GREEN + "New mailbox created!" + ANSI_RESET);
                    sleep(1500);
                    destination = mailboxes.get(mailboxes.size() - 1);
                    break;
                default:
                    int mailboxOrder = Integer.parseInt(userInput);
                    destination = mailboxes.get(mailboxOrder);
            }
            Mailbox.moveMailToMailbox(emailDirectory, destination);
            mailList.remove(mailOrder);
            System.out.printf("%s%s%s\n", ANSI_TEXT_GREEN, "Moved to ...", ANSI_RESET);
            sleep(2000);
        } else {
            // download i-th attachment
        }
    }

    private void deleteEmailHandler() {
        Path emailPath = Paths.get(emailDirectory);
        try {
            Files.delete(emailPath);
        } catch (IOException e) {
            System.out.println(ANSI_TEXT_RED + "[ERROR] Error in deleting email." + ANSI_RESET);
            sleep(1500);
            e.printStackTrace();
        }
        mailList.remove(mailOrder);
        System.out.printf("%s%s%s\n", ANSI_TEXT_GREEN, "Email removed.", ANSI_RESET);
        sleep(2000);
    }
}
