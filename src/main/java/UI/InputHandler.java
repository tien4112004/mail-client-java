package UI;

import java.util.Scanner;

public class InputHandler {
    private Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public int getMenuOption() {
        String prompt = "\nPlease choose your option: ";
        int option = Integer.parseInt(dialog(prompt));
        return option;
    }

    protected String dialog(String prompt) {
        System.out.print(prompt);
        String response = scanner.nextLine();
        return response;
    }

    protected String[] dialogList(String prompt) {
        System.out.print(prompt);
        String rawResponse = scanner.nextLine();
        String[] responseList = rawResponse.split(", ");
        for (String response : responseList) {
            response = response.trim().toLowerCase();
        }
        return responseList;
    }
}