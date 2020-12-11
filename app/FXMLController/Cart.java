package app.FXMLController;

import java.util.ArrayList;

import core.Facades.FormManager;
import core.Facades.Request;
import core.Facades.Router;
import core.Supports.SQL;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;

public class Cart {

    @FXML
    private TableView<app.Models.Cart> tableCart;

    @FXML
    private Text userText;

    @FXML
    public void initialize(){
        renderTitle();
        renderTableCart();
    }

    @FXML
    void deleteAllCart(MouseEvent event) {
        new app.Models.Cart(new String[]{
            SQL.where("username", "=", Request.belong().session().get("user") )
        }).<app.Models.Cart>getModels().forEach(cart -> {
            cart.delete();
        });
        Router.belong().route("cart");
    }

    public void renderTitle(){
        userText.setText(Request.belong().session().get("user"));
    }

    @SuppressWarnings("unchecked")
    public void renderTableCart(){
        TableColumn<app.Models.Cart,String> nameColumn = new TableColumn<>("Name");
        TableColumn<app.Models.Cart,String> numberColumn = new TableColumn<>("Number");

        nameColumn.setCellValueFactory((arg)->{
            return new SimpleStringProperty(
                new app.Models.Books(String.valueOf(arg.getValue().get("book_id"))).toString()
            );
        });
        numberColumn.setCellValueFactory(arg ->{
            return new SimpleStringProperty(
                String.valueOf(arg.getValue().get("number"))
            );
        });

        tableCart.getColumns().addAll(nameColumn,numberColumn);

        ArrayList<app.Models.Cart> cartbook = new app.Models.Cart(new String[]{
            SQL.where("username", "=", Request.belong().session().get("user")),
        }).getModels();

        tableCart.getItems().addAll(cartbook);

        TableColumn<app.Models.Cart, Boolean> buttonColumn = new TableColumn<>("Button");
        buttonColumn.setCellFactory((tableColumn) -> {
            TableCell<app.Models.Cart, Boolean> cell = new TableCell<app.Models.Cart, Boolean>() {
                private Button deleteBtn = new Button("Xoá khỏi giỏ hàng");
                private Button viewBtn = new Button("Xem sách");
                private Button addBtn = new Button("Cộng");
                private Button minusBtn = new Button("Trừ");
                private VBox vbox = new VBox();

                {
                    vbox.getChildren().addAll(deleteBtn, viewBtn, addBtn,minusBtn);

                    deleteBtn.setOnAction((ActionEvent event) -> {
                        new app.Models.Cart(
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

                    addBtn.setOnAction((ActionEvent event) -> {
                        int max = (int) new app.Models.Books(
                            String.valueOf(getTableView().getItems().get(getIndex()).get("book_id"))
                        ).get("number");

                        int number = (int) getTableView().getItems().get(getIndex()).get("number");

                        if(number+1>max) FormManager.belong().showAlert("Không đủ tồn kho");
                        else getTableView().getItems().get(getIndex()).set("number",number+1).update();
                        
                        getTableView().refresh();
                    });

                    minusBtn.setOnAction((ActionEvent event) -> {
                        int number = (int) getTableView().getItems().get(getIndex()).get("number");

                        if(number-1>0) getTableView().getItems().get(getIndex()).set("number",number-1).update();
                        else getTableView().getItems().remove(getIndex()).delete();
                        
                        getTableView().refresh();
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

        tableCart.getColumns().add(buttonColumn);
    }
}