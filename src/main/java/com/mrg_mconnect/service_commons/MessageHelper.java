package com.mrg_mconnect.service_commons;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

public class MessageHelper {
    public static void errorReply(Message<Object> msgObject, String errorDescription, int errorNo) {
        JsonObject exReply = new JsonObject();
        exReply.put("success", false);
        exReply.put("message", errorDescription);
        exReply.put("error_no", errorNo);
        msgObject.reply(exReply);
    }

    public static void successReply(Message<Object> msgObject, Object data) {
        JsonObject exReply = new JsonObject();
        exReply.put("success", true);
        exReply.put("data", data);
        msgObject.reply(exReply);
    }
}
