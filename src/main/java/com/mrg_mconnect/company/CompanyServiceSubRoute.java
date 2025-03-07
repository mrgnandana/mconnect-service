package com.mrg_mconnect.company;

import com.mrg_mconnect.manager.AuthManager;
import com.mrg_mconnect.manager.CompanyManager;
import com.mrg_mconnect.service_commons.ErrorResponse;
import com.mrg_mconnect.service_commons.SubRouter;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;

public class CompanyServiceSubRoute extends SubRouter {

    public CompanyServiceSubRoute(Vertx v, Router r) {
        super(v, r);
    }

    final String EVENT_BUS_ADDRESS = "com.mconnect.company";

    @Override
    public void setup() {
        this.subRouter.route().handler(BodyHandler.create());

        String mountPoint = "/company";
        //athenticate 
        //to do: uncomment to enable jwt token check
        subRouter.route(mountPoint + "/*").handler(JWTAuthHandler.create(new AuthManager(vertx).getAuthProvider()));

        // company
        subRouter.get(mountPoint + "/:cid").handler(this::handleGetCompanyDetails);

        // contact
        subRouter.post(mountPoint + "/contacts/list").handler(this::handleContactList);

        // structure
        subRouter.get(mountPoint + "/structure/list").handler(this::handleCompanyStructureList);

        // position
        subRouter.post(mountPoint + "/positions/").handler(this::handleCompanyCreatePosition);
        subRouter.put(mountPoint + "/positions/:pid").handler(this::handleCompanyUpdatePosition);
        subRouter.delete(mountPoint + "/positions/:pid").handler(this::handleCompanyDeletePosition);
        subRouter.get(mountPoint + "/positions/:pid").handler(this::handleCompanyGetPosition);
        subRouter.post(mountPoint + "/positions/list").handler(this::handleCompanyPositionList);

    }

