package hr.fitme.models;

import java.sql.Date;

/**
 * Created by ivanc on 01/06/2017.
 */

public class Walk {

    private int id;

    private float distance;

    private Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Walk(){
    }

}
