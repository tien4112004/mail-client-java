package Socket;

import Message.Message;
import Socket.SMTPSocket;

import org.junit.Before;
import org.junit.Test;

import Message.Message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.IOException;
import java.security.NoSuchProviderException;

public class POP3SocketTest {
    private POP3Socket POP3Socket;

    @Before
    public void setUp() throws IOException {
        String sender = "example@localhost";
        String[] recipientsTo = new String[] { "pttien@fit.hcmus.edu.vn" };
        String[] recipientsCc = new String[] { "example@localhost" };
        String[] recipientsBcc = new String[] { "example@fit.hcmus.edu.vn" };
        String subject = "Test Email";
        String content = "This is a test email";
        Message message = new Message(sender, recipientsTo, recipientsCc,
                recipientsBcc, subject, content);
        SMTPSocket SMTPSocket = new SMTPSocket("localhost", 2225);
        SMTPSocket.sendEmail(message);

        POP3Socket = new POP3Socket("localhost", 3335, "example@localhost", "123");
        POP3Socket.connect();
        POP3Socket.login();
    }

    @Test
    public void testRetrieveMessage() throws IOException, NoSuchProviderException {
        String message = POP3Socket.retrieveMessage("1");
        // int messageCount = POP3Socket.getMessageCount();

        // assertEquals(messageCount, 24);

        System.out.println(message);
        POP3Socket.quit();
    }

    @Test
    public void testRetrieveInvalidMessage() throws IOException, NoSuchProviderException {
        assertThrows(Exception.class, () -> POP3Socket.retrieveMessage("0"));
    }

    @Test
    public void testGetMessageID() throws IOException {
        String[] messagesID = POP3Socket.getMessagesID();
        for (String ID : messagesID) {
            System.out.print(ID);
        }
    }

    @Test
    public void testDoCommand() throws IOException {
        String response = POP3Socket.doCommand("LIST", "+OK");
    }

    @Test
    public void testGetMessageCount() throws IOException {
        int messageCount = POP3Socket.getMessageCount();
    }
}