package core.Facades;


public class Enviroment extends Facades {

    public static String getFacade(){
        return "enviroment";
    }

    public static core.Enviroment.Enviroment belong(){
        return (core.Enviroment.Enviroment) app.getInstance(getFacade());
    }
}
