package Filter;

import Message.Message;

public class SenderFilter implements Filter {
    private String[] senderKeywords;

    public SenderFilter(String... senderKeywords) {
        this.senderKeywords = senderKeywords;
        for (String keyword : senderKeywords) {
            keyword = keyword.toLowerCase();
        }
    }

    public boolean matches(Message email) {
        if (senderKeywords == null)
            return false;

        String emailSender = email.getSender().toLowerCase();
        for (String keyword : senderKeywords) {
            if (emailSender.contains(keyword))
                return true;
        }
        return false;
    }

    public String[] getKeywords() {
        return senderKeywords;
    }
}