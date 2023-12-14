package Filter;

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

        assertFalse(filter.matches(message));
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
        assertTrue(filter.matches(message));

        SubjectFilter filter2 = new SubjectFilter("other");
        assertFalse(filter2.matches(message));
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
        assertFalse(filter.matches(message));

        ContentFilter filter2 = new ContentFilter("test");
        System.out.println(message.getContent());
        assertTrue(filter2.matches(message));
    }
}