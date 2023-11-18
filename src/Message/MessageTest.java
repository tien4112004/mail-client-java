
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    @Test
    void testMessageCreation() {
        String sender = "sender@example.com";
        String recipientsType = "To";
        String recipients = "receiver@example.com";
        String subject = "Test Subject";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            sb.append("Hello World! " + i + " ");
        }
        String content = sb.toString();
        String attachments[] = { ".test/example.txt", ".test/example.cpp", ".test/example.zip" };

        Message message = new Message(sender, recipientsType, recipients, subject, content, attachments);

        assertTrue(message.header.contains(sender));
        assertTrue(message.header.contains(recipientsType));
        assertTrue(message.header.contains(recipients));
        assertTrue(message.header.contains(subject));
        // assertTrue(message.body.replaceAll("\r\n", "").contains(content));
        System.out.println(message);
    }

    @Test
    void testMessageToString() {
        String sender = "sender@example.com";
        String recipientsType = "To";
        String recipients = "receiver@example.com";
        String subject = "Test Subject";
        String content = "Test Content";
        String attachments[] = { ".test/example.txt", ".test/example.cpp", ".test/example.zip" };

        Message message = new Message(sender, recipientsType, recipients, subject, content, attachments);

        String messageString = message.toString();
        assertTrue(messageString.contains(sender));
        assertTrue(messageString.contains(recipientsType));
        assertTrue(messageString.contains(recipients));
        assertTrue(messageString.contains(subject));
        assertTrue(messageString.replaceAll("\r\n", "").contains(content));
        System.out.println(message);
    }
}