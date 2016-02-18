package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by dralu on 2/10/2016.
 */
@Entity
public class Sum extends Model {
    @Id
    private final String country;

    private Long valueSum;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sum sum = (Sum) o;

        if (country != null ? !country.equals(sum.country) : sum.country != null) return false;
        return valueSum != null ? valueSum.equals(sum.valueSum) : sum.valueSum == null;

    }

    @Override
    public int hashCode() {
        int result = country != null ? country.hashCode() : 0;
        result = 31 * result + (valueSum != null ? valueSum.hashCode() : 0);
        return result;
    }

    public Sum(String country, Long valueSum) {

        this.country = country;
        this.valueSum = valueSum;
    }

    public void setValueSum(Long valueSum) {
        this.valueSum = valueSum;
    }

    public String getCountry() {
        return country;
    }

    public Long getValueSum() {
        return valueSum;
    }
}
