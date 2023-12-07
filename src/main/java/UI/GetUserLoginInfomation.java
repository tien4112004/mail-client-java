package UI;

import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GetUserLoginInfomation {
    JSONParser parser = new JSONParser();
    String Username;
    String Password;

    public GetUserLoginInfomation() {
        try {
            JSONArray configEleArray = (JSONArray) parser.parse(new FileReader("src/main/java/Config/Config.json"));

            for (Object configEle : configEleArray) {
                JSONObject config = (JSONObject) configEle;
                Username = (String) config.get("Username");
                Password = (String) config.get("Password");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }
}