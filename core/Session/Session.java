package core.Session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import core.Container.Application;

public class Session implements Serializable {

    private static final long serialVersionUID = 7231396767807169880L;
    private transient Application app;
    private HashMap<String, String> session = new HashMap<>();

    public Session(Application app) {
        this.app = app;
    }

    public String get(String key) {
        return this.session.get(key);
    }

    public void clear(){
        this.session.clear();
    }

    public void add(String key,String value){
        this.session.put(key, value);
    }

    public static Session getSessionFromFile(Application app) {
        String sessionPath = app.getPath("session");
        try {
            ObjectInputStream sessionFileObject = new ObjectInputStream(new FileInputStream(new File(sessionPath)));
            Session sessionCurrent = (Session) sessionFileObject.readObject();
            sessionFileObject.close();
            
            return sessionCurrent instanceof Session? sessionCurrent: null;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }

    public void writeSessionToFile(){
        String sessionPath = this.app.getPath("session");
        try {
            ObjectOutputStream sessionFileObject = new ObjectOutputStream(new FileOutputStream(new File(sessionPath)));
            sessionFileObject.writeObject(this);
            sessionFileObject.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setApplication(Application app){
        this.app = app;
    }
}
