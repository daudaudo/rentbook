package app.Render;

import java.util.ArrayList;
import java.util.HashMap;

import app.Models.Books;
import app.Models.Cart;
import core.Closure.Render;
import core.Facades.FormManager;
import core.Facades.Request;
import core.Facades.Router;
import core.View.View;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import core.Facades.DB;

public class Heart implements Render {
    private View view;
    private core.Request.Request req;

    @Override
    public void render(View view, core.Request.Request req) {
        this.view = view;
        this.req = req;

        renderTitle();
        renderTableBook();
    }

    public void renderTitle(){
        Text usernameText = (Text) view.mainPane.lookup("#usernameText");
        usernameText.setText(req.session().get("user"));
    }

    @SuppressWarnings("unchecked")
    public void renderTableBook() {
        TableView<HeartBook> tableHeart = (TableView<HeartBook>) view.mainPane.lookup("#tableHeart");
        TableColumn<HeartBook,String> nameColumn = new TableColumn<>("Name");
        TableColumn<HeartBook,String> authorsColumn = new TableColumn<>("Authors");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        authorsColumn.setCellValueFactory(new PropertyValueFactory<>("authorsName"));

        tableHeart.getColumns().addAll(nameColumn,authorsColumn);
        ArrayList<HashMap<String,Object>> heartbook = new app.Models.Heart().where("username='"+req.session().get("user")+"'");

        for(int i=0;i<heartbook.size();i++){
            int book_id = (int) heartbook.get(i).get("book_id");
            tableHeart.getItems().add(new HeartBook(book_id));
        }

        TableColumn<HeartBook, Boolean> buttonColumn = new TableColumn<>("Button");
        buttonColumn.setCellFactory((tableColumn) -> {
            TableCell<HeartBook, Boolean> cell = new TableCell<HeartBook, Boolean>() {
                private Button deleteBtn = new Button("Xoá khỏi mục yêu thích");
                private Button viewBtn = new Button("Xem sách");
                private Button addCardBtn = new Button("Thêm vào giỏ hàng");
                private VBox vbox = new VBox();

                {
                    vbox.getChildren().addAll(deleteBtn, viewBtn, addCardBtn);

                    deleteBtn.setOnAction((ActionEvent event) -> {
                        new app.Models.Heart().deleteWhere("username='"+req.session().get("user")+"'","book_id="+getTableView().getItems().get(getIndex()).book_id);
                        getTableView().getItems().remove(getIndex());
                    });

                    viewBtn.setOnAction((ActionEvent event) -> {
                        String book_id = String.valueOf(getTableView().getItems().get(getIndex()).book_id);
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
                            newcart.put("book_id", getTableView().getItems().get(getIndex()).book_id);
                            ArrayList<HashMap<String, Object>> check =  DB.belong().prepare("SELECT * FROM rentbook.cart where username = ? and book_id = ?").binding(req.session().get("user"),getTableView().getItems().get(getIndex()).book_id).get();
                            
                            if(check.size()==0) new Cart().insert(newcart);
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

    public class HeartBook{
        private int book_id;

        public HeartBook(int book_id){
            this.book_id = book_id;
        }

        public String getBookName(){
            return (String) new Books().find(String.valueOf(this.book_id)).get("name");
        }

        public String getAuthorsName(){
            return (String) new Books().find(String.valueOf(this.book_id)).get("authors");
        }
    }

}
