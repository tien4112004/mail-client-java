package Filter;

import Message.Message;
import Message.MessageParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Mailbox {
    String name;
    Path directory;
    Filter[] filters;

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

    public void addEmailIfMatches(Path emailPath, Filter filter) {
        String rawMessage = null;
        try {
            rawMessage = new String(Files.readAllBytes(emailPath));
        } catch (IOException e) {
            System.out.println("[LOG][Mailbox] Error reading email file.");
            e.printStackTrace();
        }

        MessageParser parser = new MessageParser();
        parser.fullParse(rawMessage);
        Message email = parser.createMessage();

        if (filter.matches(email)) {
            copyEmailToDirectory(emailPath);
        }
    }

    private void copyEmailToDirectory(Path emailPath) {
        Path target = this.directory.resolve(emailPath.getFileName());

        if (Files.exists(target)) {
            System.out.println("[LOG][Mailbox] Email already exists in mailbox.");
            return;
        }

        try {
            Files.copy(emailPath, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("[LOG][Mailbox] Error in copying email file.");
            e.printStackTrace();
        }
    }

    private void createDirectory(Path directory) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                System.out.println("[LOG][createDirectory] Error in creating directory.");
                e.printStackTrace();
            }
        }
    }

    public static void moveMailToFolder(String from, String to) {
        File fromFile = new File(from);
        if (!fromFile.exists()) {
            System.out.println("[LOG][moveMailToFolder] File does not exist.");
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

    public Mailbox search(String name) {
        if (this.name.equals(name)) {
            return this;
        }
        return null;
    }
}