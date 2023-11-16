
// import java.security.cert.CRL;
import java.text.SimpleDateFormat;
import java.util.*;

public class Message {
    public String header;
    public String body;

    private String sender;
    private String recipientsType;
    private String recipients;
    private String subject;

    private static final String CRLF = "\r\n";

    public Message(String sender, String recipientsType, String recipients, String subject, String content) {
        // String contentType, String charset, String format, String
        // contentTransferEncoding) {
        String messageID = "<" + UUID.randomUUID().toString() + ">";
        this.sender = sender.trim();
        this.recipientsType = recipientsType.trim();
        this.recipients = recipients.trim();
        this.subject = subject.trim();
        this.body = content.trim();

        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
        String sendDate = format.format(new Date());

        header = "Message-ID: " + messageID + CRLF;
        header += "Date: " + sendDate + CRLF;
        header += "MIME-Version: 1.0" + CRLF;
        header += "User-Agent: " + CRLF; // Add User-Agent's name here
        // TODO: Content-Language
        header += "From: " + this.sender + CRLF;
        header += this.recipientsType + ": " + this.recipients + CRLF; // recipientsType = To or Cc or Bcc
        header += "Subject: " + this.subject + CRLF;
        header += "Content-Type: text/plain;" + " charset=UTF-8" + "; format=flowed" + CRLF;
        // TODO
        // header += "Content-Type: " + contentType + CRLF;
        // header += "Content-Transfer-Encoding: " + contentTransferEncoding + CRLF;

        body = content;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipients() {
        return recipients;
    }

    /*
     * private boolean isValidEmailAddress(String email) {
     * String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
     * 
     * int atPosition = email.indexOf('@');
     * boolean isInvalidEmail = email.matches(regex) && (atPosition < 1 ||
     * email.length() - atPosition <= 1)
     * && atPosition != email.lastIndexOf('@');
     * return !isInvalidEmail;
     * }
     */

    public String toString() {
        return header + CRLF + body + CRLF + CRLF;
    }
}
