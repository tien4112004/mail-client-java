package UI;

import Filter.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import Email.*;

import java.util.ArrayList;

public class EmailSearcher extends UI {
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
        System.out.print("Search by: ");
        String[][] filterOptions = { { "1", "Sender" }, { "2", "Subject" }, { "3", "Content" } };
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
                displayErrorMessage("Invalid filter option.");
                return;
        }
    }

    public void search() {
        getFilter();
        for (String emailDirectory : emailDirectories) {
            EmailParser messageParser = new EmailParser();
            String rawEmail = null;
            try {
                rawEmail = new String(Files.readAllBytes(Paths.get(emailDirectory)));
            } catch (IOException e) {
                displayErrorMessage("Cannot read email.");
                // e.printStackTrace();
            }
            messageParser.parseHeaderAndContent(rawEmail);
            Email currentEmail = messageParser.createMessage();
            if (filter.matches(currentEmail)) {
                searchResult.add(emailDirectory);
            }
        }
        showSearchResult();
    }

    private void showSearchResult() {
        clearConsole();
        if (searchResult.size() == 0) {
            displayErrorMessage("No matched email.");
            return;
        }
        ListEmails listSearchResult = new ListEmails(searchResult, inputHandler);
        listSearchResult.list("Search result");
    }
}