package app.FXMLController;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import java.util.ArrayList;
import java.util.HashMap;
import core.Facades.FormManager;
import core.Facades.Request;
import core.Facades.Router;
import core.Supports.SQL;
import core.Supports.Validator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.scene.control.Button;

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
    private TableView<app.Models.Address> tableAddress;

    @FXML
    private Text userText;

    @FXML
    public void initialize() {
        renderTitle();
        renderAddressTable();
    }

    public void renderTitle() {
        userText.setText(Request.belong().session().get("user"));
    }

    @SuppressWarnings("unchecked")
    public void renderAddressTable() {

        TableColumn<app.Models.Address, String> nameColumn = new TableColumn<>("Name");
        TableColumn<app.Models.Address, String> phoneColumn = new TableColumn<>("Phone");
        TableColumn<app.Models.Address, String> emailColumn = new TableColumn<>("Email");
        TableColumn<app.Models.Address, String> addressColumn = new TableColumn<>("Address");

        nameColumn.setCellValueFactory(arg -> {
            return new SimpleStringProperty(String.valueOf(arg.getValue().get("name")));
        });

        phoneColumn.setCellValueFactory(arg -> {
            return new SimpleStringProperty(String.valueOf(arg.getValue().get("phone")));
        });

        emailColumn.setCellValueFactory(arg -> {
            return new SimpleStringProperty(String.valueOf(arg.getValue().get("email")));
        });

        addressColumn.setCellValueFactory(arg -> {
            return new SimpleStringProperty(String.valueOf(arg.getValue().get("address")));
        });

        tableAddress.getColumns().addAll(nameColumn, phoneColumn, emailColumn, addressColumn);

        ArrayList<app.Models.Address> addresses = new app.Models.Address(new String[]{
            SQL.where("username", "=", Request.belong().session().get("user"))
        }).getModels();

        tableAddress.getItems().addAll(addresses);

        TableColumn<app.Models.Address, Boolean> buttonColumn = new TableColumn<>("Button");
        buttonColumn.setCellFactory((tableColumn) -> {
            TableCell<app.Models.Address, Boolean> cell = new TableCell<app.Models.Address, Boolean>() {
                private Button deleteBtn = new Button("Xoá");
                {
                    deleteBtn.setOnAction((ActionEvent event) -> {
                        new app.Models.Address(String.valueOf(getTableView().getItems().remove(getIndex()).get("id"))).delete();
                    });

                }

                @Override
                public void updateItem(Boolean item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteBtn);
                    }
                }
            };
            return cell;
        });

        tableAddress.getColumns().add(buttonColumn);
    }

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

        new app.Models.Address(address).insert();
        Router.belong().route("address");
    }
}
