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
        // get mailboxes from JSON
    }

    public MainMenu(InputHandler inputHandler, UserInformation userInfo) {
        this.inputHandler = inputHandler;
        this.userInfo = userInfo;
        this.username = userInfo.getUsername();
        this.password = userInfo.getPassword();
        // // get mailboxes from JSON
    }

    protected void showOption() {
        clearConsole();
        System.out.printf("Welcome back, %s%s%s!\n", ANSI_TEXT_BLUE, username, ANSI_RESET);
        String[][] options = {
                { "1", "Send new email" },
                { "2", "Open mailboxes" },
                { "3", "Quit" }
        };
        showOptions(options);
        int option = inputHandler.getMenuOption();
        clearConsole();
        switch (option) {
            case 1:
                new SendEmails(inputHandler).send();
                break;
            case 2:
                ListMailboxes listMailboxesUI = new ListMailboxes(username, inputHandler);
                listMailboxesUI.list();
                break;
            case 3:
                System.exit(0);
            default:
                System.out.println("Invalid option! Aborting...");
                System.exit(0);
        }
    }

    public void start() throws IOException {
        while (true)
            showOption();
    }
}