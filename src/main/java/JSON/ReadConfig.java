package JSON;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Filter.Mailbox;

public class ReadConfig {
    private final String DEFAULT_WORKING_DIRECTORY = "./";
    private JSONParser parser = new JSONParser();
    private JSONObject Config = new JSONObject();
    // private final String configDirectory = DEFAULT_WORKING_DIRECTORY +
    // "__Config.json";

    public ReadConfig() throws IOException {
        File file = new File(DEFAULT_WORKING_DIRECTORY + "Config.json");
        if (!file.exists()) {
            throw new IOException("Config file not found!");
        }

        try {
            Config = (JSONObject) parser.parse(new FileReader(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> readGeneral() {
        Map<String, Object> generalMap = new HashMap<>();
        JSONObject generalObject = (JSONObject) Config.get("General");

        for (Object obj : generalObject.entrySet()) {
            Map.Entry<String,Object> entry = (Map.Entry<String,Object>) obj;
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            generalMap.put(key, value);
        }

        return generalMap;
    }

    private Mailbox createMailbox(JSONObject mailboxObject, String mailboxName) {
        // Mailbox's directory
        String directory = mailboxObject.get("Directory").toString();

        // Mailbox's filters
        JSONObject filters = (JSONObject) mailboxObject.get("Filters");
        List<String> subjectKeywords = new ArrayList<>();
        List<String> senderKeywords = new ArrayList<>();
        List<String> contentKeywords = new ArrayList<>();
        for (Object obj : filters.entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            if (entry.getKey().toString().equals("SubjectFilter")) {
                subjectKeywords = (List<String>) entry.getValue();
            }
            if (entry.getKey().toString().equals("SenderFilter")) {
                senderKeywords = (List<String>) entry.getValue();
            }
            if (entry.getKey().toString().equals("ContentFilter")) {
                contentKeywords = (List<String>) entry.getValue();
            }
        }

        // Create new mailbox
        Mailbox mailbox = new Mailbox(mailboxName, directory, senderKeywords.toArray(new String[0]),
                subjectKeywords.toArray(new String[0]), contentKeywords.toArray(new String[0]));
        return mailbox;
    }

    public List<Mailbox> readMailboxes() {
        List<Mailbox> mailboxesList = new ArrayList<>();
        for (Object obj : ((JSONObject) Config.get("Mailboxes")).entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            JSONObject je = (JSONObject) entry.getValue();
            Mailbox mailbox = createMailbox(je, entry.getKey().toString());
            mailboxesList.add(mailbox);
        }
        return mailboxesList;
    }
}