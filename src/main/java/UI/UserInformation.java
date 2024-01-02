package UI;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.List;

import Filter.Mailbox;
import JSON.ReadConfig;
import JSON.WriteConfig;

public class UserInformation extends UI {
    private static final String DEFAULT_WORKING_DIRECTORY = "./";
    private static final String DEFAULT_CONFIG_DIRECTORY = DEFAULT_WORKING_DIRECTORY + "Config.json";
    InputHandler inputHandler;
    private String Username;
    private String Password;
    private List<Mailbox> mailboxes;
    private int retrieveIntervalSeconds;
    private String SMTPServer;
    private int SMTPPort;
    private String POP3Server;
    private int POP3Port;

    public UserInformation(String configDirectory, InputHandler inputHandler) {
        this.inputHandler = inputHandler;
        readConfig(configDirectory);
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

    public int getRetrieveIntervalSeconds() {
        return retrieveIntervalSeconds;
    }

    public String getSMTPServer() {
        return SMTPServer;
    }

    public int getSMTPPort() {
        return SMTPPort;
    }

    public String getPOP3Server() {
        return POP3Server;
    }

    public int getPOP3Port() {
        return POP3Port;
    }

    private void readConfig(String configDirectory) {
        ReadConfig readConfig = null;
        for (int tries = 0; tries < 3; tries++) {
            try {
                readConfig = new ReadConfig();
                break;
            } catch (Exception e) {
                System.out.println("Config file not found!");
                handleUserInput();
                System.out.printf("Retrying %d/3...\n", tries + 1);
            }
        }
        Map<String, Object> generalMap = readConfig.readGeneral();
        Username = (String) generalMap.get("Username");
        Password = (String) generalMap.get("Password");
        mailboxes = readConfig.readMailboxes();
        // server informations
        retrieveIntervalSeconds = Integer.parseInt((String) generalMap.get("RetrieveIntervalSecond"));
        SMTPServer = (String) generalMap.get("SMTPServer");
        SMTPPort = Integer.parseInt((String) generalMap.get("SMTPport"));
        POP3Server = (String) generalMap.get("POP3Server");
        POP3Port = Integer.parseInt((String) generalMap.get("POP3port"));
    }

    private void handleUserInput() {
        Username = inputHandler.dialog("Username: ");
        Password = inputHandler.dialog("Password: ");

        WriteConfig writeConfig = new WriteConfig();
        Mailbox defaultMailboxes[] = { new Mailbox("Inbox"), new Mailbox("Sent"), new Mailbox("Spam") };
        try {
            writeConfig.writeConfig(defaultMailboxes, Username, Password);
            displaySuccessMessage("Configuration file created!");
        } catch (Exception e) {
            displayErrorMessage("Failed to create configuration file!");
            e.printStackTrace();
        }
    }
}