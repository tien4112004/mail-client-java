package Email;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterAll;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class EmailParserTest {
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
        EmailParser parser = new EmailParser();

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
        EmailParser parser = new EmailParser();

        // Act
        parser.fullParse(rawMessage);

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
        EmailParser parser = new EmailParser();

        // Act
        parser.parseContent(bodyLines, 2);

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
        EmailParser parser = new EmailParser();

        // Act
        parser.parseFile(bodyLines[0], 2, bodyLines, "attachments/write");
        parser.parseFile(bodyLines[4], 6, bodyLines, "attachments/write");

        // Assert
        assertTrue(Files.exists(Paths.get("attachments/write/test.txt")));
        assertEquals(new String(Files.readAllBytes(Paths.get("src/test/java/attachmentsTest/test.txt"))),
                new String(Files.readAllBytes(Paths.get("attachments/write/test.txt"))));
    }

    // @Test
    public void testParseFullEmail() throws IOException {
        // Arrange
        Path filePath = Paths.get("src/test/java/Message/testEmail.msg");
        String rawEmail = new String(Files.readAllBytes(filePath));
        EmailParser parser = new EmailParser();

        // Act
        parser.fullParse(rawEmail);

        // Assert
        assertEquals("example@localhost", parser.getSender());
        assertArrayEquals(new String[] { "pttien@fit.hcmus.edu.vn" }, parser.getRecipientsTo());
        assertArrayEquals(new String[] { "example@localhost" }, parser.getRecipientsCc());
        assertEquals("Test Email", parser.getSubject());
        assertEquals("multipart/mixed; boundary=\"------------32dad99284e04931b6fd17697f2c2f61\"",
                parser.getContentType());
        assertEquals("This is a test email", parser.getContent());
        assertTrue(Files.exists(Paths.get("attachments/write/test.txt")));
        assertEquals("cpp test", new String(Files.readAllBytes(Paths.get("attachments/write/test.cpp"))));
    }

    @Test
    public void testParseFullEmailWithPdf() {
        // Arrange
        Path filePath = Paths.get("src/test/java/Email/testPdf.msg");
        String rawEmail = "";
        try {
            rawEmail = new String(Files.readAllBytes(filePath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        EmailParser parser = new EmailParser();

        // Act
        parser.fullParse(rawEmail);

        // Assert
        assertEquals("example@localhost", parser.getSender());
        assertArrayEquals(new String[] { "pttien@fit.hcmus.edu.vn" }, parser.getRecipientsTo());
        assertArrayEquals(new String[] { "example@localhost" }, parser.getRecipientsCc());
        assertEquals("pdf test", parser.getSubject());
        assertEquals("multipart/mixed; boundary=\"------------c9a195a417c24c1890187baee07e8ad5\"",
                parser.getContentType());
        assertEquals("Pdf test", parser.getContent());
        assertTrue(Files.exists(Paths.get("attachments/test.pdf")));
    }

    @Test
    void testParseContentOnly() {
        EmailParser parser = new EmailParser();

        // Test with a simple message
        String simpleMessage = "Content-Type: text/plain\r\nContent-Transfer-Encoding: 7bit\r\n\r\nHello, World!";
        parser.parseHeaderAndContent(simpleMessage);
        assertEquals("Hello, World!", parser.getContent());

        // Test with a multipart/mixed message
        String multipartMessage = "Content-Type: multipart/mixed; boundary=foo\r\n\r\n--foo\r\nContent-Type: text/plain\r\nContent-Transfer-Encoding: 7bit\r\n\r\nHello, World!\r\n--foo\r\nContent-Type: application/octet-stream\r\n\r\nSome binary data\r\n--foo--";
        parser.parseHeaderAndContent(multipartMessage);
        assertEquals("Hello, World!", parser.getContent());
    }

    @AfterAll
    public static void cleanUp() {
        try {
            Files.delete(Paths.get("attachments/write/test.txt"));
            Files.delete(Paths.get("attachments/write/test.cpp"));
            Files.delete(Paths.get("attachments/test.pdf"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}