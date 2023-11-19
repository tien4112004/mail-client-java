package Message;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    @Test
    void testMessageConstructor() {
        String sender = "sender@localhost";
        String[] recipientsTo = { "recipient1@localhost" };
        String[] recipientsCc = { "recipient2@localhost" };
        String[] recipientsBcc = { "recipient3@localhost" };
        String subject = "Test Email";
        String content = "This is a test email";
        String[] attachments = {};

        Message message = new Message(sender, recipientsTo, recipientsCc, recipientsBcc, subject, content, attachments);

        assertEquals(sender, message.getSender());
        assertArrayEquals(new String[] { sender, recipientsTo[0], recipientsCc[0], recipientsBcc[0] },
                message.getRecipients());
    }

    @Test
    void testBuildRecipientString() {
        String[] recipients = { "recipient1@localhost", "recipient2@localhost" };
        Message message = new Message("sender@localhost", recipients, new String[] {}, new String[] {}, "Test Email",
                "This is a test email", new String[] {});

        String expected = "recipient1@localhost,recipient2@localhost";
        String actual = message.buildRecipientString(recipients);

        assertEquals(expected, actual);
    }
}