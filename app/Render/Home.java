package app.Render;

import java.util.ArrayList;
import java.util.HashMap;

import app.Models.Books;
import app.Models.Cart;
import app.Models.Heart;
import core.Closure.Render;
import core.Facades.FormManager;
import core.Facades.Request;
import core.Facades.Router;
import core.View.View;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import core.Facades.DB;

public class Home implements Render {
    private View view;
    private core.Request.Request req;

    @Override
    public void render(View view, core.Request.Request req) {
        this.view = view;
        this.req = req;

        renderTitle();
        renderChoiceBox();
        renderTableBook();
    }

    public void renderTitle(){
        Text usernameText = (Text) view.mainPane.lookup("#usernameText");
        usernameText.setText(req.session().get("user"));
    }

    @SuppressWarnings("unchecked")
    public void renderChoiceBox(){
        ChoiceBox<Categories> qChoiceBox = (ChoiceBox<Categories>) view.mainPane.lookup("#qChoice");

        ArrayList<HashMap<String, Object>> categories = new app.Models.Categories().all();

        qChoiceBox.getItems().add(new Categories(-1, "Chọn thể loại sách"));

        for (int i = 0; i < categories.size(); i++) {

            int id = (int) categories.get(i).get("id");
            String name = categories.get(i).get("name").toString();

            qChoiceBox.getItems().add(new Categories(id, name));
        }
        qChoiceBox.getSelectionModel().select(0);
        qChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldCategory, newCategory) -> {
            if(req.input("category")!=null) req.input.remove("category");
            req.input.put("category", String.valueOf(newCategory.id));
            Router.belong().route("category");
        });
    }

    @SuppressWarnings("unchecked")
    public void renderTableBook() {
        TableView<Book> tableBook = (TableView<Book>) view.mainPane.lookup("#table");
        TableColumn<Book, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Book, String> authorsColumn = new TableColumn<>("Authors");
        TableColumn<Book, String> imageColumn = new TableColumn<>("Image");
        TableColumn<Book, Float> priceColumn = new TableColumn<>("Price");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorsColumn.setCellValueFactory(new PropertyValueFactory<>("authors"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableBook.getColumns().addAll(nameColumn, authorsColumn,imageColumn,priceColumn);
        ArrayList<HashMap<String, Object>> books = new Books().all();

        for (int i = 0; i < books.size(); i++) {
            int id = (int) books.get(i).get("id");
            String name = (String) books.get(i).get("name");
            String authors = (String) books.get(i).get("authors");
            String description = (String) books.get(i).get("description");
            String image = (String) books.get(i).get("image");
            float price = (float ) books.get(i).get("price");

            tableBook.getItems().add(new Book(id, name, authors, description, image,price));
        }

        TableColumn<Book, Boolean> buttonColumn = new TableColumn<>("Button");
        buttonColumn.setCellFactory((tableColumn) -> {
            TableCell<Book, Boolean> cell = new TableCell<Book, Boolean>() {
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
                        String book_id = String.valueOf(getTableView().getItems().get(getIndex()).id);
                        if(Request.belong().input("book_id")!=null)
                            Request.belong().input.remove("book_id");
                        Request.belong().input.put("book_id", book_id);
                        Router.belong().route("book");
                    });

                    addCardBtn.setOnAction((ActionEvent event) -> {
                        if(req.session().get("user")==null){
                            FormManager.belong().showAlert("Vui lòng đăng nhập trước đã bạn nhé");
                        }
                        else{
                            HashMap<String,Object> newcart = new HashMap<>();
                            newcart.put("username", req.session().get("user"));
                            newcart.put("book_id", getTableView().getItems().get(getIndex()).id);
                            ArrayList<HashMap<String, Object>> check =  DB.belong().prepare("SELECT * FROM rentbook.cart where username = ? and book_id = ?").binding(req.session().get("user"),getTableView().getItems().get(getIndex()).id).get();
                            
                            if(check.size()==0) new Cart().insert(newcart);
                            else FormManager.belong().showAlert("Bạn đã thêm vào giỏ hàng trước đó rồi");
                        }
                    });

                    addHeartBtn.setOnAction((ActionEvent event) -> {
                        if(req.session().get("user")==null){
                            FormManager.belong().showAlert("Vui lòng đăng nhập trước đã bạn nhé");
                        }
                        else{
                            HashMap<String,Object> newheart = new HashMap<>();
                            newheart.put("username", req.session().get("user"));
                            newheart.put("book_id", getTableView().getItems().get(getIndex()).id);
                            ArrayList<HashMap<String, Object>> check =  DB.belong().prepare("SELECT * FROM rentbook.heart where username = ? and book_id = ?").binding(req.session().get("user"),getTableView().getItems().get(getIndex()).id).get();
                            
                            if(check.size()==0) new Heart().insert(newheart);
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

    class Categories {
        private int id;
        private String name;

        public Categories(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    public class Book {
        private int id;
        private String name;
        private String authors;
        private String description;
        private ImageView image;
        private float price;

        public Book(int id, String name, String authors, String description, String image,float price) {
            this.id = id;
            this.name = name;
            this.authors = authors;
            this.description = description;
            image ="file:///" +core.Facades.Facades.app.getPath("resource.img") + "/" + image;
            this.image = new ImageView(new Image(image));
            this.image.setFitWidth(250);
            this.image.setFitHeight(250);

            this.price = price;
        }

        public float getPrice(){
            return this.price;
        }

        public ImageView getImage(){
            return this.image;
        }

        public int getId(){
            return this.id;
        }

        public String getName(){
            return this.name;
        }

        public String getAuthors(){
            return this.authors;
        }

        public String getDescription(){
            return this.description;
        }
    }

}
