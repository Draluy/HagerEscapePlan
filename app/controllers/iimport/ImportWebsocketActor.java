package controllers.iimport;

import akka.actor.*;

public class ImportWebsocketActor extends UntypedActor {

    public static Props props(ActorRef out) {
        return Props.create(ImportWebsocketActor.class, out);
    }

    private final ActorRef out;

    public ImportWebsocketActor(ActorRef out) {
        this.out = out;
    }

    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            out.tell("I received your message: " + message, self());
        }
    }
}