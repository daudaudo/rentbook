package core.Providers;

import java.util.HashMap;

import core.Container.Application;

public class BaseProvider implements ServiceProviders {
    private Application app;
    private HashMap<String,String> aliases = new HashMap<>();

    public BaseProvider(Application app){
        this.app=app;
    }

    @Override
    public void register() {
        this.registerAliases();
        this.registerAliasesFromConfig();
        this.bindingAliases();
    }

    @Override
    public void boot() {
        this.app.resolve("enviroment");
        this.app.resolve("db");
    }

    public void registerAliases(){
        this.aliases.put("app","core.Container.Application");
        this.aliases.put("enviroment","core.Enviroment.Enviroment");
        this.aliases.put("db","core.Database.DB");
        this.aliases.put("session","core.Session.Session");
        this.aliases.put("request","core.Request.Request");
        this.aliases.put("router","core.Router.Router");
        this.aliases.put("fxmlloader","core.Form.LoaderFXML");
        this.aliases.put("fm","core.Form.FormManager");
    }

    private void bindingAliases(){
        this.aliases.forEach((key,value)->{
            this.app.setAlias(key, value);
        });
    }

    private void registerAliasesFromConfig(){

        config.Middleware.registerAliasMiddleware();
        config.Middleware.middlewares.forEach((String alias,String obj)->{
            this.aliases.put(alias,obj);
        });

        config.Provider.registerAliasProvider();
        config.Provider.providers.forEach((alias,obj)->{
            this.aliases.put(alias,obj);
        });
    }
    
    
}
