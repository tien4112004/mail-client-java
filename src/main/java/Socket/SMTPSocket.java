package Socket;

import java.io.IOException;
import Message.Message;

public class SMTPSocket extends MailSocket {
    private final String CONNECTED = "220";
    private final String OK = "250";
    private final String DATA = "354";
    private final int RESPONSE_CODE_LENGTH = 3;

    public SMTPSocket(String server, int port) {
        super(server, port);
    }

    public SMTPSocket() {
        super("localhost", 2225);
    }

    @Override
    protected boolean isConnected() throws IOException {
        String response = getResponse();
        return parseReplyCode(response).equals(CONNECTED);
    }

    @Override
    public void doCommand(String command, String expectedReturnCode) throws IOException {
        sendCommand(command);
        String response = getResponse();
        if (!parseReplyCode(response).equals(expectedReturnCode)) {
            System.out.println("[ERROR][SMTPSocket] Unexpected return code: " + parseReplyCode(response));
            throw new IOException("Unexpected return code: " + parseReplyCode(response));
        }
    }

    @Override
    public boolean validateResponse(String response) throws IOException {
        return parseReplyCode(response).equals(OK) || parseReplyCode(response).equals(DATA);
    }

    @Override
    protected boolean isMultiLineResponse(String response) {
        return response.charAt(RESPONSE_CODE_LENGTH) == '-';
    }

    // protected String parseReplyCode(String response) {
    // return response.substring(0, RESPONSE_CODE_LENGTH);
    // }

    public void sendEmail(Message email) throws IOException {
        connect();
        doCommand("HELO " + server, OK);
        doCommand("MAIL FROM: " + email.getSender(), String.valueOf(OK));
        String[] recipients = email.getRecipients();
        for (String recipient : recipients) {
            doCommand("RCPT TO: " + recipient, String.valueOf(OK));
        }
        doCommand("DATA", String.valueOf(DATA));
        doCommand(email.toString() + CRLF + ".", String.valueOf(OK));
        closeConnection();
    }
}
