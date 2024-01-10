package Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import Email.Email;

import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchProviderException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import Filter.Mailbox;

public class POP3SocketTest {
    private POP3Socket POP3Socket;
    private String[] messagesID = null;
    private Path path;

    @Before
    public void setUp() throws IOException {
        path = Paths.get("./Inbox/");
        if (!Files.exists(path))
            Files.createDirectories(path);
        String sender = "example@localhost";
        String[] recipientsTo = new String[] { "pttien@fit.hcmus.edu.vn" };
        String[] recipientsCc = new String[] { "example@localhost" };
        String[] recipientsBcc = new String[] { "example@fit.hcmus.edu.vn" };
        String subject = "Test Email";
        String content = "This is a test email";
        Email message = new Email(sender, recipientsTo, recipientsCc,
                recipientsBcc, subject, content);
        SMTPSocket SMTPSocket = new SMTPSocket("localhost", 2225);
        SMTPSocket.sendEmail(message);

        // create mailbox list
        List<Mailbox> mailboxes = new ArrayList<>();
        mailboxes.add(new Mailbox("Inbox"));
        mailboxes.add(new Mailbox("example"));
        mailboxes.add(new Mailbox("email"));

        POP3Socket = new POP3Socket("localhost", 3335, "example@localhost", "123");
        POP3Socket.addMailboxes(mailboxes);
        POP3Socket.retrieveMessage();
        this.messagesID = POP3Socket.getMessagesID();
    }

    @Test
    public void testRetrieveMessage() throws IOException, NoSuchProviderException {
        String message = POP3Socket.RETR("1");

        System.out.println(message);
    }

    @Test
    public void testRetrieveInvalidMessage() throws IOException, NoSuchProviderException {
        assertThrows(Exception.class, () -> POP3Socket.RETR("0"));
    }

    @Test
    public void testGetMessageID() throws IOException {
        for (String ID : messagesID) {
            System.out.println(ID);
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

    @Test
    public void testRetrieveMessageServer() throws IOException {
        POP3Socket.retrieveMessage();
    }

    @After
    public void tearDownAfter() {
        try {
            Path path = Paths.get("./Inbox/");
            for (String ID : messagesID) {
                path = Paths.get("./Inbox/" + ID + ".msg");
                Files.deleteIfExists(path);
            }
            Files.deleteIfExists(path);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}