package controllers.value.dao;

import models.Value;

/**
 * Created by dralu on 2/10/2016.
 */
public interface ValueDAOService {

    void saveValue(Value val);

    void doPeriodically();
}
