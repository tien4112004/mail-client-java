import UI.UI;

public class Main {
    public static void main(String[] args) {
        UI ui = new UI();
        try {
            ui.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}