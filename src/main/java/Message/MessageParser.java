package Message;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.text.ParseException;

public class MessageParser {
    private static final String CRLF = "\r\n";
    private static final String ERROR_FILE_NOT_FOUND = "[ERROR][Message] File \"%s\" not found.";
    private static final String ERROR_CANNOT_WRITE_FILE = "[ERROR][Message] Cannot write file \"%s\".";
    private static final String DEFAULT_DATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";
    private static final String CONTENT_TYPE_TEXT = "Content-Type: text/plain; charset=UTF-8; format=flowed";
    private static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding: 7bit";
    private static final String CONTENT_DISPOSITION = "Content-Disposition: attachment; filename=\"%s\"";
    private static final String CONTENT_TRANSFER_ENCODING_BASE64 = "Content-Transfer-Encoding: base64";
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private String sender;
    private String date;
    private String[] recipientsTo;
    private String[] recipientsCc;
    private String subject;
    private String contentType;
    private String content;
    private String body;
    private String header;

    public MessageParser() {
        // do nothing
    }

    public void parse(String rawMessage) {
        String[] lines = rawMessage.split(CRLF);
        int headerEndIndex = parseHeader(lines);
        body = String.join(CRLF, Arrays.copyOfRange(lines, headerEndIndex + 1, lines.length));

        if (contentType != null && contentType.startsWith("multipart/mixed")) {
            parseMultipartBody();
        } else {
            content = body;
        }
    }

    public void quickParse(String rawMessage) {
        String[] lines = rawMessage.split(CRLF);
        parseHeader(lines);
    }

    public boolean hasAttachment() {
        return contentType != null && contentType.startsWith("multipart/mixed");
    }

    protected int parseHeader(String[] lines) {
        int headerEndIndex = 0;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].isEmpty()) {
                headerEndIndex = i;
                break;
            }
            parseHeaderLine(lines[i]);
        }
        header = String.join(CRLF, Arrays.copyOfRange(lines, 0, headerEndIndex));
        return headerEndIndex;
    }

    private void parseHeaderLine(String line) {
        if (line.startsWith("From: ")) {
            sender = line.substring(6).trim();
        } else if (line.startsWith("To: ")) {
            recipientsTo = line.substring(4).trim().split(", ");
        } else if (line.startsWith("Cc: ")) {
            recipientsCc = line.substring(4).trim().split(", ");
        } else if (line.startsWith("Subject: ")) {
            subject = line.substring(9).trim();
        } else if (line.startsWith("Content-Type: ")) {
            contentType = line.substring(14).trim();
        } else if (line.startsWith("Date: ")) {
            date = line.substring(6).trim();
        }
    }

    private void parseMultipartBody() {
        String[] bodyLines = body.split(CRLF);
        boolean isContent = false;
        for (int i = 0; i < bodyLines.length; i++) {
            if (bodyLines[i].startsWith("--")) { // reach boundary
                isContent = false;
                continue;
            }
            if (hasContent(bodyLines[i])) {
                isContent = true;
            }

            if (!isContent)
                continue;

            if (bodyLines[i].startsWith(CONTENT_TRANSFER_ENCODING)) {
                parseContent(bodyLines[i], i + 2, bodyLines);
                i += countLinesUntilBoundary(i + 2, bodyLines); // Skip lines of the current content
            } else if (bodyLines[i].startsWith("Content-Disposition: ")) {
                parseFile(bodyLines[i], i + 3, bodyLines, "./attachments/write/");
                i += countLinesUntilBoundary(i + 3, bodyLines); // Skip lines of the current file
            }
        }
    }

    private int countLinesUntilBoundary(int startIndex, String[] bodyLines) {
        int count = 0;
        while (!bodyLines[startIndex + count].startsWith("--")) {
            count++;
        }
        return count;
    }

    private boolean hasContent(String line) {
        return line.startsWith("Content-Type: ");
    }

    protected void parseContent(String line, int startIndex, String[] bodyLines) {
        String encodedContent = joinLinesUntilBoundary(startIndex, bodyLines);
        content = encodedContent.toString();
    }

    public void parseFile(String line, int startIndex, String[] bodyLines, String saveDirectory) {
        String contentDisposition = line.substring(21).trim();
        if (contentDisposition.startsWith("attachment")) {
            String fileName = contentDisposition.substring(contentDisposition.indexOf("filename=") + 10,
                    contentDisposition.length() - 1);
            String encodedFileContent = joinLinesUntilBoundary(startIndex, bodyLines);
            byte[] fileContent = Base64.getDecoder().decode(encodedFileContent);
            saveAttachment(fileName, fileContent, saveDirectory);
        }
    }

    private String joinLinesUntilBoundary(int startIndex, String[] lines) {
        StringBuilder joinedLines = new StringBuilder();

        for (int i = startIndex; i < lines.length && !lines[i].startsWith("--"); i++) {
            joinedLines.append(lines[i]);
        }

        return joinedLines.toString();
    }

    private void saveAttachment(String fileName, byte[] fileContent, String saveDirectory) {
        try {
            Path directoryPath = Paths.get(saveDirectory);
            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            Path filePath = directoryPath.resolve(fileName);
            Files.write(filePath, fileContent);
            System.out.println(String.format("Saved attachment \"%s\".", fileName));
        } catch (Exception e) {
            System.out.println(String.format(ERROR_CANNOT_WRITE_FILE, e.getMessage()));
        }
    }

    public String getSender() {
        return sender;
    }

    public String[] getRecipientsTo() {
        return recipientsTo;
    }

    public String[] getRecipientsCc() {
        return recipientsCc;
    }

    public String getSubject() {
        return subject;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContent() {
        return content;
    }

    public Message createMessage() {
        Message message = new Message(sender, recipientsTo, recipientsCc, EMPTY_STRING_ARRAY, subject, content);
        return message;
    }

    public String getDate(String format) {
        return formatDate(this.date, DEFAULT_DATE_FORMAT, format);
    }

    public String formatDate(String originalDate, String originalFormat, String targetFormat) {
        SimpleDateFormat originalDateFormat = new SimpleDateFormat(originalFormat);
        SimpleDateFormat targetDateFormat = new SimpleDateFormat(targetFormat);
        try {
            Date date = originalDateFormat.parse(originalDate);
            return targetDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
