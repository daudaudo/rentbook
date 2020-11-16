package core.Facades;

public class Request extends Facades {
    public static String getFacade(){
        return "request";
    }

    public static core.Request.Request belong(){
        return (core.Request.Request) app.getInstance(getFacade());
    }
}
