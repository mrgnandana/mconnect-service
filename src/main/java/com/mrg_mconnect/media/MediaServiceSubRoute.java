package com.mrg_mconnect.media;

import java.io.File;

import com.mrg_mconnect.service_commons.ErrorResponse;
import com.mrg_mconnect.service_commons.MediaUtil;
import com.mrg_mconnect.service_commons.SubRouter;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class MediaServiceSubRoute extends SubRouter {

    public MediaServiceSubRoute(Vertx v, Router r) {
        super(v, r);
    }

    final String EVENT_BUS_ADDRESS = "com.mconnect.media";
    DeliveryOptions deliveryOptions;

    @Override
    public void setup() {
        this.subRouter.route().handler(BodyHandler.create());
        deliveryOptions = new DeliveryOptions();
        deliveryOptions.setSendTimeout(1000 * 600);

        String mountPoint = "/media";

        subRouter.post(mountPoint + "/images/profiles/:uid/").handler(this::handleProfileImageUpload);
        subRouter.get(mountPoint + "/images/profiles/:uid/").handler(this::handleGetProfileImage);

    }

    private void handleProfileImageUpload(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        try {
            JsonObject requestObj = new JsonObject();
            String uploadedFileName = null;
            String contentType = null;
            for (FileUpload f : ctx.fileUploads()) {
                uploadedFileName = f.uploadedFileName();
                contentType = f.contentType();
                break;
            }
            final String uploadedFileNameValue = uploadedFileName;

            String userId = ctx.request().getParam("uid").trim();

            if (userId == null || userId.isEmpty()) {
                throw new Exception("User id is required");
            }

            requestObj.put("user_id", userId);
            requestObj.put("uploaded_file_name", uploadedFileName);
            requestObj.put("content_type", contentType);

            JsonObject msg = new JsonObject().put("method", "media.upload_profile_image").put("data", requestObj);
            vertx.eventBus().request(EVENT_BUS_ADDRESS, msg, deliveryOptions, res -> {
                if (res.succeeded()) {
                    JsonObject msgResult = (JsonObject) res.result().body();
                    if (msgResult.getBoolean("success")) {
                        cleanFile(ctx, uploadedFileNameValue);
                        response.putHeader("content-type", "application/json")
                                .end(msgResult.getJsonObject("data").encodePrettily());
                    } else {
                        cleanFile(ctx, uploadedFileNameValue);
                        ErrorResponse.getBuilder()
                                .response(response)
                                .statusCode(400)
                                .message(res.cause().getMessage())
                                .errorNo(400)
                                .build();
                    }
                } else {
                    cleanFile(ctx, uploadedFileNameValue);
                    ErrorResponse.getBuilder()
                            .response(response)
                            .statusCode(400)
                            .message(res.cause().getMessage())
                            .errorNo(400)
                            .build();
                }
            });
        } catch (Exception ex) {
            ErrorResponse.getBuilder()
                    .response(response)
                    .statusCode(400)
                    .message(ex.getMessage())
                    .errorNo(400)
                    .build();
        }

    }

    private void handleGetProfileImage(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        try {
            JsonObject requestObj = new JsonObject();
            String userId = ctx.request().getParam("uid").trim();

            if (userId == null || userId.isEmpty()) {
                throw new Exception("User id is required");
            }

            requestObj.put("user_id", userId);
            JsonObject msg = new JsonObject().put("method", "media.get_profile_image").put("data", requestObj);
            vertx.eventBus().request(EVENT_BUS_ADDRESS, msg, res -> {
                if (res.succeeded()) {
                    JsonObject msgResult = (JsonObject) res.result().body();
                    if (msgResult.getBoolean("success")) {
                        String imagepath = MediaUtil.MEDIA_PATH
                                + msgResult.getJsonObject("data").getString("image_location");
                        File file = new File(imagepath);
                        if (file.isFile()) {
                            response.sendFile(file.getAbsolutePath());
                        } else {
                            JsonObject errorRresponseData = new ErrorResponse.Builder()
                                    .message("File not found")
                                    .errorNo(400).build();
                            ctx.response().setStatusCode(400)
                                    .putHeader("content-type", "application/json")
                                    .end(errorRresponseData.encodePrettily());
                        }
                    } else {
                        ErrorResponse.getBuilder()
                                .response(response)
                                .statusCode(400)
                                .message(res.cause().getMessage())
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
        } catch (Exception ex) {
            ErrorResponse.getBuilder()
                    .response(response)
                    .statusCode(400)
                    .message(ex.getMessage())
                    .errorNo(400)
                    .build();
        }

    }

    private void cleanFile(RoutingContext ctx, String uploadedFileNameValue) {
        if (uploadedFileNameValue != null) {
            ctx.vertx().fileSystem().delete(uploadedFileNameValue, handleResult -> {
                if (handleResult.succeeded()) {
                    System.out.println("clean file......");
                } else {
                    System.out.println(handleResult.cause().getMessage());
                    System.out.println("clean error......");
                }
            });
        }
    }
}
