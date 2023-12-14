package UI;

import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UserInformation {
    JSONParser parser = new JSONParser();
    private String Username;
    private String Password;

    public UserInformation(String configDirectory) {
        try {
            JSONArray configEleArray = (JSONArray) parser.parse(new FileReader("src/main/java/JSON/Config.json"));
            JSONObject configEle = (JSONObject) configEleArray.get(0);
            JSONObject config = (JSONObject) configEle.get("General");
            Username = (String) config.get("Username");
            Password = (String) config.get("Password");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserInformation() {
        this("src/main/java/JSON/Config.json");
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }
}