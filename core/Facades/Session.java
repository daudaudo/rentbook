package core.Facades;

public class Session extends Facades{
    public static String getFacade(){
        return "session";
    }

    public static core.Session.Session belong(){
        return (core.Session.Session) app.getInstance(getFacade());
    }
}
