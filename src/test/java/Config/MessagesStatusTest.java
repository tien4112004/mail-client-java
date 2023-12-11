package Config;

import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import JSON.MessageStatus;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

public class MessagesStatusTest {
    @Test
    public void writeJSONTest() throws IOException {
        MessageStatus msg = new MessageStatus();
        msg.writeJSON();

        Path filePath = Paths.get("src/main/java/JSON/MessageStatus.json");
        boolean actual = Files.exists(filePath);
        assertTrue(actual);
    }
}
