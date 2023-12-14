package UI;

import Filter.Mailbox;
import Message.MessageParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ListEmails extends UI {
    private final int PAGE_SIZE = 10; // Number of emails per page
    private final String FROM = formatString("From", 20);
    private final String SUBJECT = formatString("Subject", 30);
    private final String DATE = formatString("Date", 10);
    private final String ATTACHMENT = formatString("Attachment", 10);
    private final String SHORT_DATE_DISPLAY_FORMAT = "dd/MM/yyyy";
    private final String PART_SPLITER = "========================================================================================\n";

    private Mailbox mailbox;
    private int currentPage = 0;
    List<String> mailList;
    BackToPreviousCallback backToMailboxListCallback;

    public ListEmails(Mailbox mailbox, InputHandler inputHandler, BackToPreviousCallback backToMailboxListCallback) {
        this.mailbox = mailbox;
        this.inputHandler = inputHandler;
        this.backToMailboxListCallback = backToMailboxListCallback;
    }

    protected void list() {
        loadEmails();
        displayEmails();
        handleUserInput();
    }

    private void displayEmails() {
        clearConsole();
        displayHeader();
        displayEmailList();
        displayOptions();
    }

    private void displayHeader() {
        System.out.printf("List of emails from %s%s%s: Page %d/%d\n", ANSI_TEXT_CYAN, mailbox.getMailboxName(),
                ANSI_RESET, currentPage + 1,
                mailList.size() / PAGE_SIZE + 1);
        System.out.print(PART_SPLITER);
        System.out.printf("%s   | %s | %s | %s | %s |\n", "# ", FROM, SUBJECT, DATE, ATTACHMENT);
        System.out.print(PART_SPLITER);
    }

    private void displayEmailList() {
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, mailList.size());
        for (int i = start; i < end; i++) {
            String rawMessage = "";
            try {
                rawMessage = new String(Files.readAllBytes(Paths.get(mailList.get(i))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            MessageParser parser = new MessageParser();
            parser.quickParse(rawMessage);
            String readStatus = "*";
            String sender = parser.getSender();
            sender = formatString(sender, 20);
            String subject = parser.getSubject();
            subject = formatString(subject, 30);
            String date = parser.getDate(SHORT_DATE_DISPLAY_FORMAT);
            String attachment = (parser.hasAttachment() ? formatString("*", 10) : formatString("", 10));

            System.out.printf("[%d] %s| %s | %s | %s | %s |\n", i % 10, readStatus, sender, subject, date,
                    attachment);
        }
    }

    private void displayOptions() {
        String[][] options = {
                { "<", "Previous page" },
                { ">", "Next page" },
                { "#", "Read email #" },
                { "D", "Delete mail" },
                { "M", "Move email " },
                { "Q", "Quit" }
        };
        showOptions(options);
    }

    private void loadEmails() {
        Path path = mailbox.getMailboxDirectory();
        try {
            mailList = Files.walk(path).filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(ANSI_TEXT_RED + "[ERROR] Error in listing emails." + ANSI_RESET);
            e.printStackTrace();
        }
    }

    private void previousPage() {
        currentPage = Math.max(currentPage - 1, 0);
        list();
    }

    private void nextPage() {
        currentPage = Math.min(currentPage + 1, mailList.size() / PAGE_SIZE);
        list();
    }

    private void handleUserInput() {
        String userInput = inputHandler.dialog(EMPTY_PROMPT);
        switch (userInput) {
            case "<":
                previousPage();
                break;
            case ">":
                nextPage();
                break;
            case "Q":
                backToMailboxListCallback.backToList();
            case "D":
                deleteEmailHandler(mailList, currentPage);
                break;
            case "M":
                moveEmailHandler(mailList, currentPage);
                break;
            default:
                int emailIndex = currentPage * PAGE_SIZE + Integer.parseInt(userInput);
                String emailDirectory = mailList.get(currentPage * PAGE_SIZE + Integer.parseInt(userInput));
                ViewEmail emailViewer = new ViewEmail(emailDirectory, mailList, emailIndex, mailboxes, inputHandler,
                        this::list);
                emailViewer.showEmail();
                displayEmails();
                break;
        }
    }

    private String formatString(String original, int length) {
        if (original == null)
            return formatString("null", length);
        if (original.length() > length) {
            return original.substring(0, length - 3) + "...";
        }
        return original.format("%-" + length + "." + length + "s", original);
    }

    private void deleteEmailHandler(List<String> emailList, int currentPage) {
        String userInput = inputHandler.dialog("Mail to delete: ");
        int emailIndex = currentPage * PAGE_SIZE + Integer.parseInt(userInput);
        Path emailPath = Paths.get(emailList.get(emailIndex));
        try {
            Files.delete(emailPath);
        } catch (IOException e) {
            System.out.println(ANSI_TEXT_RED + "[ERROR] Error in deleting email." + ANSI_RESET);
            e.printStackTrace();
        }
        emailList.remove(emailIndex);
        System.out.printf("%s%s%s\n", ANSI_TEXT_GREEN, "Email removed.", ANSI_RESET);
        sleep(2000);
    }

    private void moveEmailHandler(List<String> mailList, int currentPage) {
        String userInput = inputHandler.dialog("Email to move: ");
        int emailIndex = currentPage * PAGE_SIZE + Integer.parseInt(userInput);
        String emailDirectory = mailList.get(emailIndex);
        String destination = inputHandler.dialog("Destination mailbox: ");
        Mailbox.moveMailToFolder(emailDirectory, destination);
        mailList.remove(emailIndex);
    }

    public static void main(String[] args) throws IOException {
        Mailbox mailbox = new Mailbox("Test Mailbox",
                "/media/Windows_Data/OneDrive-HCMUS/Documents/CSC10008 - Computer Networking/Socket_Project-Mail_Client/example@fit.hcmus.edu.vn/");
        ListEmails listEmails = new ListEmails(mailbox, new InputHandler(), () -> System.out.println("Back to list"));
        listEmails.list();
    }
}