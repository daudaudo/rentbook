package core.Form;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import core.Container.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class LoaderFXML {
    private Application app;

    public LoaderFXML(Application app) {
        this.app = app;
    }

    public URL getURLFXML(String path) {
        path = path.replaceAll("\\.", "/");
        path = this.app.getPath("resource.fxml") + "/" + path + ".fxml";

        try {
            return new URL("file", null, path);
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public Parent getComponents(String path) {
        FXMLLoader loader = new FXMLLoader(this.getURLFXML(path));
        
        try {
            return loader.load();
        } catch (IOException e) {
            return null;
        }
    }

    public Parent getLayout(){
        FXMLLoader loader = new FXMLLoader(core.Form.LayoutController.class.getResource("layout.fxml"));;

        try {
            return loader.load();
        } catch (IOException e) {
            return null;
        }
    }
}
