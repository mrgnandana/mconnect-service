package com.mrg_mconnect.company;

import com.mrg_mconnect.company.manager.CompanyManager;
import com.mrg_mconnect.service_commons.SubRouter;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class CompanyServiceSubRoute extends SubRouter {

    public CompanyServiceSubRoute(Vertx v, Router r) {
        super(v, r);
    }
    final String EVENT_BUS_ADDRESS = "com.mconnect.company";

    @Override
    public void setup() {
        this.subRouter.route().handler(BodyHandler.create());

        String mountPoint = "/company";
        subRouter.post(mountPoint + "/contacts/list").handler(this::handleContactList);
    }

    private void handleContactList(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        JsonObject requestObj = ctx.body().asJsonObject();
        //validate page limit updated time,user_id
        //call bussinres class method
        JsonObject msg = new JsonObject().put("method", "company.contact_list").put("data", requestObj);
        vertx.eventBus().request(EVENT_BUS_ADDRESS, msg, res -> {
            if (res.succeeded()) {
                JsonObject msgResult = (JsonObject) res.result().body();
                if (msgResult.getBoolean("success")) {
                    response.putHeader("content-type", "application/json").end(msgResult.getJsonObject("data").encodePrettily());
                } else {
                    response.setStatusCode(400).end();              }
            } else {
                 response.setStatusCode(400).end();  
            }
        });

        ctx.response().setStatusCode(400)
                .putHeader("content-type", "application/json")
                .end(requestObj.encodePrettily());

    }

}
