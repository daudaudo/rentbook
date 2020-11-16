package core.View;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class View extends Stage {

    public AnchorPane mainPane;

    public View(){
        this.setStyle();
    }

    public void setStyle() {
        this.setMaximized(false);
        this.setFullScreen(false);
        this.initStyle(StageStyle.TRANSPARENT);
    }
}
