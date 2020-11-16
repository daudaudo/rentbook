package core.Facades;

public class FormManager extends Facades {
    public static String getFacade(){
        return "fm";
    }

    public static core.Form.FormManager belong(){
        return (core.Form.FormManager) app.getInstance(getFacade());
    }
}
