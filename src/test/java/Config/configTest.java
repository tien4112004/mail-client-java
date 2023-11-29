package Config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import java.nio.file.Paths;
import java.nio.file.Files;

import Config.Config;
import Editor.Editor;
import Envelope.Envelope;

public class configTest {
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
    Config config = new Config();

    Envelope envelope = new Envelope();
    envelope.recipients = new String[] { "ltttin@fit.hcmus", "example@gmail.com", "pttien@fit.hcmus.edu.vn" };
    envelope.subject = "ASAP";

    Editor editor = new Editor();
    config.writeConfig(editor, envelope);
    // check if the file is created
    boolean actual = Files.exists(Paths.get("src/main/java/config/Config.json"));
    assertTrue(actual);
  }
}