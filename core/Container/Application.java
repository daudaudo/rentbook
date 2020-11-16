package core.Container;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Application extends Container {
    private HashMap<String, String> path = new HashMap<>();
    private String[] providers = { 
        "core.Providers.BaseProvider",
        "core.Providers.FacadesProvider",
        "core.Providers.AppProvider",
    };

    public Application(String basePath) {
        this.registerBasePath(this.replaceSlash(basePath));
        this.bindingBase();
        this.bootProviders();
    }

    private void registerBasePath(String basePath) {
        this.path.put("base", basePath);
        this.path.put("core", basePath + "/core");
        this.path.put("app", basePath + "/app");
        this.path.put("env", basePath + "/.env");
        this.path.put("session", basePath + "/.Session");
        this.path.put("resource", basePath + "/resource");
        this.path.put("resource.img", basePath + "/resource/img");
        this.path.put("resource.fxml", basePath + "/resource/fxml");
        this.path.put("resource.css", basePath + "/resource/css");
    }

    public String getPath(String name) {
        return this.path.get(name);
    }

    private String replaceSlash(String name) {
        return name.replaceAll("\\\\", "/");
    }

    private void bindingBase() {
        this.binding("core.Container.Application", this);
        this.bindingProviders();
    }

    public Object getInstance(String name) {
        name = this.isAlias(name) ? this.getAlias(name) : name;
        return this.instances.get(name);
    }

    private void bindingProviders() {
        for (String provider : this.providers)
            this.resolve(provider);
    }

    private void bootProviders() {
        for (String provider : this.providers) {
            try {
                this.getInstance(provider).getClass().getMethod("register").invoke(this.getInstance(provider));
                this.getInstance(provider).getClass().getMethod("boot").invoke(this.getInstance(provider));
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException| NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    public void close(){
        ((core.Session.Session) this.getInstance("session")).writeSessionToFile();;
    }
}
