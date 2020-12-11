package app.Models;

import java.util.HashMap;

import core.Models.Models;

public class Categories extends Models {
    
    public Categories(String id){
        super(id);
    }

    public Categories(String[] where){
        super(where);
    }

    public Categories(HashMap<String,Object> data){
        super(data);
    }

    @Override
    public String toString(){
        return String.valueOf(get("name"));
    }
}
