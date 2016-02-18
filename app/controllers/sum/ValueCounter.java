package controllers.sum;

import com.avaje.ebean.Model;
import controllers.value.dao.ValueConsumer;
import models.Sum;
import models.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dralu on 2/10/2016.
 * This class will sum all the values for a certain country
 */
public class ValueCounter implements ValueConsumer {

    //public for testability, and because why not
    public class SumFactory{
        public Sum getSum(String country, Long value){
            return new Sum(country, value);
        }
    }

    private SumFactory sumFactory = new SumFactory();

    //The sum of the values for a country
    private Map<String, Long> sumValues = new HashMap<>();

    //Helper object to fecth the values from the table;
    //Table is short, this will suffice
    private  Model.Finder<String, Sum>  finder = new Sum.Finder<>(Sum.class);

    public List<Sum> getSumValues() {
        return finder.all();
    }

    public Map<String, Long> getSumValuesMap() {
        return sumValues;
    }

    /**
     * Each value consumed is put in memory, so the addition is fast
     * @param val The value
     */
    @Override
    public void saveValue(Value val) {
        Long sumValue = sumValues.get(val.getCountry());
        sumValues.put(val.getCountry(), sumValue == null ? val.getValue() : sumValue + val.getValue());
    }

    /**
     * Periodically, flush the data in the database
     */
    @Override
    public void doPeriodically() {
        for (Map.Entry<String, Long> entry : sumValues.entrySet()) {
            Sum sum = sumFactory.getSum(entry.getKey(), entry.getValue());

            Sum sumFound = finder.byId(sum.getCountry());
            if (sumFound != null){
                sumFound.setValueSum(sumFound.getValueSum() + sum.getValueSum());
                sumFound.update();
            }else {
                sum.insert();
            }
        }
        sumValues.clear();
    }

    @Override
    public void doAtTheEnd() {
        doPeriodically();
    }
}
