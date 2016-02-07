package controllers.iimport;

import play.mvc.Result;

/**
 * Created by dralu on 2/5/2016.
 */
public interface ImportController  {

    /**
     *
     * @param start The line number to start the import from
     * @return the line number it stopped at
     */
    Result start(long start);

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
}
