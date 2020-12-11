package core.Supports;

public class SQL {
    public static String where(String column , String expression , Object value){
        return column + expression + "'" + String.valueOf(value) + "'";
    }
    
    public static String set(String column , Object value){
        return column + "=" + "'" + String.valueOf(value) + "'";
    }
    
    public static String update(String tableName){
        return "UPDATE "+tableName;
    }
    
    public static String insert(String tableName){
        return "INSERT INTO "+tableName;
    }
    
    public static String delete(String tableName){
        return "DELETE FROM "+tableName;
    }
}
