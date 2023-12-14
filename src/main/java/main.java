import UI.UI;

public class main {
    public static void main(String[] args) {
        UI ui = new UI();
        try {
            ui.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}