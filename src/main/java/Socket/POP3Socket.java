package Socket;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Path;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import Filter.Mailbox;
import JSON.ReadMessageStatus;
import JSON.WriteMessageStatus;

public class POP3Socket extends MailSocket {
    private final String OK = "+OK";
    private final String ERR = "-ERR";
    private final String DEFAULT_WORKING_DIRECTORY = "./";
    private final String SPCE = " ";

    private String[] messagesID = null;
    private ReadMessageStatus readMessageStatus = new ReadMessageStatus();
    private String username;
    private String password;
    private JSONParser parser = new JSONParser();
    private JSONObject messageList = null;
    private List<Mailbox> mailboxes;

    public POP3Socket(String server, int port, String username, String password) {
        super(server, port);
        this.username = username;
        this.password = password;
        try {
            String MessageStatusJSONDirectory = DEFAULT_WORKING_DIRECTORY + "MessageStatus.json";
            File file = new File(MessageStatusJSONDirectory);
            if (file.exists()) {
                messageList = (JSONObject) parser.parse(new FileReader(MessageStatusJSONDirectory));
            } else
                messageList = new JSONObject();
            connect();
            login();
        } catch (Exception e) {
            System.err.println("[POP3] Error: " + e.getMessage());
            // e.printStackTrace();
        }
    }

    public void addMailboxes(List<Mailbox> mailboxes) {
        this.mailboxes = mailboxes;
    }

    @Override
    public String getResponse() throws IOException {
        String response = fromServer.readLine();
        return response;
    }

    @Override
    protected boolean isConnected() throws IOException {
        String response = getResponse();
        return response.startsWith(OK);
    }

    public String getMultipleLinesResponse() throws IOException {
        ArrayList<String> lines = new ArrayList<String>();
        String line = fromServer.readLine();

        while (!line.equals(".")) {
            // System.out.println(line);
            if (line == null)
                throw new IOException("Server closed the connection");

            // if (line.length() > 0 && (line.startsWith(OK + SPCE) || line.startsWith(OK + CRLF)) ) {
            //     line = line.substring(1);
            // }

            lines.add(line);

            line = fromServer.readLine();
        }

        return String.join(CRLF, lines);
    }

    @Override
    public boolean validateResponse(String response) throws IOException {
        return response.startsWith(OK);
    }

    @Override
    protected boolean isMultiLineResponse(String responseLine) {
        return true;
    }

    @Override
    public String doCommand(String command, String expectedReturnCode) throws IOException {
        sendCommand(command);
        String response = getResponse();
        if (!response.startsWith(ERR) && (command.startsWith("LIST") || command.startsWith("RETR"))) {
            response = getMultipleLinesResponse();
        }

        if (response.startsWith(ERR)) {
            throw new IOException("Unexpected return code: " + response);
        }
        return response;
    }

    public void login() throws IOException {
        doCommand("USER " + username, OK);
        doCommand("PASS " + password, OK);
    }

    public int getMessageCount() throws IOException {
        sendCommand("STAT");
        String response = getResponse();

        int messageCount = Integer.parseInt(response.split(" ")[1]);

        return messageCount;
    }

    public String getHeader(String messageOrder) throws IOException {
        sendCommand("LIST " + messageOrder);
        return getResponse();
    }

    public String[] getHeaders() throws IOException {
        sendCommand("LIST");
        return getMultipleLinesResponse().split(CRLF);
    }

    public void UIDL() throws IOException {
        doCommand("UIDL", OK);
        String response = getMultipleLinesResponse();
        if (response == null || response.length() == 0) {
            this.messagesID = new String[0];
            return;
        }

        String[] rawID = response.split(CRLF);
        this.messagesID = new String[rawID.length];
        for (int i = 0; i < rawID.length; i++) {
            if (rawID[i].length() > 2) {
                int beginIndex = rawID[i].indexOf(" ") + 1;
                int endIndex = rawID[i].length() - 4;
                this.messagesID[i] = rawID[i].substring(beginIndex, endIndex);
            }
        }
    }

    public String getMessageID(int messageOrder) throws IOException {
        return messagesID[messageOrder]; // UIDL + number
    }

    // private boolean exist(JSONObject messageObject) {
    // return (messageList == null) ? false : messageList.contains(messageObject);
    // }

    public String RETR(String messageOrder) throws IOException {
        String keyObject = messagesID[Integer.parseInt(messageOrder) - 1];
        String message = "retrieved";
        if (!readMessageStatus.exist(messageList, keyObject))
            message = doCommand("RETR " + messageOrder, OK);

        return message;
    }

    public void retrieveMessage() throws IOException {
        UIDL();
        for (int i = 0; i < messagesID.length; i++) {
            if (readMessageStatus.exist(messageList, messagesID[i])) {
                continue;
            }
            String rawMessage = RETR(i + 1 + "");
            messageList.put(messagesID[i], false);
            String emailDirectory = DEFAULT_WORKING_DIRECTORY + "Inbox/" + messagesID[i] + ".msg";
            saveEmail(emailDirectory, rawMessage);
            filterEmail(emailDirectory, mailboxes);
        }
        try {
            WriteMessageStatus writeMessageStatus = new WriteMessageStatus(messageList);
            writeMessageStatus.writeJSON();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // quit();
    }

    private void saveEmail(String filename, String rawMessage) {
        try {
            Path emailPath = Paths.get(filename);
            Files.write(emailPath, rawMessage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterEmail(String emailDirectory, List<Mailbox> mailboxes) {
        if (mailboxes == null) {
            System.out.println("[ERROR] Mailboxes not initialized.");
            return;
        }
        Path emailPath = Paths.get(emailDirectory);
        for (Mailbox mailbox : mailboxes) {
            mailbox.addEmailIfMatches(emailPath);
        }
    }

    public void deleteMessage(String messageOrder) throws IOException {
        doCommand("DELE" + messageOrder, OK);
    }

    public void quit() throws IOException {
        doCommand("QUIT", OK);
    }

    public String[] getMessagesID() {
        return messagesID;
    }
}