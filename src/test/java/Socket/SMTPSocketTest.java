package Socket;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;

import Message.Message;

public class SMTPSocketTest {
    private SMTPSocket SMTPSocket;

    @Before
    public void setUp() throws IOException {
        SMTPSocket = new SMTPSocket("localhost", 2225);
        SMTPSocket.connect();
    }

    @Test
    public void testDoCommand() {
        assertDoesNotThrow(() -> {
            SMTPSocket.doCommand("HELO localhost", "250");
        });
    }

    @Test
    public void testDoCommandThrowsException() {
        assertThrows(Exception.class, () -> {
            SMTPSocket.doCommand("TEST", "250");
        });
    }

    @Test
    public void testSendEmail() throws UnknownHostException, IOException {
        String sender = "example@localhost";
        String[] recipientsTo = new String[] { "pttien@fit.hcmus.edu.vn" };
        String[] recipientsCc = new String[] { "example@localhost" };
        String[] recipientsBcc = new String[] { "example@fit.hcmus.edu.vn" };
        String subject = "Test Email";
        String content = "This is a test email";
        Message message = new Message(sender, recipientsTo, recipientsCc,
                recipientsBcc, subject, content, new String[] {});
        assertArrayEquals(new String[] { "lttin@fit.hcmus.edu.vn" },
                message.getRecipients());
        assertDoesNotThrow(() -> SMTPSocket.sendEmail(message));
    }

    @Test
    public void testSendEmailWithAttachments() throws UnknownHostException, IOException {
        String sender = "example@localhost";
        String[] recipientsTo = new String[] { "pttien@fit.hcmus.edu.vn" };
        String[] recipientsCc = new String[] { "example@localhost" };
        String[] recipientsBcc = new String[] { "example@fit.hcmus.edu.vn" };
        String subject = "Test Email w/ attachments";
        String content = "This is a test email with long content...This is a test email with long content...This is a test email with long content...This is a test email with long content...This is a test email with long content...This is a test email with long content...This is a test email with long content...This is a test email with long content...";
        String[] attachments = { "src/test/java/attachmentsTest/test.cpp", "src/test/java/attachmentsTest/test.txt" };
        Message message = new Message(sender, recipientsTo, recipientsCc,
                recipientsBcc, subject, content, attachments);
        assertArrayEquals(new String[] { "pttien@fit.hcmus.edu.vn", "example@localhost", "example@fit.hcmus.edu.vn" },
                message.getRecipients());
        assertDoesNotThrow(() -> SMTPSocket.sendEmail(message));
    }

    @Test
    public void testSendEmailWithPdf() throws UnknownHostException, IOException {
        String sender = "example@localhost";
        String[] recipientsTo = new String[] { "pttien@fit.hcmus.edu.vn" };
        String[] recipientsCc = new String[] { "example@localhost" };
        String[] recipientsBcc = new String[] { "example@fit.hcmus.edu.vn" };
        String subject = "pdf test";
        String content = "Pdf test";
        String[] attachments = { "src/test/java/attachmentsTest/test.pdf" };
        Message message = new Message(sender, recipientsTo, recipientsCc,
                recipientsBcc, subject, content, attachments);
        assertArrayEquals(new String[] { "pttien@fit.hcmus.edu.vn", "example@localhost", "example@fit.hcmus.edu.vn" },
                message.getRecipients());
        assertDoesNotThrow(() -> SMTPSocket.sendEmail(message));
    }
}