    private void handleContactList(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        try {
            JsonObject requestObj = ctx.body().asJsonObject();
            String userId = "";
            if (requestObj.getString("user_id") != null) {
                userId = requestObj.getString("user_id").trim();
            }

            if (userId.isEmpty()) {
                throw new Exception("User id is required");
            }

            int page = 0;
            if (requestObj.getInteger("page") != null) {
                page = requestObj.getInteger("page");
            }

            int limit = 20;
            if (requestObj.getInteger("limit") != null) {
                limit = requestObj.getInteger("limit");
            }

            long updateTime = 0;
            if (requestObj.getLong("update_time") != null) {
                updateTime = requestObj.getLong("update_time");
            }

            requestObj.put("user_id", userId);
            requestObj.put("page", page);
            requestObj.put("limit", limit);
            requestObj.put("update_time", updateTime);
            JsonObject msg = new JsonObject().put("method", "company.contact_list").put("data", requestObj);
            vertx.eventBus().request(EVENT_BUS_ADDRESS, msg, res -> {
                if (res.succeeded()) {
                    JsonObject msgResult = (JsonObject) res.result().body();
                    if (msgResult.getBoolean("success")) {
                        response.putHeader("content-type", "application/json")
                                .end(msgResult.getJsonArray("data").encodePrettily());
                    } else {
                        ErrorResponse.getBuilder()
                                .response(response)
                                .statusCode(400)
                                .message(msgResult.getString("message"))
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

    private void handleCompanyStructureList(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        try {
            JsonObject requestObj = new JsonObject();
            JsonObject msg = new JsonObject().put("method", "company.structure_list").put("data", requestObj);
            vertx.eventBus().request(EVENT_BUS_ADDRESS, msg, res -> {
                if (res.succeeded()) {
                    JsonObject msgResult = (JsonObject) res.result().body();
                    if (msgResult.getBoolean("success")) {
                        response.putHeader("content-type", "application/json")
                                .end(msgResult.getJsonArray("data").encodePrettily());
                    } else {
                        ErrorResponse.getBuilder()
                                .response(response)
                                .statusCode(400)
                                .message(msgResult.getString("message"))
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

    //company positions 
    private void handleCompanyCreatePosition(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        try {
            JsonObject requestObj = ctx.body().asJsonObject();
            String positionName = "";
            if (requestObj.getString("position_name") != null) {
                positionName = requestObj.getString("position_name").trim();
            }

            if (positionName.isEmpty()) {
                throw new Exception("Position Name is required");
            }

            requestObj.put("position_name", positionName);
            JsonObject msg = new JsonObject().put("method", "company.position_create").put("data", requestObj);
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
                                .message(msgResult.getString("message"))
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

    private void handleCompanyUpdatePosition(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        try {
            JsonObject requestObj = ctx.body().asJsonObject();
            String positionId = ctx.request().getParam("pid").trim();;

            if (positionId == null || positionId.isEmpty()) {
                throw new Exception("Position Id is required");
            }

            String positionName = "";
            if (requestObj.getString("position_name") != null) {
                positionName = requestObj.getString("position_name").trim();
            }

            if (positionName.isEmpty()) {
                throw new Exception("Position Name is required");
            }

            requestObj.put("position_id", Integer.parseInt(positionId));
            requestObj.put("position_name", positionName);

            JsonObject msg = new JsonObject().put("method", "company.position_update").put("data", requestObj);
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
                                .message(msgResult.getString("message"))
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

    private void handleCompanyDeletePosition(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        try {
            JsonObject requestObj = new JsonObject();
            String positionId = ctx.request().getParam("pid").trim();;

            if (positionId == null || positionId.isEmpty()) {
                throw new Exception("Position Id is required");
            }

            requestObj.put("position_id", Integer.parseInt(positionId));
            JsonObject msg = new JsonObject().put("method", "company.position_delete").put("data", requestObj);
            vertx.eventBus().request(EVENT_BUS_ADDRESS, msg, res -> {
                if (res.succeeded()) {
                    JsonObject msgResult = (JsonObject) res.result().body();
                    if (msgResult.getBoolean("success")) {
                        response.putHeader("content-type", "application/json").setStatusCode(204)
                                .end();
                    } else {
                        ErrorResponse.getBuilder()
                                .response(response)
                                .statusCode(400)
                                .message(msgResult.getString("message"))
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

    private void handleCompanyGetPosition(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        try {
            JsonObject requestObj = new JsonObject();
            String positionId = ctx.request().getParam("pid").trim();;

            if (positionId == null || positionId.isEmpty()) {
                throw new Exception("Position Id is required");
            }

            requestObj.put("position_id", Integer.parseInt(positionId));
            JsonObject msg = new JsonObject().put("method", "company.position_get").put("data", requestObj);
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
                                .message(msgResult.getString("message"))
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

    private void handleCompanyPositionList(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        try {
            JsonObject requestObj = new JsonObject();
            JsonObject msg = new JsonObject().put("method", "company.position_list").put("data", requestObj);
            vertx.eventBus().request(EVENT_BUS_ADDRESS, msg, res -> {
                if (res.succeeded()) {
                    JsonObject msgResult = (JsonObject) res.result().body();
                    if (msgResult.getBoolean("success")) {
                        response.putHeader("content-type", "application/json")
                                .end(msgResult.getJsonArray("data").encodePrettily());
                    } else {
                        ErrorResponse.getBuilder()
                                .response(response)
                                .statusCode(400)
                                .message(msgResult.getString("message"))
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

    private void handleGetCompanyDetails(RoutingContext ctx) {
        HttpServerResponse response = ctx.response();

        try {
            JsonObject requestObj = new JsonObject();

            String companyId = ctx.request().getParam("cid").trim();

            if (companyId == null || companyId.isEmpty()) {
                throw new Exception("Company id is required");
            }

            requestObj.put("company_id", companyId);
            JsonObject msg = new JsonObject().put("method", "company.details").put("data", requestObj);
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
                                .message(msgResult.getString("message"))
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
}
