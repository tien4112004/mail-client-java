package Envelope;

import org.junit.jupiter.api.Test;
import java.net.UnknownHostException;
import static org.junit.jupiter.api.Assertions.*;
import Message.Message;

class EnvelopeTest {
    @Test
    void testDefaultConstructor() {
        Envelope envelope = new Envelope();
        assertEquals("", envelope.sender);
        assertArrayEquals(new String[0], envelope.recipients);
        assertEquals("", envelope.destHost);
        assertNull(envelope.destIP);
        assertNull(envelope.message);
    }

    @Test
    void testConstructorWithMessage() {
        String sender = "sender@localhost";
        String[] recipients = { "recipient@localhost" };
        String subject = "Test Email";
        String content = "This is a test email";
        String[] attachments = {};
        Message message = new Message(sender, recipients, new String[] {}, new String[] {}, subject, content,
                attachments);

        assertDoesNotThrow(() -> {
            Envelope envelope = new Envelope(message, "localhost");
            assertEquals(sender, envelope.sender);
            assertArrayEquals(recipients, envelope.recipients);
            assertEquals("localhost", envelope.destHost);
            assertNotNull(envelope.destIP);
            assertEquals(message, envelope.message);
        });
    }
}