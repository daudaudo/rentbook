package app.FXMLController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import core.Facades.Router;
import core.Facades.Request;

public class Login {
    @FXML
    private TextField user;

    @FXML
    private PasswordField pass;

    @FXML
    private Button loginBtn;

    @FXML
    private Button signinBtn;

    @FXML
    void signin(ActionEvent event) {
        Router.belong().route("signin");
    }

    @FXML
    void login(ActionEvent event) {
        Request.belong().input.put("user", user.getText());
        Request.belong().input.put("pass", pass.getText());

        Router.belong().route("login_post");
    }
}
