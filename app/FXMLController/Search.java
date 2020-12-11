package app.FXMLController;

import java.util.ArrayList;
import java.util.HashMap;

import app.Models.Books;
import app.Models.Cart;
import app.Models.Categories;
import app.Models.Heart;
import core.Facades.FormManager;
import core.Facades.Request;
import core.Facades.Router;
import core.Supports.SQL;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class Search {

    @FXML
    private TableView<app.Models.Books> tableBook;

    @FXML
    private Text userText;

    @FXML
    private Text searchText;

    @FXML
    private Text searchRsText;

    @FXML
    public void initialize(){
        renderTitle();
        renderTableBook();
    }

    private void renderTitle(){
        userText.setText(Request.belong().session().get("user"));

        if(Request.belong().getRouterName().equals("category")) {
            searchText.setText("Kết quả tìm kiếm cho : "+new Categories(Request.belong().input("category")).get("name"));
        }
        else{
            searchText.setText("Kết quả tìm kiếm cho : "+Request.belong().input("search"));
        }
    }

    private void renderSearchRs(int number){
        searchRsText.setText("Có "+ number+" kết quả tìm kiếm");
    }

    @SuppressWarnings("unchecked")
    private void renderTableBook() {
        TableColumn<app.Models.Books, String> nameColumn = new TableColumn<>("Name");
        TableColumn<app.Models.Books, String> authorsColumn = new TableColumn<>("Authors");
        TableColumn<app.Models.Books, ImageView> imageColumn = new TableColumn<>("Image");
        TableColumn<app.Models.Books, String> priceColumn = new TableColumn<>("Price");

        nameColumn.setCellValueFactory((arg)->{
            return new SimpleStringProperty(String.valueOf(arg.getValue().get("name")));
        });

        authorsColumn.setCellValueFactory((arg)->{
            return new SimpleStringProperty(String.valueOf(arg.getValue().get("authors")));
        });

        imageColumn.setCellValueFactory((arg)->{
            String image = "file:///" + core.Facades.Facades.app.getPath("resource.img") + "/" + String.valueOf(arg.getValue().get("image"));
            return new SimpleObjectProperty<ImageView>(new ImageView(new Image(image)));
        });

        priceColumn.setCellValueFactory((arg)->{
            return new SimpleStringProperty(String.valueOf(arg.getValue().get("price")));
        });

        tableBook.getColumns().addAll(nameColumn, authorsColumn,imageColumn,priceColumn);
        ArrayList<Books> books;
        if(Request.belong().getRouterName().equals("category")) {
            books = new Books(new String[]{
                SQL.where("category_id", "=", Request.belong().input("category"))
            }).getModels();
        }
        else{
            books = new Books("").all().getModels();
            
            books.removeIf((book)->{
                return String.valueOf(book.get("name")).indexOf(Request.belong().input("search")) == -1;
            });
        }

        renderSearchRs(books.size());
        tableBook.getItems().addAll(books);

        TableColumn<app.Models.Books, Boolean> buttonColumn = new TableColumn<>("Button");
        buttonColumn.setCellFactory((tableColumn) -> {
            TableCell<app.Models.Books, Boolean> cell = new TableCell<app.Models.Books, Boolean>() {
                private Button hideBtn = new Button("Ẩn sách");
                private Button viewBtn = new Button("Xem sách");
                private Button addCardBtn = new Button("Thêm vào giỏ hàng");
                private Button addHeartBtn = new Button("Thêm vào mục yêu thích");
                private VBox vbox = new VBox();

                {
                    vbox.getChildren().addAll(hideBtn, viewBtn, addCardBtn,addHeartBtn);

                    hideBtn.setOnAction((ActionEvent event) -> {
                        getTableView().getItems().remove(getIndex());
                    });

                    viewBtn.setOnAction((ActionEvent event) -> {
                        String book_id = String.valueOf(getTableView().getItems().get(getIndex()).get("id"));
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
                            newcart.put("book_id", getTableView().getItems().get(getIndex()).get("id"));

                            ArrayList<Cart> check =  new Cart(new String[]{
                                SQL.where("username", "=", Request.belong().session().get("user")),
                                SQL.where("book_id","=",getTableView().getItems().get(getIndex()).get("id"))
                            }).getModels();
                            
                            if(check.size()==0) new Cart(newcart).insert();
                            else FormManager.belong().showAlert("Bạn đã thêm vào giỏ hàng trước đó rồi");
                        }
                    });

                    addHeartBtn.setOnAction((ActionEvent event) -> {
                        if(Request.belong().session().get("user")==null){
                            FormManager.belong().showAlert("Vui lòng đăng nhập trước đã bạn nhé");
                        }
                        else{
                            HashMap<String,Object> newheart = new HashMap<>();
                            newheart.put("username", Request.belong().session().get("user"));
                            newheart.put("book_id", getTableView().getItems().get(getIndex()).get("id"));
                            
                            ArrayList<Heart> check =  new Heart(new String[]{
                                SQL.where("username", "=", Request.belong().session().get("user")),
                                SQL.where("book_id","=",getTableView().getItems().get(getIndex()).get("id"))
                            }).getModels();
                            
                            if(check.size()==0) new Heart(newheart).insert();
                            else FormManager.belong().showAlert("Bạn đã thêm vào mục yêu thích trước đó rồi");
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

        tableBook.getColumns().add(buttonColumn);
    }
}