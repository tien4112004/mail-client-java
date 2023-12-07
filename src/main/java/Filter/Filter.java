package Filter;

import Envelope.*;

public interface Filter {
    boolean matches(Envelope envelope);
}

class SenderFilter implements Filter {
    private String sender;

    public SenderFilter(String sender) {
        this.sender = sender.toLowerCase();
    }

    public boolean matches(Envelope envelope) {
        String emailSender = envelope.sender.toLowerCase();
        System.out.println(emailSender);
        System.out.println(sender);
        return emailSender.equals(sender);
    }
}

class SubjectFilter implements Filter {
    private String subject;

    public SubjectFilter(String subject) {
        this.subject = subject.toLowerCase();
    }

    public boolean matches(Envelope envelope) {
        String emailSubject = envelope.subject.toLowerCase();
        System.out.println(emailSubject);
        System.out.println(subject);
        return emailSubject.contains(subject);
    }
}

class ContentFilter implements Filter {
    private String content;

    public ContentFilter(String content) {
        this.content = content.toLowerCase();
    }

    public boolean matches(Envelope envelope) {
        String emailContent = envelope.message.getContent().toLowerCase();
        System.out.println(emailContent);
        System.out.println(content);
        if (emailContent == null) {
            return false;
        }
        return emailContent.contains(content);
    }
}
