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
        POP3Socket = new POP3Socket("localhost", 3335, "lttin@fit.hcmus.edu.vn", "123456");
        POP3Socket.connect();
        POP3Socket.login();
    }

    @Test
    // Test getMessage
    public void testGetMessage() throws IOException, NoSuchProviderException {
        String message = POP3Socket.getMessage("1");
        String[] actualMessagesID = POP3Socket.getMessagesID();
        String[] expectedMessagesID = { null, "20231117085813249", "20231117085938773", "20231117093429488",
                "20231117094302227", "20231117205307263", "20231117205422184", "20231123182621428",
                "20231206083952191", "20231208211614495" };
        int messageCount = POP3Socket.getMessageCount();

        assertArrayEquals(actualMessagesID, expectedMessagesID);
        assertEquals(messageCount, 9);

        System.out.println(message);
    }

}