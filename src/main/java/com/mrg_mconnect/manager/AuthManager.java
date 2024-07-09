package com.mrg_mconnect.manager;

import java.util.Random;
import java.util.UUID;

import com.mrg_mconnect.dataaccess.DbClient;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.authentication.Credentials;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Transaction;

/**
 *
 * @author Nandana
 */

public class AuthManager {
    DbClient dbClient;
    String SECRET = "mconnect-token-secret-b70a7912-ea84-4658-a727-26f11e3b711f";
    Vertx vertx;

    public AuthManager(Vertx v) {
        dbClient = DbClient.getInstance();
        this.vertx = v;
    }

    public static String getRandomNumberString() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }

    public Future<JsonObject> requestLogin(String mobileNo, String empId, String email, String deviceId) {
        // currently ignore email
        // check employee exists from mobileNo or empId
        // if exists,
        // invalidate current sessions for the device
        // create new session with, sessionkey, otp,
        // create outsmsq record
        // send sms
        // return sessionkey

        Promise<JsonObject> result = Promise.promise();

        dbClient.getPool().getConnection(res -> {
            if (res.succeeded()) {
                System.out.println("Get connection ok");
                // Transaction must use a connection
                SqlConnection conn = res.result();

                // Begin the transaction
                conn.begin(ar0 -> {
                    System.out.println("tx begin");
                    if (ar0.succeeded()) {
                        Transaction tx = ar0.result();
                        // Various statements
                        conn
                                .query("SELECT * FROM vath_users where mobile_no = '" + mobileNo + "' or emp_id = '"
                                        + empId + "'")
                                .execute(ar1 -> {
                                    if (ar1.succeeded()) {
                                        // check record found
                                        System.out.println("Query succeed, check employee");
                                        if (ar1.result().size() >= 1) {
                                            // employee found
                                            System.out.println("Employee found, create session record");
                                            String sessionId = UUID.randomUUID().toString();
                                            String userId = "";
                                            String mNo = "";
                                            for (Row row : ar1.result()) {
                                                userId = row.getString("id");
                                                mNo = row.getString("mobile_no");
                                                break;
                                            }
                                            String otp = getRandomNumberString();
                                            String otpSms = "Use OTP: " + otp;
                                            final String mobileNx = mNo;

                                            // create session record start
                                            /*
                                             * INSERT INTO mconnect-db.ath_plotp_login_session (session_id, user_id,
                                             * mobile_no, otp, otp_checked, device_id) VALUES ('xxa', 'xx2',
                                             * '+99875555', '123456', '0', 'xxx');
                                             */
                                            long createdAt = System.currentTimeMillis() / 1000;
                                            System.out.println(
                                                    "INSERT INTO ath_plotp_login_session (`session_id`, `user_id`, `mobile_no`, `otp`, `otp_checked`, `device_id`, `created_at`) VALUES ('"
                                                            + sessionId + "','" + userId + "','" + mobileNx + "','"
                                                            + otp
                                                            + "','" + 0 + "','" + deviceId + "', '" + createdAt + "')");
                                            conn
                                                    .query("INSERT INTO ath_plotp_login_session (`session_id`, `user_id`, `mobile_no`, `otp`, `otp_checked`, `device_id`, `created_at`) VALUES ('"
                                                            + sessionId + "','" + userId + "','" + mobileNx + "','"
                                                            + otp
                                                            + "','" + 0 + "','" + deviceId + "', '" + createdAt + "')")
                                                    .execute(ar2 -> {
                                                        if (ar2.succeeded()) {
                                                            // create the otpoutq record
                                                            // INSERT INTO ath_plotp_outq (mobile_no, otp, sms_text,
                                                            // is_sent, sent_count) VALUES ('asdf', 'asdf', 'sdfdsf',
                                                            // '0', '0');

                                                            conn
                                                                    .query("INSERT INTO ath_plotp_outq (mobile_no, otp, sms_text, is_sent, sent_count, session_id) VALUES ('"
                                                                            + mobileNx + "', '" + otp + "', '" + otpSms
                                                                            + "', '0', '0', '" + sessionId + "')")
                                                                    .execute(ar3 -> {

                                                                        if (ar3.succeeded()) {
                                                                            System.out.println(
                                                                                    "outq record create success");

                                                                            // Commit the transaction
                                                                            tx.commit(ar4 -> {
                                                                                if (ar4.succeeded()) {
                                                                                    System.out.println(
                                                                                            "Transaction succeeded");
                                                                                    JsonObject r = new JsonObject();
                                                                                    r.put("sid", sessionId);
                                                                                    result.complete(r);

                                                                                } else {
                                                                                    System.out.println(
                                                                                            "Transaction failed " + ar4
                                                                                                    .cause()
                                                                                                    .getMessage());
                                                                                    result.fail("Transaction failed");
                                                                                }
                                                                                // Return the connection to the pool
                                                                                conn.close();
                                                                            });

                                                                        } else {
                                                                            System.out.println(
                                                                                    "outq record create failed "
                                                                                            + ar3.cause().getMessage());
                                                                            conn.close();
                                                                            result.fail("outq record create failed");
                                                                        }

                                                                    });
                                                            // create the otpoutq record end

                                                        } else {
                                                            // Return the connection to the pool
                                                            conn.close();
                                                            System.out.println("Session recoord create failed");
                                                            result.fail("User not found");
                                                        }
                                                    });
                                            // create session record end

                                        } else {
                                            conn.close();
                                            System.out.println("Employee not found");
                                            result.fail("User not found");
                                        }

                                    } else {
                                        // Return the connection to the pool
                                        conn.close();
                                        System.out.println("Db query failed");
                                        result.fail("Db query failed");
                                    }
                                });
                    } else {
                        // Return the connection to the pool
                        conn.close();
                        System.out.println("Db transaction failed");
                        result.fail("Db transaction failed");
                    }
                });
            } else {
                System.out.println("get connection failed");
                result.fail("get connection failed");
            }
        });

        return result.future();
    }

    public Future<JsonObject> token(String sessionId, String otp) {
        // check session exists
        // check sms sent
        // create refresh token and access token
        // update session record
        // send tokens

        Promise<JsonObject> result = Promise.promise();

        dbClient.getPool().getConnection(res -> {
            if (res.succeeded()) {
                System.out.println("Get connection ok");
                // Transaction must use a connection
                SqlConnection conn = res.result();

                // Begin the transaction
                conn.begin(ar0 -> {
                    System.out.println("tx begin");
                    if (ar0.succeeded()) {
                        Transaction tx = ar0.result();
                        // Various statements
                        conn
                                .query("SELECT * FROM vath_plotp_outq where session_id = '" + sessionId + "' and otp = '"+ otp +"'")
                                .execute(ar1 -> {
                                    if (ar1.succeeded()) {
                                        // check record found
                                        System.out.println("Query succeed, check employee");
                                        if (ar1.result().size() >= 1) {
                                            // employee found
                                            System.out.println("Session record found, create tokens");

                                            String userId = "";
                                            //String mNo = "";
                                            for (Row row : ar1.result()) {
                                                userId = row.getString("user_id");
                                                break;
                                            }

                                            JWTAuth provider = JWTAuth.create(vertx, new JWTAuthOptions()
                                            .addPubSecKey(new PubSecKeyOptions()
                                                .setAlgorithm("HS256")
                                                .setBuffer(SECRET)));

                                            JsonArray audArray = new JsonArray();
                                            audArray.add("user");

                                            long createdAt = System.currentTimeMillis()/1000 + 300;

                                            String refreshToken = provider.generateToken(new JsonObject().put("sid", sessionId).put("sub", userId));
                                            String accessToken = provider.generateToken(new JsonObject().put("sid", sessionId).put("sub", userId).put("aud", audArray).put("exp", createdAt));

                                            JsonObject data = new JsonObject();
                                            data.put("access_token", accessToken);
                                            data.put("refresh_token", refreshToken);
                                            


                                            // update session record start
                                                            
                                            conn
                                                    .query("update ath_plotp_login_session set otp_checked = 1 where session_id ='"+ sessionId +"'")
                                                    .execute(ar2 -> {
                                                        if (ar2.succeeded()) {
                                                            // Commit the transaction
                                                            tx.commit(ar4 -> {
                                                                if (ar4.succeeded()) {
                                                                    System.out.println(
                                                                            "Transaction succeeded");
                                                                            result.complete(data);

                                                                } else {
                                                                    System.out.println(
                                                                            "Transaction failed " + ar4
                                                                                    .cause()
                                                                                    .getMessage());
                                                                    result.fail("Transaction failed");
                                                                }
                                                                // Return the connection to the pool
                                                                conn.close();
                                                            });

                                                        } else {
                                                            // Return the connection to the pool
                                                            conn.close();
                                                            System.out.println("update session record failed");
                                                            result.fail("update session record failed");
                                                        }
                                                    });
                                            // create session record end

                                        } else {
                                            conn.close();
                                            System.out.println("Session not found");
                                            result.fail("Session not found");
                                        }

                                    } else {
                                        // Return the connection to the pool
                                        conn.close();
                                        System.out.println("Db query failed");
                                        result.fail("Db query failed");
                                    }
                                });
                    } else {
                        // Return the connection to the pool
                        conn.close();
                        System.out.println("Db transaction failed");
                        result.fail("Db transaction failed");
                    }
                });
            }else{
                System.out.println("get connection failed");
                result.fail("get connection failed");
            }
        });

        return result.future();
    }

    public Future<JsonObject> verify(String token){
        Promise<JsonObject> result = Promise.promise();

        // JWTAuth provider = JWTAuth.create(vertx, new JWTAuthOptions()
        //                                     .addPubSecKey(new PubSecKeyOptions()
        //                                         .setAlgorithm("HS256")
        //                                         .setBuffer(SECRET)));

        // JWTAuthHandler.create(provider).handle(null);

        // provider.authenticate(new JsonObject().put("token", token))
        // .onSuccess(user -> System.out.println("User: " + user.principal()))
        // .onFailure(err -> {
        //   // Failed!
        // });

        return result.future();
    }
}
