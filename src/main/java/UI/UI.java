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
    // private static Message message;
    public String username; // TODO: get this from JSON
    public String password;

    protected Scanner readConsole;

    public UI() {
        this.readConsole = new Scanner(System.in);
    }

    protected void clearConsole() {
        System.out.print("\033[H\033[2J");
    }

    // private void login() {
    // System.out.println("Welcome to our email client!");
    // System.out.println("Please input your username and password to login");
    // System.out.print("Username: ");
    // username = readConsole.nextLine();
    // System.out.print("Password: ");
    // password = readConsole.nextLine();
    // // Check if username and password have already in JSON file, if not then
    // write
    // // to JSON file, else check if matche with JSON file
    // // clear console
    // clearConsole();
    // }

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
                // new ListEmails().list();
                break;
            default:
                System.exit(0);
        }
    }

    // This start the UI in console
    public void start() throws IOException {
        // login();
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
