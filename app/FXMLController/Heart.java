package app.FXMLController;

import core.Facades.Request;
import core.Facades.Router;
import core.Supports.SQL;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.HashMap;
import core.Facades.FormManager;

public class Heart {

    @FXML
    private TableView<app.Models.Heart> tableHeart;

    @FXML
    private Text userText;

    public void initialize(){
        renderTitle();
        renderTableHeart();
    }

    public void renderTitle(){
        userText.setText(Request.belong().session().get("user"));
    }

    @SuppressWarnings("unchecked")
    public void renderTableHeart() {
        
        TableColumn<app.Models.Heart,String> nameColumn = new TableColumn<>("Name");
        TableColumn<app.Models.Heart,String> authorsColumn = new TableColumn<>("Authors");

        nameColumn.setCellValueFactory((arg)->{
            return new SimpleStringProperty(
                String.valueOf(new app.Models.Books( String.valueOf(arg.getValue().get("book_id")) ).get("name"))
            );
        });
        authorsColumn.setCellValueFactory((arg)->{
            return new SimpleStringProperty(
                String.valueOf(new app.Models.Books( String.valueOf(arg.getValue().get("book_id")) ).get("name"))
            );
        });

        tableHeart.getColumns().addAll(nameColumn,authorsColumn);

        tableHeart.getItems().addAll(
            new app.Models.Heart(new String[]{
                SQL.where("username", "=", Request.belong().session().get("user"))
            }).<app.Models.Heart>getModels()
        );


        TableColumn<app.Models.Heart, Boolean> buttonColumn = new TableColumn<>("Button");
        buttonColumn.setCellFactory((tableColumn) -> {
            TableCell<app.Models.Heart, Boolean> cell = new TableCell<app.Models.Heart, Boolean>() {
                private Button deleteBtn = new Button("Xoá khỏi mục yêu thích");
                private Button viewBtn = new Button("Xem sách");
                private Button addCardBtn = new Button("Thêm vào giỏ hàng");
                private VBox vbox = new VBox();

                {
                    vbox.getChildren().addAll(deleteBtn, viewBtn, addCardBtn);

                    deleteBtn.setOnAction((ActionEvent event) -> {
                        new app.Models.Heart(
                            String.valueOf(getTableView().getItems().remove(getIndex()).get("id"))
                        ).delete();
                    });

                    viewBtn.setOnAction((ActionEvent event) -> {
                        String book_id = String.valueOf(getTableView().getItems().get(getIndex()).get("book_id"));
                        if(Request.belong().input("book_id")!=null)
                            Request.belong().input.remove("book_id");
                        Request.belong().input.put("book_id", book_id);
                        Router.belong().route("book");
                    });

                    addCardBtn.setOnAction((ActionEvent event) -> {
                        if(Request.belong().session().get("user")==null){
                            FormManager.belong().showAlert("Vui lòng đăng nhập trước đã bạn nhé");
                        }
                        else{
                            HashMap<String,Object> newcart = new HashMap<>();
                            newcart.put("username", Request.belong().session().get("user"));
                            newcart.put("book_id", getTableView().getItems().get(getIndex()).get("book_id"));
                            
                            ArrayList<app.Models.Cart> check =  new app.Models.Cart(new String[]{
                                SQL.where("username", "=", Request.belong().session().get("user")),
                                SQL.where("book_id","=",getTableView().getItems().get(getIndex()).get("book_id"))
                            }).getModels();

                            if(check.size()==0) new app.Models.Cart(newcart).insert();
                            else FormManager.belong().showAlert("Bạn đã thêm vào giỏ hàng trước đó rồi");
                        }
                    });
                }

                @Override
                public void updateItem(Boolean item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(vbox);
                    }
                }
            };
            return cell;
        });

        tableHeart.getColumns().add(buttonColumn);
    }
}