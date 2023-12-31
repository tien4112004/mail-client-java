package UI;

import java.io.IOException;
import java.util.List;

import Filter.Mailbox;

public class MainMenu extends UI {
    private UserInformation userInfo;
    public String username;
    public String password;
    List<Mailbox> mailboxes;
    String currentEmailDirectory;

    protected InputHandler inputHandler;

    public MainMenu() {
        this.inputHandler = new InputHandler();
        this.userInfo = new UserInformation(inputHandler);
        this.username = userInfo.getUsername();
        this.password = userInfo.getPassword();
    }

    public MainMenu(InputHandler inputHandler, UserInformation userInfo) {
        this.inputHandler = inputHandler;
        this.userInfo = userInfo;
        this.username = userInfo.getUsername();
        this.password = userInfo.getPassword();
        this.mailboxes = userInfo.getMailboxes();
    }

    protected void displayMenu() {
        clearConsole();
        System.out.printf("Welcome back, %s%s%s!\n", ANSI_TEXT_BLUE, username, ANSI_RESET);
        String[][] options = {
                { "S", "Send new email" },
                { "M", "Open mailboxes" },
                { "Q", "Quit" }
        };
        showOptions(options);

        System.out.print("\nPlease select an option: ");
    }

    private void handleUserInput() {
        String userInput = inputHandler.dialog(EMPTY_PROMPT);
        switch (userInput) {
            case "S":
                clearConsole();
                new SendEmails(username, inputHandler).send();
                return;
            case "M":
                clearConsole();
                ListMailboxes listMailboxesUI = new ListMailboxes(username, inputHandler, mailboxes);
                listMailboxesUI.list();
                return;
            case "Q":
                clearConsole();
                System.exit(0);
            default:
                System.out.println("Invalid option! Aborting...");
                System.exit(0);
        }
    }

    public void start() throws IOException {
        while (true) {
            clearConsole();
            displayMenu();
            handleUserInput();
        }
    }
}