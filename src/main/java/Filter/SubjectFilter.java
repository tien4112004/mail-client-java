package Filter;

import java.util.ArrayList;
import java.util.List;

import Message.Message;

public class SubjectFilter implements Filter {
    private List<String> subjectKeywords;

    public SubjectFilter(String... subjectKeywords) {
        this.subjectKeywords = new ArrayList<String>();
        addKeywords(subjectKeywords);
    }

    public boolean matches(Message email) {
        if (subjectKeywords == null)
            return false;

        String emailSubject = email.getSubject().toLowerCase();
        for (String keyword : subjectKeywords) {
            if (emailSubject.contains(keyword))
                return true;
        }
        return false;
    }

    public List<String> getKeywords() {
        return subjectKeywords;
    }

    public void addKeywords(String... keywords) {
        for (String keyword : keywords) {
            keyword = keyword.toLowerCase();
            subjectKeywords.add(keyword);
        }
    }
}