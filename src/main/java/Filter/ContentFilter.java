package Filter;

import java.util.ArrayList;
import java.util.List;

import Message.Message;

public class ContentFilter implements Filter {
    private List<String> contentKeywords;

    public ContentFilter(String... contentKeywords) {
        this.contentKeywords = new ArrayList<String>();
        addKeywords(contentKeywords);
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

    public List<String> getKeywords() {
        return contentKeywords;
    }

    public void addKeywords(String... keywords) {
        for (String keyword : keywords) {
            keyword = keyword.toLowerCase();
            contentKeywords.add(keyword);
        }
    }
}