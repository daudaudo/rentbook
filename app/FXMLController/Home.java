package app.FXMLController;

import core.Facades.Request;
import core.Facades.Router;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Home {

    @FXML
    private TextField qSearch;

    @FXML
    void search(MouseEvent event) {
        if(Request.belong().input("search")==null)
            Request.belong().input.put("search", qSearch.getText());
        else {
            Request.belong().input.remove("search");
            Request.belong().input.put("search", qSearch.getText());
        }
        
        Router.belong().route("search");
    }

}
