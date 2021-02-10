package com.services;

import com.utils.constants.Fields;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.apache.log4j.Logger;



public class Notes extends AbstractVerticle {
    private final Logger logger = Logger.getLogger(Notes.class);
    public static final String COLLECTION = "notes";
    private MongoClient mongo;
    @Override
    public void start(Future<Void> future){
        mongo = MongoClient.createShared(vertx, config().getJsonObject("db"),"GestionControle");
        vertx.eventBus().consumer("ListerNoteX", this::GetNote);
        future.complete();
    }

    private void GetNote(Message message) {
        JsonObject body = (JsonObject) message.body();
        String module = body.getString("module");
        String Etd_nom = body.getString("nom");
        String Etd_prenom = body.getString("prenom");
        String semester = body.getString("semester");
        String anneeScolaire = body.getString("anneeScolaire");
        String session = body.getString("session");

        JsonObject query = new JsonObject()
                .put("element.module.nom",module)
                .put("element.module.semester",semester)
                .put("element.module.anneeScolaire",anneeScolaire)
                .put("etudiant.nom",Etd_nom)
                .put("etudiant.prenom",Etd_prenom)
                .put("session",session);

        mongo.find(COLLECTION, query, hdlr -> {
            if (hdlr.succeeded()) {
                JsonArray reply = new JsonArray(hdlr.result());
                message.reply(reply);
                System.out.println("success");
            }else{
                System.out.println("fail");
                message.fail(1, "document not found");
            }
        });

    }

}
