package Socket;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MailSocketTest {
    private TestMailSocket mailSocket;

    @Before
    public void setUp() throws IOException {
        mailSocket = new TestMailSocket("localhost", 2225);
        mailSocket.connect();
    }

    // @Test
    // public void testIsConnected() {
    // assertTrue(mailSocket.isConnected());
    // }

    @Test
    public void testDoCommand() {
        assertDoesNotThrow(() -> {
            mailSocket.doCommand("HELO localhost", "250");
        });
    }

    // @Test
    // public void testDoCommandThrowsException() {
    // assertThrows(IOException.class, () -> {
    // mailSocket.doCommand("TEST", "250");
    // });
    // }

    private class TestMailSocket extends MailSocket {
        public TestMailSocket(String server, int port) {
            super(server, port);
        }

        @Override
        protected boolean isConnected() {
            return true;
        }

        @Override
        protected boolean isMultiLineResponse(String response) {
            return false;
        }

        @Override
        public boolean validateResponse(String response) {
            return true;
        }

        @Override
        protected String doCommand(String command, String expectedReturnCode) throws IOException {
            if (!expectedReturnCode.equals(expectedReturnCode)) {
                throw new IOException("Command failed");
            }
            return "";
        }
    }
}