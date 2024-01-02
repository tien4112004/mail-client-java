package JSON;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import net.lecousin.framework.concurrent.async.MutualExclusion;

public class WriteMessageStatus {
    private final String DEFAULT_WORKING_DIRECTORY = "./";
    private JSONObject messageList;

    public WriteMessageStatus(JSONObject messageList) {
        this.messageList = messageList;
    }

    public void writeJSON() throws IOException {
        synchronized(MutualExclusion.class) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = gson.toJsonTree(messageList);
            String prettyJson = gson.toJson(je);

            String messageStatusJSONDirectory = DEFAULT_WORKING_DIRECTORY + "MessageStatus.json";
            try (FileWriter file = new FileWriter(messageStatusJSONDirectory)) {
                file.write(prettyJson);
                file.flush();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
