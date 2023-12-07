package Socket;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;

import Envelope.Envelope;
import Message.Message;
// import SMTPClient.SMTPClient;
import Socket.SMTPSocket;

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
        Envelope envelope = new Envelope(message, "localhost");
        assertArrayEquals(new String[] { "pttien@fit.hcmus.edu.vn", "example@localhost", "example@fit.hcmus.edu.vn" },
                envelope.recipients);
        assertDoesNotThrow(() -> SMTPSocket.sendEmail(envelope));
    }

    @Test
    public void testSendEmailWithAttachments() throws UnknownHostException, IOException {
        String sender = "example@localhost";
        String[] recipientsTo = new String[] { "pttien@fit.hcmus.edu.vn" };
        String[] recipientsCc = new String[] { "example@localhost" };
        String[] recipientsBcc = new String[] { "example@fit.hcmus.edu.vn" };
        String subject = "Test Email w/ attachments";
        String content = "This is a test email with long content...This is a test email with long content...This is a test email with long content...This is a test email with long content...This is a test email with long content...This is a test email with long content...This is a test email with long content...This is a test email with long content...";
        String[] attachments = { "attachments/test.cpp", "attachments/test.txt" };
        Message message = new Message(sender, recipientsTo, recipientsCc,
                recipientsBcc, subject, content, attachments);
        Envelope envelope = new Envelope(message, "localhost");
        assertArrayEquals(new String[] { "pttien@fit.hcmus.edu.vn", "example@localhost", "example@fit.hcmus.edu.vn" },
                envelope.recipients);
        assertDoesNotThrow(() -> SMTPSocket.sendEmail(envelope));
    }

    @Test
    public void testSendEmailWithPdf() throws UnknownHostException, IOException {
        String sender = "example@localhost";
        String[] recipientsTo = new String[] { "pttien@fit.hcmus.edu.vn" };
        String[] recipientsCc = new String[] { "example@localhost" };
        String[] recipientsBcc = new String[] { "example@fit.hcmus.edu.vn" };
        String subject = "pdf test";
        String content = "Pdf test";
        String[] attachments = { "attachments/test.pdf" };
        Message message = new Message(sender, recipientsTo, recipientsCc,
                recipientsBcc, subject, content, attachments);
        Envelope envelope = new Envelope(message, "localhost");
        assertArrayEquals(new String[] { "pttien@fit.hcmus.edu.vn", "example@localhost", "example@fit.hcmus.edu.vn" },
                envelope.recipients);
        assertDoesNotThrow(() -> SMTPSocket.sendEmail(envelope));
    }
}
