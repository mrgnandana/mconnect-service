/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrg_mconnect.auth;

import com.mrg_mconnect.manager.AuthManager;
import com.mrg_mconnect.service_commons.MessageHelper;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Nandana
 */
public class AuthVerticle extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(AuthVerticle.class);
    final String EVENT_BUS_ADDRESS = "com.mconnect.auth";
    AuthManager authManager;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        System.out.println("auth verticle starting");
        authManager = new AuthManager();
        vertx.eventBus().consumer(EVENT_BUS_ADDRESS, (message) -> {
            try {
                JsonObject req = (JsonObject) message.body();
                String method = req.getString("method");

                logger.debug("method ---> " + method);

                switch (method) {
                    case "auth.login_request":
                        loginRequest(message, req.getJsonObject("data"));
                        break;

                    default:
                        throw new Exception("Invalid option");
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage());
                MessageHelper.errorReply(message, ex.getMessage(), 1);
            }
        });
        startPromise.complete();
    }

    private void loginRequest(Message<Object> message, JsonObject data) {
        //MessageHelper.errorReply(message, "Test error message", 2);
        System.out.println("Login request auth verticle");
        authManager.requestLogin(data.getString("mobile_no"), data.getString("emp_id"), "", data.getString("device_id")).andThen(res -> {
            if (res.succeeded()) {
                JsonObject resx = new JsonObject()
                        .put("success", true).put("data", res.result());

                MessageHelper.successReply(message, resx);
            } else {
                MessageHelper.errorReply(message, res.cause().getMessage(), 2);
            }

        });
    }

}
