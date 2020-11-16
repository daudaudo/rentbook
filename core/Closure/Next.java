package core.Closure;

import core.Request.Request;

public interface Next {
    void to(Request req);
}
