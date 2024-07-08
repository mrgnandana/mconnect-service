package com.mrg_mconnect.auth;

import com.mrg_mconnect.service_commons.ErrorResponse;
import com.mrg_mconnect.service_commons.SubRouter;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class AuthServiceSubRoute extends SubRouter {

    final String EVENT_BUS_ADDRESS = "com.mconnect.auth";

    public AuthServiceSubRoute(Vertx v, Router r) {
        super(v, r);
    }

    @Override
    public void setup() {
        this.subRouter.route().handler(BodyHandler.create());

        String mountPoint = "/auth";
        subRouter.post(mountPoint + "/token").handler(this::handleToken);
        subRouter.post(mountPoint + "/login-request").handler(this::handleLoginRequest);
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
        // if (res.succeeded()) {

        // JsonObject msgResult = (JsonObject) res.result().body();

        // if (msgResult.getBoolean("success")) {
        // response.putHeader("content-type",
        // "application/json").end(msgResult.getJsonObject("data").encodePrettily());
        // } else {
        // ErrorResponse.errorResponse(ctx, msgResult.getJsonObject("fault"), 400);
        // }
        // } else {
        // ErrorResponse.internalError(ctx, res.cause());
        // }
        // });
    }

    private void handleLoginRequest(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        try {

            JsonObject requestObj = ctx.body().asJsonObject();
            // validate mobile_no/emp_id
            String mobileNo = "";
            if (requestObj.getString("mobile_no") != null) {
                mobileNo = requestObj.getString("mobile_no").trim();
            }

            String empId = "";
            if (requestObj.getString("emp_id") != null) {
                empId = requestObj.getString("emp_id").trim();
            }

            if (mobileNo.isEmpty() && empId.isEmpty()) {
                throw new Exception("Mobile no or emp id is required");
            }

            // call bussinres class method
            requestObj.put("mobile_no", mobileNo);
            requestObj.put("emp_id", empId);

            JsonObject msg = new JsonObject().put("method", "auth.login_request").put("data", requestObj);
            vertx.eventBus().request(EVENT_BUS_ADDRESS, msg, res -> {
                if (res.succeeded()) {
                    JsonObject msgResult = (JsonObject) res.result().body();
                    if (msgResult.getBoolean("success")) {
                        response.putHeader("content-type", "application/json")
                                .end(msgResult.getJsonObject("data").encodePrettily());
                    } else {
                        JsonObject errorRresponseData = new ErrorResponse.Builder().message("Login request failed")
                                .errorNo(400).build();
                        response.setStatusCode(400).end(errorRresponseData.encodePrettily());
                    }
                } else {
                    JsonObject errorRresponseData = new ErrorResponse.Builder().message(res.cause().getMessage())
                            .errorNo(400).build();
                    response.setStatusCode(400).end(errorRresponseData.encodePrettily());
                }
            });

        } catch (Exception e) {
            JsonObject errorRresponseData = new ErrorResponse.Builder().message(e.getMessage()).errorNo(400).build();
            response.setStatusCode(400).end(errorRresponseData.encodePrettily());
        }

    }

}
