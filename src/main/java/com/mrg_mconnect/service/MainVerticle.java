package com.mrg_mconnect.service;

import com.mrg_mconnect.auth.AuthVerticle;
import com.mrg_mconnect.company.CompanyVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.print.attribute.standard.Media;

// import io.vertx.core.logging.Logger;
// import io.vertx.core.logging.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mrg_mconnect.dataaccess.DbClient;
import com.mrg_mconnect.dataaccess.DbConfig;
import com.mrg_mconnect.media.MediaVerticle;
import com.mrg_mconnect.service_commons.MediaUtil;

/**
 *
 * @author Nandana
 */

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        // set vertx logger delegate factory to slf4j
        // String logFactory =
        // System.getProperty("org.vertx.logger-delegate-factory-class-name");

        // if (logFactory == null) {
        // System.setProperty("org.vertx.logger-delegate-factory-class-name",
        // SLF4JLogDelegateFactory.class.getName());
        // }
        // System.setProperty("org.vertx.logger-delegate-factory-class-name",
        // SLF4JLogDelegateFactory.class.getName());
        Logger logger = LoggerFactory.getLogger(MainVerticle.class);
        logger.error("Hello from Logback");
        logger.info("getNumber() : {}", 1);

        configureDb();

        startVerticles().andThen(result -> {
            if (result.succeeded()) {
                startPromise.complete();
                //System.out.println("HTTP server started on port " + config().getInteger("port", 8080));
                logger.info("This is a test logging");
            } else {
                logger.error("Start failed");
                startPromise.fail(result.cause());
            }

        });

        MediaUtil.MEDIA_PATH = config().getString("media_base_path", "D://spk//mconnect//");
        

        // vertx.createHttpServer().requestHandler(req -> {
        //   req.response()
        //       .putHeader("content-type", "text/plain")
        //       .end("Hello from Vert.x!");
        // }).listen(config().getInteger("port", 8080), http -> {
        //   if (http.succeeded()) {
        //     startPromise.complete();
        //     System.out.println("HTTP server started on port " + config().getInteger("port", 8080));
        //     logger.info("This is a test logging");
        //   } else {
        //     startPromise.fail(http.cause());
        //   }
        // });
    }

    public void configureDb() {
        DbConfig dbConfig = new DbConfig();
        dbConfig.setDbName(config().getString("db.name", "mconnect-db"));
        dbConfig.setDbHost(config().getString("db.host", "localhost"));
        dbConfig.setDbUser(config().getString("db.user", "root"));
        dbConfig.setDbUserPassword(config().getString("db.user.password", ""));
        dbConfig.setDbPort(config().getInteger("db.port", 3306));
        DbClient.getInstance().configure(vertx, dbConfig);
    }

    private Future<Void> startVerticles() {
        Logger logger = LoggerFactory.getLogger(MainVerticle.class);
        final Promise<Void> promise = Promise.promise();

        // deploy verticles
        VertxOptions vertxOptions = new VertxOptions();
        vertxOptions.setBlockedThreadCheckIntervalUnit(TimeUnit.MINUTES);
        vertxOptions.setBlockedThreadCheckInterval(10);
        vertx = Vertx.vertx(vertxOptions);

        DeploymentOptions options = new DeploymentOptions().setConfig(config());
//         DeploymentOptions options2 = new
//         DeploymentOptions().setConfig(config()).setInstances(1);
        // DeploymentOptions options3 = new
        // DeploymentOptions().setConfig(config()).setWorker(true).setInstances(1);
        List<Future<Void>> list = new ArrayList<>();

        list.add(deployVerticle(vertx, options, RESTMainVerticle.class.getName()));
        list.add(deployVerticle(vertx, options, CompanyVerticle.class.getName()));
        list.add(deployVerticle(vertx, options, AuthVerticle.class.getName()));
        list.add(deployVerticle(vertx, options, MediaVerticle.class.getName()));

        Future.all(list).onComplete(res -> {
            if (res.succeeded()) {
                promise.complete();
            } else {
                promise.fail(res.cause());
            }
        });
        // deploy verticles end

        return promise.future();
    }

    private static Future<Void> deployVerticle(final Vertx vertx, DeploymentOptions options, final String name) {
        Logger logger = LoggerFactory.getLogger(MainVerticle.class);
        final Promise<Void> promise = Promise.promise();
        vertx.deployVerticle(name, options, res -> {
            if (res.failed()) {
                logger.error("Failed to deploy verticle!", name);
                promise.fail(res.cause());
            } else {
                logger.info("{} verticle deployed!", name);
                promise.complete();
            }
        });

        return promise.future();
    }
}
