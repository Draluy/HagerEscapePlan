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
    public String country;

    public Long valueSum;
}
