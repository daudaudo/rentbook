package app.FXMLController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import core.Facades.Request;
import core.Facades.Router;

public class UserInfo {
    @FXML
    private Text title;

    @FXML
    public void initialize(){
        renderTitle();
    }

    private void renderTitle(){
        title.setText("Xin chào " + Request.belong().session().get("user"));
    }

    @FXML
    void logout(ActionEvent event) {
        Router.belong().route("logout");
    }

    @FXML
    void toAddress(ActionEvent event) {
        Router.belong().route("address");
    }

}
