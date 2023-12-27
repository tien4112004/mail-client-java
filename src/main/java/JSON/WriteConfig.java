package JSON;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.omg.CORBA.Object;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.FileWriter;
import java.io.IOException;

import Filter.*;
import UI.MainMenu;

public class WriteConfig {
    private final String DEFAULT_WORKING_DIRECTORY = "./";

    // private JSONObject keywordsObject (String nameFilter, String[] keywords) {
    // return (JSONObject)(new JSONObject().put(nameFilter, keywords));
    // }

    // private void addKeywords(JSONObject jsonObject, String nameFilter, List<String> keywords) {
    //     for (String keyword : keywords) {
    //         ((JSONArray) jsonObject.get(nameFilter)).add(keyword);
    //     }
    // }

    private JSONObject createMailbox(Mailbox mailbox, Filter[] filters) {
        JSONObject mailboxObject = new JSONObject();
        mailboxObject.put("Directory", mailbox.getMailboxDirectory().toString());

        JSONObject filterObject = new JSONObject();
        for (Filter filter : filters) {
            if (filter instanceof SenderFilter)
                filterObject.put("SenderFilter", ((SenderFilter) filter).getKeywords());
            if (filter instanceof SubjectFilter)
                filterObject.put("SubjectFilter", ((SubjectFilter) filter).getKeywords());
            if (filter instanceof ContentFilter)
                filterObject.put("ContentFilter", ((ContentFilter) filter).getKeywords());                
        }
    
        mailboxObject.put("Filters", filterObject);
        return mailboxObject;
    }

    private JSONObject createGeneral(MainMenu UI) throws IOException {
        JSONObject general = new JSONObject();

        general.put("Username", UI.username);
        general.put("Password", UI.password);
        general.put("SMPTServer", "127.0.0.1");
        general.put("SMTPport", 2225);
        general.put("POP3Server", "127.0.0.1");
        general.put("POP3port", 1225);
        general.put("RetrieveIntervalSecond", 10);

        return general;
    }

    public void writeConfig(Mailbox[] mailboxes, MainMenu UI) throws IOException {
        JSONObject namedFilter = new JSONObject();
        namedFilter.put("General", createGeneral(UI));

        JSONObject mailboxFilters = new JSONObject();
        for (Mailbox mailbox : mailboxes) {
            mailboxFilters.put(mailbox.getMailboxName(), createMailbox(mailbox, mailbox.getFilters()));
        }
        namedFilter.put("Mailboxes", mailboxFilters);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = gson.toJsonTree(namedFilter);
        // JSONArray jsonArray = new JSONArray();
        // jsonArray.add(je);
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
