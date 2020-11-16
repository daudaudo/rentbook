package app.FXMLController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import core.Facades.Router;

public class UserInfo {
    @FXML
    private Text title;

    @FXML
    void logout(ActionEvent event) {
        Router.belong().route("logout");
    }

    @FXML
    void toAddress(ActionEvent event) {
        Router.belong().route("address");
    }

}
