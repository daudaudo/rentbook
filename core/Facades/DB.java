package core.Facades;

public class DB extends Facades{
    public static String getFacade() {
        return "db";
    }

    public static core.Database.DB belong(){
        return (core.Database.DB) app.getInstance(getFacade());
    }
}
