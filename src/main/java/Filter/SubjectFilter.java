package Filter;

import Message.Message;

public class SubjectFilter implements Filter {
    private String[] subjectKeywords;

    public SubjectFilter(String... subjectKeywords) {
        this.subjectKeywords = subjectKeywords;
        for (String keyword : subjectKeywords) {
            keyword = keyword.toLowerCase();
        }
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

    public String[] getKeywords() {
        return subjectKeywords;
    }
}