package SMTPClient;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class SMTPClientTest {
    @Test
    void testSMTPClient() throws IOException {
        TestSMTPClient client = new TestSMTPClient("localhost", 2225);
        assertEquals("localhost", client.getSMTPServer());
        assertEquals(2225, client.getPort());
    }

    @Test
    void testSendCommand() throws IOException {
        TestSMTPClient client = new TestSMTPClient("localhost", 2225);
        assertDoesNotThrow(() -> client.sendCommand("MAIL FROM:<sender@example.com>", 250));
    }

    static class TestSMTPClient extends SMTPClient {

        TestSMTPClient(String SMTPServer, int port) throws IOException {
            super(SMTPServer, port);
        }

        // @Override
        // protected void connect() {
        // // do nothing
        // }

        // @Override
        // protected void disconnect() {
        // // do nothing
        // }

        // @Override
        // protected String readResponse() throws IOException {
        // return "250 Requested mail action okay, completed\r\n";
        // }

        // @Override
        // protected void send(String command) throws IOException {
        // // do nothing
        // }
    }
}