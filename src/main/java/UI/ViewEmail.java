package UI;

import Message.MessageParser;

import java.util.List;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class ViewEmail extends UI {
    private final String LONG_DAY_DISPLAY_FORMAT = "EEE, d MMM yyyy HH:mm:ss";

    private String emailDirectory;
    private List<String> mailList;

    private int mailOrder;

    public ViewEmail(String emailDirectory, List<String> mailList, int mailOrder) {
        this.emailDirectory = emailDirectory;
        this.mailList = mailList;
        this.mailOrder = mailOrder;
    }

    public void showEmail() throws IOException {
        clearConsole();
        Path emailPath = Paths.get(emailDirectory);
        String rawMessage = new String(java.nio.file.Files.readAllBytes(emailPath));
        MessageParser parser = new MessageParser();
        parser.parse(rawMessage);
        System.out.println("Date: " + parser.getDate(LONG_DAY_DISPLAY_FORMAT));
        System.out.println("From: " + parser.getSender());
        printList("To: ", parser.getRecipientsTo());
        printList("Cc: ", parser.getRecipientsCc());
        System.out.println("Subject: " + parser.getSubject());
        System.out.println("Content: " + parser.getContent());

        String[] attachmentDirectories = parser.getAttachmentDirectories();
        System.out.println("Attachments: ");
        for (int i = 0; i < attachmentDirectories.length; i++) {
            System.out.printf("[%d] %s\n", i + 1, attachmentDirectories[i]);
        }

        String[][] options = {
                { ".", "Back to list" },
                { "#", "Download attachment #" },
                { "A", "Dowmload all attachments" },
                { "D", "Delete email" },
                { "M", "Move to mailbox" }
        };
        showOptions(options);
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

    public void handleUserInput(String userInput) {
        if (userInput.equals("Q")) {
            // return to email list - nothing here
        } else if (userInput.equals("A")) {

        } else if (userInput.equals("D")) {
            Path emailPath = Paths.get(emailDirectory);
            try {
                Files.delete(emailPath);
            } catch (IOException e) {
                System.out.println(ANSI_TEXT_RED + "[ERROR] Error in deleting email." + ANSI_RESET);
                e.printStackTrace();
            }
            mailList.remove(mailOrder);
        } else {
            // download i-th attachment
        }
    }
}
