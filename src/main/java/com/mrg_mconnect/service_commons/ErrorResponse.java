package com.mrg_mconnect.service_commons;

import io.vertx.core.json.JsonObject;

/**
 *
 * @author Nandana
 */

public class ErrorResponse {

    public interface ISetErrorNo {
        public Ibuilder errorNo(int errorNo);

    }

    public interface ISetMessage {
        public ISetErrorNo message(String message);

    }

    public interface Ibuilder {
        public JsonObject build();
    }

    public static class Builder implements
            ISetMessage, ISetErrorNo, Ibuilder {

        int errorNo;
        String message;

        @Override
        public JsonObject build() {
            System.err.println("Test " + this.message + " >" + this.errorNo);
            JsonObject result = new JsonObject();
            result.put("message", message);
            result.put("error_no", errorNo);
            return result;
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

    }

    public void test() {
        JsonObject test = new ErrorResponse.Builder().message("test message").errorNo(1).build();
        System.err.println(test);
    }

}
