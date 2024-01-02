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
    private String SMTPServer;
    private int SMTPPort;

    public SendEmails(String SMTPServer, int SMTPPort, String sender, InputHandler inputHandler) {
        this.SMTPServer = SMTPServer;
        this.SMTPPort = SMTPPort;
        this.sender = sender;
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

        try {
            System.out.println(ANSI_TEXT_YELLOW + "Sending email..." + ANSI_RESET);
            SMTPSocket smtpSocket = new SMTPSocket(SMTPServer, SMTPPort);
            Message message = new Message(sender, recipientsTo, recipientsCc,
                    recipientsBcc, subject, content,
                    attachments);
            smtpSocket.connect();
            smtpSocket.sendEmail(message);
            displaySuccessMessage("Email sent!");
        } catch (Exception e) {
            displayErrorMessage(e.getMessage());
            // e.printStackTrace();
            return;
        } finally {
            // smtpSocket.quit();
            sleep(TIME_2_5_SECONDS);
            clearConsole();
        }
    }
}