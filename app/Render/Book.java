package app.Render;

import java.util.ArrayList;
import java.util.HashMap;

import app.Models.Books;
import core.Closure.Render;
import core.Facades.FormManager;
import core.Facades.Router;
import core.View.View;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import core.Facades.DB;
import app.Models.Cart;

public class Book implements Render {
    private View view;
    private core.Request.Request req;
    private HashMap<String,Object> book;

    @Override
    public void render(View view, core.Request.Request req) {
        this.view = view;
        this.req = req;
        this.book = new Books().find(req.input("book_id"));

        renderTitle();
        renderInfoBook();
        renderButton();
    }
    
    public void renderTitle(){
        Text usernameText = (Text) view.mainPane.lookup("#usernameText");
        usernameText.setText(req.session().get("user"));
    }

    public void renderInfoBook(){
        Text bookName = (Text) view.mainPane.lookup("#booknameText");
        bookName.setText((String) this.book.get("name"));

        Text authors = (Text) view.mainPane.lookup("#authorsText");
        authors.setText("Tác giả: "+(String) this.book.get("authors"));

        Text price = (Text) view.mainPane.lookup("#priceText");
        price.setText("Giá thuê: "+(float) this.book.get("price")+"đ/tháng");

        ImageView imageBook = (ImageView) view.mainPane.lookup("#imageBook");
        imageBook.setImage(new Image("file:///" +core.Facades.Facades.app.getPath("resource.img") + "/"+(String) this.book.get("image")));

        Text numberText = (Text) view.mainPane.lookup("#numberText");
        numberText.setText("Còn: "+(int) this.book.get("number")+" sản phẩm");

        TextArea description = (TextArea) view.mainPane.lookup("#description");
        description.setText((String) this.book.get("description"));
    }

    public void renderButton(){
        Button addToCartBtn = (Button) view.mainPane.lookup("#addToCartBtn");
        addToCartBtn.setOnAction((event)->{
            if(req.session().get("user")==null){
                FormManager.belong().showAlert("Vui lòng đăng nhập trước đã bạn nhé");
            }
            else{
                HashMap<String,Object> newcart = new HashMap<>();
                newcart.put("username", req.session().get("user"));
                newcart.put("book_id", req.input("book_id"));
                ArrayList<HashMap<String, Object>> check =  DB.belong().prepare("SELECT * FROM rentbook.cart where username = ? and book_id = ?").binding(req.session().get("user"),req.input("book_id")).get();
                
                if(check.size()==0) {new Cart().insert(newcart);Router.belong().route("cart");}
                else FormManager.belong().showAlert("Bạn đã thêm vào giỏ hàng trước đó rồi");
            }
        });
    }
}
