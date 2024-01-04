package Filter;

import java.util.ArrayList;
import java.util.List;

import Email.Email;

public class SenderFilter implements Filter {
    // private String[] senderKeywords;
    private List<String> senderKeywords;

    public SenderFilter(String... senderKeywords) {
        this.senderKeywords = new ArrayList<String>();
        addKeywords(senderKeywords);
    }

    public boolean matches(Email email) {
        if (senderKeywords == null)
            return false;

        String emailSender = email.getSender().toLowerCase();
        for (String keyword : senderKeywords) {
            if (emailSender.contains(keyword))
                return true;
        }
        return false;
    }

    public List<String> getKeywords() {
        return senderKeywords;
    }

    public void addKeywords(String... keywords) {
        for (String keyword : keywords) {
            keyword = keyword.toLowerCase();
            senderKeywords.add(keyword);
        }
    }
}