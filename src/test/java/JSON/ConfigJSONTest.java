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
        Mailbox testMailbox = new Mailbox("test1", "./test1/");
        Filter filter = new ContentFilter("test");
        testMailbox.addEmailIfMatches(Paths.get("./test1/testEmail.msg"), filter);
        assertTrue(Files.exists(Paths.get("./test1/testEmail.msg")));

        Mailbox exampleMailbox = new Mailbox("example", "./example/");
        Filter exampleFilter = new SenderFilter("example");
        exampleMailbox.addEmailIfMatches(Paths.get("./test/testEmail.msg"), exampleFilter);
        assertTrue(Files.exists(Paths.get("./example/testEmail.msg")));

        Mailbox emailMailbox = new Mailbox("email", "./email/");
        Filter emailFilter = new SubjectFilter("email");
        emailMailbox.addEmailIfMatches(Paths.get("./test/testEmail.msg"), emailFilter);
        assertTrue(Files.exists(Paths.get("./email/testEmail.msg")));

        Mailbox[] mailboxs = {testMailbox, exampleMailbox, emailMailbox};
        Filter[] filters = {filter, exampleFilter, emailFilter};

        MainMenu UI = new MainMenu();
        UI.username = "example@localhost";
        UI.password = "123456";

        ConfigJSON configJSON = new ConfigJSON();
        try {
            configJSON.writeConfig(mailboxs, filters, UI);
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
