package core.Facades;

import java.lang.reflect.InvocationTargetException;

import core.Container.Application;

public class Facades {
    public static Application app;

    public static void setApplication(Application app) {
        Facades.app = app;
    }

    protected static String getFacade() {
        try {
            throw new Exception();
        } catch (Exception e) {
            System.out.println("Overide this method");
        }
        return null;
    };

    public static Object callMethod(String nameObject,String nameMethod, Object... params) {
        try { 
            return app.getInstance(nameObject).getClass().getMethod(nameMethod,getParameterTypes(params)).invoke(app.getInstance(nameObject),params);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException| SecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Class<?>[] getParameterTypes(Object... params){
        Class<?> parameterTypes[] = new Class<?>[params.length];
        for(int i=0;i<params.length;i++) parameterTypes[i] = params[i].getClass();
        return parameterTypes;
    }
}
