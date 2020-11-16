package app.Render;

import java.util.ArrayList;
import java.util.HashMap;
import core.Closure.Render;
import core.View.View;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class Address implements Render {

    private View view;
    private core.Request.Request req;

    @Override
    public void render(View view, core.Request.Request req) {
        this.view = view;
        this.req = req;

        renderTitle();
        renderAddressTable();
    }

    public void renderTitle(){
        Text usernameText = (Text) view.mainPane.lookup("#usernameText");
        usernameText.setText(req.session().get("user"));
    }

    @SuppressWarnings("unchecked")
    public void renderAddressTable(){
        TableView<AddressUser> tableAddress = (TableView<AddressUser>) view.mainPane.lookup("#tableAddress");
        
        TableColumn<AddressUser, String> nameColumn = new TableColumn<>("Name");
        TableColumn<AddressUser, String> phoneColumn = new TableColumn<>("Phone");
        TableColumn<AddressUser, String> emailColumn = new TableColumn<>("Email");
        TableColumn<AddressUser, String> addressColumn = new TableColumn<>("Address");

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        tableAddress.getColumns().addAll(nameColumn,phoneColumn,emailColumn,addressColumn);

        ArrayList<HashMap<String,Object>> addresses = new app.Models.Address().where("username='"+req.session().get("user")+"'");
        for(int i=0;i<addresses.size();i++){
            int id = (int) addresses.get(i).get("id");
            String name = (String) addresses.get(i).get("name");
            String phone = (String) addresses.get(i).get("phone");
            String email = (String) addresses.get(i).get("email");
            String address = (String) addresses.get(i).get("address");
            tableAddress.getItems().add(new AddressUser(id,name, phone, email, address));
        }

        TableColumn<AddressUser, Boolean> buttonColumn = new TableColumn<>("Button");
        buttonColumn.setCellFactory((tableColumn) -> {
            TableCell<AddressUser, Boolean> cell = new TableCell<AddressUser, Boolean>() {
                private Button deleteBtn = new Button("Xoá");
                {
                    deleteBtn.setOnAction((ActionEvent event) -> {
                        new app.Models.Address().deleteWhere("id="+getTableView().getItems().remove(getIndex()).id);
                    });

                }

                @Override
                public void updateItem(Boolean item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteBtn);
                    }
                }
            };
            return cell;
        });

        tableAddress.getColumns().add(buttonColumn);
    }

    public class AddressUser{
        private String name;
        private String phone;
        private String email;
        private String address;
        private int id;

        public AddressUser(int id,String name,String phone,String email,String address){
            this.id = id ;
            this.name = name ;
            this.phone = phone;
            this.email = email;
            this.address = address;
        }

        public String getName(){
            return this.name;
        }

        public String getPhone(){
            return this.phone;
        }

        public String getEmail(){
            return this.email;
        }

        public String getAddress(){
            return this.address;
        }
    }


    
}
