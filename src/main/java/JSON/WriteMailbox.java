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
    private final String DEFAULT_WORKING_DIRECTORY = "./";

    JSONArray mailBoxList = null;
    JSONParser parser = new JSONParser();

    public WriteMailbox() {
        String mailboxJSONDirectory = DEFAULT_WORKING_DIRECTORY + "Mailbox.json";
        File file = new File(mailboxJSONDirectory);
        try {
            if (file.exists())
                mailBoxList = (JSONArray) parser.parse(new FileReader(mailboxJSONDirectory));
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

        String mailboxJSONDirectory = DEFAULT_WORKING_DIRECTORY + "Mailbox.json";
        try (FileWriter file = new FileWriter(mailboxJSONDirectory)) {
            file.write(prettyJson);
            file.flush();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
