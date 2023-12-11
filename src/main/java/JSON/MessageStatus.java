package JSON;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import Socket.POP3Socket;

public class MessageStatus {

    private String[] messagesID;
    private int numbersOfMessage;

    public MessageStatus() throws IOException {
        POP3Socket pop3Socket = new POP3Socket("localhost", 3335, "example@localhost", "123");
        pop3Socket.connect();
        pop3Socket.login();
        numbersOfMessage = pop3Socket.getMessageCount();
        messagesID = pop3Socket.getMessagesID();
    }

    private JSONObject createJSONObject() {
        JSONObject obj = new JSONObject();
        boolean status = false;

        for (int i = 0; i < numbersOfMessage; i++)
            obj.put(messagesID[i], status);
        return obj;
    }

    public void writeJSON() {
        JSONObject status = new JSONObject();
        status.put("Status", createJSONObject());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = gson.toJsonTree(status);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(je);
        String prettyJson = gson.toJson(jsonArray);

        try (FileWriter file = new FileWriter("src/main/java/Config/MessageStatus.json")) {
            file.write(prettyJson);
            file.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
