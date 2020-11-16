package core.Form;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import core.Facades.Router;

public class LayoutController {
    @FXML
    private HBox titleBar;

    @FXML
    private FontAwesomeIcon minusBtn;

    @FXML
    private FontAwesomeIcon closeBtn;

    private double x,y;


    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void minus(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void pressed(MouseEvent event) {
        this.x=event.getSceneX();
        this.y=event.getSceneY();
    }

    @FXML
    void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX()-x);
        stage.setY(event.getScreenY()-y);
    }

    @FXML
    void chatClick(MouseEvent event) {

    }

    @FXML
    void userClick(MouseEvent event) {
        Router.belong().route("userinfo");
    }

    @FXML
    void heartClick(MouseEvent event) {
        Router.belong().route("heart");
    }

    @FXML
    void homeClick(MouseEvent event) {
        Router.belong().route("home");
    }

    @FXML
    void cartClick(MouseEvent event) {
        Router.belong().route("cart");
    }

}