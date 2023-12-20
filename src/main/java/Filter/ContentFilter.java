package Filter;

import Message.Message;

public class ContentFilter implements Filter {
    private String[] contentKeywords;

    public ContentFilter(String... contentKeywords) {
        this.contentKeywords = contentKeywords;
        for (String keyword : contentKeywords) {
            keyword = keyword.toLowerCase();
        }
    }

    public boolean matches(Message email) {
        if (contentKeywords == null)
            return false;

        String emailContent = email.getContent().toLowerCase();
        for (String keyword : contentKeywords) {
            if (emailContent.contains(keyword))
                return true;
        }
        return false;
    }

    public String[] getKeywords() {
        return contentKeywords;
    }
}