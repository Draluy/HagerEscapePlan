package controllers.year;

import controllers.value.dao.ValueByYearDAO;

import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dralu on 2/11/2016.
 * This class  handles the requests to get data for a specified year, or for an arbitrary span
 * It converts the parameters to and from the DAO
 */
public class ValuesByYearService {

    //Value DAO
    private final ValueByYearDAO valueByYearDAO = new ValueByYearDAO();

    /**
     * Get all the possible years from the database
     * @return A list containing all the possible years
     */
    public List<Integer> getYears(){
        List<Integer> years  = new ArrayList<>();
        final Long[] minMaxTimestamps = valueByYearDAO.getMinMaxTimestamps();
        if (minMaxTimestamps != null) {
            int minYear  = getYear(minMaxTimestamps[0]);
            int maxYear  = getYear(minMaxTimestamps[1]);

            for (int i=minYear; i<=maxYear;i++){
                years.add(i);
            }
        }

        return years;
    }

    //Get the year from the timestamp
    private int getYear(Long millisFromEpoch){
        final Instant instant = Instant.ofEpochMilli(millisFromEpoch);
        final LocalDateTime localTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localTime.getYear();
    }

    /**
     * Returns a map of the sum of all values per day. Each day is represented by its index
     * @param year The year
     * @return The sum of values per day
     */
    public Map<Integer,Long> getValues(int year) {
        return valueByYearDAO.getValuesByYear(year);
    }

    /**
     * Returns the sum of the values for a given day
     * @param lowerBound The lower bound to fetch from the database
     * @param upperBound The upper bound to fetch from the database
     * @return A map with the day indexes as keys, and the sum of all the values for that day as values
     */
    public Map<Integer,Long> getValues(LocalDateTime lowerBound, LocalDateTime upperBound) {
        final long lowerMillis = lowerBound.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
        final long upperMillis = upperBound.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
        return valueByYearDAO.getValues(lowerMillis, upperMillis);
    }
}
