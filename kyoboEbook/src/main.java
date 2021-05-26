import java.io.IOException;

public class main {
    public static void main(String[] args) throws IOException {

        AppController app = new AppController();
        AppController app1 = new AppController(1100, 500);

        app.run();
    }
}
