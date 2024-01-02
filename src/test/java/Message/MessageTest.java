package Message;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    @Test
    void testMessageConstructor() {
        String sender = "sender@localhost";
        String[] recipientsTo = { "recipientTo1@localhost", "recipientTo2@localhost" };
        String[] recipientsCc = { "recipientCc1@localhost", "recipientCc2@localhost" };
        String[] recipientsBcc = { "recipientBcc1@localhost", "recipientBcc2@localhost" };
        String subject = "Test Email";
        String content = "This is a test email";
        String[] attachments = {};

        Message message = new Message(sender, recipientsTo, recipientsCc, recipientsBcc, subject, content, attachments);

        assertEquals(sender, message.getSender());
        assertArrayEquals(new String[] { recipientsTo[0], recipientsTo[1], recipientsCc[0], recipientsCc[1],
                recipientsBcc[0], recipientsBcc[1] }, message.getRecipients());
        assertEquals(subject, message.getSubject());
        // assertEquals(content, message.getContent());
        // assertArrayEquals(attachments, message.getAttachments());
    }

    @Test
    void testMessageConstructorWithEmptyRecipients() {
        String sender = "sender@localhost";
        String[] recipientsTo = {};
        String[] recipientsCc = {};
        String[] recipientsBcc = {};
        String subject = "Test Email";
        String content = "This is a test email";
        String[] attachments = {};

        assertThrows(IllegalArgumentException.class, () -> {
            new Message(sender, recipientsTo, recipientsCc, recipientsBcc, subject, content, attachments);
        });
    }

    @Test
    void testMessageConstructorWithAttachments() {
        String sender = "sender@localhost";
        String[] recipientsTo = { "recipientTo1@localhost", "recipientTo2@localhost" };
        String[] recipientsCc = { "recipientCc1@localhost", "recipientCc2@localhost" };
        String[] recipientsBcc = { "recipientBcc1@localhost", "recipientBcc2@localhost" };
        String subject = "Test Email";
        String content = "This is a test email";
        String[] attachments = { "src/test/java/attachmentsTest/test.cpp", "src/test/java/attachmentsTest/test.txt" };

        Message message = new Message(sender, recipientsTo, recipientsCc, recipientsBcc, subject, content, attachments);

        assertEquals(sender, message.getSender());
        assertArrayEquals(new String[] { recipientsTo[0], recipientsTo[1], recipientsCc[0], recipientsCc[1],
                recipientsBcc[0], recipientsBcc[1] }, message.getRecipients());
        assertEquals(subject, message.getSubject());
        // assertEquals(content, message.getContent());
        // assertArrayEquals(attachments, message.getAttachments());
        System.out.println(message.toString());
    }

    @Test
    void testMessageConstructorWithInvalidAttachment() {
        String sender = "sender@localhost";
        String[] recipientsTo = { "recipientTo1@localhost", "recipientTo2@localhost" };
        String[] recipientsCc = { "recipientCc1@localhost", "recipientCc2@localhost" };
        String[] recipientsBcc = { "recipientBcc1@localhost", "recipientBcc2@localhost" };
        String subject = "Test Email";
        String content = "This is a test email";
        String[] attachments = { "path/to/nonexistent/attachment" };

        assertThrows(Exception.class, () -> {
            new Message(sender, recipientsTo, recipientsCc, recipientsBcc, subject,
                    content, attachments);
        });
    }

    @Test
    void testGetRecipients() {
        String sender = "sender@localhost";
        String[] recipientsTo = { "recipientTo1@localhost", "recipientTo2@localhost" };
        String[] recipientsCc = { "recipientCc1@localhost", "recipientCc2@localhost" };
        String[] recipientsBcc = { "recipientBcc1@localhost", "recipientBcc2@localhost" };
        String subject = "Test Email";
        String content = "This is a test email";
        String[] attachments = {};

        Message message = new Message(sender, recipientsTo, recipientsCc, recipientsBcc, subject, content, attachments);

        assertArrayEquals(new String[] { recipientsTo[0], recipientsTo[1], recipientsCc[0], recipientsCc[1],
                recipientsBcc[0], recipientsBcc[1] }, message.getRecipients());
    }
}
