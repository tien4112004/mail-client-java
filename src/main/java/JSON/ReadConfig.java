package JSON;

import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Filter.Mailbox;
import scala.concurrent.impl.FutureConvertersImpl.P;

public class ReadConfig {
    private final String DEFAULT_WORKING_DIRECTORY = "./";
    private JSONParser parser = new JSONParser();
    private JSONObject Config = new JSONObject();
    // private final String configDirectory = DEFAULT_WORKING_DIRECTORY +
    // "__Config.json";

    public ReadConfig() {
        File file = new File(DEFAULT_WORKING_DIRECTORY + "Config.json");
        try {
            Config = (JSONObject) parser.parse(new FileReader(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> readGeneral(JSONObject Config) {
        Map<String, Object> generalMap = new HashMap<>();
        JSONObject generalObject = (JSONObject) Config.get("General");

        for (Object obj : generalObject.entrySet()) {
            JSONObject je = (JSONObject) obj;
            String key = je.keySet().toArray()[0].toString();
            generalMap.put(key, je.get(key));
        }

        return generalMap;
    }

    public Mailbox createMailbox(JSONObject mailboxObject, String mailboxName) {
        // Mailbox's directory
        String directory = mailboxObject.get("Directory").toString();

        // Mailbox's filters
        JSONObject filters = (JSONObject)mailboxObject.get("Filters");
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
        Mailbox mailbox = new Mailbox(mailboxName, directory, senderKeywords.toArray(new String[0]), subjectKeywords.toArray(new String[0]), contentKeywords.toArray(new String[0]));
        return mailbox;
    }

    public Mailbox[] readMailboxes() {
        List<Mailbox> mailboxesList = new ArrayList<>();
        for (Object obj : ((JSONObject) Config.get("Mailboxes")).entrySet()) {
            Map.Entry entry = (Map.Entry) obj;
            JSONObject je = (JSONObject) entry.getValue();
            Mailbox mailbox = createMailbox(je, entry.getKey().toString());
            mailboxesList.add(mailbox);
        }
        Mailbox[] mailboxes = mailboxesList.toArray(new Mailbox[0]);
        return mailboxes;
    }
}