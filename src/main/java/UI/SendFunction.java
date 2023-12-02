package UI;

import java.io.IOException;
import java.util.Scanner;

import Envelope.Envelope;
import Message.Message;
import Socket.SMTPSocket;

public class SendFunction {
    private String username;
    private String[] recipientsTo;
    private String[] recipientsCc;
    private String[] recipientsBcc;
    private String subject;
    private String content;
    private String[] attachments;

    private Scanner readConsole;

    public SendFunction() {
        this.readConsole = new Scanner(System.in);
    }

    private void clearConsole() {
        System.out.print("\033[H\033[2J");
    }

    private void attachmentsHandler() {
        System.out.println("Send attachments? (Y/N)");
        String answer = readConsole.nextLine();
        if (answer == "Y") {
            System.out.println("Please enter the path of attachments, separated by comma:");
            String attachments = readConsole.hasNextLine() ? readConsole.nextLine() : "";
            this.attachments = attachments.split(", ");
        }
    }

    private void compose() {
        System.out.println("Compose new email:");
        // Recipients
        System.out.print("To: ");
        String recipientsTo = readConsole.nextLine();
        this.recipientsTo = recipientsTo.split(", ");
        System.out.print("Cc: ");
        String recipientsCc = readConsole.hasNextLine() ? readConsole.nextLine() : "";
        this.recipientsCc = recipientsCc.split(", ");
        System.out.print("Bcc: ");
        String recipientsBcc = readConsole.hasNextLine() ? readConsole.nextLine() : "";
        this.recipientsBcc = recipientsBcc.split(", ");

        // Subject and content
        System.out.print("Subject: ");
        subject = readConsole.hasNextLine() ? readConsole.nextLine() : "";
        System.out.print("Content: ");
        content = readConsole.hasNextLine() ? readConsole.nextLine() : "";
        attachmentsHandler();
        // clear console
        clearConsole();
    }

    public void send() throws IOException {
        username = "sender@localhost";
        compose();
        Message message = new Message(username, recipientsTo, recipientsCc, recipientsBcc, subject, content,
                attachments);
        Envelope envelope = new Envelope(message, "localhost");
        SMTPSocket smtpSocket = new SMTPSocket("localhost", 2225);

        try {
            smtpSocket.connect();
            smtpSocket.sendEmail(envelope);
        } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
    }
}
