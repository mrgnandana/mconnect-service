/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mrg_mconnect.manager;

import com.mrg_mconnect.dataaccess.DbClient;
import com.mrg_mconnect.service_commons.MediaUtil;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlConnection;

/**
 *
 * @author Yohan
 */
public class MediaManager {

    DbClient dbClient;

    public MediaManager() {
        dbClient = DbClient.getInstance();
    }

    public Future<JsonObject> uploadProfileImage(String userId, String uploadedFileName, String contentType) {
        Promise<JsonObject> response = Promise.promise();
        try {
            dbClient.getPool().getConnection(con -> {
                if (con.succeeded()) {
                    SqlConnection conn = con.result();
                    isUserExists(conn, userId).andThen(user -> {
                        if (user.succeeded()) {
                            if (user.result()) {
                                conn.query("select * from mda_profile_image where user_id = '" + userId + "'")
                                        .execute(res -> {
                                            RowSet<Row> rows = res.result();
                                            try {
                                                String imageLocation = "media/profile_images/" + userId + "."
                                                        + contentType.replace("image/", "");
                                                MediaUtil.createThumbnail(uploadedFileName, MediaUtil.MEDIA_PATH + imageLocation);
                                                if (rows.size() > 0) {
                                                    conn.query(
                                                        "update mda_profile_image set image_location = '"+imageLocation+"' where user_id = '" + userId+ "'")
                                                        .execute(ar1 -> {
                                                            if (ar1.succeeded()) {
                                                                JsonObject result = new JsonObject();
                                                                result.put("user_id", userId);
                                                                result.put("image_location", imageLocation);
                                                                conn.close();
                                                                response.complete(result);
                                                            } else {
                                                                conn.close();
                                                                response.fail("Profile image update failed");
                                                            }

                                                        });

                                                } else {

                                                    conn.query(
                                                            "insert into mda_profile_image(user_id, image_location) values ('" + userId
                                                            + "','" + imageLocation + "')")
                                                            .execute(ar1 -> {
                                                                if (ar1.succeeded()) {
                                                                    JsonObject result = new JsonObject();
                                                                    result.put("user_id", userId);
                                                                    result.put("image_location", imageLocation);
                                                                    conn.close();
                                                                    response.complete(result);
                                                                } else {
                                                                    conn.close();
                                                                    response.fail("Profile image record create failed");
                                                                }

                                                            });

                                                }
                                            } catch (Exception ex) {
                                                conn.close();
                                                response.fail("Image file save failed");
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
                    .query("select * from com_employee where id = '" + userId + "'")
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

    public Future<JsonObject> getProfileImage(String userId) {
        Promise<JsonObject> response = Promise.promise();
        try {
            dbClient.getPool().getConnection(con -> {
                if (con.succeeded()) {
                    SqlConnection conn = con.result();
                    isUserExists(conn, userId).andThen(user -> {
                        if (user.succeeded()) {
                            if (user.result()) {
                                conn.query("select * from mda_profile_image where user_id = '" + userId + "'")
                                        .execute(res -> {
                                            RowSet<Row> rows = res.result();
                                            JsonObject profileImage = new JsonObject();
                                            if (rows.size() > 0) {
                                                for (Row row : rows) {
                                                    profileImage.put("user_id", row.getString("user_id"));
                                                    profileImage.put("image_location", row.getString("image_location"));
                                                    conn.close();
                                                    response.complete(profileImage);
                                                    break;
                                                }
                                            } else {
                                                conn.close();
                                                response.fail("Profile Image does not exists");
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

}
