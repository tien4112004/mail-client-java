package UI;

import Filter.Mailbox;
import Message.MessageParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
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

    protected void list(Mailbox mailbox) throws IOException {
        final String LEFT_ARROW = "<";
        final String RIGHT_ARROW = ">";
        final String QUIT = "Q";

        Path path = mailbox.getMailboxDirectory();
        List<String> result = Files.walk(path)
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .collect(Collectors.toList());

        int currentPage = 0;
        Scanner scanner = new Scanner(System.in);

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
                String rawMessage = new String(Files.readAllBytes(Paths.get(result.get(i))));
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
            String input = scanner.nextLine();

            if (input.equals(QUIT)) {
                // scanner.close();
                return;
            } else if (input.equals(LEFT_ARROW)) {
                if (currentPage > 0) {
                    currentPage--;
                }
            } else if (input.equals(RIGHT_ARROW)) {
                if (currentPage < result.size() / PAGE_SIZE) {
                    currentPage++;
                }
            } else if (input.equals("D")) {
                deleteEmailHandler(scanner, result, currentPage);
            } else if (input.equals("M")) {
                System.out.print("Email to move: ");
                String userInput = scanner.nextLine();
                String emailDirectory = result.get(currentPage * PAGE_SIZE + Integer.parseInt(userInput));
                System.out.print("Destination mailbox: ");
                String destination = scanner.nextLine();
                Mailbox.moveMailToFolder(emailDirectory, destination);
                int mailOrder = currentPage * PAGE_SIZE + Integer.parseInt(userInput);
                result.remove(mailOrder);
            } else {
                int mailOrder = currentPage * PAGE_SIZE + Integer.parseInt(input);
                ViewEmail emailViewer = new ViewEmail(result.get(currentPage * PAGE_SIZE + Integer.parseInt(input)),
                        result, mailOrder);
                emailViewer.showEmail();
                String userInput = scanner.nextLine();
                emailViewer.handleUserInput(userInput);
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

    private void deleteEmailHandler(Scanner scanner, List<String> result, int currentPage) {
        System.out.println("Delete which email?");
        String userInput = scanner.nextLine();
        Path emailPath = Paths.get(result.get(currentPage * PAGE_SIZE + Integer.parseInt(userInput)));
        try {
            Files.delete(emailPath);
        } catch (IOException e) {
            System.out.println(ANSI_TEXT_RED + "[ERROR] Error in deleting email." + ANSI_RESET);
            e.printStackTrace();
        }
        result.remove(currentPage * PAGE_SIZE + Integer.parseInt(userInput));
    }

    public static void main(String[] args) throws IOException {
        ListEmails listEmails = new ListEmails();
        Mailbox mailbox = new Mailbox("Test Mailbox",
                "/media/Windows_Data/OneDrive-HCMUS/Documents/CSC10008 - Computer Networking/Socket_Project-Mail_Client/example@fit.hcmus.edu.vn/");
        listEmails.list(mailbox);

    }
}