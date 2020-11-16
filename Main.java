
import core.Container.Application;

public class Main {

    public static void main(String... args) {
        Application app = new Application(System.getProperty("user.dir"));
        app.close();
    }
}