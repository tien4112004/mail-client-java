package Email;

// import java.security.cert.CRL;
import java.text.SimpleDateFormat;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;

import java.util.Base64;
import java.util.Date;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class Email {
    private static final String CRLF = "\r\n";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String ERROR_RECIPIENT_EMPTY = "[ERROR][Message] Recipients cannot be empty.";
    private static final String MIME_VERSION = "MIME-Version: 1.0";
    private static final String USER_AGENT = "User-Agent: "; // Add User-Agent's name here
    private static final String CONTENT_LANGUAGE = "Content-Language: en-US";
    private static final String CONTENT_TYPE_TEXT = "Content-Type: text/plain; charset=UTF-8; format=flowed";
    private static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding: 7bit";
    private static final String CONTENT_DISPOSITION = "Content-Disposition: attachment; filename=\"%s\"";
    private static final String CONTENT_TRANSFER_ENCODING_BASE64 = "Content-Transfer-Encoding: base64";

    public String header = "";
    public String body = "";

    private String sender;
    private String[] recipientsTo;
    private String[] recipientsCc;
    private String[] recipientsBcc;
    private String subject;
    private String content;
    private String[] attachmentDirectories;

    public Email(String sender, String[] recipientsTo, String[] recipientsCC, String[] recipientsBCC,
            String subject, String content, String... attachments) {
        // handle null value 
        this.sender = (sender == null) ? "" : sender.trim();
        this.subject = (subject == null) ? "" : subject.trim();
        this.recipientsTo = recipientsTo;
        this.recipientsCc = recipientsCC;
        this.recipientsBcc = recipientsBCC;
        this.content = content.trim();
        this.attachmentDirectories = attachments;

        validateRecipients();

        String boundary = processAttachments(attachments);
        processMessageHeader();
        processMessageContent(content);

        if (attachments != null)
            processAttachments(attachments, boundary);
    }

    private String buildRecipientString(String[] recipients) {
        if (recipients == null || recipients.length == 0) {
            return "";
        }
        return String.join(", ", recipients);
    }

    private void validateRecipients() {
        boolean hasRecipientsTo = recipientsTo != null && recipientsTo.length > 0 && recipientsTo[0].length() > 0;
        if (!hasRecipientsTo)
            recipientsTo = new String[0];
        boolean hasRecipientsCc = recipientsCc != null && recipientsCc.length > 0 && recipientsCc[0].length() > 0;
        if (!hasRecipientsCc)
            recipientsCc = new String[0];
        boolean hasRecipientsBcc = recipientsBcc != null && recipientsBcc.length > 0 && recipientsBcc[0].length() > 0;
        if (!hasRecipientsBcc)
            recipientsBcc = new String[0];

        if (!hasRecipientsTo && !hasRecipientsCc && !hasRecipientsBcc) {
            throw new IllegalArgumentException(ERROR_RECIPIENT_EMPTY);
        }
    }

    private String processAttachments(String[] attachments) {
        String boundary = "";
        boolean hasAttachments = attachments != null && attachments.length > 0 && attachments[0].length() > 0;
        if (hasAttachments) {
            boundary = "------------" + UUID.randomUUID().toString().replaceAll("-", "");
            header = "Content-Type: multipart/mixed; boundary=\"" + boundary + "\"" + CRLF;
            body = CRLF + "This is a multi-part message in MIME format." + CRLF + "--" + boundary + CRLF;
        }
        return boundary;
    }

    private void processMessageHeader() {
        String recipientsToString = buildRecipientString(recipientsTo);
        String recipientsCcString = buildRecipientString(recipientsCc);
        if (recipientsToString.isEmpty() && recipientsCcString.isEmpty()) {
            recipientsToString = "undisclosed-recipients";
        }

        String messageID = "<" + UUID.randomUUID().toString() + ">";
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
        String sendDate = format.format(new Date());

        header += "Message-ID: " + messageID + CRLF;
        header += "Date: " + sendDate + CRLF;
        header += MIME_VERSION + CRLF;
        header += USER_AGENT + CRLF;
        header += CONTENT_LANGUAGE + CRLF;
        header += "From: " + this.sender + CRLF;
        header += !recipientsToString.isEmpty() ? "To: " + recipientsToString + CRLF : "";
        header += !recipientsCcString.isEmpty() ? "Cc: " + recipientsCcString + CRLF : "";
        header += "Subject: " + this.subject + CRLF;
    }

    private void processMessageContent(String content) {
        content = content.trim();
        content = content.replaceAll("(.{76})", "$1" + CRLF);
        body += CONTENT_TYPE_TEXT + CRLF;
        body += CONTENT_TRANSFER_ENCODING + CRLF + CRLF;
        body += content + CRLF + CRLF;
    }

    private void processAttachments(String[] attachments, String boundary) throws IllegalArgumentException {
        if (attachments == null || attachments.length == 0 || attachments[0].length() == 0) {
            return;
        }
        long fileSize = 0;
        for (String attachment : attachments) {
            try {
                Path filePath = Paths.get(attachment);
                fileSize += validateFileSize(filePath);
                if (fileSize > 3 * 1024 * 1024) {
                    throw new IllegalArgumentException("File size exceeds 3MB");
                }
                String MimeType = new MimetypesFileTypeMap().getContentType(filePath.toString());
                byte[] fileContent = Files.readAllBytes(filePath);
                String encodedFile = Base64.getEncoder().encodeToString(fileContent).replaceAll("(.{76})",
                        "$1" + CRLF);
                body += "--" + boundary + CRLF;
                body += "Content-Type: " + MimeType + ";";
                body += " charset=" + DEFAULT_CHARSET + ";";
                body += " name=\"" + filePath.getFileName() + "\"" + CRLF;
                body += String.format(CONTENT_DISPOSITION, filePath.getFileName()) + CRLF;
                body += CONTENT_TRANSFER_ENCODING_BASE64 + CRLF + CRLF;
                body += encodedFile + CRLF;
            } catch (Exception e) {
                throw new IllegalArgumentException("Error in reading attachment: " + e.getMessage());
            }
        }
        body += "--" + boundary + "--" + CRLF;
    }

    private long validateFileSize(Path filePath) throws IOException, IllegalArgumentException {
        long fileSize = Files.size(filePath);
        if (fileSize > 3 * 1024 * 1024) {
            throw new IllegalArgumentException("File size exceeds 3MB");
        }
        return fileSize;
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

    public String getContent() {
        return content;
    }

    public String[] getAttachments() {
        return attachmentDirectories;
    }

    public String toString() {
        return header + body;
    }

    public void saveMail(String mailId) {
        String rawMessage = toString();
        try {
            Files.write(Paths.get("Inbox/" + mailId + ".msg"), rawMessage.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
