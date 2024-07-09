package com.mrg_mconnect.auth;

import java.util.Map;

import com.mrg_mconnect.manager.AuthManager;
import com.mrg_mconnect.service_commons.ErrorResponse;
import com.mrg_mconnect.service_commons.SubRouter;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.authentication.Credentials;
import io.vertx.ext.auth.authentication.TokenCredentials;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;

/**
 *
 * @author Nandana
 */
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
        subRouter.get(mountPoint + "/token-verify").handler(this::handleTokenVerify);
        subRouter.post(mountPoint + "/refresh").handler(this::handleRefresh);
    }

    private void handleToken(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        try {
            // validate session id and otp provided
            JsonObject requestObj = ctx.body().asJsonObject();
            // validate mobile_no/emp_id, device id
            String sessionId = "";
            if (requestObj.getString("sid") != null) {
                sessionId = requestObj.getString("sid").trim();
            }

            String otp = "";
            if (requestObj.getString("otp") != null) {
                otp = requestObj.getString("otp").trim();
            }

            if (sessionId.isEmpty() || otp.isEmpty()) {
                throw new Exception("Invalid request parameters");
            }

            // handover to manamger
            JsonObject reqData = new JsonObject();
            reqData.put("otp", otp);
            reqData.put("sid", sessionId);

            JsonObject msg = new JsonObject().put("method", "auth.token").put("data", reqData);
            vertx.eventBus().request(EVENT_BUS_ADDRESS, msg, res -> {
                if (res.succeeded()) {
                    JsonObject msgResult = (JsonObject) res.result().body();
                    if (msgResult.getBoolean("success")) {
                        response.putHeader("content-type", "application/json")
                                .end(msgResult.getJsonObject("data").encodePrettily());
                    } else {
                        ErrorResponse.getBuilder()
                                .response(response)
                                .statusCode(400)
                                .message("Token request failed")
                                .errorNo(400)
                                .build();
                    }
                } else {

                    ErrorResponse.getBuilder()
                            .response(response)
                            .statusCode(400)
                            .message(res.cause().getMessage())
                            .errorNo(400)
                            .build();
                }
            });

        } catch (Exception e) {
            ErrorResponse.getBuilder()
                    .response(response)
                    .statusCode(400)
                    .message(e.getMessage())
                    .errorNo(400)
                    .build();
        }

    }

    private void handleLoginRequest(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        try {

            JsonObject requestObj = ctx.body().asJsonObject();
            // validate mobile_no/emp_id, device id
            String deviceId = "";
            if (requestObj.getString("device_id") != null) {
                deviceId = requestObj.getString("device_id").trim();
            }

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

            if (deviceId.isEmpty()) {
                throw new Exception("Invalid device id");
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
                        ErrorResponse.getBuilder()
                                .response(response)
                                .statusCode(400)
                                .message("Login request failed")
                                .errorNo(400)
                                .build();
                    }
                } else {

                    ErrorResponse.getBuilder()
                            .response(response)
                            .statusCode(400)
                            .message(res.cause().getMessage())
                            .errorNo(400)
                            .build();
                }
            });

        } catch (Exception e) {
            ErrorResponse.getBuilder()
                    .response(response)
                    .statusCode(400)
                    .message(e.getMessage())
                    .errorNo(400)
                    .build();

        }

    }

    private void handleRefresh(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        try {
            // check refresh token
            JsonObject requestObj = ctx.body().asJsonObject();
            String authToken = requestObj.getString("refresh_token");

            if (!(authToken != null && !authToken.isEmpty())) {
                throw new Exception("Invalid request parameters");
            }

            // verify token
            AuthManager manager = new AuthManager(vertx);
            manager.refresh(authToken.replace("Bearer", "" ).trim()).andThen(res ->{
                if(res.succeeded()) {
                    response.putHeader("content-type", "application/json")
                    .end(res.result().encodePrettily());
                }else{
                    ErrorResponse.getBuilder()
                    .response(response)
                    .statusCode(401)
                    .message("Invalid token")
                    .errorNo(401)
                    .build();
                }
            });

        } catch (Exception e) {
            ErrorResponse.getBuilder()
                    .response(response)
                    .statusCode(400)
                    .message(e.getMessage())
                    .errorNo(400)
                    .build();

        }

    }

    private void handleTokenVerify(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        try {
            // check header
            String authToken = ctx.request().getHeader("Authorization");

            if (!(authToken != null && !authToken.isEmpty())) {
                throw new Exception("Invalid request parameters");
            }

            // verify token
            AuthManager manager = new AuthManager(vertx);
            manager.verify(authToken.replace("Bearer", "" ).trim()).andThen(res ->{
                if(res.succeeded()) {
                    response.putHeader("content-type", "application/json")
                    .end(res.result().encodePrettily());
                }else{
                    ErrorResponse.getBuilder()
                    .response(response)
                    .statusCode(401)
                    .message("Invalid token")
                    .errorNo(401)
                    .build();
                }
            });

        } catch (Exception e) {
            ErrorResponse.getBuilder()
                    .response(response)
                    .statusCode(400)
                    .message(e.getMessage())
                    .errorNo(400)
                    .build();

        }

    }

}
