package controllers.value.dao;

import models.Value;

/**
 * Created by dralu on 2/10/2016.
 * Represents an object that will consume values to apply a treatment on them.
 * This can be saving into the database, summing the values by country.
 */
public interface ValueConsumer {

    /**
     * Consume a avalue
     * @param val The value
     */
    void saveValue(Value val);

    /**
     * If something needs to be done periodically
     */
    void doPeriodically();

    /**
     * If something needs to be done at the end of the values list
     */
    void doAtTheEnd();
}
