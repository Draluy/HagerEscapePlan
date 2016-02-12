package controllers.value.dao;

import models.Value;

/**
 * Created by dralu on 2/10/2016.
 */
public interface ValueConsumer {

    void saveValue(Value val);

    void doPeriodically();

    void doAtTheEnd();
}
