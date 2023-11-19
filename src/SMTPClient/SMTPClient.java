package SMTPClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.security.cert.CRL;
import java.util.StringTokenizer;
import Envelope.*;
import Message.Message;

public class SMTPClient {
    private final String CRLF = "\r\n";
    private final int CONNECTED = 220;
    private final int OK = 250;
    private final int DATA = 354;

    private static Socket serverSocket = null;
    private static DataOutputStream toServer = null;
    private static BufferedReader fromServer = null;

    private String SMTPServer;
    private int port;

    public SMTPClient(String SMTPServer, int port) {
        this.SMTPServer = SMTPServer.trim();
        this.port = port;
    }

    public SMTPClient() {
        this.SMTPServer = "localhost";
        this.port = 2225;
    }

    public void sendEmail(Envelope envelope) throws IOException {
        // StringBuilder recipients = new StringBuilder();
        // for (String recipient : envelope.recipients) {
        // recipients.append(recipient + ",");
        // }
        // envelope.recipients = recipients.toString();

        connect();
        sendCommand("MAIL FROM: " + envelope.sender, OK);
        // TODO: multiple recipients
        sendCommand("RCPT TO: " + envelope.recipients, OK);
        sendCommand("DATA", DATA);
        sendCommand(envelope.message.toString() + CRLF + ".", OK);
        closeConnection();
    }

    protected void connect() throws IOException {
        serverSocket = new Socket(SMTPServer, port);
        toServer = new DataOutputStream(serverSocket.getOutputStream());
        fromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

        System.out.println("Connected to server " + SMTPServer + ":" + port);
        String response = getResponse();

        if (parseReplyCode(response) != CONNECTED) {
            System.out.println("[ERROR][SMTPClient] Unexpected return code: " + response);
            throw new IOException("Unexpected return code");
        }
    }

    protected void sendCommand(String command, int expectedReturnCode) throws IOException {
        System.out.println("[CLIENT] " + command);
        toServer.writeBytes(command + CRLF);
        toServer.flush();

        String response = getResponse();
        int responseCode = parseReplyCode(response);
        if (responseCode != expectedReturnCode) {
            System.out.println("[ERROR][sendCommand] Unexpected return code: " + response);
            throw new IOException("Unexpected return code");
        }
    }

    /*
     * Handling multiline response from server (e.g. 354-Start mail input; end with
     * <CRLF>.<CRLF>)
     * Each line in response contains a 3-digit return code followed by a hyphen
     * and a text string, followed by CRLF.
     * If there is a "-" immediately after the 3-digit return code, then the server
     * is indicating that the response is not complete and that another line of
     * text will follow.
     */
    protected String getResponse() throws IOException {
        String response = fromServer.readLine();
        String line = "";
        do {
            line = fromServer.readLine();
            if (line == null || line.length() < 3) {
                throw new IOException("SMTPClient: getResponse: bad server response");
            }
            response += line + CRLF;
        } while ((line.length() > 3) && (line.charAt(3) == '-'));
        System.out.println("[SERVER] " + response);
        return response;
    }

    int parseReplyCode(String reply) {
        StringTokenizer tokenizer = new StringTokenizer(reply);
        int returnCode = Integer.parseInt(tokenizer.nextToken());
        return returnCode;
    }

    public String getSMTPServer() {
        return serverSocket.getInetAddress().getHostName();
    }

    public int getPort() {
        return serverSocket.getPort();
    }

    public void closeConnection() {
        try {
            serverSocket.close();
        } catch (Exception e) {
            System.out.println("[ERROR][closeConnection] " + e.getMessage());
        }
    }

    // for test
    // public static void main(String args[]) {
    // try {
    // SMTPClient client = new SMTPClient("localhost", 2225);
    // Envelope envelope = new Envelope();
    // envelope.sender = "sender@localhost";
    // envelope.recipients = "<your recipient mail>";
    // String subject = "Test #03 - 2023-11-18";
    // String content = "This is a test email";
    // envelope.message = new Message(envelope.sender, "Cc", envelope.recipients,
    // subject,
    // content);
    // System.out.println(envelope.message.toString());
    // client.sendEmail(envelope);
    // client.closeConnection();
    // } catch (Exception e) {
    // System.out.println("[ERROR][main] " + e.getMessage());
    // }
    // }
}
