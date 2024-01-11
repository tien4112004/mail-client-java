package JSON;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Filter.*;

public class WriteConfig {
    private final String DEFAULT_WORKING_DIRECTORY = "./";

    private JSONObject createMailbox(Mailbox mailbox, Filter[] filters) {
        Map<String, Object> mailboxObject = new HashMap<String, Object>();
        mailboxObject.put("Directory", mailbox.getMailboxDirectory().toString());

        Map<String, Object> filterObject =  new HashMap<String, Object>();
        if (filters == null) {
            filters = new Filter[3];
            filters[0] = new SenderFilter();
            filters[1] = new SubjectFilter();
            filters[2] = new ContentFilter();
        }

        for (Filter filter : filters) {
            if (filter instanceof SenderFilter)
                filterObject.put("SenderFilter", ((SenderFilter) filter).getKeywords());
            if (filter instanceof SubjectFilter)
                filterObject.put("SubjectFilter", ((SubjectFilter) filter).getKeywords());
            if (filter instanceof ContentFilter)
                filterObject.put("ContentFilter", ((ContentFilter) filter).getKeywords());
        }

        mailboxObject.put("Filters", filterObject);
        return new JSONObject(mailboxObject);
    }

    private JSONObject createGeneral(String username, String password) throws IOException {
        Map<String, Object> general = new HashMap<String, Object>();

        general.put("Username", username);
        general.put("Password", password);
        general.put("SMPTServer", "127.0.0.1");
        general.put("SMTPport", 2225);
        general.put("POP3Server", "127.0.0.1");
        general.put("POP3port", 3335);
        general.put("RetrieveIntervalSecond", 10);

        return new JSONObject(general);
    }

    public void writeConfig(Mailbox[] mailboxes, String username, String password) throws IOException {
        Map<String, Object> namedFilterMap = new HashMap<String, Object>();
        namedFilterMap.put("General", createGeneral(username, password));

        Map<String, Object> mailboxFilters = new HashMap<String, Object>();
        for (Mailbox mailbox : mailboxes) {
            mailboxFilters.put(mailbox.getMailboxName(), createMailbox(mailbox, mailbox.getFilters()));
        }
        namedFilterMap.put("Mailboxes", mailboxFilters);

        JSONObject namedFilter = new JSONObject(namedFilterMap);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = gson.toJsonTree(namedFilter);
        String prettyJson = gson.toJson(je);
        String configDirectory = DEFAULT_WORKING_DIRECTORY + "Config.json";
        try (FileWriter file = new FileWriter(configDirectory)) {
            file.write(prettyJson);
            file.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
