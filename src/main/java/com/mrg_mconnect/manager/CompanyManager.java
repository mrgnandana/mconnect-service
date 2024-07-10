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
                                        + " and is_deactivated = 0 and deleted = 0 limit " + limit + " offset "
                                        + ((page - 1) * limit))
                                        .execute(res -> {
                                            if (res.succeeded()) {
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
                                                    contact.put("company_structure_name", row.getString("company_structure_name"));
                                                    contact.put("anyone_can_call", row.getBoolean("anyone_can_call"));
                                                    contact.put("last_updated_time", row.getLong("last_updated_time"));

                                                    result.add(contact);
                                                }

                                                conn.close();
                                                response.complete(result);
                                            } else {
                                                conn.close();
                                                response.fail("Get Contact list failed");
                                            }
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
                    .query("select * from com_employee where id = '" + userId + "' and deleted = 0 ")
                    .execute(res -> {
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
                    conn.query("select * from com_structure where deleted = 0 order by sort_index").execute(res -> {
                        if (res.succeeded()) {
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
                        } else {
                            conn.close();
                            response.fail("Get Structure list failed");
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

    //company position 
    public Future<JsonArray> getPositionList() {
        Promise<JsonArray> response = Promise.promise();
        try {
            dbClient.getPool().getConnection(con -> {
                if (con.succeeded()) {
                    SqlConnection conn = con.result();
                    conn.query("select * from com_position where deleted = 0 ").execute(res -> {
                        if (res.succeeded()) {
                            RowSet<Row> rows = res.result();
                            JsonArray result = new JsonArray();
                            for (Row row : rows) {
                                JsonObject position = new JsonObject();
                                position.put("position_id", row.getInteger("position_id"));
                                position.put("position_name", row.getString("position_name"));

                                result.add(position);
                            }
                            conn.close();
                            response.complete(result);
                        } else {
                            conn.close();
                            response.fail("Get Position list failed");
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

    public Future<JsonObject> createCompanyPosition(String positionName) {
        Promise<JsonObject> response = Promise.promise();
        try {
            dbClient.getPool().getConnection(con -> {
                if (con.succeeded()) {
                    SqlConnection conn = con.result();
                    getNextId(conn, "com_position", "position_id").andThen(getNextId -> {
                        if (getNextId.succeeded()) {

                            conn.query("insert into com_position(position_id, position_name) values (" + getNextId.result() + ",'" + positionName + "')").execute(res -> {
                                if (res.succeeded()) {
                                    JsonObject position = new JsonObject();
                                    position.put("position_id", getNextId.result());
                                    position.put("position_name", positionName);

                                    conn.close();
                                    response.complete(position);
                                } else {
                                    conn.close();
                                    response.fail("Position creation failed.");
                                }
                            });
                        } else {
                            conn.close();
                            response.fail("Get next id error");

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

    public Future<JsonObject> updateCompanyPosition(int positionId, String positionName) {
        Promise<JsonObject> response = Promise.promise();
        try {
            dbClient.getPool().getConnection(con -> {
                if (con.succeeded()) {
                    SqlConnection conn = con.result();
                    isCompanyPositionExists(conn, positionId).andThen(companyPosition -> {
                        if (companyPosition.succeeded()) {
                            if (companyPosition.result()) {

                                conn.query("update com_position set position_name ='" + positionName + "' where position_id =" + positionId).execute(res -> {
                                    if (res.succeeded()) {
                                        JsonObject position = new JsonObject();
                                        position.put("position_id", positionId);
                                        position.put("position_name", positionName);

                                        conn.close();
                                        response.complete(position);
                                    } else {
                                        conn.close();
                                        response.fail("Position update failed.");
                                    }
                                });
                            } else {
                                conn.close();
                                response.fail("Company position does not exists.");
                            }
                        } else {
                            conn.close();
                            response.fail("Position update failed.");

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

    public Future<Boolean> deleteCompanyPosition(int positionId) {
        Promise<Boolean> response = Promise.promise();
        try {
            dbClient.getPool().getConnection(con -> {
                if (con.succeeded()) {
                    SqlConnection conn = con.result();
                    isCompanyPositionExists(conn, positionId).andThen(companyPosition -> {
                        if (companyPosition.succeeded()) {
                            if (companyPosition.result()) {

                                conn.query("update com_position set deleted = 1 where position_id =" + positionId).execute(res -> {
                                    if (res.succeeded()) {
                                        conn.close();
                                        response.complete(true);
                                    } else {
                                        conn.close();
                                        response.fail("Position delete failed.");
                                    }
                                });
                            } else {
                                conn.close();
                                response.fail("Company position does not exists.");
                            }
                        } else {
                            conn.close();
                            response.fail("Position delete failed.");

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

    public Future<JsonObject> getCompanyPosition(int positionId) {
        Promise<JsonObject> response = Promise.promise();
        try {
            dbClient.getPool().getConnection(con -> {
                if (con.succeeded()) {
                    SqlConnection conn = con.result();
                    isCompanyPositionExists(conn, positionId).andThen(companyPosition -> {
                        if (companyPosition.succeeded()) {
                            if (companyPosition.result()) {

                                conn.query("select * from com_position where deleted = 0 and position_id =" + positionId).execute(res -> {
                                    if (res.succeeded()) {
                                        RowSet<Row> rows = res.result();
                                        JsonObject position = new JsonObject();
                                        for (Row row : rows) {
                                            position.put("position_id", row.getInteger("position_id"));
                                            position.put("position_name", row.getString("position_name"));
                                            break;
                                        }

                                        conn.close();
                                        response.complete(position);
                                    } else {
                                        conn.close();
                                        response.fail("Position get failed.");
                                    }
                                });
                            } else {
                                conn.close();
                                response.fail("Company position does not exists.");
                            }
                        } else {
                            conn.close();
                            response.fail("Position get failed.");

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

    private Future<Boolean> isCompanyPositionExists(SqlConnection con, int positionId) {
        Promise<Boolean> response = Promise.promise();
        try {
            con
                    .query("select * from com_position where position_id = " + positionId + " and deleted = 0 ")
                    .execute(res -> {
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

    public Future<JsonObject> getCompanyDetails(String companyId) {
        Promise<JsonObject> response = Promise.promise();
        try {
            dbClient.getPool().getConnection(con -> {
                if (con.succeeded()) {
                    SqlConnection conn = con.result();
                    conn.query("select * from com_company where id ='" + companyId + "'").execute(res -> {
                        if (res.succeeded()) {
                            RowSet<Row> rows = res.result();
                            JsonObject company = new JsonObject();
                            if (rows.size() > 0) {
                                for (Row row : rows) {
                                    company.put("id", row.getString("id"));
                                    company.put("company_name", row.getString("company_name"));
                                    company.put("address", row.getString("address"));
                                    company.put("icon_url", row.getString("icon_url"));
                                    break;
                                }
                                conn.close();
                                response.complete(company);

                            } else {
                                conn.close();
                                response.fail("Company does not exists");
                            }

                        } else {
                            conn.close();
                            response.fail("Get Company detail failed");
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

    private Future<Integer> getNextId(SqlConnection con, String table, String columnName) {
        Promise<Integer> response = Promise.promise();
        try {
            con
                    .query("select max(" + columnName + ")+1 as next_id from  " + table)
                    .execute(res -> {
                        if (res.succeeded()) {
                            if (res.result().size() > 0) {
                                for (Row row : res.result()) {
                                    response.complete(row.getInteger("next_id"));
                                    break;
                                }
                            } else {
                                response.fail("Next id get errror.");
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

    private Future<Boolean> isCompanyStructureExists(SqlConnection con, String structureId) {
        Promise<Boolean> response = Promise.promise();
        try {
            con
                    .query("select * from com_structure where structure_id = '" + structureId + "' and deleted = 0 ")
                    .execute(res -> {
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

}
