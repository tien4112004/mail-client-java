import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Filter.Mailbox;
import Socket.POP3Socket;
import UI.MainMenu;

public class Main {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_TEXT_YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        MainMenu ui = new MainMenu();
        List<Mailbox> mailboxes = new ArrayList<Mailbox>(
                Arrays.asList(new Mailbox("Inbox"), new Mailbox("Sent"), new Mailbox("Spam"))); // load from config
        POP3Socket pop3Socket = new POP3Socket("localhost", 3335, ui.username, ui.password);
        try {
            System.out.println(ANSI_TEXT_YELLOW + "Retrieving message..." + ANSI_RESET);
            pop3Socket.retrieveMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ui.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}