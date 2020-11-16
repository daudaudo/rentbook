package app.FXMLController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.util.HashMap;
import core.Facades.FormManager;
import core.Facades.Request;
import core.Facades.Router;
import core.Supports.Validator;

public class Address {

    @FXML
    private TextField nameField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField addressField;

    @FXML
    void addAddress(ActionEvent event) {
        String err = "";
        if(!Validator.isEmail(emailField.getText())){
            err+="Sai định dạng email \n";
        }
        if(!Validator.isNumeric(phoneField.getText())){
            err+="Sai định dạng số điện thoại \n";
        }
        if(nameField.getText().length()==0||phoneField.getText().length()==0||emailField.getText().length()==0||addressField.getText().length()==0){
            err+="Không được để trống bất kỳ trường nào \n";
        }
        if(err.length()!=0){
            FormManager.belong().showAlert(err);
            return;
        }
        HashMap<String,Object> address = new HashMap<>();

        address.put("username",Request.belong().session().get("user"));
        address.put("name",nameField.getText());
        address.put("phone",phoneField.getText());
        address.put("email",emailField.getText());
        address.put("address",addressField.getText());

        new app.Models.Address().insert(address);
        Router.belong().route("address");
    }

}
