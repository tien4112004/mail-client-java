package JSON;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.FileWriter;
import java.io.IOException;

import Filter.*;
import UI.MainMenu;

// class NodeSerializer implements JsonSerializer<Node> {
//     @Override
//     public JsonElement serialize(Node src, Type typeOfSrc, JsonSerializationContext context) {
//         JsonObject jsonObject = new JsonObject();
//         jsonObject.addProperty("value", src.value);
//         // Check for circular reference
//         if (src.next != src) {
//             jsonObject.add("next", context.serialize(src.next));
//         }
//         return jsonObject;
//     }
// }

public class ConfigJSON {
    private final String DEFAULT_WORKING_DIRECTORY = "./";

    // private JSONObject keywordsObject (String nameFilter, String[] keywords) {
    //     return (JSONObject)(new JSONObject().put(nameFilter, keywords));
    // } 

    private JSONObject createMailbox(Mailbox mailbox, Filter[] filters) {
        JSONObject mailboxObject = new JSONObject();
        mailboxObject.put("Directory", mailbox.getMailboxDirectory());
        
        JSONObject filterObject = new JSONObject();
        
        if (filters != null) {
            for (Filter filter : filters) {
                if (filter instanceof SenderFilter)
                    filterObject.put("senderFilter", ((SenderFilter)filter).getKeywords());
                if (filter instanceof SubjectFilter)
                    filterObject.put("subjectFilter", ((SubjectFilter)filter).getKeywords());    
                if (filter instanceof ContentFilter)
                    filterObject.put("contentFilter", ((ContentFilter)filter).getKeywords());
            }
        }
        else {
            filterObject.put("senderFilter", "null");
            filterObject.put("subjectFilter", "null");
            filterObject.put("contentFilter", "null");

        }
        mailboxObject.put("Filter", filterObject);

        return mailboxObject;
    }

    private JSONObject createGeneral(MainMenu UI) throws IOException {
        JSONObject general = new JSONObject();

        String username = UI.username;
        general.put("Username", username);
        String password = UI.password;
        general.put("Password", password);
        String SMPTServer = "127.0.0.1";
        general.put("SMPTServer", SMPTServer);
        int SMPTport = 2225;
        general.put("SMTPport", SMPTport);
        String POP3Server = "127.0.0.1";
        general.put("POP3Server", POP3Server);
        int POP3port = 1225;
        general.put("POP3port", POP3port);
        int autoReload = 10;
        general.put("AutoReload", autoReload);

        return general;
    }

    public void writeConfig(Mailbox[] mailboxs, MainMenu UI) throws IOException {
        JSONObject general = createGeneral(UI);
        
        JSONObject namedFilter = new JSONObject();
        
        JSONObject mailboxFilters = new JSONObject();
        for (Mailbox mailbox : mailboxs){
            JSONObject filter = createMailbox(mailbox, mailbox.getFilters());
            mailboxFilters.put(mailbox.getMailboxName(), filter);
        }
        namedFilter.put("Mailbox", mailboxFilters);
        namedFilter.put("General", general);

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
