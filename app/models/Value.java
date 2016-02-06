package models;

import com.avaje.ebean.Model ;
import com.avaje.ebean.annotation.Index;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.sql.Timestamp;
import java.util.Date;

@Entity
public class Value extends Model{

    @Id
    @Index
    @Constraints.Required
    public Long timestamp;


    @Constraints.Required
    public Long value;

    @Index
    @Constraints.Required
    public String country;
}
