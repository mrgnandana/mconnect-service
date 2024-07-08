package com.mrg_mconnect.dataaccess;

public class DbClient {
    static DbClient instance = new DbClient();

    DbConfig config;

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
}
