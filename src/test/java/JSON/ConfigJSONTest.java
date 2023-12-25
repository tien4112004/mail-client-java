package JSON;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import Filter.*;
import Message.Message;
import UI.MainMenu;
import JSON.ConfigJSON;

public class ConfigJSONTest {
    @BeforeEach
    void setUp() {
        Mailbox mailbox = new Mailbox("test", "./test/");
        try {
            Files.copy(Paths.get("./src/test/java/Message/testEmail.msg"), Paths.get("./test/testEmail.msg"));
            System.out.println("[INFO] [MailboxTest] Test initiation successful.");
        } catch (Exception e) {
            System.out.println("[ERROR][initiation] Error in copying test email file.");
            e.printStackTrace();
            return;
        }
    }

    @Test
    public void testFilter() {
        String[] senderKeywords = { "null", "null", "null", "null", "null" };
        String[] subjectKeywords = { "null", "null", "null", "null", "null" };
        String[] contentKeywords = { "null", "null", "null", "null", "null" };

        Mailbox testMailbox = new Mailbox("test1", "./test1/", senderKeywords, subjectKeywords, contentKeywords);

        Mailbox exampleMailbox = new Mailbox("example", "./example/", senderKeywords, subjectKeywords, contentKeywords);

        Mailbox emailMailbox = new Mailbox("email", "./email/", senderKeywords, subjectKeywords, contentKeywords);

        Mailbox[] mailboxs = { testMailbox, exampleMailbox, emailMailbox };

        MainMenu UI = new MainMenu();
        UI.username = "example@localhost";
        UI.password = "123456";

        ConfigJSON configJSON = new ConfigJSON();
        try {
            configJSON.writeConfig(mailboxs, UI);
        } catch (Exception e) {
            e.printStackTrace();
        }

        deleteFolder(new File("./test1/"));
        deleteFolder(new File("./example/"));
        deleteFolder(new File("./email/"));
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files == null)
            return;
        for (File file : files) {
            if (file.isDirectory()) {
                deleteFolder(file);
            } else {
                file.delete();
            }
        }
        folder.delete();
    }
}
