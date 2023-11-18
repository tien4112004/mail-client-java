package SMTPClient;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import Envelope.Envelope;
import Message.Message;

class SMTPClientTest {
    @Test
    void testSMTPClient() {
        SMTPClient client = new SMTPClient("localhost", 2225);
        assertEquals("localhost", client.getSMTPServer());
        assertEquals(2225, client.getPort());
    }

    @Test
    void testSendEmail() {
        SMTPClient client = new SMTPClient("localhost", 2225);
        Envelope envelope = new Envelope();
        envelope.sender = "sender@localhost";
        envelope.recipients = "recipient@localhost";
        String subject = "Test Email";
        String content = "This is a test email";
        envelope.message = new Message(envelope.sender, "Cc", envelope.recipients, subject, content);
        assertDoesNotThrow(() -> client.sendEmail(envelope));
    }
}