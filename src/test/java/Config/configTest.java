package Config;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import java.nio.file.Paths;
import java.nio.file.Files;

import Editor.Editor;
import Envelope.Envelope;

public class configTest {
  @Test
  public void testWriteConfig() throws IOException {
    config config = new config();

    Envelope envelope = new Envelope();
    envelope.recipients = new String[] { "ltttin@fit.hcmus, example@gmail.com, pttien@fit,hcmus.edu.vn" };
    envelope.subject = "urgent, ASAP";

    Editor editor = new Editor();
    config.writeConfig(editor, envelope);
    // check if the file is created
    boolean actual = Files.exists(Paths.get("src/main/java/config/Config.json"));
    assertTrue(actual);
  }
}