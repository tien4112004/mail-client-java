import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    @Test
    void testMessageCreation() {
        String sender = "sender@example.com";
        String recipientsType = "To";
        String recipients = "recipient@example.com";
        String subject = "Test Subject";
        String content = "Test Content";

        Message message = new Message(sender, recipientsType, recipients, subject, content);
        System.out.println(message.toString());
        assertEquals(sender, message.getSender());
        assertEquals(recipients, message.getRecipients());
        assertTrue(message.toString().contains(subject));
        assertTrue(message.toString().contains(content));
    }
}