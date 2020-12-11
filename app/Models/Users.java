package app.Models;

import java.util.HashMap;

import core.Models.Models;

public class Users extends Models {
    public Users(String id){
        super(id,"username");
    }

    public Users(String[] where){
        super(where,"username");
    }

    public Users(HashMap<String,Object> data){
        super(data,"username");
    }
    
}
