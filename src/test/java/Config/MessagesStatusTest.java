package Config;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import JSON.WriteMessageStatus;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

public class MessagesStatusTest {
    @Test
    public void writeJSONTest() throws Exception {
        WriteMessageStatus msg =  new WriteMessageStatus();

        JSONArray messageList = new JSONArray();
        JSONObject msgObject = new JSONObject();
        msgObject.put("1", false);
        messageList.add(msgObject);
        msg.writeJSON(messageList);

        Path filePath = Paths.get("src/main/java/JSON/MessageStatus.json");
        boolean actual = Files.exists(filePath);
        assertTrue(actual);
    }
}
