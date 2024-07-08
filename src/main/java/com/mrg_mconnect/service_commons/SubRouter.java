package com.mrg_mconnect.service_commons;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public abstract class SubRouter {
    protected Vertx vertx;
    protected Router router;
    protected Router subRouter;

    public SubRouter(Vertx v, Router r){
        this.vertx = v;
        this.router = r;
        this.subRouter = Router.router(vertx);
    }

    public abstract void setup();

    public Router getSubRouter(){
        return subRouter;
    }
}
