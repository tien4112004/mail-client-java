package UI;

import Filter.*;
import Message.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

import javax.security.auth.Subject;

public class EmailSearcher extends UI {
    private static final int PAGE_SIZE = 10;
    private int currentPage = 0;

    private Filter filter;
    private List<String> emailDirectories;
    private InputHandler inputHandler;
    private List<String> searchResult = new ArrayList<>();

    public EmailSearcher(Filter filter, List<String> emailDirectories, InputHandler inputHandler) {
        this.filter = filter;
        this.emailDirectories = emailDirectories;
        this.inputHandler = inputHandler;
    }

    public EmailSearcher(List<String> emailDirectories, InputHandler inputHandler) {
        this.emailDirectories = emailDirectories;
        this.inputHandler = inputHandler;
    }

    public void getFilter() {
        String[][] filterOptions = { { "1", "Sender" }, { "2", "Subject" }, { "3", "Content" } };
        System.out.print("Filter by: ");
        showOptions(filterOptions);
        String option = inputHandler.dialog(EMPTY_PROMPT);
        String[] keywords = inputHandler.dialogList("Keywords [separated by commas]: \n");

        switch (option) {
            case "1":
                this.filter = new SenderFilter(keywords);
                break;
            case "2":
                this.filter = new SubjectFilter(keywords);
                break;
            case "3":
                this.filter = new ContentFilter(keywords);
                break;
            default:
                System.out.println(ANSI_TEXT_RED + "[ERROR] Invalid filter option." + ANSI_RESET);
                return;
        }
    }

    public void search() {
        getFilter();
        for (String emailDirectory : emailDirectories) {
            MessageParser messageParser = new MessageParser();
            String rawEmail = null;
            try {
                rawEmail = new String(Files.readAllBytes(Paths.get(emailDirectory)));
            } catch (IOException e) {
                System.out.println(ANSI_TEXT_RED + "[ERROR] Cannot read email." + ANSI_RESET);
                // e.printStackTrace();
            }
            messageParser.parseHeaderAndContent(rawEmail);
            Message currentEmail = messageParser.createMessage();
            if (filter.matches(currentEmail)) {
                searchResult.add(emailDirectory);
            }
        }
        showSearchResult();
    }

    private void showSearchResult() {
        clearConsole();
        if (searchResult.size() == 0) {
            System.out.println(ANSI_TEXT_RED + "[ERROR] No email found." + ANSI_RESET);
            return;
        }
        ListEmails listSearchResult = new ListEmails(searchResult, inputHandler);
        listSearchResult.list("Search result");
    }
}