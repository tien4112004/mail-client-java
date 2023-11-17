package Envelope;

import java.net.*;
import java.util.*;

import message.Message;

public class envelope {
    public String sender; // sender's address
    public String recipient; // array of recipients

    public String destHost;
    public InetAddress destIP;

    public Message message;

    public envelope() {
        sender = "";
        recipient = "";
        destHost = "";
        destIP = null;
        message = null;
    }

    public envelope(Message message, String localServer) throws UnknownHostException{
        sender = message.getSender();
        recipient = message.getRecipients();

        message = escapeMessge(message);

        destHost = localServer;
        try {
            destIP = InetAddress.getByName(destHost);
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: " + destHost);
            System.out.println(e);
            throw e;
        }
        return;
    }

    private Message escapeMessge(Message message) {
        String escapedBody = "";
        String token;
        StringTokenizer parser = new StringTokenizer(message.body, "\r\n", true);

        while (parser.hasMoreTokens()) {
            token = parser.nextToken();
            if (token.startsWith(token, '.')) {
                token = "." + token;
            }
            escapedBody += token;
        }
        message.body = escapedBody;
        return message;
    }

    public String toString() {
        String res = "";
        res += "Sender: " + sender + "\n";
        res += "Recipient: " + recipient + "\n";
        res += "MX-hpst: " + destHost + ", address: " + destIP + "\n";
        res += message.toString();
        return res;
    }
}
