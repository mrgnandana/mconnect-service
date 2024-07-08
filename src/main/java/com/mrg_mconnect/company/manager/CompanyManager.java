/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrg_mconnect.company.manager;

import com.mrg_mconnect.dataaccess.DbClient;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;

/**
 *
 * @author Yohan
 */
public class CompanyManager {
    
    DbClient dbClient;
    
    public CompanyManager() {
        dbClient = DbClient.getInstance();
    }
    
    public Future<JsonArray> getContactList(int page, int limit, String userId, long lastUpdatedAt) {
        Promise<JsonArray> result = Promise.promise();
        dbClient.getPool().getConnection(con -> {
            con.result().query("select * from com_employee").execute().onComplete(res -> {
                System.out.println(res.result());
                System.out.println(res.result().size());
                result.complete(new JsonArray());
            });
        });
        return result.future();
    }
}
