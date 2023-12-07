package UI;

import java.io.IOException;
import java.util.Scanner;

// import javax.imageio.stream.ImageOutputStreamImpl;
// import javax.sound.sampled.SourceDataLine;

// import Config.Config;
// import Message.Message;
// import Socket.SMTPSocket;
// import Envelope.Envelope;
// import UI.SendFunction;

public class UI {
    public String username = new GetUserLoginInfomation().getUsername(); // TODO: get this from JSON
    public String password = new GetUserLoginInfomation().getPassword();

    protected Scanner readConsole;

    public UI() {
        this.readConsole = new Scanner(System.in);
    }

    protected void clearConsole() {
        System.out.print("\033[H\033[2J");
    }

    protected void showOption() throws IOException {
        System.out.println("Menu: ");
        System.out.println("1: Send email");
        System.out.println("2: Watch list of emails");
        System.out.println("3: Quit");
        System.out.print("Please choose your option: ");
        int option = readConsole.nextInt();
        clearConsole();
        switch (option) {
            case 1:
                new SendEmails().send();
                break;
            case 2:
                new ListEmails().list();
                break;
            case 3:
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option! Aborting...");
                System.exit(0);
        }
    }

    // This start the UI in console
    public void start() throws IOException {
        while (true)
            showOption();
    }

    // public static void main(String[] args) throws IOException {
    // UI ui = new UI();
    // ui.start();
    // }

    // private boolean verifyEmail(String email) {
    // return email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    // }
}
