package Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import Envelope.Envelope;
import Editor.Editor;

public class Config {
    private Map<String, Object> filterMap = new HashMap<String, Object>();
    private Map<String, Object> generalMap = new HashMap<String, Object>();

    // Read keyword about work and spam from text files, then put it into JSONObject
    // filter.
    private String[] workKeywordsHandler() {
        String data = "";
        try {
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

    private JSONObject createFilter(Envelope envelope) throws IOException {
        String[] workKeywords = workKeywordsHandler();
        String[] spamKeywords = spamKeywordsHandler();
        String[] recipients = envelope.recipients;

        ArrayList<String> from = new ArrayList<String>();

        for (String recipient : recipients) {
            from.add(recipient);
        }
        filterMap.put("Recipients:", from);

        String subject = envelope.subject;
        filterMap.put("Subject:", subject);

        ArrayList<String> work = new ArrayList<String>();

        for (String keyword : workKeywords) {
            work.add(keyword);
        }
        filterMap.put("Work:", work);

        for (String keyword : spamKeywords) {
            work.add(keyword);
        }
        filterMap.put("Spam:", spamKeywords);

        JSONObject filter = new JSONObject(filterMap);
        return filter;
    }

    // private JSONObject createGeneral(Editor editor) throws IOException {
    // String userName = editor.userName;
    // String password = editor.password;
    // int smtpServer = editor.smtpServer;
    // int pop3Server = editor.pop3Server;
    // int autoReload = editor.autoReload;

    // generalMap.put("User Name:", userName);
    // generalMap.put("Password:", password);
    // generalMap.put("SMTP Server:", smtpServer);
    // generalMap.put("POP3 Server:", pop3Server);
    // generalMap.put("Auto Reload:", autoReload);

    // JSONObject general = new JSONObject(generalMap);
    // return general;
    // }

    public void writeConfig(Editor editor, Envelope envelope) throws IOException {
        JSONObject filter = createFilter(envelope);
        // JSONObject general = createGeneral(editor);

        JSONObject namedFilter = new JSONObject();
        namedFilter.put("Filter", filter);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonElement je = gson.toJsonTree(namedFilter);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(je);
        String prettyJson = gson.toJson(jsonArray);

        try (FileWriter file = new FileWriter("src/main/java/config/Config.json")) {
            file.write(prettyJson);
            file.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
