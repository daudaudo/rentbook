package core.Form;

import java.util.HashMap;


import core.View.View;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;

public class FormManager {
    public LoaderFXML loader;

    public FormManager(LoaderFXML loader){
        this.loader = loader;
    }

    private HashMap<String, View> forms = new HashMap<>();

    public void binding(String name,View view){
        this.setLayout(view);
        this.forms.put(name,view);
    }

    public void open(String name){
        this.forms.get(name).show();
    }

    public void openWithOwnner(String name , String ownner){
        this.forms.get(name).initModality(Modality.APPLICATION_MODAL);
        this.forms.get(name).initOwner(this.forms.get(ownner));

        this.open(name);
    }

    private void setLayout(View view){
        view.setScene(new Scene(this.loader.getLayout()));
    }

    public void close(String name){
        this.forms.get(name).close();
        this.forms.remove(name);
    }

    public void setMainContentPane(String nameView,String mainContentPaneName){

        AnchorPane container = (AnchorPane) this.forms.get(nameView).getScene().getRoot().lookup("#mainContentContainer");
        
        this.forms.get(nameView).mainPane = container;

        if(container.getChildren().isEmpty()){
            container.getChildren().add(this.loader.getComponents(mainContentPaneName));
        }
        else {
            container.getChildren().remove(0);
            container.getChildren().add(this.loader.getComponents(mainContentPaneName));
        }
    }

    public View getView(String nameView){
        return this.forms.get(nameView);
    }

    public void showAlert(String content){
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Có gì đó sai sai ??");
        alert.setContentText(content);

        alert.show();
    }
}

