package hr.fitme.models;


import java.sql.Date;

/**
 * Created by ivanc on 25/05/2017.
 */

public class UserWeight {
    private int id;
    private float weight;
    private Date date;

    public UserWeight() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UserWeight(int id, float weight, Date date) {
        this.id = id;
        this.weight = weight;
        this.date = date;
    }
}
