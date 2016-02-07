package controllers.iimport;

import play.mvc.Result;

/**
 * Created by dralu on 2/5/2016.
 */
public interface ImportController  {

    /**
     *
     * @return the line number it stopped at
     */
    Result start();

    /**
     * Stops the import
     * @return
     */
    Result stop ();

    /**
     * Cleans the table
     * @return
     */
    Result deleteAllData();

    /**
     * Returns the total nb of lines to import
     * @return
     */
    Result getNbLines();

    /**
     * Return the number of lines imported so far
     * @return
     */
    Result getCurrentNbLines();
}
