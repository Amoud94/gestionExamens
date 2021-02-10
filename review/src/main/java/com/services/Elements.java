package com.services;

import com.utils.constants.Fields;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.apache.log4j.Logger;

public class Elements extends AbstractVerticle {
    private final Logger logger = Logger.getLogger(Elements.class);
    public static final String COLLECTION = "elements";
    private MongoClient mongo;
    @Override
    public void start(Future<Void> future) {
        mongo = MongoClient.createShared(vertx, config().getJsonObject("db"),"GestionControle");
        vertx.eventBus().consumer("ListerElementsByModule", this::ListerElementByModule);
        future.complete();
    }
    private void ListerElementByModule(Message message) {
        logger.debug("inside LISTER ELEMENTS BY MODULE");
        JsonObject body = (JsonObject) message.body();
        String module = body.getString("module");
        mongo.find(COLLECTION,new JsonObject().put("module.nom",module),hdlr ->{
            if (hdlr.succeeded()) {
                JsonArray reply = new JsonArray(hdlr.result());
                message.reply(reply);
            } else {
                JsonObject resp = new JsonObject()
                        .put(Fields.RESPONSE_FLUX_SUCCEEDED, false)
                        .put(Fields.RESPONSE_FLUX_MESSAGE,"KO");
                message.reply(resp);
            }
        });
    }

}
