package com.services;


import com.utils.constants.Fields;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.apache.log4j.Logger;

public class Enseignants extends AbstractVerticle {
    private final Logger logger = Logger.getLogger(Enseignants.class);
    public static final String COLLECTION = "enseignants";
    private MongoClient mongo;
    @Override
    public void start(Future<Void> future) {
        mongo = MongoClient.createShared(vertx, config().getJsonObject("db"),"GestionControle");
        vertx.eventBus().consumer("Enseignant_Element", this::getEnseignant);

        future.complete();
    }

    private void getEnseignant(Message message) {
        JsonObject body = (JsonObject) message.body();
        String element = body.getString("element");

        JsonObject query = new JsonObject()
                .put("elements.nom",element);
        mongo.findOne(COLLECTION, query, new JsonObject(), hdlr -> {
            if (hdlr.succeeded()) {
                JsonObject doc = hdlr.result();
                if(doc == null){
                    System.out.println("fail");
                    message.fail(1, "document not found");
                }else{
                    System.out.println("success");
                    message.reply(doc);
                }
            } else {
                System.out.println("fail");
                message.fail(1, hdlr.cause().getMessage());
            }
        });

    }
}
