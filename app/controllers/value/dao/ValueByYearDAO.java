package controllers.value.dao;

/**
 * Created by dralu on 2/11/2016.
 */

import play.Logger;
import play.db.DB;

import java.sql.*;
import java.time.*;
import java.util.*;

/**
 * Makes all the requests for a certain year, not caring about the countries. This service is time-oriented
 */
public class ValueByYearDAO {

    private static final Long MILLIS_IN_YEAR = 31536000000L;
    private static final Long MILLIS_IN_DAY = 86400000L;

    private final ConnectionFactory connectionFactory = new ConnectionFactory();

    /**
     * Gets the min and max timestamps of the imported data. God bless indexes.
     *
     * @return a Long array of 2 values if the query succeeded, null otherwise
     */
    public Long[] getMinMaxTimestamps() {
        Long[] result = null;
        try (Connection conn = DB.getConnection()) {
            final Statement statement = conn.createStatement();
            final ResultSet resultSet = statement.executeQuery("select min(timestamp), max(timestamp) from value");
            if (resultSet.next()) {
                if (resultSet.getObject(1) != null && resultSet.getObject(2) != null) {
                    result = new Long[]{resultSet.getLong(1), resultSet.getLong(2)};
                }
                resultSet.close();
            }
        } catch (SQLException ex) {
            Logger.error("Error when fetching min and max timestamps.", ex);
        }
        return result;
    }

    /**
     * Get the sum of the values by day for a certain year.
     * @param year The year
     * @return see getValues
     */
    public Map<Integer, Long> getValuesByYear(int year) {
        Map<Integer, Long> map = new TreeMap<>();
        Long lowerBound = LocalDateTime.of(year, Month.JANUARY, 1,0,0,0).atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
        Long upperBound =LocalDateTime.of(year +1 , Month.JANUARY, 1,0,0,0).atZone(ZoneOffset.UTC).toInstant().toEpochMilli();

        return getValues(lowerBound, upperBound);
    }

    /**
     * Return all rows in the database from a set timestamp to another
     * @param lowerBound
     * @param upperBound
     * @return A map of the sum of the values for a set days. The key is the index of the day, the value is the sum of the values
     */
    public Map<Integer, Long> getValues(long lowerBound, long upperBound) {
        Map<Integer, Long> map = new TreeMap<>();
        try (Connection conn = connectionFactory.getConnection()) {

            final PreparedStatement statement = conn.prepareStatement("WITH series AS ("
                    + " SELECT generate_series(" + lowerBound + " , " + (upperBound - MILLIS_IN_DAY) + ", " + MILLIS_IN_DAY + ") AS r_from),"
                    + " range AS (SELECT r_from, (r_from + " + (MILLIS_IN_DAY - 1) + ") AS r_to FROM series)"
                    + "SELECT r_from, r_to,"
                    + "(SELECT sum(value) FROM value WHERE timestamp  BETWEEN r_from AND r_to) as nb "
                    + "FROM range");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Long from = rs.getLong(1);
                Long to = rs.getLong(2);
                Long sum = rs.getLong(3);

                int dayIndex = (int) ((from - lowerBound) / MILLIS_IN_DAY) + 1;
                map.put(dayIndex, sum);
            }
        } catch (SQLException ex) {
            Logger.error("Error when fetching min and max timestamps.", ex);
        }
        return map;
    }
}

