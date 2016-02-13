package controllers.value.dao;

/**
 * Created by dralu on 2/11/2016.
 */

import play.Logger;
import play.db.DB;

import java.sql.*;
import java.time.LocalDate;
import java.time.Year;
import java.util.*;

/**
 * Makes all the requests for a certain year, not caring about the countries. This service is time-oriented
 *
 */
public class ValueByYearDAO {

    private static final Long MILLIS_IN_YEAR =  31536000000L;
    private static final Long MILLIS_IN_DAY =  86400000L;

    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    /**
     * Gets the min and max timestamps of the imported data. God bless indexes.
     * @return a Long array of 2 values if the query succeeded, null otherwise
     */
    public Long[] getMinMaxTimestamps(){
        Long[] result = null;
        try(Connection conn = DB.getConnection()){
            final Statement statement = conn.createStatement();
            final ResultSet resultSet = statement.executeQuery("select min(timestamp), max(timestamp) from value");
            if (resultSet.next()) {
                result = new Long[]{resultSet.getLong(1), resultSet.getLong(2)};
                resultSet.close();
            }
        }
        catch (SQLException ex){
            Logger.error("Error when fetching min and max timestamps.",ex);
        }
        return result;
    }

    public Map<Integer, Long> getValuesByYear(int year) {
        Map<Integer, Long> map = new TreeMap<>();
        try(Connection conn = connectionFactory.getConnection()){

            Long millisInYear  = Year.of(year).isLeap()?  MILLIS_IN_YEAR + MILLIS_IN_DAY : MILLIS_IN_YEAR;
            Long lowerBound = (year-1970) * MILLIS_IN_YEAR;
            Long upperBound = lowerBound + millisInYear;
            final PreparedStatement statement = conn.prepareStatement("WITH series AS ("
            +" SELECT generate_series("+lowerBound+" , "+(upperBound - MILLIS_IN_DAY)+", "+MILLIS_IN_DAY+") AS r_from),"
            +" range AS (SELECT r_from, (r_from + "+(MILLIS_IN_DAY - 1)+") AS r_to FROM series)"
            +"SELECT r_from, r_to,"
            +"(SELECT sum(value) FROM value WHERE timestamp  BETWEEN r_from AND r_to) as nb "
            +"FROM range");
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Long from = rs.getLong(1);
                Long to = rs.getLong(2);
                Long sum = rs.getLong(3);

                int dayIndex = (int)((from - lowerBound) / MILLIS_IN_DAY) +1;
                map.put(dayIndex, sum);
            }
        } catch (SQLException ex){
            Logger.error("Error when fetching min and max timestamps.",ex);
        }
        return map;
    }
}

