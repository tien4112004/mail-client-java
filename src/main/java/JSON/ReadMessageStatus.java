package JSON;

import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Socket.POP3Socket;

public class ReadMessageStatus extends MessageStatus{
    public ReadMessageStatus(String server, int port, String username, String password) throws Exception {
        super(server, port, username, password);
    }

    public JSONObject getMessageObject(int messageOrder) {
        String messageID = messagesID[messageOrder];
        return (JSONObject) messageList.get(messageList.indexOf(messageID));
    }
    
    public JSONArray getMessageList() {
        return messageList;
    }
}

    

    