package app.Models;

import java.util.HashMap;

import core.Models.Models;

public class Address extends Models {
    public Address(String id){
        super(id);
    }

    public Address(String[] where){
        super(where);
    }

    public Address(HashMap<String,Object> data){
        super(data);
    }
}
