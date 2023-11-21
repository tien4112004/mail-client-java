package Editor;

import Message.*;

public class Editor {
    private static Message message;
    private static String sender = "test@localhost"; // TODO: get this from JSON
    private static String[] recipientsTo;
    private static String[] recipientsCc;
    private static String[] recipientsBcc;
    private static String subject;
    private static String content;
    private static String[] attachments;

    public static Message newMessageFromConsole() {
        System.out.println("Please input the following informations, left blank to skip");
        System.out.println("Sender: " + sender);

        System.out.println("Recipients: [Input the email address, seperated by commas]");
        recipientsTo = getInputList("To: ");
        recipientsCc = getInputList("Cc: ");
        recipientsBcc = getInputList("Bcc: ");

        System.out.print("Subject: ");
        subject = System.console().readLine();

        attachments = getInputList("Attachments: [Input the directory of attachment, seperated by commas]\n");

        // Read content from user
        System.out.println("Content: [Press Ctrl+D to finish input]");
        String line;
        content = "";
        while ((line = System.console().readLine()) != null) {
            content += line + "\n";
        }

        return message = new Message(sender, recipientsTo, recipientsCc, recipientsBcc, subject, content, attachments);
    }

    private static String[] getInputList(String prompt) {
        System.out.print(prompt);
        String input = System.console().readLine();
        input.trim();
        return input.split(",");
    }

    public static void main(String args[]) {
        newMessageFromConsole();
        System.out.println(message.toString());
    }
}