package core.Models;

import java.util.ArrayList;
import java.util.HashMap;
import core.Database.DB;
import core.Supports.SQL;

public class Models implements Cloneable {
    public static DB db;

    protected String primaryKey = "id";
    protected String tableName = getClass().getSimpleName().toLowerCase();
    
    protected String id = null;
    protected String[] where = null;

    private ArrayList<HashMap<String,Object>> result = null;
    private HashMap<String,Object> data = null;
    
    private String columns = "";
    private String values = "";
    private String set = "";
    
    public Models(String id,String... config){
        this.id = id;
        primaryKey = config.length >= 1 ? config[0] : primaryKey;
        tableName = config.length > 1 ? config[1] : tableName;
        find();
    }
    
    public Models(String[] where,String... config){
        this.where = where;
        primaryKey = config.length >= 1 ? config[0] : primaryKey;
        tableName = config.length > 1 ? config[1] : tableName;
        where();
    }
    
    public Models(HashMap<String,Object> data,String... config){
        primaryKey = config.length >= 1 ? config[0] : primaryKey;
        tableName = config.length > 1 ? config[1] : tableName;
        this.data = data ;
    }

    private void find(){
        this.result = db.excuteQuery("SELECT * from " + tableName + " where " + SQL.where(primaryKey, "=", id) ).get();
        this.data = this.result.isEmpty() ? null : this.result.get(0);
    }

    public void insert(){
        String sql = SQL.insert(tableName);
        columns = "";
        values = "";
        
        data.forEach((column,value)->{
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

    private void where(){
        String sql = "SELECT * from " + tableName +" where ";

        for (String w : where) {
            sql = sql + w + " and ";
        }
        sql = sql.substring(0, sql.length()-5);

        this.result = db.excuteQuery(sql).get();
    }

    public Models all(){
        this.result = db.excuteQuery("SELECT * from " + tableName).get();
        return this;
    }

    public void delete(){
        String sql = SQL.delete(tableName)+" where " + SQL.where(primaryKey, "=", get(primaryKey));
        db.execute(sql);
    }

    public void update(){
        set = "";
        
        this.data.forEach((column , value)->{
            if(!column.equals(primaryKey))
            set += SQL.set(column , value) + ",";
        });
        
        set = set.substring(0, set.length()-1);
        
        String sql = SQL.update(tableName)+" set "+set+" where " + SQL.where(primaryKey, "=", get(primaryKey));
        
        db.execute(sql);
    }

    @SuppressWarnings("unchecked")
    public <T> ArrayList<T> getModels(){
        ArrayList<T> modelsArr = new ArrayList<>();
        
        result.stream().map((rs) -> {
            Models model = clone();
            model.set(rs);
            return model;
        }).forEach((model) -> {
            modelsArr.add((T) model);
        });
        
        return modelsArr;
    }

    public Object get(String column){
        return data.get(column);
    }

    public HashMap<String,Object> get(){
        return data;
    }

    public Models set(String column , Object value){
        data.replace(column, value);
        return this;
    }
    
    public void set(HashMap<String,Object> data){
        this.data = data;
    }
    
    @Override
    public String toString(){
        return data.toString();
    }
    
    @Override
    public Models clone(){
        try {
            return (Models) super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }
}
