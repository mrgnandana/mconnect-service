package com.mrg_mconnect.dataaccess;

import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLBuilder;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;

public class DbClient {

    static DbClient instance = new DbClient();

    DbConfig config;
    Pool dbPool;
    Vertx vertx;

    private DbClient() {
    }

    public static DbClient getInstance() {
        return instance;
    }

    public void setConfig(DbConfig config) {
        this.config = config;
    }

    public DbConfig getConfig() {
        return config;
    }

    public void createPool() {
        if (config != null) {
            try {
                MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                        .setPort(config.getDbPort())
                        .setHost(config.getDbHost())
                        .setDatabase(config.getDbName())
                        .setUser(config.getDbUser())
                        .setPassword(config.getDbUserPassword());

                PoolOptions poolOptions = new PoolOptions()
                        .setMaxSize(5);

                dbPool = MySQLBuilder.pool()
                        .with(poolOptions)
                        .connectingTo(connectOptions)
                        .using(vertx)
                        .build();
            } catch (Exception e) {
                System.out.println(e);
            }

        }

    }

    public void closePool() {
        if (dbPool != null) {
            dbPool.close();
        }
    }

    public Pool getPool() {
        if (dbPool != null) {
            return dbPool;
        } else {
            createPool();
            return dbPool;
        }
    }

    public void configure(Vertx vertx, DbConfig config) {
        this.vertx = vertx;
        setConfig(config);
    }

}
