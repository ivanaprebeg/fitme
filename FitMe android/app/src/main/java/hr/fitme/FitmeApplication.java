package hr.fitme;

import android.app.Application;

import com.google.android.gms.location.LocationRequest;

import hr.fitme.models.User;

/**
 * Created by ivanc on 22/05/2017.
 */

public class FitmeApplication extends Application {

    private LocationRequestData locationRequestData = LocationRequestData.FREQUENCY_MEDIUM_ONE;

    //this is current auth token:
    public static String token = "";
    public static String TokenName = "Authorization";

    private static FitmeApplication singleton;

    public static FitmeApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public LocationRequest createLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(locationRequestData.getInterval());
        locationRequest.setFastestInterval(locationRequestData.getFastestInterval());
        locationRequest.setPriority(locationRequestData.getPriority());
        locationRequest.setSmallestDisplacement(locationRequestData.getSmallestDisplacement());
        return locationRequest;
    }
}
