package JSON;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Filter.Mailbox;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.Assert.assertTrue;

public class WriteMailBoxTest {
    JSONArray mailboxList = null;
    JSONParser parser = new JSONParser();
    WriteMailbox writeMailBox = null;

    @BeforeEach
    public void WriteMailBoxTest() {
        File file = new File("src/main/java/JSON/Mailbox.json");
        writeMailBox = new WriteMailbox();
        try {
            if (file.exists()) 
                mailboxList = (JSONArray) parser.parse(new FileReader("src/main/java/JSON/Mailbox.json"));
            else 
                mailboxList = new JSONArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void writeTest() {
        Mailbox mailbox1 = new Mailbox("test1", "./test");
        Mailbox mailbox2 = new Mailbox("test2", "./test");
        Mailbox mailbox3 = new Mailbox("test3", "./test");
        Mailbox mailbox4 = new Mailbox("test4", "./test");
        
        Mailbox[] mailboxs = {mailbox1, mailbox2, mailbox3, mailbox4};

        for (Mailbox mailbox : mailboxs) {
            JSONObject mailboxObject = new JSONObject();
            mailboxObject.put(mailbox.getMailboxName(), mailbox.getMailboxDirectory().toString());
            mailboxList.add(mailboxObject);
        }
        
        writeMailBox.writeJSON(mailboxList);    
        JSONObject mailboxObject = new JSONObject();
        mailboxObject.put("test2", "./test");
        boolean fileExists = Files.exists(Paths.get("src/main/java/JSON/Mailbox.json"));
        assertTrue(mailboxList.contains(mailboxObject));
        assertTrue(fileExists);
    }
}
