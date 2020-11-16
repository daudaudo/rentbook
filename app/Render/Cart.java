package app.Render;

import java.util.ArrayList;
import java.util.HashMap;

import app.Models.Books;
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

public class Cart implements Render {

    private View view;
    private core.Request.Request req;

    @Override
    public void render(View view, core.Request.Request req) {
        this.view = view;
        this.req = req;

        renderTitle();
        renderTableCart();
        renderButton();
    }

    public void renderTitle(){
        Text usernameText = (Text) view.mainPane.lookup("#usernameText");
        usernameText.setText(req.session().get("user"));
    }

    public void renderButton(){
        Button deleteAllCart = (Button) view.mainPane.lookup("#deleteAllCart");
        deleteAllCart.setOnAction((event)->{
            new app.Models.Cart().deleteWhere("username='"+req.session().get("user")+"'");
            Router.belong().route("cart");
        });
    }

    @SuppressWarnings("unchecked")
    public void renderTableCart(){
        TableView<CartBook> tableCart = (TableView<CartBook>) view.mainPane.lookup("#tableCart");

        TableColumn<CartBook,String> nameColumn = new TableColumn<>("Name");
        TableColumn<CartBook,String> numberColumn = new TableColumn<>("Number");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        tableCart.getColumns().addAll(nameColumn,numberColumn);

        ArrayList<HashMap<String,Object>> cartbook = new app.Models.Cart().where("username='"+req.session().get("user")+"'");

        for(int i=0;i<cartbook.size();i++){
            int book_id = (int) cartbook.get(i).get("book_id");
            int number = (int) cartbook.get(i).get("number");

            tableCart.getItems().add(new CartBook(book_id, number));
        }

        TableColumn<CartBook, Boolean> buttonColumn = new TableColumn<>("Button");
        buttonColumn.setCellFactory((tableColumn) -> {
            TableCell<CartBook, Boolean> cell = new TableCell<CartBook, Boolean>() {
                private Button deleteBtn = new Button("Xoá khỏi giỏ hàng");
                private Button viewBtn = new Button("Xem sách");
                private Button addBtn = new Button("Cộng");
                private Button minusBtn = new Button("Trừ");
                private VBox vbox = new VBox();

                {
                    vbox.getChildren().addAll(deleteBtn, viewBtn, addBtn,minusBtn);

                    deleteBtn.setOnAction((ActionEvent event) -> {
                        new app.Models.Cart().deleteWhere("username='"+req.session().get("user")+"'","book_id="+getTableView().getItems().get(getIndex()).book_id);
                        getTableView().getItems().remove(getIndex());
                    });

                    viewBtn.setOnAction((ActionEvent event) -> {
                        String book_id = String.valueOf(getTableView().getItems().get(getIndex()).book_id);
                        if(Request.belong().input("book_id")!=null)
                            Request.belong().input.remove("book_id");
                        Request.belong().input.put("book_id", book_id);
                        Router.belong().route("book");
                    });

                    addBtn.setOnAction((ActionEvent event) -> {
                        int number = getTableView().getItems().get(getIndex()).number;
                        int book_id = getTableView().getItems().get(getIndex()).book_id;
                        String username = req.session().get("user");

                        if(number+1>(int) new Books().find(String.valueOf(book_id)).get("number")){
                            FormManager.belong().showAlert("Không đủ sản phẩm trong kho");
                        }
                        else{
                            new app.Models.Cart().updateWhere("number="+(number+1), "username='"+username+"'","book_id="+book_id);
                            Router.belong().route("cart");
                        }
                    });

                    minusBtn.setOnAction((ActionEvent event) -> {
                        int number = getTableView().getItems().get(getIndex()).number;
                        int book_id = getTableView().getItems().get(getIndex()).book_id;
                        String username = req.session().get("user");

                        if(number-1==0){
                            new app.Models.Cart().deleteWhere("username='"+req.session().get("user")+"'","book_id="+getTableView().getItems().get(getIndex()).book_id);
                            getTableView().getItems().remove(getIndex());
                        }
                        else{
                            new app.Models.Cart().updateWhere("number="+(number-1), "username='"+username+"'","book_id="+book_id);
                            Router.belong().route("cart");
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

        tableCart.getColumns().add(buttonColumn);
    }

    public class CartBook{
        private int book_id;
        private int number;

        public CartBook(int book_id,int number){
            this.book_id = book_id;
            this.number = number;
        }

        public int getNumber(){
            return this.number;
        }

        public String getBookName(){
            return (String) new Books().find(String.valueOf(this.book_id)).get("name");
        }
    }
    
}
