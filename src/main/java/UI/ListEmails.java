package UI;

import Filter.Mailbox;
import Message.MessageParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.io.IOException;

public class ListEmails extends UI {
    private final int PAGE_SIZE = 10; // Number of emails per page
    private final String FROM = formatString("From", 20);
    private final String SUBJECT = formatString("Subject", 30);
    private final String DATE = formatString("Date", 10);
    private final String ATTACHMENT = formatString("Attachment", 10);
    private final String SHORT_DATE_DISPLAY_FORMAT = "dd/MM/yyyy";
    private final String PART_SPLITER = "========================================================================================\n";

    private Mailbox mailbox;
    private int currentPage = 1;

    public ListEmails(Mailbox mailbox, InputHandler inputHandler) {
        this.mailbox = mailbox;
        this.inputHandler = inputHandler;
    }

    protected void list() {
        final String LEFT_ARROW = "<";
        final String RIGHT_ARROW = ">";
        final String QUIT = "Q";

        Path path = mailbox.getMailboxDirectory();
        List<String> result;
        try {
            result = Files.walk(path)
                    .filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(ANSI_TEXT_RED + "[ERROR] Error in listing emails." + ANSI_RESET);
            e.printStackTrace();
            return;
        }

        while (true) {
            clearConsole();
            System.out.printf("List of emails from %s: Page %d/%d\n", mailbox.getMailboxName(), currentPage + 1,
                    result.size() / PAGE_SIZE + 1);
            System.out.print(PART_SPLITER);
            System.out.printf("%s   | %s | %s | %s | %s |\n", "# ", FROM, SUBJECT, DATE, ATTACHMENT);
            System.out.print(PART_SPLITER);

            int start = currentPage * PAGE_SIZE;
            int end = Math.min(start + PAGE_SIZE, result.size());
            for (int i = start; i < end; i++) {
                String rawMessage = "";
                try {
                    rawMessage = new String(Files.readAllBytes(Paths.get(result.get(i))));
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

            String[][] options = {
                    { "<", "Previous page" },
                    { ">", "Next page" },
                    { "#", "Read email #" },
                    { "D", "Delete mail" },
                    { "M", "Move email " },
                    { "Q", "Quit" }
            };
            showOptions(options);
            String input = inputHandler.dialog(EMPTY_PROMPT);

            if (input.equals(QUIT)) {
                return;
            } else if (input.equals(LEFT_ARROW)) {
                if (currentPage > 0) {
                    currentPage--;
                }
                list();
            } else if (input.equals(RIGHT_ARROW)) {
                if (currentPage < result.size() / PAGE_SIZE) {
                    currentPage++;
                }
                list();
            } else if (input.equals("D")) {
                deleteEmailHandler(result, currentPage);
                list();
            } else if (input.equals("M")) {
                String userInput = inputHandler.dialog("Email to move: ");
                String emailDirectory = result.get(currentPage * PAGE_SIZE + Integer.parseInt(userInput));
                String destination = inputHandler.dialog("Destination mailbox: ");
                Mailbox.moveMailToFolder(emailDirectory, destination);
                int mailOrder = currentPage * PAGE_SIZE + Integer.parseInt(userInput);
                result.remove(mailOrder);
                list();
            } else {
                int mailOrder = currentPage * PAGE_SIZE + Integer.parseInt(input);
                ViewEmail emailViewer = new ViewEmail(result.get(currentPage * PAGE_SIZE + Integer.parseInt(input)),
                        result, mailOrder, mailboxes, inputHandler);
                emailViewer.showEmail();
            }
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
        Path emailPath = Paths.get(emailList.get(currentPage * PAGE_SIZE + Integer.parseInt(userInput)));
        try {
            Files.delete(emailPath);
        } catch (IOException e) {
            System.out.println(ANSI_TEXT_RED + "[ERROR] Error in deleting email." + ANSI_RESET);
            e.printStackTrace();
        }
        emailList.remove(currentPage * PAGE_SIZE + Integer.parseInt(userInput));
    }

    public static void main(String[] args) throws IOException {
        Mailbox mailbox = new Mailbox("Test Mailbox",
                "/media/Windows_Data/OneDrive-HCMUS/Documents/CSC10008 - Computer Networking/Socket_Project-Mail_Client/example@fit.hcmus.edu.vn/");
        ListEmails listEmails = new ListEmails(mailbox, new InputHandler());
        listEmails.list();
    }
}