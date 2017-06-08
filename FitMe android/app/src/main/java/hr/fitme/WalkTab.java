package hr.fitme;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hr.fitme.models.MyLocation;
import hr.fitme.models.User;
import hr.fitme.models.Walk;

/**
 * Created by ivanc on 05/05/2017.
 */

public class WalkTab extends Fragment {

    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private float distance;

    private BarChart barChart;
    private List<Walk> walks;

    private Button mapButton;

    private List<Location> locations;
    private List<Location> fetchedLocations;

    private View scrollView;
    private ShareButton shareButton;

    private TextView distanceTextView;
    private TextView stepsTextView;
    private TextView caloriesTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.walk_tab, container, false);
        shareButton = (ShareButton) rootView.findViewById(R.id.share_walks_fb);
        walks = new ArrayList<>();
        distanceTextView = (TextView) rootView.findViewById(R.id.distance_text_view);
        stepsTextView = (TextView) rootView.findViewById(R.id.steps_text_view);
        caloriesTextView = (TextView) rootView.findViewById(R.id.burned_calories);
        fetchedLocations = new ArrayList<>();
        new AddLocations().execute();
        barChart = (BarChart) rootView.findViewById(R.id.walk_chart);
        mapButton = (Button) rootView.findViewById(R.id.show_my_movement);
        scrollView = rootView.findViewById(R.id.scrollView);
        barChart.setDescription("");    // Hide the description
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getXAxis().setDrawLabels(false);
        barChart.setDrawValueAboveBar(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawAxisLine(false);
        barChart.setViewPortOffsets(-40f, 0f, 0f, 0f);
        barChart.getLegend().setEnabled(false);
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                int position = h.getXIndex();
                Toast.makeText(getContext(), "On " + walks.get(position).getDate()
                        + " you walked " + walks.get(position).getDistance() + " kilometers.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        locations = new ArrayList<>();

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putParcelableArrayListExtra("locations", (ArrayList<? extends Parcelable>) fetchedLocations);
                startActivity(intent);
            }
        });


        BroadcastReceiver reciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                locations.add((Location) intent.getParcelableExtra("location"));
            }
        };

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                reciever, new IntentFilter("GPSLocationUpdates"));


        return rootView;
    }

    @Override
    public void onResume() {
        new getWalks().execute();
        super.onResume();
    }


    private class getWalks extends AsyncTask<Void, Void, Walk[]> {
        @Override
        protected Walk[] doInBackground(Void... params) {
            try {
                final String url = getResources().getString(R.string.rest_url) + "/walk";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add(FitmeApplication.TokenName,FitmeApplication.token);

                HttpEntity entity = new HttpEntity(headers);

                ResponseEntity<Walk[]> forNow = restTemplate.exchange(
                        url, HttpMethod.GET, entity, Walk[].class);

                return forNow.getBody();

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Walk[] searchList) {
            super.onPostExecute(searchList);
            if (searchList == null)
                return;
            walks.clear();
            walks.addAll(Arrays.asList(searchList));
            ArrayList<BarEntry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();
            for (Walk walk : walks) {
                entries.add(new BarEntry(walk.getDistance(), walks.indexOf(walk)));
                labels.add(walk.getDate().toString());
            }
            BarDataSet dataset = new BarDataSet(entries, "walks");
            BarData data = new BarData(labels, dataset);
            barChart.setData(data);
            dataset.setValueTextColor(Color.WHITE);
            dataset.setColor(getResources().getColor(R.color.colorAccent));
            dataset.setHighLightColor(getResources().getColor(R.color.colorAccent));
            barChart.notifyDataSetChanged();
            barChart.invalidate();

            scrollView.setDrawingCacheEnabled(true);
            scrollView.buildDrawingCache(true);
            Bitmap image = Bitmap.createBitmap(scrollView.getDrawingCache());
            scrollView.setDrawingCacheEnabled(false);

            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image)
                    .build();
            final SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            shareButton.setShareContent(content);

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareDialog dialog = new ShareDialog((Activity) v.getContext());
                    dialog.show(content, ShareDialog.Mode.AUTOMATIC);
                }
            });

        }

    }

    public class updateTodayWalk extends AsyncTask<Void, Void, Walk> {

        @Override
        protected Walk doInBackground(Void... params) {
            try {
                final String url = getResources().getString(R.string.rest_url) + "/walk/"
                        + String.valueOf(distance);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add(FitmeApplication.TokenName, FitmeApplication.token);

                HttpEntity entity = new HttpEntity(headers);
                ResponseEntity<Walk> todayWalk = null;
                try {
                    todayWalk = restTemplate.exchange(
                            url, HttpMethod.PUT, entity, Walk.class);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Unable to change today walk.", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), "Walking is updated.", Toast.LENGTH_SHORT).show();
                return todayWalk.getBody();

            } catch (Exception e) {
                Log.e("WalkTab", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Walk walk) {
            super.onPostExecute(walk);
            //TODO napravi racunanje koraka
            distance = 0;
        }
    }

    public class AddLocations extends AsyncTask<Void, Void, MyLocation[]> {

        @Override
        protected MyLocation[] doInBackground(Void... params) {
            try {
                final String url = getResources().getString(R.string.rest_url) + "/location";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add(FitmeApplication.TokenName, FitmeApplication.token);

                HttpEntity entity = new HttpEntity(headers);

                ResponseEntity<MyLocation[]> forNow = restTemplate.exchange(
                        url, HttpMethod.GET, entity, MyLocation[].class);

                return forNow.getBody();

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(MyLocation[] myLocations) {
            super.onPostExecute(myLocations);
            Boolean flag = false;
            Location firstLocation = new Location("");
            Location temp = new Location("");
            distance = 0;
            if (myLocations != null) {
                for (MyLocation loc : myLocations) {
                    if (!flag) {
                        firstLocation.setLatitude(loc.getLat());
                        firstLocation.setLongitude(loc.getLng());
                        flag = true;
                    } else {
                        temp.setLatitude(loc.getLat());
                        temp.setLongitude(loc.getLng());
                        fetchedLocations.add(new Location(temp));
                        distance += temp.distanceTo(firstLocation);
                        firstLocation.setLongitude(temp.getLongitude());
                        firstLocation.setLatitude(temp.getLatitude());
                    }
                }
                distanceTextView.setText(String.format("%.2f", distance / 1000) + " KILOMETERS WALKED");
            }
            new FetchUser().execute();
        }
    }

    private class FetchUser extends AsyncTask<Void, Void, User> {
        @Override
        protected User doInBackground(Void... params) {
            try {
                final String url = getResources().getString(R.string.rest_url) + "/users/current";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add(FitmeApplication.TokenName, FitmeApplication.token);

                HttpEntity entity = new HttpEntity(headers);

                ResponseEntity<User> forNow = restTemplate.exchange(
                        url, HttpMethod.GET, entity, User.class);

                return forNow.getBody();

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            stepsTextView.setText((int)(distance / user.calculateStep()) + " STEPS MADE");
            caloriesTextView.setText(user.calculateCalories(distance) + " CALORIES BURNED");
        }
    }

}
