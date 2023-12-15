import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Filter.Mailbox;
import Socket.POP3Socket;
import UI.UI;

public class Main {
    public static void main(String[] args) {
        UI ui = new UI();
        List<Mailbox> mailboxes = new ArrayList<Mailbox>(
                Arrays.asList(new Mailbox("Inbox"), new Mailbox("Sent"), new Mailbox("Spam")));
        POP3Socket pop3Socket = new POP3Socket("localhost", 3335, ui.username, ui.password);
        try {
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