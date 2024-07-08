package com.mrg_mconnect.service;

import com.mrg_mconnect.auth.AuthServiceSubRoute;
import com.mrg_mconnect.company.CompanyServiceSubRoute;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mrg_mconnect.service_commons.SubRouter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class RESTMainVerticle extends AbstractVerticle {

    private final Logger logger = LoggerFactory.getLogger(RESTMainVerticle.class);
    // ServiceDiscovery discovery;
    Router router;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        // uncaught exceptions
        vertx.exceptionHandler(new Handler<Throwable>() {
            @Override
            public void handle(Throwable event) {
                // do what you meant to do on uncaught exception, e.g.:
                logger.error(event + " throws exception: " + event.getStackTrace());
            }
        });

        // init router
        this.router = Router.router(vertx);
        handleCors();

        router.route().handler(BodyHandler.create());

        router.route("/*").handler(StaticHandler.create());
        router.get("/health").handler(this::handleHealthCheck);

        SubRouter authService = new AuthServiceSubRoute(vertx, router);
        authService.setup();
        router.route().subRouter(authService.getSubRouter());

        SubRouter companyService = new CompanyServiceSubRoute(vertx, router);
        companyService.setup();
        router.route().subRouter(companyService.getSubRouter());

        // String discoveryAnounceAddress = config().getString("discovery.address",
        // "base-services");
        // String discoveryName = config().getString("discovery.name", "base");
        // //init service discovery instance
        // discovery = ServiceDiscovery.create(vertx,
        // new ServiceDiscoveryOptions()
        // .setAnnounceAddress(discoveryAnounceAddress)
        // .setName(discoveryName)
        // .setBackendConfiguration(config()));
        //String serviceHost = config().getString("service.host", "localhost");
        int port = config().getInteger("service.port", 8080);

        vertx.createHttpServer().requestHandler(router).listen(port, http -> {
            if (http.succeeded()) {
                logger.info("REST service started at " + port);
                startPromise.complete();
                // String serviceName = "sample";
                // String serviceRoot = "/";
                // String apiName = "sample";
                // publishHttpEndpoint(serviceHost, port, serviceName, serviceRoot, apiName);
            } else {
                startPromise.fail(http.cause());
            }
        });

    }

    private void handleCors() {
        // cors handler start
        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add("x-requested-with");
        allowedHeaders.add("Access-Control-Allow-Origin");
        allowedHeaders.add("origin");
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("accept");
        allowedHeaders.add("X-PINGARUNER");
        allowedHeaders.add("Authorization");
        allowedHeaders.add("userId");

        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.OPTIONS);
        allowedMethods.add(HttpMethod.DELETE);
        allowedMethods.add(HttpMethod.PATCH);
        allowedMethods.add(HttpMethod.PUT);
        router.route().handler(CorsHandler.create("*").allowedHeaders(allowedHeaders).allowedMethods(allowedMethods));
//         cors handler end
    }

    // private void publishHttpEndpoint(String serviceHost, int servicePort, String
    // serviceName, String serviceRoot, String apiName) {
    // //discovery record
    // Record record = HttpEndpoint.createRecord(serviceName, serviceHost,
    // servicePort, serviceRoot, new JsonObject().put("api.name", apiName));
    // //publish the service
    // discovery.publish(record, ar -> {
    // if (ar.succeeded()) {
    // logger.info("\"" + record.getName() + "\" successfully published!");
    // //Record publishedRecord = ar.result();
    // } else {
    // // publication failed
    // logger.error("publishing micro service api failed. name: " + apiName);
    // }
    // discovery.close();
    // });
    // }
    private void handleHealthCheck(RoutingContext ctx) {
        try {
            JsonObject o = new JsonObject();
            o.put("time_stamp", Instant.now().getEpochSecond());
            HttpServerResponse response = ctx.response();
            response.putHeader("content-type", "application/json").end(o.encodePrettily());

        } catch (Exception e) {
            // ServerFailedException ex = new ServerFailedException(e.getMessage());
            // ctx.response().setStatusCode(400)
            // .putHeader("content-type", "application/json")
            // .end(ErrorResponse.getFaultResponseContent(ex).encode());
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
        }
    }

}
