/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrg_mconnect.company;

import com.mrg_mconnect.company.manager.CompanyManager;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Yohan *
 */
public class CompanyVerticle extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(CompanyVerticle.class);
    final String EVENT_BUS_ADDRESS = "com.mconnect.company";
    CompanyManager companyManager;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        System.out.println("company verticle started");
        companyManager = new CompanyManager();
        vertx.eventBus().consumer(EVENT_BUS_ADDRESS, (message) -> {
            try {
                JsonObject req = (JsonObject) message.body();
                String method = req.getString("method");

                logger.debug("method  ---> " + method);

                switch (method) {
                    case "company.contact_list":
                        getContactList(message);
                        break;

                    default:
                        throw new Exception("Invalid option");
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage());

                message.reply("");
            }
        });
        System.out.println("company verticle start success");
        startPromise.complete();
    }

    private void getContactList(Message<Object> message) {
        companyManager.getContactList(1, 10, "u1", 0).andThen(res -> {
            JsonObject rest = new JsonObject()
                    .put("success", true).put("data", res.result());

            message.reply(rest);
        });
    }

}
