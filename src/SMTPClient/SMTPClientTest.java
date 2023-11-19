package SMTPClient;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import Envelope.Envelope;
import Message.Message;
import java.net.UnknownHostException;

class SMTPClientTest {
    @Test
    void testSMTPClient() {
        SMTPClient client = new SMTPClient("localhost", 2225);
        assertEquals("localhost", client.getSMTPServer());
        assertEquals(2225, client.getPort());
    }

    @Test
    void testSendEmail() throws UnknownHostException, IOException {
        SMTPClient client = new SMTPClient("localhost", 2225);
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
        assertDoesNotThrow(() -> client.sendEmail(envelope));
    }
}