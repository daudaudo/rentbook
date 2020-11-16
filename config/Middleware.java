package config;

import java.util.HashMap;

public class Middleware {
    public static HashMap<String,String> middlewares = new HashMap<>();

    public static void registerAliasMiddleware(){
        middlewares.put("auth", app.Middlewares.Auth.class.getName());
        // middlewares.put("maintenance", app.Middlewares.Maintenance.class.getName());
    }
}
