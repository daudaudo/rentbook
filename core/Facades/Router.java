package core.Facades;

public class Router extends Facades {
    public static String getFacade(){
        return "router";
    }

    public static core.Router.Router belong(){
        return (core.Router.Router) app.getInstance(getFacade());
    }
}
