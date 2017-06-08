package hr.fitme.models;

import java.sql.Date;

/**
 * Created by ivanc on 06/06/2017.
 */

public class MyLocation {
    private double lat;

    private double lng;

    private Date date;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
