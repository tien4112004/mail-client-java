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

    private InputHandler inputHandler;
    private String emailDirectory;
    private List<String> mailList;
    private List<Mailbox> mailboxes;

    private int emailIndex;

    public ViewEmail(String emailDirectory, List<String> mailList, int emailIndex, List<Mailbox> mailboxes,
            InputHandler inputHandler) {
        this.emailDirectory = emailDirectory;
        this.mailList = mailList;
        this.emailIndex = emailIndex;
        this.mailboxes = mailboxes;
        this.inputHandler = inputHandler;
    }

    public void showEmail() {
        clearConsole();
        String rawEmail = readEmail();
        if (rawEmail == null) {
            return;
        }

        MessageParser parser = new MessageParser();
        parser.fullParse(rawEmail);
        displayEmailContent(parser);
        handleUserInput();
    }

    private String readEmail() {
        Path emailPath = Paths.get(emailDirectory);
        try {
            return new String(java.nio.file.Files.readAllBytes(emailPath));
        } catch (IOException e) {
            System.out.println(ANSI_TEXT_RED + "[ERROR] Error in reading email." + ANSI_RESET);
            e.printStackTrace();
            return null;
        }
    }

    private void displayEmailContent(MessageParser parser) {
        System.out.println("Date: " + parser.getDate(LONG_DAY_DISPLAY_FORMAT));
        System.out.println("From: " + parser.getSender());
        printList("To: ", parser.getRecipientsTo());
        printList("Cc: ", parser.getRecipientsCc());
        System.out.println("Subject: " + parser.getSubject());
        System.out.println("Content: " + parser.getContent());

        String[] attachmentDirectories = resolveAttachmentDirectories(parser.getAttachmentDirectories());
        System.out.println("Attachments: ");
        for (int i = 0; i < attachmentDirectories.length; i++) {
            System.out.printf("[%d] %s\n", i + 1, attachmentDirectories[i]);
        }

        String[][] options = {
                { "Q", "Back to list" },
                { "#", "Download attachment #" },
                { "A", "Download all attachments" },
                { "D", "Delete email" },
                { "M", "Move to mailbox" }
        };
        showOptions(options);
    }

    private String[] resolveAttachmentDirectories(String[] attachmentDirectories) {
        String[] resolvedAttachmentDirectories = new String[attachmentDirectories.length];
        for (int i = 0; i < attachmentDirectories.length; i++) {
            Path attachmentPath = Paths.get(attachmentDirectories[i]);
            resolvedAttachmentDirectories[i] = attachmentPath.toAbsolutePath().toString();
        }
        return resolvedAttachmentDirectories;
    }

    public void handleUserInput() {
        String userInput = inputHandler.dialog(EMPTY_PROMPT);

        switch (userInput) {
            case "Q":
                return;
            case "A":
                // Handle "A" input
                break;
            case "D":
                deleteEmail();
                return;
            case "M":
                moveEmail();
                return;
            default:
                // Handle other inputs (e.g., download i-th attachment)
        }
    }

    private void deleteEmail() {
        Path emailPath = Paths.get(emailDirectory);
        try {
            Files.delete(emailPath);
        } catch (IOException e) {
            System.out.println(ANSI_TEXT_RED + "[ERROR] Error in deleting email." + ANSI_RESET);
            sleep(1500);
            e.printStackTrace();
        }
        mailList.remove(emailIndex);
        System.out.printf("%s%s%s\n", ANSI_TEXT_GREEN, "Email removed.", ANSI_RESET);
        sleep(2000);
    }

    private void moveEmail() {
        String destination = inputHandler.dialog("Destination mailbox: ");
        Mailbox.moveMailToFolder(emailDirectory, destination);
        System.out.printf("%s%s%s\n", ANSI_TEXT_GREEN, "Email moved to " + destination + ".", ANSI_RESET);
        sleep(TIME_2_SECONDS);
        mailList.remove(emailIndex);
    }
}
