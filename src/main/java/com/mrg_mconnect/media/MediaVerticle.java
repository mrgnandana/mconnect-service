/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrg_mconnect.media;

import com.mrg_mconnect.manager.MediaManager;
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
public class MediaVerticle extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(MediaVerticle.class);
    final String EVENT_BUS_ADDRESS = "com.mconnect.media";
    MediaManager mediaManager;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        mediaManager = new MediaManager();
        vertx.eventBus().consumer(EVENT_BUS_ADDRESS, (message) -> {
            try {
                JsonObject req = (JsonObject) message.body();
                String method = req.getString("method");

                logger.debug("method  ---> " + method);

                switch (method) {
                    case "media.upload_profile_image":
                        uploadProfileImage(message, req.getJsonObject("data"));
                        break;

                    case "media.get_profile_image":
                        getProfileImage(message, req.getJsonObject("data"));
                        break;

                    case "media.upload_company_icon":
                        uploadCompanyIcon(message, req.getJsonObject("data"));
                        break;

                    case "media.get_company_icon":
                        getCompanyIcon(message, req.getJsonObject("data"));
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

    private void uploadCompanyIcon(Message<Object> message, JsonObject data) {

        mediaManager.uploadCompanyIcon(data.getString("company_id"), data.getString("uploaded_file_name"), data.getString("content_type")).andThen(res -> {
            if (res.succeeded()) {
                MessageHelper.successReply(message, res.result());
            } else {
                MessageHelper.errorReply(message, res.cause().getMessage(), 2);
            }
        });
    }

    private void getCompanyIcon(Message<Object> message, JsonObject data) {

        mediaManager.getCompanyIcon(data.getString("company_id")).andThen(res -> {
            if (res.succeeded()) {
                MessageHelper.successReply(message, res.result());
            } else {
                MessageHelper.errorReply(message, res.cause().getMessage(), 2);
            }
        });
    }

    private void uploadProfileImage(Message<Object> message, JsonObject data) {

        mediaManager.uploadProfileImage(data.getString("user_id"), data.getString("uploaded_file_name"), data.getString("content_type")).andThen(res -> {
            if (res.succeeded()) {
                MessageHelper.successReply(message, res.result());
            } else {
                MessageHelper.errorReply(message, res.cause().getMessage(), 2);
            }
        });
    }

    private void getProfileImage(Message<Object> message, JsonObject data) {

        mediaManager.getProfileImage(data.getString("user_id")).andThen(res -> {
            if (res.succeeded()) {
                MessageHelper.successReply(message, res.result());
            } else {
                MessageHelper.errorReply(message, res.cause().getMessage(), 2);
            }
        });
    }

}
