package core.Models;

import java.util.ArrayList;
import java.util.HashMap;

import core.Database.DB;

public class Models {
    public static DB db;

    protected String primaryKey = "id";
    protected String tableName = null;

    private String columns = "";
    private String values = "";

    public HashMap<String,Object> find(String id){
        return db.excuteQuery("SELECT * from " + tableName + " where " + primaryKey + " = '" + id+"'").get().size()==0?null:db.excuteQuery("SELECT * from " + tableName + " where " + primaryKey + " = '" + id+"'").get().get(0);
    }

    public void insert(HashMap<String,Object> insertField){
        String sql = "INSERT INTO "+tableName;
        columns = "";
        values = "";
        
        insertField.forEach((column,value)->{
            columns+=column + ",";
            if(value instanceof String)
                values+= "'" + value + "'"+ ",";
            else 
                values+= value + ",";
        });

        columns = "(" + columns.substring(0, columns.length()-1) + ")";
        values = "values"+ "(" + values.substring(0, values.length()-1) + ")" ;

        sql = sql + " " + columns +" " +values;

        db.execute(sql);
    }

    public HashMap<String,Object> findWhere(String id,String... where){
        String sql = "SELECT * from " + tableName + " where " + primaryKey + " = '" + id +"' and ";

        for(int i=0;i<where.length;i++){
            sql = sql +where[i] +" and ";
        }
        sql = sql.substring(0, sql.length()-5);

        return db.excuteQuery(sql).get().size()==0?null:db.excuteQuery(sql).get().get(0);
    }

    public ArrayList<HashMap<String,Object>> where(String... where){
        String sql = "SELECT * from " + tableName +" where ";

        for(int i=0;i<where.length;i++){
            sql = sql +where[i] +" and ";
        }
        sql = sql.substring(0, sql.length()-5);

        return db.excuteQuery(sql).get();
    }

    public ArrayList<HashMap<String,Object>> all(){
        return db.excuteQuery("SELECT * from " + tableName).get();
    }

    public void deleteWhere(String... where){
        String sql = "DELETE FROM "+tableName+" where ";

        for(int i=0;i<where.length;i++){
            sql = sql +where[i] +" and ";
        }
        sql = sql.substring(0, sql.length()-5);

        db.execute(sql);
    }

    public void updateWhere(String set,String... where){
        String sql = "UPDATE "+tableName+" set "+set+" where ";

        for(int i=0;i<where.length;i++){
            sql = sql +where[i] +" and ";
        }
        sql = sql.substring(0, sql.length()-5);

        db.execute(sql);
    }

}
