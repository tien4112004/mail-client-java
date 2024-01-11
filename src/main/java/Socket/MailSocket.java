package Socket;

import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public abstract class MailSocket {
    protected final String CRLF = "\r\n";
    protected final int RESPONSE_CODE_LENGTH = 3;

    protected static Socket serverSocket = null;
    protected static DataOutputStream toServer = null;
    protected static BufferedReader fromServer = null;

    protected String server;
    protected int port;

    public MailSocket(String server, int port) {
        this.server = server;
        this.port = port;
    }

    public void connect() throws IOException {
        serverSocket = new Socket(server, port);
        toServer = new DataOutputStream(serverSocket.getOutputStream());
        fromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
        if (!isConnected())     
            throw new IOException("Unable to connect to server " + server + ":" + port);
    }

    protected abstract boolean isConnected() throws IOException;

    protected abstract String doCommand(String command, String expectedReturnCode) throws IOException;

    public void sendCommand(String command) throws IOException {
        toServer.writeBytes(command + CRLF);
        toServer.flush();
    }

    public abstract boolean validateResponse(String response) throws IOException;

    public String getResponse() throws IOException {
        String response = "";
        String responseLine = "";
        do {
            responseLine = fromServer.readLine();
            response += responseLine + CRLF;
        } while (isMultiLineResponse(responseLine));
        return response;
    }

    protected abstract boolean isMultiLineResponse(String responseLine);

    protected String parseReplyCode(String reply) {
        StringTokenizer tokenizer = new StringTokenizer(reply);
        return tokenizer.nextToken().trim();
    }

    public String getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public void closeConnection() {
        try {
            sendCommand("QUIT");
            fromServer.close();
            toServer.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
