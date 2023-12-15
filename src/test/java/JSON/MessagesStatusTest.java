package JSON;

import java.io.EOFException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

import JSON.WriteMessageStatus;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

public class MessagesStatusTest {
    JSONArray messageList = null;
    WriteMessageStatus msg = null;

    @Before
    public void MessagesStatusTest() {
        msg = new WriteMessageStatus();
        messageList = new JSONArray();
        JSONObject msgObject = new JSONObject();
        msgObject.put("1", false);
        messageList.add(msgObject);
        try {
            msg.writeJSON(messageList);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    @Test
    public void writeJSONTest() throws Exception {
        JSONArray messageList = new JSONArray();
        JSONObject msgObject = new JSONObject();
        msgObject.put("1", false);
        messageList.add(msgObject);
        msg.writeJSON(messageList);

        Path filePath = Paths.get("src/main/java/JSON/MessageStatus.json");
        boolean actual = Files.exists(filePath);
        assertTrue(actual);
    }

    // @Test
    public void readJSONTest() throws Exception {
        JSONParser parser = new JSONParser();
        messageList = (JSONArray) parser.parse(new FileReader("src/main/java/JSON/MessageStatus.json"));
        JSONObject msgObject = new JSONObject();
        msgObject.put("1", false);
        assertTrue(messageList.contains(msgObject));
    }

    @Test
    public void replaceValue() throws Exception {
        JSONObject msgObject = new JSONObject();
        msgObject = (JSONObject) messageList.get(0);
        messageList.remove(msgObject);
        assertTrue(messageList.isEmpty());

        msgObject.replace("1", true);
        messageList.add(msgObject);
        assertTrue(messageList.contains(msgObject));
        msg.writeJSON(messageList);

        msgObject = new JSONObject();
        msgObject.put("2", false);
        messageList.add(msgObject);
        msg.writeJSON(messageList);
    }
}
