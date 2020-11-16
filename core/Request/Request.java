package core.Request;

import java.util.HashMap;

import core.Session.Session;

public class Request {
    private Session session;
    public HashMap<String,String> input = new HashMap<>();

    private String routerName;
    
    public Request(Session session){
        this.session = session;
    }

    public Session session(){
        return this.session;
    }
    
    public String input(String key){
        return this.input.get(key);
    }

    public void update(){
        this.input.clear();
    }

    public void setRouterName(String routerName){
        this.routerName = routerName;
    }

    public String getRouterName(){
        return this.routerName;
    }

    public void close(){
        session.writeSessionToFile();
    }
}
