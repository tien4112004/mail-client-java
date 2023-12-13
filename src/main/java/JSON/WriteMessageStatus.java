package JSON;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.omg.CORBA.ExceptionList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import Socket.POP3Socket;
// import JSON.ReadMessageStatus;

public class WriteMessageStatus{
    // public WriteMessageStatus(String server, int port, String username, String password) throws Exception {
    //     super(server, port, username, password);
    // }

    // public JSONArray createJSONArray() throws IOException {
    //     JSONArray messageList = new JSONArray();
    //     boolean status = false;

    //     for (int i = 0; i < messagesID.length; i++){
    //         if (exist(i))  break;

    //         JSONObject msgObject = new JSONObject();
    //         msgObject.put(messagesID[i], status);
    //         messageList.add(msgObject);
    //     }

    //     return messageList;
    // }

    public void writeJSON(JSONArray messageList) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = gson.toJsonTree(messageList);
        String prettyJson = gson.toJson(je);

        try (FileWriter file = new FileWriter("src/main/java/JSON/MessageStatus.json")) {
            file.write(prettyJson);
            file.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
