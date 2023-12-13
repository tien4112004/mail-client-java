package UI;

import Filter.Mailbox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class ListEmailsTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    // @BeforeEach
    // public void setUpStreams() {
    // System.setOut(new PrintStream(outContent));
    // }

    // @Test
    void testList() throws Exception {
        ListEmails listEmails = new ListEmails();
        Mailbox mailbox = new Mailbox("Test Mailbox",
                "/media/Windows_Data/OneDrive-HCMUS/Documents/CSC10008 - Computer Networking/Socket_Project-Mail_Client/example@fit.hcmus.edu.vn/");
        listEmails.list(mailbox);

        String expectedOutput = "List of emails from Test Mailbox: \n" +
                "====================================\n" +
                "# | | From | Subject | Date | attachment |\n" +
                "====================================\n";
        // Add expected output for each email in the mailbox

        System.out.println(outContent.toString());
        // assertEquals(expectedOutput, outContent.toString());
    }
}