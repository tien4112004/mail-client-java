package Message;

// import java.security.cert.CRL;
import java.text.SimpleDateFormat;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;
import javax.security.auth.login.CredentialException;

import java.util.Base64;
import java.util.Date;
import java.net.FileNameMap;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class Message {
    public String header;
    public String body;

    private String sender;
    private String[] recipientsTo;
    private String[] recipientsCc;
    private String[] recipientsBcc;
    private String subject;

    private static final String CRLF = "\r\n";
    private static final String DEFAULT_CHARSET = "UTF-8";

    public Message(String sender, String[] recipientsTo, String[] recipientsCC, String[] recipientsBCC,
            String subject, String content, String... attachments) {
        header = "";
        body = "";

        // attachments preprocessing
        String boundary = "";
        String isMIME = "";
        boolean hasAttachments = attachments.length > 0;
        if (hasAttachments) {
            boundary = "------------" + UUID.randomUUID().toString().replaceAll("-", "");
            header = "Content-Type: multipart/mixed; boundary=\"" + boundary + "\"" + CRLF;
            isMIME = (hasAttachments
                    ? CRLF + "This is a multi-part message in MIME format." + CRLF + "--" + boundary + CRLF
                    : "");
        }

        // recipients preprocessing
        this.recipientsTo = recipientsTo;
        this.recipientsCc = recipientsCC;
        this.recipientsBcc = recipientsBCC;

        boolean hasRecipientsTo = recipientsTo.length > 0;
        boolean hasRecipientsCc = recipientsCc.length > 0;
        boolean hasRecipientsBcc = recipientsBcc.length > 0;

        String recipientsToString = buildRecipientString(recipientsTo);
        String recipientsCcString = buildRecipientString(recipientsCc);

        if (!hasRecipientsTo && !hasRecipientsCc && hasRecipientsBcc) {
            recipientsToString = "undisclosed-recipients";
            hasRecipientsTo = true;
        }

        String messageID = "<" + UUID.randomUUID().toString() + ">";
        this.sender = sender.trim();
        this.subject = subject.trim();
        content = content.trim();
        // if the message is too long to fit in a single line, it will be split into
        // multiple lines. Each line will be separated by CRLF
        content = content.replaceAll("(.{76})", "$1" + CRLF);

        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
        String sendDate = format.format(new Date());

        header += "Message-ID: " + messageID + CRLF;
        header += "Date: " + sendDate + CRLF;
        header += "MIME-Version: 1.0" + CRLF;
        header += "User-Agent: " + CRLF; // Add User-Agent's name here
        header += "Content-Language: en-US" + CRLF;
        header += "From: " + this.sender + CRLF;
        header += hasRecipientsTo ? "To: " + recipientsToString + CRLF : "";
        header += hasRecipientsCc ? "Cc: " + recipientsCcString + CRLF : "";
        header += "Subject: " + this.subject + CRLF;
        body += isMIME;
        body += "Content-Type: text/plain;" + " charset=UTF-8" + "; format=flowed" + CRLF; // MimeType of the content
        body += "Content-Transfer-Encoding: 7bit" + CRLF + CRLF;

        body += content + CRLF + CRLF;

        if (!hasAttachments)
            return;

        for (String attachment : attachments) {
            try {
                Path filePath = Paths.get(attachment);
                String MimeType = new MimetypesFileTypeMap().getContentType(filePath.toString());
                byte[] fileContent = Files.readAllBytes(filePath);
                String encodedFile = Base64.getEncoder().encodeToString(fileContent).replaceAll("(.{76})",
                        "$1" + CRLF);
                body += "--" + boundary + CRLF;
                body += "Content-Type: " + MimeType + ";";
                body += " charset=" + DEFAULT_CHARSET + ";";
                body += " name=\"" + filePath.getFileName() + "\"" + CRLF;

                body += "Content-Disposition: attachment;";
                body += " filename=\"" + filePath.getFileName() + "\"" + CRLF;

                body += "Content-Transfer-Encoding: base64" + CRLF + CRLF;

                body += encodedFile + CRLF;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            body += "--" + boundary + "--" + CRLF;
        }
    }

    public String getSender() {
        return sender;
    }

    public String[] getRecipients() {
        String recipients[] = new String[recipientsTo.length + recipientsCc.length + recipientsBcc.length];
        System.arraycopy(recipientsTo, 0, recipients, 0, recipientsTo.length);
        System.arraycopy(recipientsCc, 0, recipients, recipientsTo.length, recipientsCc.length);
        System.arraycopy(recipientsBcc, 0, recipients, recipientsTo.length + recipientsCc.length,
                recipientsBcc.length);

        return recipients;
    }

    public String getSubject() {
        return subject;
    }

    protected String buildRecipientString(String[] recipients) {
        String recipientString = "";
        for (String recipient : recipients) {
            recipientString += recipient + ',';
        }
        if (recipientString.length() == 0)
            return "";
        return recipientString.substring(0, recipientString.length() - 1); // remove the last ','
    }
    //
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
        return header + body;
    }
}
