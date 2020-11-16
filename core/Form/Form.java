package core.Form;

import core.Closure.Boot;
import javafx.application.Application;
import javafx.stage.Stage;

public class Form extends Application {
    public static Boot boot;

    @Override
    public void start(Stage primaryStage) throws Exception {
        boot.boot();
    }
    
}
