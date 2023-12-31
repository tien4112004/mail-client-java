package UI;

import Filter.Mailbox;
import Message.MessageParser;
import JSON.WriteMessageStatus;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ListEmails extends MainMenu {
    private final int PAGE_SIZE = 10;
    private final String FROM = formatString("From", 20);
    private final String SUBJECT = formatString("Subject", 30);
    private final String DATE = formatString("Date", 10);
    private final String ATTACHMENT = formatString("Attachment", 10);
    private final String SHORT_DATE_DISPLAY_FORMAT = "dd/MM/yyyy";
    private final String PART_SPLITER = "========================================================================================\n";
    private final String DEFAULT_WORKING_DIRECTORY = "./";
    String MessageStatusJSONDirectory = DEFAULT_WORKING_DIRECTORY + "MessageStatus.json";

    private Mailbox mailbox;
    private int currentPage = 0;
    List<String> mailList;

    JSONParser parser = new JSONParser();
    WriteMessageStatus writeMessageStatus = null;
    JSONArray messageList = null;
    JSONObject messageObject = null;

    public ListEmails(Mailbox mailbox, InputHandler inputHandler) {
        this.mailbox = mailbox;
        this.inputHandler = inputHandler;
        this.writeMessageStatus = new WriteMessageStatus();
        try {
            this.messageList = (JSONArray) parser.parse(new FileReader(MessageStatusJSONDirectory));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ListEmails(List<String> mailList, InputHandler inputHandler) {
        this.mailList = mailList;
        this.inputHandler = inputHandler;
        this.writeMessageStatus = new WriteMessageStatus();
        try {
            this.messageList = (JSONArray) parser.parse(new FileReader(MessageStatusJSONDirectory));
        } catch (Exception e) {
            System.out.printf("%s%s%s\n", ANSI_TEXT_RED, "[ERROR] Error in reading email status.", ANSI_RESET);
            return;
        }
    }

    protected void list() {
        boolean keepRunning = true;
        while (keepRunning) {
            loadEmails();
            displayEmails();
            keepRunning = handleUserInput();
        }
    }

    protected void list(String customPrompt) {
        boolean keepRunning = true;
        while (keepRunning) {
            displayEmails(customPrompt);
            keepRunning = handleUserInput();
        }
    }

    private void displayEmails() {
        clearConsole();
        displayHeader();
        displayEmailList();
        displayOptions();
    }

    private void displayEmails(String customPrompt) {
        clearConsole();
        displayHeader(customPrompt);
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

    private void displayHeader(String customPrompt) {
        System.out.printf("%s%s%s: Page %d/%d\n", ANSI_TEXT_CYAN, customPrompt,
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
            messageObject = (JSONObject) messageList.get(i);
            String readStatus = (messageObject.containsValue(false)) ? "*" : " ";
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
        String[][] options = { { "<", "Previous page" },
                { ">", "Next page" },
                { "#", "Read email #" },
                { "S", "Search email" },
                // { "F", "Add new filter" },
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

        try {
            messageList = (JSONArray) parser.parse(new FileReader(MessageStatusJSONDirectory));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean handleUserInput() {
        String userInput = inputHandler.dialog(EMPTY_PROMPT);
        switch (userInput) {
            case "<":
                previousPage();
                return true;
            case ">":
                nextPage();
                return true;
            case "Q":
                return false;
            case "D":
                deleteEmailHandler(mailList, currentPage);
                return true;
            case "M":
                moveEmailHandler(mailList, currentPage);
                return true;
            case "S":
                EmailSearcher emailSearcher = new EmailSearcher(mailList, inputHandler);
                emailSearcher.search();
                return true;
            default:
                handleEmailSelection(userInput);
                return true;
        }
    }

    private void previousPage() {
        currentPage = Math.max(currentPage - 1, 0);
    }

    private void nextPage() {
        currentPage = Math.min(currentPage + 1, mailList.size() / PAGE_SIZE);
    }

    private void handleEmailSelection(String userInput) {
        if (isInvalidOptions(userInput))
            return;

        int emailIndex = currentPage * PAGE_SIZE + Integer.parseInt(userInput);
        if (emailIndex >= mailList.size() || Integer.parseInt(userInput) > 9 || Integer.parseInt(userInput) < 0) {
            System.out.printf("%s[ERROR] Invalid email number!%s\n", ANSI_TEXT_RED, ANSI_RESET);
            sleep(TIME_2_SECONDS);
            return;
        }

        String emailDirectory = mailList.get(emailIndex);
        ViewEmail emailViewer = new ViewEmail(emailDirectory, mailList, emailIndex, mailboxes, inputHandler);

        // TO BE CHANGED
        messageObject = (JSONObject) messageList.get(emailIndex);
        messageList.remove(messageObject);
        String keyObject = (String) messageObject.keySet().toArray()[0];
        messageObject.replace(keyObject, true);
        messageList.add(emailIndex, messageObject);
        try {
            writeMessageStatus.writeJSON(messageList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // END

        emailViewer.showEmail();
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
        sleep(TIME_2_SECONDS);
    }

    private void moveEmailHandler(List<String> mailList, int currentPage) {
        String userInput = inputHandler.dialog("Email to move: ");
        int emailIndex = currentPage * PAGE_SIZE + Integer.parseInt(userInput);
        String emailDirectory = mailList.get(emailIndex);
        String destination = inputHandler.dialog("Destination mailbox: ");
        Mailbox.moveMailToFolder(emailDirectory, destination);
        System.out.printf("%s%s%s\n", ANSI_TEXT_GREEN, "Email moved to " + destination + ".", ANSI_RESET);
        sleep(TIME_2_SECONDS);
        mailList.remove(emailIndex);
    }
}