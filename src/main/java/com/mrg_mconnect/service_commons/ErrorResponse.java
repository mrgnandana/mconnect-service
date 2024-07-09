package com.mrg_mconnect.service_commons;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;

/**
 *
 * @author Nandana
 */

public class ErrorResponse {

    public interface IStatusCode {
        public ISetMessage statusCode(int statusCode);
        
    }

    public interface IResponse {
         public IStatusCode response(HttpServerResponse response);
        
    }

    public interface ISetErrorNo {
        public Ibuilder errorNo(int errorNo);

    }

    public interface ISetMessage {
        public ISetErrorNo message(String message);

    }

    public interface Ibuilder {
        public JsonObject build();
    }

    public static IResponse getBuilder(){
        return new Builder();
    }

    public static class Builder implements
            ISetMessage, ISetErrorNo, Ibuilder, IResponse, IStatusCode {

        int errorNo;
        String message;
        HttpServerResponse response;
        int statusCode = 400;

        @Override
        public JsonObject build() {

            System.err.println("Error response data: " + this.message + " >" + this.errorNo);

            //error data
            JsonObject result = new JsonObject();
            result.put("message", message);
            result.put("error_no", errorNo);

            //complese response if response is set
            if(response != null){
                    response
                        .setStatusCode(statusCode)
                        .putHeader("content-type", "application/json")
                        .end(result.encodePrettily());
            }else{
                return result;
            }

            return null;           
            
        }

        @Override
        public Ibuilder errorNo(int errorNo) {
            this.errorNo = errorNo;
            return this;
        }

        @Override
        public ISetErrorNo message(String message) {
            this.message = message;
            return this;
        }

        @Override
        public IStatusCode response(HttpServerResponse response) {
            this.response = response;
            return this;
        }

        @Override
        public ISetMessage statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

    }

    public void test() {
        JsonObject test = new ErrorResponse.Builder().message("test message").errorNo(1).build();
        System.err.println(test);
    }

}
