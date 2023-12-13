package Config;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import JSON.WriteMessageStatus;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

public class MessagesStatusTest {
    @Test
    public void writeJSONTest() throws Exception {
        WriteMessageStatus msg =  new WriteMessageStatus("localhost", 3335, "example@localhost", "123");

        JSONArray messageList = msg.createJSONArray();
        msg.writeJSON(messageList);

        Path filePath = Paths.get("src/main/java/JSON/MessageStatus.json");
        boolean actual = Files.exists(filePath);
        assertTrue(actual);
    }
}
