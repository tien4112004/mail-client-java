package Filter;

import Message.Message;

public interface Filter {
    public abstract boolean matches(Message email);
}

class SenderFilter implements Filter {
    private String senderKeywords[];

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
}

class SubjectFilter implements Filter {
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
}

class ContentFilter implements Filter {
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
}