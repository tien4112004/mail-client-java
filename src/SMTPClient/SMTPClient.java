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

    public SMTPClient(String SMTPServer, int port) {
        try {
            connect(SMTPServer, port);
            sendCommand("HELO " + SMTPServer, OK);
        } catch (Exception e) {
            System.out.println("[ERROR][SMTPClient]" + e.getMessage());
        }
    }

    public void sendEmail(Envelope envelope) throws IOException {
        // StringBuilder recipients = new StringBuilder();
        // for (String recipient : envelope.recipients) {
        // recipients.append(recipient + ",");
        // }
        // envelope.recipients = recipients.toString();

        sendCommand("MAIL FROM: " + envelope.sender, OK);
        // TODO: multiple recipients
        sendCommand("RCPT TO: " + envelope.recipients, OK);
        sendCommand("DATA", DATA);
        sendCommand(envelope.message.toString() + CRLF + ".", OK);
    }

    protected void sendCommand(String command, int expectedReturnCode) throws IOException {
        System.out.println("[CLIENT] " + command);
        toServer.writeBytes(command + CRLF);
        toServer.flush();

        String response = fromServer.readLine();
        System.out.println("[SERVER] " + response);
        int responseCode = parseReplyCode(response);
        if (responseCode != expectedReturnCode) {
            System.out.println("[ERROR][sendCommand] Unexpected return code: " + response);
            throw new IOException("Unexpected return code");
        }
    }

    protected void connect(String SMTPServer, int port) throws IOException {
        serverSocket = new Socket(SMTPServer, port);
        toServer = new DataOutputStream(serverSocket.getOutputStream());
        fromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

        System.out.println("Connected to server " + SMTPServer + ":" + port);
        String response = fromServer.readLine();
        System.out.println("[DEBUG][SERVER] " + response);

        if (parseReplyCode(response) != CONNECTED) {
            System.out.println("[ERROR][SMTPClient] Unexpected return code: " + response);
            throw new IOException("Unexpected return code");
        }
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
