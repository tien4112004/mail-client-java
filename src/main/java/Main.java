import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Filter.Mailbox;
import Socket.POP3Socket;
import UI.MainMenu;

class Multithreading extends Thread {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_TEXT_YELLOW = "\u001B[33m";
    private String server;
    private int port;
    private String username;
    private String password;

    private POP3Socket pop3Socket;
    
    public Multithreading(String server, int port, String username, String password) {
        this.server = server;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run() {
        // TODO: loop with condition.
        while (true){
            try {
                // System.out.println(ANSI_TEXT_YELLOW + "Retrieving message..." + ANSI_RESET);
                pop3Socket = new POP3Socket(server, port, username, password);
                pop3Socket.quit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                sleep(10000);  
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

public class Main {
    // private static final String ANSI_RESET = "\u001B[0m";
    // private static final String ANSI_TEXT_YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        // List<Mailbox> mailboxes = new ArrayList<Mailbox>(
        //         Arrays.asList(new Mailbox("Inbox"), new Mailbox("Sent"), new Mailbox("Spam"))); // load from config
        // POP3Socket pop3Socket = new POP3Socket("localhost", 3335, ui.username, ui.password);
        // try {
        //     System.out.println(ANSI_TEXT_YELLOW + "Retrieving message..." + ANSI_RESET);
        //     pop3Socket.retrieveMessage();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        MainMenu ui = new MainMenu();
        Multithreading reloadInterval = new Multithreading("localhost", 3335, ui.username, ui.password);
        reloadInterval.start();
        try {
            ui.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}