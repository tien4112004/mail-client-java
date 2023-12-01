package Message;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

public class MessageParser {
    private static final String CRLF = "\r\n";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String ERROR_RECIPIENT_EMPTY = "[ERROR][Message] Recipients cannot be empty.";
    private static final String ERROR_FILE_NOT_FOUND = "[ERROR][Message] File \"%s\" not found.";
    private static final String ERROR_CANNOT_WRITE_FILE = "[ERROR][Message] Cannot write file \"%s\".";
    private static final String MIME_VERSION = "MIME-Version: 1.0";
    private static final String USER_AGENT = "User-Agent: "; // Add User-Agent's name here
    private static final String CONTENT_LANGUAGE = "Content-Language: en-US";
    private static final String CONTENT_TYPE_TEXT = "Content-Type: text/plain; charset=UTF-8; format=flowed";
    private static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding: 7bit";
    private static final String CONTENT_DISPOSITION = "Content-Disposition: attachment; filename=\"%s\"";
    private static final String CONTENT_TRANSFER_ENCODING_BASE64 = "Content-Transfer-Encoding: base64";

    private String sender;
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
        }
    }

    private void parseMultipartBody() {
        String[] bodyLines = body.split(CRLF);
        boolean isContent = false;
        for (int i = 0; i < bodyLines.length; i++) {
            if (bodyLines[i].startsWith("--")) {
                if (isContent) {
                    break;
                }
                continue;
            }
            if (bodyLines[i].startsWith("Content-Type: ")) {
                isContent = parseContentType(bodyLines[i]);
            } else if (bodyLines[i].startsWith("Content-Transfer-Encoding: 7bit")) {
                parseContent(bodyLines[i], i + 2, bodyLines);
            } else if (bodyLines[i].startsWith("Content-Disposition: ")) {
                parseFile(bodyLines[i], i + 4, bodyLines);
            }
        }
    }

    private boolean parseContentType(String line) {
        String contentType = line.substring(14).trim();
        return contentType.startsWith("text/plain");
    }

    protected void parseContent(String line, int startIndex, String[] bodyLines) {
        String encodedContent = joinLinesUntilBoundary(startIndex, bodyLines);
        content = encodedContent.toString();
    }

    public void parseFile(String line, int startIndex, String[] bodyLines) {
        String contentDisposition = line.substring(21).trim();
        if (contentDisposition.startsWith("attachment")) {
            String fileName = contentDisposition.substring(contentDisposition.indexOf("filename=") + 10,
                    contentDisposition.length() - 1);
            String encodedFileContent = joinLinesUntilBoundary(startIndex, bodyLines);
            String fileContent = new String(Base64.getDecoder().decode(encodedFileContent));
            saveAttachment(fileName, fileContent);
        }
    }

    private String joinLinesUntilBoundary(int startIndex, String[] lines) {
        StringBuilder joinedLines = new StringBuilder();

        for (int i = startIndex; i < lines.length && !lines[i].startsWith("--"); i++) {
            joinedLines.append(lines[i]);
        }

        return joinedLines.toString();
    }

    private void saveAttachment(String fileName, String fileContent) {
        try {
            Files.write(Paths.get("attachments/write/" + fileName), fileContent.getBytes());
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
}
