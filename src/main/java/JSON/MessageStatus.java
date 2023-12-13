package JSON;

import java.io.FileReader;
import java.io.IOException;
import java.io.File;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.omg.CORBA.ExceptionList;

import Socket.POP3Socket;
import scala.concurrent.impl.FutureConvertersImpl.P;

public class MessageStatus {
    private POP3Socket pop3Socket;
    private JSONParser parser;
    private File file;

    protected String[] messagesID = null;
    protected JSONArray messageList = null;

    public MessageStatus(String server, int port, String username, String password) throws Exception{
        this.pop3Socket = new POP3Socket(server, port, username, password);
        this.parser = new JSONParser();
        this.file = new File("src/main/java/JSON/MessageStatus.json");
        

        this.pop3Socket.connect();
        this.pop3Socket.login();
        this.messagesID = pop3Socket.getMessagesID();

        if (this.file.exists()){
            this.messageList = (JSONArray) parser.parse(new FileReader("src/main/java/JSON/MessageStatus.json"));
        } else {
            this.messageList = new JSONArray();
        }
    }

    protected boolean exist(int messageOrder) throws IOException {
        String messageID = messagesID[messageOrder];
        boolean isExist = (messageList != null) ? messageList.contains(messageID) : false;
        return isExist;
    }

    protected void quit() throws IOException {  
        pop3Socket.quit();
    }
}
