package app.Middlewares;

import core.Closure.Next;
import core.Facades.Router;
import core.Middleware.Middleware;
import core.Request.Request;

public class Auth implements Middleware {

    @Override
    public void handle(Request req, Next next) {
        if(req.session().get("user")!=null) next.to(req);
        
        else Router.belong().route("login");
    }
    
}
