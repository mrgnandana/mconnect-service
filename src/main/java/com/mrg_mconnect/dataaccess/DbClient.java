package com.mrg_mconnect.dataaccess;

import io.vertx.mysqlclient.MySQLBuilder;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlClient;

public class DbClient {

    static DbClient instance = new DbClient();

    DbConfig config;
    Pool dbPool;

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
            MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                    .setPort(config.getDbPort())
                    .setHost(config.dbHost)
                    .setDatabase(config.dbName)
                    .setUser(config.dbUser)
                    .setPassword(config.dbUserPassword);

            PoolOptions poolOptions = new PoolOptions()
                    .setMaxSize(5);

            dbPool = MySQLBuilder.pool()
                    .with(poolOptions)
                    .connectingTo(connectOptions)
                    .build();
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
    
    
}
