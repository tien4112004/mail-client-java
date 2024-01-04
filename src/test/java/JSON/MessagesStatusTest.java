package JSON;

import java.util.Map;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import Socket.POP3Socket;

public class MessagesStatusTest {
    JSONObject messageList = null;
    Map<String, Object> messagesList;
    JSONObject msgObject = new JSONObject();
    POP3Socket pop3Socket;
    ReadMessageStatus msgRead;

    @Before
    public void MessagesStatusTest() {
        pop3Socket = new POP3Socket("localhost", 3335, "example@localhost", "123456");
        try {
            pop3Socket.retrieveMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        msgRead = new ReadMessageStatus();
        try {
            messageList = msgRead.getMessageList();
            messagesList = msgRead.readStatus();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // @Test
    // public void writeJSONTest() throws Exception {
    // JSONArray messageList = new JSONArray();
    // WriteMessageStatus msg = new WriteMessageStatus(messageList);
    // msgObject.put("1", false);
    // messageList.add(msgObject);
    // msg.writeJSON();

    // Path filePath = Paths.get("src/main/java/JSON/MessageStatus.json");
    // boolean actual = Files.exists(filePath);
    // assertTrue(actual);
    // }

    @Test
    public void replaceValue() throws Exception {
        msgRead.setStatus(messageList, "20240101144007219", true);
        WriteMessageStatus msg = new WriteMessageStatus(messageList); // using Map to write
        msg.writeJSON();
    }
}
