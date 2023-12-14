package JSON;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import Filter.Mailbox;

public class WriteMailbox {
    JSONArray mailBoxList = null;
    JSONParser parser = new JSONParser();

    public WriteMailbox() {
        File file = new File("src/main/java/JSON/MailBox.json");
        try {
            if (file.exists())
                mailBoxList = (JSONArray) parser.parse(new FileReader("src/main/java/JSON/MailBox.json"));
            else 
                mailBoxList = new JSONArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMailBox(Mailbox mailbox) {
        JSONObject mailBoxObject = new JSONObject();
        mailBoxObject.put(mailbox.getMailboxName(), mailbox.getMailboxDirectory());
        mailBoxList.add(mailBoxObject);
    }

    public void writeJSON(JSONArray mailBoxList) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = gson.toJsonTree(mailBoxList);
        String prettyJson = gson.toJson(je);

        try (FileWriter file = new FileWriter("src/main/java/JSON/Mailbox.json")) {
            file.write(prettyJson);
            file.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
