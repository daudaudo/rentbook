package core.Database;

import core.Enviroment.Enviroment;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DB {
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet rs = null;

    public DB(Enviroment env) {
        if (env.env("DB_CONNECTION").equals("mysql")) {
            this.statement = new ConnectorMySQL(env).getStatement();
        }
    }

    public DB excuteQuery(String sql) {
        try {
            this.rs = this.statement.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println(sql);
        }
        return this;
    }

    public void execute(String sql) {
        try {
            this.statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(sql);
        }
    }

    public DB prepare(String sql) {
        try {
            this.preparedStatement = this.statement.getConnection().prepareStatement(sql);
        } catch (SQLException e) {
            this.preparedStatement = null;
        }
        return this;
    }

    public DB binding(Object... params) {
        try {
            for (int i = 0; i < params.length; i++)
                this.preparedStatement.setObject(i + 1, params[i]);
            this.rs = this.preparedStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return this;
    }

    public ArrayList<HashMap<String, Object>> get() {
        ArrayList<HashMap<String, Object>> arr = new ArrayList<>();
        try {
            ResultSetMetaData rsMeta = this.rs.getMetaData();
            while(this.rs.next()){
                HashMap<String, Object> hs = new HashMap<>();
                for(int i=0;i<rsMeta.getColumnCount();i++){
                    hs.put(rsMeta.getColumnName(i+1),this.rs.getObject(rsMeta.getColumnName(i+1)));
                }
                arr.add(hs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return arr;
    }
}
