package UI;

import java.util.List;

import Email.EmailParser;
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
    private EmailParser parser;
    private String[] attachmentDirectories;

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
        displayStatusMessage("Loading email...", 0);
        String rawEmail;
        try {
            rawEmail = readEmail();
        } catch (IOException e) {
            return;
        }
        parser = new EmailParser();
        parser.fullParse(rawEmail);
        clearConsole();
        displayEmailContent();
        handleUserInput();
    }

    private String readEmail() throws IOException {
        Path emailPath = Paths.get(emailDirectory);
        try {
            return new String(java.nio.file.Files.readAllBytes(emailPath));
        } catch (IOException e) {
            displayErrorMessage("Error in reading email.");
            e.printStackTrace();
            throw e;
        }
    }

    private void displayEmailContent() {
        System.out.println("Date: " + parser.getDate(LONG_DAY_DISPLAY_FORMAT));
        System.out.println("From: " + parser.getSender());
        printList("To: ", parser.getRecipientsTo());
        printList("Cc: ", parser.getRecipientsCc());
        System.out.println("Subject: " + parser.getSubject());
        System.out.println("Content: " + parser.getContent());

        attachmentDirectories = resolveAttachmentDirectories(parser.getAttachmentDirectories());
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
            case "q":
                return;
            case "A":
            case "a":
                saveAllAttachments();
                return;
            case "D":
            case "d":
                deleteEmail();
                return;
            case "M":
            case "m":
                moveEmail();
                return;
            default:
                saveAttachment(userInput);
                return;
        }
    }

    private void deleteEmail() {
        Path emailPath = Paths.get(emailDirectory);
        displayStatusMessage("Deleting email...");
        try {
            Files.delete(emailPath);
        } catch (IOException e) {
            displayErrorMessage("Error in deleting email.");
            e.printStackTrace();
        }
        mailList.remove(emailIndex);
        displaySuccessMessage("Email deleted.");
    }

    private void moveEmail() {
        String destination = inputHandler.dialog("Destination mailbox: ");
        displayStatusMessage("Moving email...");
        Mailbox.moveMailToFolder(emailDirectory, destination);
        mailList.remove(emailIndex);
        displaySuccessMessage("Email moved to " + destination + ".");
    }

    private void saveAttachment(int attachmentIndex, String saveDirectory) {
        try {
            Path attachmentPath = Paths.get(attachmentDirectories[attachmentIndex - 1]);
            Path savePath = Paths.get(saveDirectory + "/" + attachmentPath.getFileName());
            displayStatusMessage("Saving attachment...");
            Files.copy(attachmentPath, savePath);
            displaySuccessMessage(String.format("Attachment #%d saved to %s.", attachmentIndex, saveDirectory));
        } catch (IOException e) {
            displayErrorMessage("Error in saving attachment.");
            // e.printStackTrace();
        }
    }

    private void saveAllAttachments() {
        String saveDirectory = inputHandler.dialog("Save directory: ");
        for (int i = 1; i <= attachmentDirectories.length; i++) {
            saveAttachment(i, saveDirectory);
        }
        sleep(TIME_2_5_SECONDS);
    }

    private void saveAttachment(String attachmentIndex) {
        if (isInvalidOptions(attachmentIndex))
            return;
        String saveDirectory = inputHandler.dialog("Save directory: ");
        saveAttachment(Integer.parseInt(attachmentIndex), saveDirectory);
        sleep(TIME_2_SECONDS);
    }
}
