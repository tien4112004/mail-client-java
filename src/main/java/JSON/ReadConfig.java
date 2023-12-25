package JSON;

import java.io.File;
import java.io.FileReader;
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
        File file = new File(DEFAULT_WORKING_DIRECTORY + "__Config.json");
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

    public String readMailboxAttributes(JSONObject mailboxObject, String mailboxName) {
        // Map<String, Object> mailboxMap = new HashMap<>();
        // Mailbox's directory
        String directory = mailboxObject.get("Directory").toString();
        // Mailbox's filters
        // Map<String, Object> filters = new HashMap<>();
        // filters.put("senderFilter", mailboxObject.get("senderFilter"));
        // filters.put("subjectFilter", mailboxObject.get("subjectFilter"));
        // filters.put("contentFilter", mailboxObject.get("contentFilter"));

        // mailboxMap.put("Directory", directory);
        // mailboxMap.put("Filters", filters);

        Mailbox mailbox = new Mailbox(mailboxName, directory);
        return mailbox;
    }

    public Mailbox[] readMailboxs() {
        for (Object obj : ((JSONObject) Config.get("Mailboxs")).entrySet()) {
            JSONObject je = (JSONObject) obj;

            String directory = je.get("Directory").toString();
            Mailbox mailbox = new Mailbox();
        }
    }
}