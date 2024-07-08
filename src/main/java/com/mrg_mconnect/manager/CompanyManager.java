/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrg_mconnect.manager;

import com.mrg_mconnect.dataaccess.DbClient;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;

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
        Promise<JsonArray> response = Promise.promise();
        try {
            dbClient.getPool().getConnection(con -> {
                if (con.succeeded()) {
                    SqlConnection conn = con.result();
                    isUserExists(conn, userId).andThen(user -> {
                        if (user.succeeded()) {
                            if (user.result()) {
                                conn.query("select * from vcom_employee where last_updated_time >= " + lastUpdatedAt
                                        + " and is_deactivated = 0  limit " + limit + " offset "
                                        + ((page - 1) * limit))
                                        .execute().onComplete(res -> {
                                            RowSet<Row> rows = res.result();
                                            JsonArray result = new JsonArray();
                                            for (Row row : rows) {
                                                JsonObject contact = new JsonObject();
                                                contact.put("id", row.getString("id"));
                                                contact.put("emp_id", row.getString("emp_id"));
                                                contact.put("emp_name", row.getString("emp_name"));
                                                contact.put("mobile_no", row.getString("mobile_no"));
                                                contact.put("email", row.getString("email"));
                                                contact.put("position_id", row.getInteger("position_id"));
                                                contact.put("position_name", row.getString("position_name"));
                                                contact.put("company_structure_id", row.getInteger("company_structure_id"));
                                                contact.put("company_structure_name",row.getString("company_structure_name"));
                                                contact.put("anyone_can_call", row.getBoolean("anyone_can_call"));
                                                contact.put("last_updated_time", row.getLong("last_updated_time"));

                                                result.add(contact);
                                            }

                                            conn.close();
                                            response.complete(result);
                                        });
                            } else {
                                conn.close();
                                response.fail("User does not exists");
                            }
                        } else {
                            conn.close();
                            response.fail(user.result().toString());
                        }
                    });

                } else {
                    response.fail("Database Connection Failed");
                }

            });
        } catch (Exception ex) {
            response.fail(ex);
        }
        return response.future();
    }

    private Future<Boolean> isUserExists(SqlConnection con, String userId) {
        Promise<Boolean> response = Promise.promise();
        try {
            con
                    .query("select * from com_employee where id = '" + userId + "'")
                    .execute().onComplete(res -> {
                        if (res.succeeded()) {
                            if (res.result().size() > 0) {
                                response.complete(true);
                            } else {
                                response.complete(false);
                            }
                        } else {
                            response.fail(res.cause().getMessage());
                        }

                    });
        } catch (Exception e) {
            throw e;
        }
        return response.future();
    }

    public Future<JsonArray> getStructureList() {
        Promise<JsonArray> response = Promise.promise();
        try {
            dbClient.getPool().getConnection(con -> {
                if (con.succeeded()) {
                    SqlConnection conn = con.result();
                    conn.query("select * from com_structure order by sort_index").execute().onComplete(res -> {
                        RowSet<Row> rows = res.result();
                        JsonArray result = new JsonArray();
                        for (Row row : rows) {
                            JsonObject structure = new JsonObject();
                            structure.put("structure_id", row.getInteger("structure_id"));
                            structure.put("structure_parent_id", row.getInteger("structure_parent_id"));
                            structure.put("structure_name", row.getString("structure_name"));
                            structure.put("structure_ref", row.getString("structure_ref"));
                            structure.put("sort_index", row.getInteger("sort_index"));

                            result.add(structure);
                        }
                        conn.close();
                        response.complete(result);
                    });
                } else {
                    response.fail("Database Connection Failed");
                }
            });
        } catch (Exception ex) {
            response.fail(ex);
        }

        return response.future();
    }
}
