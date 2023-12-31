import java.util.List;

import Filter.Mailbox;
import Socket.POP3Socket;
import UI.InputHandler;
import UI.MainMenu;
import UI.UserInformation;

class Multithreading extends Thread {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_TEXT_YELLOW = "\u001B[33m";
    private String server;
    private int port;
    private String username;
    private String password;

    private POP3Socket pop3Socket;
    private List<Mailbox> mailboxes;
    private int retrieveIntervalSeconds;

    public Multithreading(String server, int port, String username, String password, List<Mailbox> mailboxes,
            int retrieveIntervalSeconds) {
        this.server = server;
        this.port = port;
        this.username = username;
        this.password = password;
        this.mailboxes = mailboxes;
        this.retrieveIntervalSeconds = retrieveIntervalSeconds;
    }

    @Override
    public void run() {
        int tries = 0;
        while (tries < 3) {
            try {
                // System.out.println(ANSI_TEXT_YELLOW + "Retrieving message..." + ANSI_RESET);
                pop3Socket = new POP3Socket(server, port, username, password);
                pop3Socket.addMailboxes(mailboxes);
                pop3Socket.retrieveMessage();
                pop3Socket.quit();
            } catch (Exception e) {
                // e.printStackTrace();
                tries++;
                System.out.printf("%s[POP3] Retrying... (%d/3)%s\n", ANSI_TEXT_YELLOW, tries, ANSI_RESET);
            }
            try {
                sleep(retrieveIntervalSeconds * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.printf("%s[POP3] Stopped%s\n", ANSI_TEXT_YELLOW, ANSI_RESET);
    }
}

public class Main {
    public static void main(String[] args) {
        InputHandler inputHandler = new InputHandler();
        UserInformation userInfo = new UserInformation(inputHandler);
        int retrieveIntervalSeconds = userInfo.getRetrieveIntervalSeconds();
        String POP3Server = userInfo.getPOP3Server();
        int POP3Port = userInfo.getPOP3Port();

        MainMenu ui = new MainMenu(inputHandler, userInfo);
        List<Mailbox> mailboxes = userInfo.getMailboxes();
        Multithreading reloadInterval = new Multithreading(POP3Server, POP3Port, ui.username, ui.password,
                mailboxes, retrieveIntervalSeconds);
        reloadInterval.start();
        try {
            ui.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}