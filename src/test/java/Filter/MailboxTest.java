package Filter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MailboxTest {
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
    void testConstructor() {
        assertTrue(Files.exists(Paths.get("./test/")));
    }

    @Test
    void testAddEmailIfMatches() {
        Mailbox testMailbox = new Mailbox("test1", "./test1/");
        Filter filter = new ContentFilter("test");
        testMailbox.addEmailIfMatches(Paths.get("./test/testEmail.msg"), filter);
        assertTrue(Files.exists(Paths.get("./test1/testEmail.msg")));

        Mailbox exampleMailbox = new Mailbox("example", "./example/");
        Filter exampleFilter = new SenderFilter("example");
        exampleMailbox.addEmailIfMatches(Paths.get("./test/testEmail.msg"), exampleFilter);
        assertTrue(Files.exists(Paths.get("./example/testEmail.msg")));

        Mailbox emailMailbox = new Mailbox("email", "./email/");
        Filter emailFilter = new SubjectFilter("email");
        emailMailbox.addEmailIfMatches(Paths.get("./test/testEmail.msg"), emailFilter);
        assertTrue(Files.exists(Paths.get("./email/testEmail.msg")));

        deleteFolder(new File("./test1/"));
        deleteFolder(new File("./example/"));
        deleteFolder(new File("./email/"));
    }

    @AfterEach
    void tearDown() throws Exception {
        File testFolder = new File("./test/");
        deleteFolder(testFolder);
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