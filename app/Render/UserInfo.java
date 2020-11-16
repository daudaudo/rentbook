package app.Render;

import core.Closure.Render;
import core.Request.Request;
import core.View.View;
import javafx.scene.text.Text;

public class UserInfo implements Render {
    @Override
    public void render(View view, Request req) {
        Text title = (Text) view.mainPane.lookup("#titleUserInfo");

        title.setText("Xin chào " + req.session().get("user"));
    }

    
    
}
