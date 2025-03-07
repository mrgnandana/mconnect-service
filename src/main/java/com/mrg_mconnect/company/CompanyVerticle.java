/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrg_mconnect.company;

import com.mrg_mconnect.manager.CompanyManager;
import com.mrg_mconnect.service_commons.MessageHelper;

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
        companyManager = new CompanyManager();
        vertx.eventBus().consumer(EVENT_BUS_ADDRESS, (message) -> {
            try {
                JsonObject req = (JsonObject) message.body();
                String method = req.getString("method");

                logger.debug("method  ---> " + method);

                switch (method) {
                    case "company.contact_list":
                        getContactList(message, req.getJsonObject("data"));
                        break;

                    case "company.structure_list":
                        getStructureList(message, req.getJsonObject("data"));
                        break;

                    case "company.position_list":
                        getPositionList(message, req.getJsonObject("data"));
                        break;

                    case "company.position_create":
                        createCompanyPosition(message, req.getJsonObject("data"));
                        break;

                    case "company.position_update":
                        updateCompanyPosition(message, req.getJsonObject("data"));
                        break;

                    case "company.position_delete":
                        deleteCompanyPosition(message, req.getJsonObject("data"));
                        break;

                    case "company.position_get":
                        getCompanyPosition(message, req.getJsonObject("data"));
                        break;

                    case "company.details":
                        getCompanyDetails(message, req.getJsonObject("data"));
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

    private void getContactList(Message<Object> message, JsonObject data) {

        companyManager.getContactList(data.getInteger("page"), data.getInteger("limit"), data.getString("user_id"), data.getLong("update_time")).andThen(res -> {
            if (res.succeeded()) {
                MessageHelper.successReply(message, res.result());
            } else {
                MessageHelper.errorReply(message, res.cause().getMessage(), 2);
            }
        });
    }

    private void getStructureList(Message<Object> message, JsonObject data) {

        companyManager.getStructureList().andThen(res -> {
            if (res.succeeded()) {
                MessageHelper.successReply(message, res.result());
            } else {
                MessageHelper.errorReply(message, res.cause().getMessage(), 2);
            }
        });
    }

    private void getPositionList(Message<Object> message, JsonObject data) {

        companyManager.getPositionList().andThen(res -> {
            if (res.succeeded()) {
                MessageHelper.successReply(message, res.result());
            } else {
                MessageHelper.errorReply(message,res.cause().getMessage(), 2);
            }
        });
    }

    private void createCompanyPosition(Message<Object> message, JsonObject data) {

        companyManager.createCompanyPosition(data.getString("position_name")).andThen(res -> {
            if (res.succeeded()) {
                MessageHelper.successReply(message, res.result());
            } else {
                MessageHelper.errorReply(message, res.cause().getMessage(), 2);
            }
        });
    }

    private void updateCompanyPosition(Message<Object> message, JsonObject data) {

        companyManager.updateCompanyPosition(data.getInteger("position_id"),data.getString("position_name")).andThen(res -> {
            if (res.succeeded()) {
                MessageHelper.successReply(message, res.result());
            } else {
                MessageHelper.errorReply(message, res.cause().getMessage(), 2);
            }
        });
    }

    private void deleteCompanyPosition(Message<Object> message, JsonObject data) {

        companyManager.deleteCompanyPosition(data.getInteger("position_id")).andThen(res -> {
            if (res.succeeded()) {
                MessageHelper.successReply(message, res.result());
            } else {
                MessageHelper.errorReply(message, res.cause().getMessage(), 2);
            }
        });
    }

    private void getCompanyPosition(Message<Object> message, JsonObject data) {

        companyManager.getCompanyPosition(data.getInteger("position_id")).andThen(res -> {
            if (res.succeeded()) {
                MessageHelper.successReply(message, res.result());
            } else {
                MessageHelper.errorReply(message, res.cause().getMessage(), 2);
            }
        });
    }

    private void getCompanyDetails(Message<Object> message, JsonObject data) {

        companyManager.getCompanyDetails(data.getString("company_id")).andThen(res -> {
            if (res.succeeded()) {
                MessageHelper.successReply(message, res.result());
            } else {
                MessageHelper.errorReply(message, res.cause().getMessage(), 2);
            }
        });
    }

}
