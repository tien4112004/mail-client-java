package Filter;

import Envelope.*;
import Message.Message;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FilterTest {
    @Test
    void testSenderFilter() throws Exception {
        SenderFilter filter = new SenderFilter("pttien@fit.hcmus.edu.vn");
        String sender = "example@localhost";
        String[] recipientsTo = new String[] { "pttien@fit.hcmus.edu.vn" };
        String[] recipientsCc = new String[] { "example@localhost" };
        String[] recipientsBcc = new String[] { "example@fit.hcmus.edu.vn" };
        String subject = "Test Email";
        String content = "This is a test email";
        String[] attachments = { "attachments/test.cpp", "attachments/test.txt" };
        Message message = new Message(sender, recipientsTo, recipientsCc,
                recipientsBcc, subject, content, attachments);
        Envelope envelope = new Envelope(message, "localhost");

        assertFalse(filter.matches(envelope));
    }

    @Test
    void testSubjectFilter() throws Exception {
        SubjectFilter filter = new SubjectFilter("test");
        String sender = "example@localhost";
        String[] recipientsTo = new String[] { "pttien@fit.hcmus.edu.vn" };
        String[] recipientsCc = new String[] { "example@localhost" };
        String[] recipientsBcc = new String[] { "example@fit.hcmus.edu.vn" };
        String subject = "Test Email";
        String content = "This is a test email";
        String[] attachments = { "attachments/test.cpp", "attachments/test.txt" };
        Message message = new Message(sender, recipientsTo, recipientsCc,
                recipientsBcc, subject, content, attachments);
        Envelope envelope = new Envelope(message, "localhost");
        System.out.println(envelope.subject);
        assertTrue(filter.matches(envelope));

        SubjectFilter filter2 = new SubjectFilter("other");
        assertFalse(filter2.matches(envelope));
    }

    @Test
    void testContentFilter() throws Exception {
        ContentFilter filter = new ContentFilter("content");
        String sender = "example@localhost";
        String[] recipientsTo = new String[] { "pttien@fit.hcmus.edu.vn" };
        String[] recipientsCc = new String[] { "example@localhost" };
        String[] recipientsBcc = new String[] { "example@fit.hcmus.edu.vn" };
        String subject = "Test Email";
        String content = "This is a test email";
        String[] attachments = { "attachments/test.cpp", "attachments/test.txt" };
        Message message = new Message(sender, recipientsTo, recipientsCc,
                recipientsBcc, subject, content, attachments);
        Envelope envelope = new Envelope(message, "localhost");
        assertFalse(filter.matches(envelope));

        ContentFilter filter2 = new ContentFilter("test");
        System.out.println(envelope.message.getContent());
        assertTrue(filter2.matches(envelope));
    }
}