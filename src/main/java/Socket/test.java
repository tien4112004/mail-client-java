package Socket;

public class test {
    public static void main(String[] args) {
        String response = "9 20231117085813249.msg";
        response = response.substring(2, 18);

        int messageID = Integer.parseInt(response.split(" ")[1]);
        System.out.println(messageID);

    }
}