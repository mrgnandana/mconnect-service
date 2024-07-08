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
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

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
            con.result().query("select * from vcom_employee where last_updated_time >= " + lastUpdatedAt + " and is_deactivated = 0  limit " + limit + " offset " + ((page - 1) * limit)).execute().onComplete(res -> {
                RowSet<Row> rows = res.result();
                JsonArray res1 = new JsonArray();
                for (Row row : rows) {
                    JsonObject ob = new JsonObject();
                    ob.put("id", row.getString("id"));
                    ob.put("emp_id", row.getString("emp_id"));
                    ob.put("emp_name", row.getString("emp_name"));
                    ob.put("mobile_no", row.getString("mobile_no"));
                    ob.put("email", row.getString("email"));
                    ob.put("position_id", row.getInteger("position_id"));
                    ob.put("position_name", row.getString("position_name"));
                    ob.put("company_structure_id", row.getInteger("company_structure_id"));
                    ob.put("company_structure_name", row.getString("company_structure_name"));
                    ob.put("anyone_can_call", row.getBoolean("anyone_can_call"));
                    ob.put("last_updated_time", row.getLong("last_updated_time"));

                    res1.add(ob);
                }

                result.complete(res1);
            });
        });
        return result.future();
    }
}
