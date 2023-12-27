package UI;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Filter.Mailbox;
import JSON.ReadConfig;
import JSON.WriteConfig;

public class UserInformation {
    private static final String DEFAULT_WORKING_DIRECTORY = "./";
    private static final String DEFAULT_CONFIG_DIRECTORY = DEFAULT_WORKING_DIRECTORY + "Config.json";
    JSONParser parser = new JSONParser();
    InputHandler inputHandler;
    private String Username;
    private String Password;
    private List<Mailbox> mailboxes;

    public UserInformation(String configDirectory, InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        ReadConfig readConfig = null;
        for (int i = 0; i < 3; i++) {
            try {
                readConfig = new ReadConfig();
                break;
            } catch (Exception e) {
                System.out.println("Config file not found!");
                handleUserInput();
                System.out.printf("Retrying %d/3...\n", i + 1);
            }
        }
        Map<String, Object> generalMap = readConfig.readGeneral();
        Username = (String) generalMap.get("Username");
        Password = (String) generalMap.get("Password");
        mailboxes = readConfig.readMailboxes();
    }

    public UserInformation(InputHandler inputHandler) {
        this(DEFAULT_CONFIG_DIRECTORY, inputHandler);
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public List<Mailbox> getMailboxes() {
        return mailboxes;
    }

    private void handleUserInput() {
        Username = inputHandler.dialog("Username: ");
        Password = inputHandler.dialog("Password: ");

        WriteConfig writeConfig = new WriteConfig();
        Mailbox defaultMailboxes[] = { new Mailbox("Inbox"), new Mailbox("Sent"), new Mailbox("Spam") };
        try {
            writeConfig.writeConfig(defaultMailboxes, Username, Password);
            System.out.println("\u001B[32mConfiguration file created.\u001B[0m");
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("Error in writing configuration file.");
            e.printStackTrace();
        }
    }

}