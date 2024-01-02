package JSON;

import java.util.HashMap;
import java.util.Map;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ReadMessageStatus{
    String DEFAULT_WORKING_DIRECTORY = "./";
    JSONParser parser = new JSONParser();
    JSONObject messageList;
    public ReadMessageStatus() {
        try {
            String MessageStatusJSONDirectory = DEFAULT_WORKING_DIRECTORY + "MessageStatus.json";
            File file = new File(MessageStatusJSONDirectory);
            if (file.exists())
                messageList = (JSONObject) parser.parse(new FileReader(MessageStatusJSONDirectory));
            else
                messageList = new JSONObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 

    public void setStatus(Map<String, Object> messagesID, String messageID, boolean value) {
        for (Map.Entry<String, Object> entry : messagesID.entrySet()) {
            String key = entry.getKey();
            if (key.equals(messageID)) {
                messagesID.replace(key, value);
                break;
            }
        }
    }

    public boolean isRead(Map<String, Object> messagesID, String messageID) {
        for (Map.Entry<String, Object> entry : messagesID.entrySet()) {
            String key = (String) entry.getKey();
            if (key.equals(messageID)) {
                String status = entry.getValue().toString();
                return (status == "true");
            }
        }
        return false;
    }

    public boolean exist(JSONObject messagesID, String messageID) {
        if (messagesID.size() == 0) return false;
        for (Object e : messagesID.keySet()) {
            String key = (String) e;
            if (key.equals(messageID)) {
                return true;
            }
        }
        return false;
    }

    public Map<String, Object> readStatus() {
        Map<String, Object> messageStatus = new HashMap<>();   
        for (Object obj : messageList.entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            String key = entry.getKey().toString();
            boolean value = (entry.getValue().toString() == "true")?true:false;
            messageStatus.put(key, value);
        }
        return messageStatus;
    }

    public JSONObject getMessageList() {
        return messageList;
    }
}