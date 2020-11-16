package core.Providers;

import core.Container.Application;
import core.Database.DB;
import core.Form.FormManager;
import core.Session.Session;
import app.Routes;

public class AppProvider implements ServiceProviders {
    private Application app;

    public AppProvider(Application app) {
        this.app = app;
    }

    @Override
    public void register() {
        this.bindingSession();
        this.app.resolve("request");
        this.app.resolve("router");
        this.app.resolve("fxmlloader");
        this.app.resolve("fm");
    }

    @Override
    public void boot() {
        core.Models.Models.db = (DB) this.app.resolve("db");
        
        this.registerRouter();
        this.showForms();
    }
    
    private void bindingSession(){
        if(Session.getSessionFromFile(this.app)!=null) {
            Session session = Session.getSessionFromFile(this.app);
            this.app.binding("session", session);
            session.setApplication(this.app);
            return;
        }
        this.app.resolve("session");
    }

    private void registerRouter(){
        Routes.register();
    }

    private void showForms(){
        FormManager fm = (FormManager) this.app.resolve("fm");

        core.Form.Form.boot = ()->{
            fm.binding("main", new core.View.View());
            fm.open("main");
            ((core.Router.Router) this.app.make("router")).route("home");
        };

        javafx.application.Application.launch(core.Form.Form.class);
    }
}
