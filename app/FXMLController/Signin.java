package app.FXMLController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import core.Supports.Validator;
import app.Models.Users;
import core.Facades.FormManager;
import core.Facades.Request;
import core.Facades.Router;

public class Signin {

    @FXML
    private TextField user;

    @FXML
    private PasswordField pass;

    @FXML
    private Button signinBtn;

    @FXML
    private TextField email;

    @FXML
    void signin(ActionEvent event) {
        String error = "";
        if(!Validator.isUsername(user.getText()) || !Validator.isPassword(pass.getText())) error += "username và password cần có số ký tự lớn hơn 6 \n";

        if(!Validator.isEmail(email.getText())) error+="Email sai định dạng \n";

        if(new Users(user.getText()).get() != null) error+="Username đã tồn tại";

        if(!error.equals("")){
            FormManager.belong().showAlert(error);
            return;
        }
        Request.belong().input.put("user", user.getText());
        Request.belong().input.put("pass", pass.getText());
        Request.belong().input.put("email", email.getText());

        Router.belong().route("signin_post");
    }

}
