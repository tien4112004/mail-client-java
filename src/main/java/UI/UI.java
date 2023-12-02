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

    private Scanner readConsole;

    public UI() {
        this.readConsole = new Scanner(System.in);
    }

    private void clearConsole() {
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

    private void showOption() throws IOException {
        System.out.println("Menu: ");
        System.out.println("1: Send email");
        System.out.println("2: Watch list of emails");
        System.out.println("3: Quit");
        System.out.print("Please choose your option: ");
        int option = readConsole.nextInt();
        clearConsole();
        switch (option) {
            case 1:
                new SendFunction().send();
                break;
            case 2:
                listFunction();
                break;
            default:
                System.exit(0);
        }
    }

    private void listFunction() throws IOException {
        System.out.print("This function is in development! Want to goback? (Y/N) ");
        readConsole.nextLine();
        String answer = readConsole.nextLine();
        if (answer.equalsIgnoreCase("Y")) {
            clearConsole();
            showOption();
        }
    }

    // This start the UI in console
    private void start() throws IOException {
        // login();
        showOption();
    }

    public static void main(String[] args) throws IOException {
        UI ui = new UI();
        ui.start();
    }

    // private boolean verifyEmail(String email) {
    // return email.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    // }
}
