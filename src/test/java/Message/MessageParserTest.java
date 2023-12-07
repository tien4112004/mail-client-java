package Message;

// import Message.MessageParser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class MessageParserTest {
    @Test
    public void testParseHeader() {
        // Arrange
        String[] lines = {
                "From: sender@example.com",
                "To: recipient@example.com",
                "Cc: cc@example.com",
                "Subject: Test Subject",
                "Content-Type: text/plain",
                "",
                "Hello world"
        };
        MessageParser parser = new MessageParser();

        // Act
        int headerEndIndex = parser.parseHeader(lines);

        // Assert
        assertEquals(5, headerEndIndex);
        assertEquals("sender@example.com", parser.getSender());
        assertArrayEquals(new String[] { "recipient@example.com" },
                parser.getRecipientsTo());
        assertArrayEquals(new String[] { "cc@example.com" },
                parser.getRecipientsCc());
        assertEquals("Test Subject", parser.getSubject());
        assertEquals("text/plain", parser.getContentType());
    }

    @Test
    public void testParse() {
        // Arrange
        String rawMessage = "From: sender@example.com\r\nTo: recipient@example.com\r\nSubject: Hello world!\r\n\r\nHello world";
        MessageParser parser = new MessageParser();

        // Act
        parser.parse(rawMessage);

        // Assert
        assertEquals("sender@example.com", parser.getSender());
        assertArrayEquals(new String[] { "recipient@example.com" }, parser.getRecipientsTo());
        assertEquals("Hello world", parser.getContent());
    }

    @Test
    public void testParseContent() {
        // Arrange
        String[] bodyLines = {
                "Content-Transfer-Encoding: 7bit",
                "",
                "Hello world",
                "--"
        };
        MessageParser parser = new MessageParser();

        // Act
        parser.parseContent(bodyLines[0], 2, bodyLines);

        // Assert
        assertEquals("Hello world", parser.getContent());
    }

    @Test
    public void testParseFile() throws Exception {
        // Arrange
        String[] bodyLines = {
                "Content-Disposition: attachment; filename=\"test.txt\"",
                "",
                "SGVsbG8gd29ybGQ=",
                "--",
                "Content-Disposition: attachment; filename=\"test.cpp\"",
                ""
        };
        MessageParser parser = new MessageParser();

        // Act
        parser.parseFile(bodyLines[0], 2, bodyLines, "src/test/java/attachments/write");
        parser.parseFile(bodyLines[4], 6, bodyLines, "src/test/java/attachments/write");

        // Assert
        assertTrue(Files.exists(Paths.get("src/test/java/attachments/write/test.txt")));
        assertEquals(new String(Files.readAllBytes(Paths.get("src/test/java/attachments/test.txt"))),
                new String(Files.readAllBytes(Paths.get("src/test/java/attachments/write/test.txt"))));
    }

    @Test
    public void testParseFullEmail() throws IOException {
        // Arrange
        Path filePath = Paths.get("src/test/java/Message/testEmail.msg");
        String rawEmail = new String(Files.readAllBytes(filePath));
        MessageParser parser = new MessageParser();

        // Act
        parser.parse(rawEmail);

        // Assert
        assertEquals("example@localhost", parser.getSender());
        assertArrayEquals(new String[] { "pttien@fit.hcmus.edu.vn" }, parser.getRecipientsTo());
        assertArrayEquals(new String[] { "example@localhost" }, parser.getRecipientsCc());
        assertEquals("Test Email", parser.getSubject());
        assertEquals("multipart/mixed; boundary=\"------------32dad99284e04931b6fd17697f2c2f61\"",
                parser.getContentType());
        assertEquals("This is a test email", parser.getContent());
        assertTrue(Files.exists(Paths.get("src/test/java/attachments/test.txt")));
        assertEquals("cpp test", new String(Files.readAllBytes(Paths.get("src/test/java/attachments/test.cpp"))));
    }

    @Test
    public void testParseFullEmailWithPdf() {
        // Arrange
        Path filePath = Paths.get("src/test/java/Message/testPdf.msg");
        String rawEmail = "";
        try {
            rawEmail = new String(Files.readAllBytes(filePath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        MessageParser parser = new MessageParser();

        // Act
        parser.parse(rawEmail);

        // Assert
        assertEquals("example@localhost", parser.getSender());
        assertArrayEquals(new String[] { "pttien@fit.hcmus.edu.vn" }, parser.getRecipientsTo());
        assertArrayEquals(new String[] { "example@localhost" }, parser.getRecipientsCc());
        assertEquals("pdf test", parser.getSubject());
        assertEquals("multipart/mixed; boundary=\"------------c9a195a417c24c1890187baee07e8ad5\"",
                parser.getContentType());
        assertEquals("Pdf test", parser.getContent());
        assertTrue(Files.exists(Paths.get("attachments/write/test.pdf")));
    }
}