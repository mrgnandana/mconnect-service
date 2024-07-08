package com.mrg_mconnect.auth;

import com.mrg_mconnect.service_commons.SubRouter;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class AuthServiceSubRoute extends SubRouter{

    public AuthServiceSubRoute(Vertx v, Router r) {
        super(v, r);
    }

    @Override
    public void setup() {
        this.subRouter.route().handler(BodyHandler.create());

        String mountPoint = "/auth";
        subRouter.post(mountPoint + "/token").handler(this::handleToken);
    }

    private void handleToken(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        JsonObject o = new JsonObject();
        o.put("code", 1);
        // o.put("error", ex.getError());
        // o.put("type", ex.getErrorType());
        // o.put("description", ex.getErrorMsg());

        JsonObject ret = new JsonObject();
        ret.put("fault", o);

        ctx.response().setStatusCode(400)
                .putHeader("content-type", "application/json")
                .end(ret.encodePrettily());

        // JsonObject reqObj = ctx.body().asJsonObject();
        // JsonObject msg = EventBusMessageUtil.buildMessage("user.create", reqObj);
        // vertx.eventBus().request(EB_ADDRESS, msg, res -> {
        //     if (res.succeeded()) {

        //         JsonObject msgResult = (JsonObject) res.result().body();

        //         if (msgResult.getBoolean("success")) {
        //             response.putHeader("content-type", "application/json").end(msgResult.getJsonObject("data").encodePrettily());
        //         } else {
        //             ErrorResponse.errorResponse(ctx, msgResult.getJsonObject("fault"), 400);
        //         }
        //     } else {
        //         ErrorResponse.internalError(ctx, res.cause());
        //     }
        // });
    }
    
}
