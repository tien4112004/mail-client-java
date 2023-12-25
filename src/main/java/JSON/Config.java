package JSON;

import java.io.File;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileNotFoundException;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import Filter.*;
import UI.MainMenu;
import Message.Message;

public class Config {
    private final String DEFAULT_WORKING_DIRECTORY = "./";

    private Map<String, Object> filterMap = new HashMap<String, Object>();
    private Map<String, Object> generalMap = new HashMap<String, Object>();

    // Read all filter from object filter and put it into a JSONArray.
    private String[] workKeywordsHandler() {
        String data = "";
        try {
            // String workFileDirectory = DEFAULT_WORKING_DIRECTORY + "Work_Keywords.txt";
            File workFile = new File("src/main/java/Config/Work_Keywords.txt");
            Scanner myReader = new Scanner(workFile);
            while (myReader.hasNextLine()) {
                data = (new StringBuilder()).append(data).append(myReader.nextLine()).toString();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        return data.split(", ");
    }

    private String[] spamKeywordsHandler() {
        String data = "";
        try {
            File spamFile = new File("src/main/java/Config/Spam_Keywords.txt");
            Scanner myReader = new Scanner(spamFile);
            while (myReader.hasNextLine()) {
                data = (new StringBuilder()).append(data).append(myReader.nextLine()).toString();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        return data.split(", ");
    }

    private JSONObject createFilter(Message message) throws IOException {
        String[] workKeywords = workKeywordsHandler();
        String[] spamKeywords = spamKeywordsHandler();
        String[] recipients = message.getRecipients();

        ArrayList<String> from = new ArrayList<String>();

        for (String recipient : recipients) {
            from.add(recipient);
        }
        filterMap.put("Recipients", from);

        String subject = message.getSubject();
        filterMap.put("Subject", subject);

        ArrayList<String> work = new ArrayList<String>();

        for (String keyword : workKeywords) {
            work.add(keyword);
        }
        filterMap.put("Work", work);

        for (String keyword : spamKeywords) {
            work.add(keyword);
        }
        filterMap.put("Spam", spamKeywords);

        JSONObject filter = new JSONObject(filterMap);
        return filter;
    }

    private JSONObject createGeneral(MainMenu UI) throws IOException {
        String username = UI.username;
        generalMap.put("Username", username);
        String password = UI.password;
        generalMap.put("Password", password);
        String SMPTServer = "127.0.0.1";
        generalMap.put("SMPTServer", SMPTServer);
        int SMPTport = 2225;
        generalMap.put("SMTPport", SMPTport);
        String POP3Server = "127.0.0.1";
        generalMap.put("POP3Server", POP3Server);
        int POP3port = 1225;
        generalMap.put("POP3port", POP3port);
        int autoReload = 10;
        generalMap.put("AutoReload", autoReload);

        JSONObject general = new JSONObject(generalMap);
        return general;
    }

    public void writeConfig(MainMenu UI, Message message) throws IOException {
        JSONObject filter = createFilter(message);
        JSONObject general = createGeneral(UI);

        JSONObject namedFilter = new JSONObject();
        namedFilter.put("General", general);
        namedFilter.put("Filter", filter);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = gson.toJsonTree(namedFilter);
        // JsonArray jsonArray = new JsonArray();
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
