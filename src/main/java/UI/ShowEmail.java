package UI;

import Message.MessageParser;

import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class ShowEmail {
    private static final String LONG_DAY_DISPLAY_FORMAT = "EEE, d MMM yyyy HH:mm:ss";
    private static final String CLEAR_CONSOLE = "\033[H\033[2J";

    public static void showEmail(String emailDirectory) throws IOException {
        System.out.print(CLEAR_CONSOLE);
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
        System.out.println(
                "[.] Back to list \t [#] Download #-th attachment \t [A] Download all attachments \t [D] Delete email");
    }

    private static void printList(String prompt, String[] list) {
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

    public static void handleUserInput(String userInput) {
        if (userInput.equals(".")) {
            // return to email list - nothing here
        } else if (userInput.equals("A")) {

        } else if (userInput.equals("D")) {
            // delete email
        } else {
            // download i-th attachment
        }
    }

    public static void printOption(String option, int index) {
        System.out.println("[" + index + "] " + option);
    }
}
