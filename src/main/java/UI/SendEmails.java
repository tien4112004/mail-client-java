package UI;

import java.io.IOException;
// import java.util.Scanner;

import Message.Message;
import Socket.SMTPSocket;

public class SendEmails extends UI {
    private String[] recipientsTo;
    private String[] recipientsCc;
    private String[] recipientsBcc;
    private String subject;
    private String content;
    private String[] attachments;

    private void attachmentsHandler() {
        System.out.print("Send attachments? (Y/N) ");
        String answer;
        do {
            answer = readConsole.nextLine();
            if (answer.equalsIgnoreCase("Y")) {
                System.out.println("Please enter the path of attachments, separated by comma:");
                String attachments = readConsole.hasNextLine() ? readConsole.nextLine() : "";
                this.attachments = attachments.split(", ");
            }
        } while (answer.equalsIgnoreCase("N"));
    }

    private void compose() {
        System.out.println("Compose new email:");
        // Recipients

        recipientsTo = getInputList("To: ");
        recipientsCc = getInputList("Cc: ");
        recipientsBcc = getInputList("Bcc: ");

        System.out.print("Subject: ");
        subject = readConsole.hasNextLine() ? readConsole.nextLine() : "";
        System.out.print("Content: ");
        content = readConsole.hasNextLine() ? readConsole.nextLine() : "";
        attachmentsHandler();
        // clear console
        clearConsole();
    }

    private String[] getInputList(String prompt) {
        System.out.print(prompt);
        String input = readConsole.nextLine();
        input.trim();
        return input.split(",");
    }

    public void send() throws IOException {
        compose();
        Message message = new Message(username, recipientsTo, recipientsCc, recipientsBcc, subject, content,
                attachments);
        SMTPSocket smtpSocket = new SMTPSocket("localhost", 2225);

        try {
            smtpSocket.connect();
            smtpSocket.sendEmail(message);
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }
}
