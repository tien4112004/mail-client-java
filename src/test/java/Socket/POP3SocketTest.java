package Socket;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.security.NoSuchProviderException;

public class POP3SocketTest {
    private POP3Socket POP3Socket;

    @Before
    public void setUp() throws IOException {
        POP3Socket = new POP3Socket("localhost", 3335, "lttin@fit.hcmus.edu.vn", "123456");
        POP3Socket.connect();
        POP3Socket.login();
    }

    @Test
    // Test getMessage
    public void testGetMessage() throws IOException, NoSuchProviderException {
        String message = POP3Socket.getMessage("1");
        String[] messageID = POP3Socket.getMessagesID();
        int messageCount = POP3Socket.getMessageCount();

        assertEquals(messageID[1], "1 20231117085813249.msg");
        assertEquals(messageCount, 9);

        System.out.println(message);
    }

}