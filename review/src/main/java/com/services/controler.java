package com.services;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.apache.log4j.Logger;

public class controler extends AbstractVerticle {
    private final Logger logger = Logger.getLogger(controler.class);
    public static final String COLLECTION = "etudiants";
    private MongoClient mongo;
    @Override
    public void start(Future<Void> future) {
        mongo = MongoClient.createShared(vertx, config().getJsonObject("db"),"GestionControle");
        vertx.eventBus().consumer("LOGIN", this::LOGIN);
        future.complete();
    }

    private void LOGIN(Message message) {
        JsonObject body = (JsonObject) message.body();
        String email = body.getString("username");
        String CNE = body.getString("login");

        JsonObject query = new JsonObject()
                .put("emailInstitutionnel",email)
                .put("cin",CNE);

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
