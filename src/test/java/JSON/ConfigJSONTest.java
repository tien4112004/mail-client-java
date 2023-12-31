package JSON;

import java.util.List;

import javax.security.auth.Subject;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import Filter.*;
import UI.MainMenu;

import JSON.ReadConfig;

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
    public void testWrite() {
        String[] senderKeywords = { "null", "null", "null", "null", "null" };
        String[] subjectKeywords = { "null", "null", "null", "null", "null" };
        String[] contentKeywords = { "null", "null", "null", "null", "null" };

        Mailbox testMailbox = new Mailbox("Inbox", "./Inbox/", senderKeywords, subjectKeywords, contentKeywords);

        Mailbox exampleMailbox = new Mailbox("example", "./example/", senderKeywords, subjectKeywords, contentKeywords);

        Mailbox emailMailbox = new Mailbox("email", "./email/", senderKeywords, subjectKeywords, contentKeywords);

        Mailbox[] mailboxes = { testMailbox, exampleMailbox, emailMailbox };

        MainMenu UI = new MainMenu();
        UI.username = "example@localhost";
        UI.password = "123456";

        WriteConfig configJSON = new WriteConfig();
        try {
            configJSON.writeConfig(mailboxes, "example@localhost", "*****");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readJSONTest() {
        ReadConfig readConfig = null;
        try {
            readConfig = new ReadConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Mailbox> mailboxes = readConfig.readMailboxes();
        for (Mailbox mailbox : mailboxes) {
            System.out.println("Name: " + mailbox.getMailboxName());
            System.out.println("\t - Directory: " + mailbox.getMailboxDirectory());
            System.out.println("\t - Filters: ");
            for (Filter filter : mailbox.getFilters()) {
                if (filter instanceof SenderFilter) {
                    System.out.print("\t\t + SenderFilter: ");
                    printKeywords(((SenderFilter) filter).getKeywords());
                }
                if (filter instanceof SubjectFilter) {
                    System.out.print("\t\t + SubjectFilter: ");
                    printKeywords(((SubjectFilter) filter).getKeywords());
                }
                if (filter instanceof ContentFilter) {
                    System.out.print("\t\t + ContentFilter: ");
                    printKeywords(((ContentFilter) filter).getKeywords());
                }
            }
        }
    }

    private void printKeywords(List<String> keywords) {
        for (String word : keywords) {
            System.out.print(word + " ");
        }
        System.out.println();
    }
}
