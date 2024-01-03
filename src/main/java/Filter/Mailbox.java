package Filter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import Email.Email;
import Email.EmailParser;

public class Mailbox {
    String name;
    Path directory;
    Filter[] filters;

    public Mailbox(String name, String directory, String[] senderKeywords, String[] subjectKeywords,
            String[] contentKeywords) {
        this(name, directory);
        SenderFilter senderFilter = null;
        SubjectFilter subjectFilter = null;
        ContentFilter contentFilter = null;
        int notNull = 0;

        if (senderKeywords != null && senderKeywords.length != 0) {
            senderFilter = new SenderFilter(senderKeywords);
            notNull++;
        }
        if (subjectKeywords != null && subjectKeywords.length != 0) {
            subjectFilter = new SubjectFilter(subjectKeywords);
            notNull++;
        }
        if (contentKeywords != null && contentKeywords.length != 0) {
            contentFilter = new ContentFilter(contentKeywords);
            notNull++;
        }

        filters = new Filter[notNull];
        if (senderFilter != null)
            filters[--notNull] = senderFilter;
        if (subjectFilter != null)
            filters[--notNull] = subjectFilter;
        if (contentFilter != null)
            filters[--notNull] = contentFilter;
    }

    public Mailbox(String name, String directory) {
        this.name = name;
        this.directory = Paths.get(directory);

        createDirectory(this.directory);
    }

    public Mailbox(String name) {
        this(name, "./" + name + "/");
    }

    public Mailbox() {
        this("mailbox");
    }

    public void addEmailIfMatches(Path emailPath) {
        String rawMessage = null;
        try {
            rawMessage = new String(Files.readAllBytes(emailPath));
        } catch (IOException e) {
            System.out.println("[ERROR] Error reading email file.");
            e.printStackTrace();
        }

        EmailParser parser = new EmailParser();
        parser.parseHeaderAndContent(rawMessage);
        Email email = parser.createMessage();

        if (filters == null)
            return;

        for (Filter filter : filters) {
            if (filter.matches(email)) {
                copyEmailToDirectory(emailPath);
            }
        }
    }

    private void copyEmailToDirectory(Path emailPath) {
        Path target = this.directory.resolve(emailPath.getFileName());

        if (Files.exists(target)) {
            System.out.println("[ERROR] Email already exists in mailbox.");
            return;
        }

        try {
            Files.copy(emailPath, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("[ERROR] Error in copying email file.");
            e.printStackTrace();
        }
    }

    private void createDirectory(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                System.out.println("[ERROR] Error in creating directory.");
                e.printStackTrace();
            }
        }
    }

    public static void moveMailToFolder(String from, String to) {
        File fromFile = new File(from);
        if (!fromFile.exists()) {
            System.out.println("[ERROR] File does not exist.");
            return;
        }
        File toFile = new File(to);
        if (!toFile.exists()) {
            toFile.mkdirs();
        }
        fromFile.renameTo(new File(to + "/" + fromFile.getName()));
        fromFile.delete();
    }

    public static void moveMailToMailbox(String from, Mailbox to) {
        moveMailToFolder(from, to.directory.toString());
    }

    public static void moveMail(Mailbox from, Mailbox to) {
        moveMailToFolder(from.directory.toString(), to.directory.toString());
    }

    public Path getMailboxDirectory() {
        return this.directory;
    }

    public String getMailboxName() {
        return this.name;
    }

    public Filter[] getFilters() {
        return filters;
    }
}