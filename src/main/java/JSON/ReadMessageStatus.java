package JSON;

import java.util.HashMap;
import java.util.Map;

import java.io.File;
import java.io.FileReader;

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

    public void setStatus(JSONObject messagesID, String path, boolean value) {
        String messageID = getMessageID(path);
        Map<String, Object> entry = new HashMap<String, Object>(messagesID);
        for (Map.Entry<String, Object> e : entry.entrySet()) {
            String key = e.getKey();
            if (key.equals(messageID)) {
                messagesID.replace(key, value);
                break;
            }
        }
    }

    public boolean getStatus(JSONObject messagesID, String path) {
        String messageID = getMessageID(path);
        Map<String, Object> entry = new HashMap<>(messagesID);
        for (Map.Entry<String, Object> e : entry.entrySet()) {
            String key = (String) e.getKey();
            if (key.equals(messageID)) {
                String status = e.getValue().toString();
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

    public JSONObject readStatus() {
        Map<String, Object> messageStatus = new HashMap<>();   
        for (Object obj : messageList.entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            String key = entry.getKey().toString();
            boolean value = (entry.getValue().toString() == "true")?true:false;
            messageStatus.put(key, value);
        }
        return new JSONObject(messageStatus);
    }

    public JSONObject getMessageList() {
        return messageList;
    }

    private String getMessageID(String path) {
        String[] pathSplited = path.split("/");
        String messageID = pathSplited[pathSplited.length - 1];
        return messageID.substring(0, messageID.length() - 4);
    }
}