package UI;

import Filter.Mailbox;
import Filter.Mailbox.*;
import Message.MessageParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.io.IOException;

public class ListEmails extends UI {
    protected void list(Mailbox mailbox) throws IOException {
        final int PAGE_SIZE = 10; // Number of emails per page
        final String FROM = formatString("From", 20);
        final String SUBJECT = formatString("Subject", 30);
        final String DATE = formatString("Date", 10);
        final String ATTACHMENT = formatString("Attachment", 10);
        final String SHORT_DATE_DISPLAY_FORMAT = "dd/MM/yyyy";
        final String PART_SPLITER = "========================================================================================\n";

        // keys constant
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
            System.out.print("\033[H\033[2J");
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
                parser.parse(rawMessage);
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

            System.out.println(
                    "\n[<] Previous page \t [>] Next page \t [#] Read #-th email \t [Q] Quit");
            String input = scanner.nextLine();

            // left arrow to go to previous page amd right arrow to go to next page
            if (input.equals(QUIT)) {
                break;
            } else if (input.equals(LEFT_ARROW)) {
                if (currentPage > 0) {
                    currentPage--;
                }
            } else if (input.equals(RIGHT_ARROW)) {
                if (currentPage < result.size() / PAGE_SIZE) {
                    currentPage++;
                }
            } else {
            }
        }
    }

    String formatString(String original, int length) {
        if (original == null)
            return formatString("null", length);
        if (original.length() > length) {
            return original.substring(0, length - 3) + "...";
        }
        return original.format("%-" + length + "." + length + "s", original);
    }

    public static void main(String[] args) throws IOException {
        ListEmails listEmails = new ListEmails();
        Mailbox mailbox = new Mailbox("Test Mailbox",
                "/media/Windows_Data/OneDrive-HCMUS/Documents/CSC10008 - Computer Networking/Socket_Project-Mail_Client/example@fit.hcmus.edu.vn/");
        listEmails.list(mailbox);

    }
}