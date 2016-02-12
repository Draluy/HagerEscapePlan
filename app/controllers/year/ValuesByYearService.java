package controllers.year;

import controllers.value.dao.ValueByYearDAO;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dralu on 2/11/2016.
 */
public class ValuesByYearService {

    private final ValueByYearDAO valueByYearDAO = new ValueByYearDAO();

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
}
