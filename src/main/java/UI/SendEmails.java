package UI;

import java.io.IOException;
// import java.util.Scanner;

import Message.Message;
import Socket.SMTPSocket;

public class SendEmails extends UI {
    private String sender = "example@localhost";
    private String[] recipientsTo;
    private String[] recipientsCc;
    private String[] recipientsBcc;
    private String subject;
    private String content;
    private String[] attachments;

    public SendEmails() {
        this.inputHandler = super.inputHandler;
    }

    private void compose() {
        System.out.printf("%sPlease input the following informations, left blank to skip.%s\n", ANSI_TEXT_YELLOW,
                ANSI_RESET);
        System.out.println("Sender: " + sender);

        System.out.printf("Recipients: %s[Input the email address, seperated by commas]%s\n", ANSI_TEXT_YELLOW,
                ANSI_RESET);
        recipientsTo = getInputList("To: ");
        recipientsCc = getInputList("Cc: ");
        recipientsBcc = getInputList("Bcc: ");

        System.out.print("Subject: ");
        subject = System.console().readLine();

        attachments = getInputList("Attachments: " + ANSI_TEXT_YELLOW
                + "[Input the directory of attachment, seperated by commas]" + ANSI_RESET + "\n");

        // Read content from user
        System.out.printf("Content: %s[End with a single dot on a line '.']%s\n", ANSI_TEXT_YELLOW, ANSI_RESET);
        String line;
        content = "";
        while ((line = System.console().readLine()) != null) {
            if (line.equals("."))
                break;
            content += line + "\n";
        }
    }

    private String[] getInputList(String prompt) {
        String input = inputHandler.dialog(prompt).trim();
        return input.split(",");
    }

    public void send() {
        compose();
        Message message = new Message(sender, recipientsTo, recipientsCc, recipientsBcc, subject, content,
                attachments);
        SMTPSocket smtpSocket = new SMTPSocket("localhost", 2225);

        System.out.println(ANSI_TEXT_YELLOW + "Sending email..." + ANSI_RESET);

        try {
            smtpSocket.connect();
            smtpSocket.sendEmail(message);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.printf("%s[ERROR]%s " + e.getMessage() + "\n", ANSI_TEXT_RED, ANSI_RESET);
            return;
        }

        System.out.println(ANSI_TEXT_GREEN + "Email sent." + ANSI_RESET);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        clearConsole();
    }
}
