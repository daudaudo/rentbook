package app.Models;

import java.util.HashMap;

import core.Models.Models;

public class Books extends Models {
    public Books(String id){
        super(id);
    }

    public Books(String[] where){
        super(where);
    }

    public Books(HashMap<String,Object> data){
        super(data);
    }

    @Override
    public String toString(){
        return String.valueOf(get("name"));
    }
}
