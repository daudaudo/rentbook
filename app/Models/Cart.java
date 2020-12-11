package app.Models;

import java.util.HashMap;

import core.Models.Models;

public class Cart extends Models {
    
    public Cart(String id){
        super(id);
    }

    public Cart(String[] where){
        super(where);
    }

    public Cart(HashMap<String,Object> data){
        super(data);
    }

}
