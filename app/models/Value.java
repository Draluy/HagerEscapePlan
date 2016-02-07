package models;

import com.avaje.ebean.Model ;
import com.avaje.ebean.annotation.Index;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
public class Value extends Model {

    @Index
    @Constraints.Required
    public Long timestamp;


    @Constraints.Required
    public Long value;

    @Index
    @Constraints.Required
    public String country;

    @Override
    public String toString() {
        return "Value{" +
                "timestamp=" + timestamp +
                ", value=" + value +
                ", country='" + country + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Value value = (Value) o;

        return timestamp.equals(value.timestamp);

    }

    @Override
    public int hashCode() {
        return timestamp.hashCode();
    }
}
