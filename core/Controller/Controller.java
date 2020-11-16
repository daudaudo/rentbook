package core.Controller;

import core.Request.Request;

public interface Controller {
    core.Response.Response control(Request req);
}
