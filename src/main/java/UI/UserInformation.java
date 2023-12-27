package UI;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UserInformation {
    private static final String DEFAULT_WORKING_DIRECTORY = "./";
    private static final String DEFAULT_CONFIG_DIRECTORY = DEFAULT_WORKING_DIRECTORY + "Config.json";
    JSONParser parser = new JSONParser();
    InputHandler inputHandler;
    private String Username;
    private String Password;

    public UserInformation(String configDirectory, InputHandler inputHandler) {
        try {
            this.inputHandler = inputHandler;
            // JSONArray configEleArray = (JSONArray) parser.parse(new FileReader(DEFAULT_CONFIG_DIRECTORY));
            // JSONObject configEle = (JSONObject) configEleArray.get(0);
            JSONObject configEle = (JSONObject) parser.parse(new FileReader(DEFAULT_CONFIG_DIRECTORY));
            JSONObject config = (JSONObject) configEle.get("General");
            Username = (String) config.get("Username");
            Password = (String) config.get("Password");
        } catch (Exception e) {
            System.out.println("Config file not found!");
            handleUserInput();
            // e.printStackTrace();
        }
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

    private void handleUserInput() {
        Username = inputHandler.dialog("Enter username: ");
        Password = inputHandler.dialog("Enter password: ");

        JSONObject config = new JSONObject();
        config.put("Username", Username);
        config.put("Password", Password);

        JSONObject general = new JSONObject();
        general.put("General", config);

        JSONArray configEleArray = new JSONArray();
        configEleArray.add(general);

        try (FileWriter file = new FileWriter(DEFAULT_CONFIG_DIRECTORY)) {
            file.write(configEleArray.toJSONString());
            System.out.println("Config file created successfully.");
        } catch (IOException ioException) {
            System.out.println("Error writing to config file.");
            ioException.printStackTrace();
        }
    }
}