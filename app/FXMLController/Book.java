package app.FXMLController;

import java.util.ArrayList;
import java.util.HashMap;

import app.Models.Books;
import core.Facades.DB;
import core.Facades.FormManager;
import core.Facades.Request;
import core.Facades.Router;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class Book {

    private Books book ;

    @FXML
    private Text userText;

    @FXML
    private Text booknameText;

    @FXML
    private ImageView imageBook;

    @FXML
    private Text authorsText;

    @FXML
    private Text priceText;

    @FXML
    private TextArea description;

    @FXML
    private Text numberText;

    @FXML
    void addToCartBtn(MouseEvent event) {
        if(Request.belong().session().get("user")==null){
            FormManager.belong().showAlert("Vui lòng đăng nhập trước đã bạn nhé");
        }
        else{
            HashMap<String,Object> newcart = new HashMap<>();
            newcart.put("username", Request.belong().session().get("user"));
            newcart.put("book_id", Request.belong().input("book_id"));
            ArrayList<HashMap<String, Object>> check =  DB.belong().prepare("SELECT * FROM cart where username = ? and book_id = ?").binding(Request.belong().session().get("user"),Request.belong().input("book_id")).get();
            
            if(check.size()==0) {new app.Models.Cart(newcart).insert();Router.belong().route("cart");}
            else FormManager.belong().showAlert("Bạn đã thêm vào giỏ hàng trước đó rồi");
        }
    }

    @FXML
    public void initialize(){
        this.book = new Books(Request.belong().input("book_id"));
        renderTitle();
        renderInfoBook();
    }

    private void renderTitle(){
        userText.setText(Request.belong().session().get("user"));
    }

    public void renderInfoBook(){
        booknameText.setText((String) this.book.get("name"));
        authorsText.setText("Tác giả: "+(String) this.book.get("authors"));
        priceText.setText("Giá thuê: "+(float) this.book.get("price")+"đ/tháng");
        imageBook.setImage(new Image("file:///" +core.Facades.Facades.app.getPath("resource.img") + "/"+(String) this.book.get("image")));
        numberText.setText("Còn: "+(int) this.book.get("number")+" sản phẩm");
        description.setText((String) this.book.get("description"));
    }
}
