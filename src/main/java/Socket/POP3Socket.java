package Socket;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import Config.Config;

public class POP3Socket extends MailSocket {
    // STAT: Get number of messages and total size
    // LIST: Get size of message
    // RETR: Get message
    // DELE: Delete message
    // NOOP: No operation
    // RSET: Reset
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
    //     super("localhost", 2225);
    //     // TODO:
    //     username = Config.get("username");
    //     password = Config.get("password");
    // }

    @Override
    protected boolean isConnected() throws IOException {
        String response = getResponse();
        return response.startsWith(OK);
    }
    
    @Override
    public String getResponse() throws IOException{
        ArrayList<String> lines = new ArrayList<String>();
        while (true) {
            String line = fromServer.readLine();

            if (line == null) {
                throw new IOException("Server closed the connection");
            }

            if (line.equals('.')) {
                break;
            }

            if (line.length() > 0 && line.charAt(0) == '.') {
                line = line.substring(1);
            }

            lines.add(line);
        }

        String[] response = new String[lines.size()];
        lines.toArray(response);
        return response.toString();
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

    public void login() throws IOException {
        doCommand("USER" + username, OK);
        doCommand("PASS" + password, OK);
    }

    public int getMessageCount() throws IOException{
        doCommand("STAT", OK);
        String response = getResponse();

        int messageCount = Integer.parseInt(response.split(" ")[1]);
        
        return messageCount;
    }

    public String[] getHeaders() throws IOException {
        doCommand("LIST", OK);
        return getResponse().split(CRLF);
    }

    public String getHeader(String messageOrder) throws IOException {
        doCommand("LIST " + messageOrder, OK);
        return getResponse();
    }

    public String getMessage(String messageOrder) throws IOException{
        doCommand("RETR " + messageOrder, OK);
        String[] messageLines = getResponse().split(CRLF);
        StringBuffer message = new StringBuffer();
        for (int i = 0; i < messageLines.length; i++) {
            message.append(messageLines[i]);
            message.append(CRLF);
        }

        return new String(message);
    }

    public void deleteMessage(String messageOrder) throws IOException{
        doCommand("DEL" + messageOrder, OK);
    } 

    public String[] getMessageHead(String messageOrder) throws IOException{
        doCommand("TOP " + messageOrder + " 1", OK);
        return getResponse().split(CRLF);
    }

    public String getMessageID(String messageOrder) throws IOException{
        doCommand("UIDL " + messageOrder, OK);
        String response = getResponse();

        return response;
    }


    public void quit() throws IOException{
        doCommand("QUIT", OK);
    }
}
