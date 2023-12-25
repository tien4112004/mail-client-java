package JSON;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import java.nio.file.Paths;
import java.nio.file.Files;

// import Config.Config;
import UI.MainMenu;
import Message.Message;

public class ConfigTest {
  // @Test
  // public void testWorkKeywordsHandler() {
  // Config config = new Config();

  // String[] actual = config.workKeywordsHandler();
  // String[] expected = { "report", "meeting" };

  // assertArrayEquals(actual, expected);
  // }

  // @Test
  // void testSpamKeywordHanlder() {
  // Config config = new Config();

  // String[] actual = config.spamKeywordsHandler();
  // String[] expected = { "virus", "hack", "crack", "virus", "hack", "crack",
  // "virus", "hack", "crack" };

  // assertArrayEquals(actual, expected);
  // }

  @Test
  public void testWriteConfig() throws IOException {
    // Config config = new Config();
    String sender = "sender@localhost";
    String[] recipientsTo = { "recipientTo1@localhost", "recipientTo2@localhost" };
    String[] recipientsCc = { "recipientCc1@localhost", "recipientCc2@localhost" };
    String[] recipientsBcc = { "recipientBcc1@localhost", "recipientBcc2@localhost" };
    String subject = "Test Email";
    String content = "This is a test email";
    String[] attachments = {};

    Message message = new Message(sender, recipientsTo, recipientsCc, recipientsBcc, subject, content, attachments);

    MainMenu UI = new MainMenu();
    UI.username = "example@localhost";
    UI.password = "123456";
    // config.writeConfig(UI, message);
    // check if the file is created
    boolean actual = Files.exists(Paths.get("./Config.json"));
    assertTrue(actual);
  }
}