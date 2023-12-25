package UI;

import Message.Message;
import Socket.SMTPSocket;

public class SendEmails extends UI {
    private String sender = "example@localhost"; // get from JSON
    private String[] recipientsTo;
    private String[] recipientsCc;
    private String[] recipientsBcc;
    private String subject;
    private String content;
    private String[] attachments;

    public SendEmails(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
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
        String[] result = input.split(",");
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].trim();
        }
        return result;
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
            System.out.printf("%s[ERROR]%s " + e.getMessage() + "\n", ANSI_TEXT_RED, ANSI_RESET);
            sleep(TIME_3_SECONDS);
            e.printStackTrace();
            return;
        }

        System.out.println(ANSI_TEXT_GREEN + "Email sent." + ANSI_RESET);
        sleep(TIME_3_SECONDS);

        clearConsole();
    }
}