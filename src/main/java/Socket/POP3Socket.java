package Socket;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import Config.Config;
import scala.collection.mutable.StringBuilder;

public class POP3Socket extends MailSocket {
    // STAT: Get number of messages and total size
    // LIST: Get size of message
    // UIDL: Get the unique id of message
    // RETR: Get message
    // DELE: Delete message
    // QUIT: Close connection
    private final String OK = "+OK";
    private final String ERR = "-ERR";
    private final String username;
    private final String password;

    public POP3Socket(String server, int port, String username, String password) {
        super(server, port);
        this.username = username;
        this.password = password;
    }

    // public POP3Socket() {
    // super("localhost", 2225);
    // // TODO:
    // username = Config.get("username");
    // password = Config.get("password");
    // }

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
        while (true) {
            String line = fromServer.readLine();

            if (line == null) {
                throw new IOException("Server closed the connection");
            }

            if (line.equals(".")) {
                break;
            }

            if (line.length() > 0 && line.charAt(0) == '+') {
                line = line.substring(1);
            }

            lines.add(line);
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
    public void doCommand(String command, String expectedReturnCode) throws IOException {
        sendCommand(command);
        String response = getResponse();
        if (response.startsWith(ERR)) {
            System.out.println("[ERROR][POP3Socket] Unexpected return code: " + response);
            throw new IOException("Unexpected return code: " + response);
        }
    }

    // Single Line Response
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

    // Multiple Lines Response
    public String[] getMessagesID() throws IOException {
        sendCommand("UIDL");
        return getMultipleLinesResponse().split(CRLF);
    }

    public String[] getHeaders() throws IOException {
        sendCommand("LIST");
        return getMultipleLinesResponse().split(CRLF);
    }

    public String getMessage(String messageOrder) throws IOException {
        sendCommand("RETR " + messageOrder);
        String[] messageLines = getMultipleLinesResponse().split(CRLF);
        StringBuffer message = new StringBuffer();
        for (int i = 0; i < messageLines.length; i++) {
            message.append(messageLines[i]);
            message.append(CRLF);
        }

        return new String(message);
    }

    public void deleteMessage(String messageOrder) throws IOException {
        sendCommand("DEL" + messageOrder);
    }

    public String[] getMessageHead(String messageOrder) throws IOException {
        sendCommand("TOP " + messageOrder + " 1");
        return getMultipleLinesResponse().split(CRLF);
    }

    public void quit() throws IOException {
        doCommand("QUIT", OK);
    }
}
