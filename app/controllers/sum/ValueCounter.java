package controllers.sum;

import com.avaje.ebean.Model;
import controllers.value.dao.ValueDAOService;
import models.Sum;
import models.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dralu on 2/10/2016.
 */
public class ValueCounter implements ValueDAOService {
    private Map<String, Long> sumValues = new HashMap<>();

    public static List<Sum> getSumValues() {
        return new Sum.Finder<>(Sum.class).all();
    }

    @Override
    public void saveValue(Value val) {
        Long sumValue = sumValues.get(val.country);
        sumValues.put(val.country, sumValue == null ? val.value : sumValue + val.value);
    }

    @Override
    public void doPeriodically() {
        final Model.Finder<String, Sum> stringSumFinder = new Sum.Finder<>(Sum.class);
        for (Map.Entry<String, Long> entry : sumValues.entrySet()) {
            Sum sum = new Sum();
            sum.country = entry.getKey();
            sum.valueSum = entry.getValue();

            Sum sumFound = stringSumFinder.byId(sum.country);
            if (sumFound != null){
                sumFound.valueSum += sum.valueSum;
                sumFound.update();
            }else {
                sum.insert();
            }
        }
    }
}
