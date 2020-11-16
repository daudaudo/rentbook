package config;

import java.util.HashMap;

public class Provider {
    public static HashMap<String,String> providers = new HashMap<>();

    public static void registerAliasProvider(){
        providers.put("auth", app.Middlewares.Auth.class.getName());
        // providers.put("maintenance", app.Middlewares.Maintenance.class.getName());
    }
}
