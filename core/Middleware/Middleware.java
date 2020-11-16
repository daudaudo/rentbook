package core.Middleware;

import core.Closure.Next;
import core.Request.Request;

public interface Middleware {
    void handle(Request req , Next next);
}
