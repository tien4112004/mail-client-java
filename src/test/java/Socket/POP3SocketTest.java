package Socket;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.io.IOException;
import java.security.NoSuchProviderException;

public class POP3SocketTest {
    private POP3Socket POP3Socket;

    @Before
    public void setUp() throws IOException {
        POP3Socket = new POP3Socket("localhost", 3335, "example@localhost", "123");
        POP3Socket.connect();
        POP3Socket.login();
    }

    @Test
    // Test getMessage
    public void testGetMessageCount() throws IOException, NoSuchProviderException {
        String message = POP3Socket.getMessage("1");
        // int messageCount = POP3Socket.getMessageCount();

        // assertEquals(messageCount, 24);

        System.out.println(message);
        POP3Socket.quit();
    }

}