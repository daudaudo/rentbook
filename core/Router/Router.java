package core.Router;

import java.util.HashMap;

import core.Container.Application;
import core.Controller.Controller;
import core.Middleware.Middleware;
import core.Request.Request;
import core.Response.Response;

public class Router {
    public Application app;

    private Request request;

    private HashMap<String,Controller> routers = new HashMap<>();

    private HashMap<String,String[]> middlewares = new HashMap<>();

    private String currentRouter;

    private int positionOfJourney;

    public Router(Application app , Request request){
        this.app = app;
        this.request = request;
    }


	public Router set(String name, Controller controller) {
        this.currentRouter = name;
        this.add(name,controller);
        return this;
    }
    
    public void middleware(String... middlewares){
        this.middlewares.put(this.currentRouter,middlewares);
    }

    private void add(String name,Controller controller){
        this.routers.put(name,controller);
    }

    private void dispatchToRouter(String name){
        Response res = this.routers.get(name).control(this.request);
        if(res!=null) res.view();
    }

    public void route(String name){
        if(!this.has(name)) return;

        this.request.setRouterName(name);
        this.positionOfJourney = 0;

        if(this.middlewares.get(name)!=null){
            this.through(this.middlewares.get(name)[0]);
            if(this.middlewares.get(name).length != this.positionOfJourney) return;
        }
        
        this.dispatchToRouter(name);
    }

    public void through(String middleware){
        Middleware mw = (Middleware) this.app.resolve(middleware);
        mw.handle(this.request, (Request req)->{
            this.positionOfJourney++;

            if(this.middlewares.get(req.getRouterName()).length == this.positionOfJourney) return;

            this.through(this.middlewares.get(req.getRouterName())[this.positionOfJourney]);
        });
    }

    public boolean has(String routerName){
        return this.routers.get(routerName)!=null;
    }
}
