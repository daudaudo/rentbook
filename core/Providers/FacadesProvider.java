package core.Providers;

import core.Container.Application;
import core.Facades.Facades;

public class FacadesProvider implements ServiceProviders {
    private Application app;
    public FacadesProvider(Application app){
        this.app=app;
    }

    @Override
    public void register() {

    }

    @Override
    public void boot() {
        Facades.setApplication(this.app);
    }
    
}
