package Socket;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.IOException;
import java.security.NoSuchProviderException;
import java.util.Properties;

public class POP3SocketTest {
    private POP3Socket POP3Socket;

    @Before
    public void setUp() throws IOException {
        POP3Socket = new POP3Socket("localhost", 2225, "lttin@fit.hcmus.edu.vn", "123456");
        POP3Socket.connect();
    }

    @Test
    public void testDoCommand() {
        assertDoesNotThrow(() -> {
            POP3Socket.doCommand("HELO localhost", "250");
        });
    }
    
    @Test
    
}