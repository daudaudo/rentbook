package app.Models;

import java.util.HashMap;

import core.Models.Models;

public class Heart extends Models {
    public Heart(String id){
        super(id);
    }

    public Heart(String[] where){
        super(where);
    }

    public Heart(HashMap<String,Object> data){
        super(data);
    }
}
