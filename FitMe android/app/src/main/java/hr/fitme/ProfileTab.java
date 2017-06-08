package hr.fitme;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import hr.fitme.models.User;
import hr.fitme.models.UserWeight;

/**
 * Created by ivanc on 06/05/2017.
 */

public class ProfileTab extends Fragment {
    private User user;
    private int adjustedCalories;
    private Boolean succes = false;
    private ProgressDialog progressDialog;
    private int calculatedCalorieIntake=0;

    private List<UserWeight> userWeights;

    private TextView username;
    private TextView height;
    private TextView weight;
    private TextView currBmi;
    private TextView calorieIntakeDaily;
    private TextView adjustedCalorieIntake;
    private SeekBar seekCalories;
    private Button submitBtn;
    private LineChart barChart;
    private Button addWeight;
    private String newWeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_tab, container, false);
        userWeights = new LinkedList<>();
        username = (TextView) rootView.findViewById(R.id.username_text_view);
        height = (TextView) rootView.findViewById(R.id.height_text_view);
        weight = (TextView) rootView.findViewById(R.id.weight_text_view);
        currBmi = (TextView) rootView.findViewById(R.id.current_bmi);
        submitBtn = (Button) rootView.findViewById(R.id.submit_button);
        submitBtn.setEnabled(false);
        calorieIntakeDaily = (TextView) rootView.findViewById(R.id.calculated_calorie_intake);
        adjustedCalorieIntake = (TextView) rootView.findViewById(R.id.adjusted_calorie_intake);
        seekCalories = (SeekBar) rootView.findViewById(R.id.seek_calorie);
        seekCalories.setProgress(500);
        new FetchUser().execute();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustedCalories = calculatedCalorieIntake - 500 + seekCalories.getProgress();
                new ChangeUser().execute();
            }
        });
        barChart = (LineChart) rootView.findViewById(R.id.weight_chart);

        setChart();
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                final int position = h.getXIndex();
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("On " + userWeights.get(position).getDate() + " your weight was " +
                    userWeights.get(position).getWeight());

                builder.setPositiveButton("Delete weight record", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DeleteUserWeight(userWeights.get(position)).execute();
                        LineData data = barChart.getLineData();
                        data.removeXValue(position);
                        barChart.setData(data);
                        barChart.invalidate();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        addWeight = (Button) rootView.findViewById(R.id.add_weight_button);
        addWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWeight();
            }
        });


        return rootView;
    }

    public void setChart() {
        barChart.setDescription("");
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getXAxis().setDrawLabels(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.setDrawGridBackground(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawAxisLine(false);
        barChart.setViewPortOffsets(-40f, 0f, 0f, 0f);
        barChart.setBackground(getResources().getDrawable(R.drawable.main_header_selector));
        barChart.getLegend().setEnabled(false);
        barChart.setExtraTopOffset(1);
    }


    private void addWeight() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Add new weight");

        final EditText input = new EditText(this.getContext());

        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        builder.setView(input);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               newWeight = input.getText().toString();
                user.setDesiredWeight(Float.valueOf(input.getText().toString()));
                new UpdateUserWeight().execute();
                new ChangeUser().execute();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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
        protected void onPostExecute(User searchList) {
            if (searchList != null) {
                Log.i("MainActivity", searchList.toString());
                user = searchList;
                //progressDialog.hide();
                username.setText(user.getUsername());
                height.setText("HEIGHT " + String.valueOf(user.getHeight()));
                adjustedCalorieIntake.setText("Adjust calorie intake.");
                new GetUserWeight().execute();
                seekCalories.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        adjustedCalories = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        adjustedCalorieIntake.setText("Adjusted calorie intake: " + String.valueOf(adjustedCalories - 500 + calculatedCalorieIntake));
                        submitBtn.setEnabled(true);
                    }
                });
            }

        }
    }

    private class ChangeUser extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... params) {
            try {
                user.setDesiredKcal(adjustedCalories);
                final String url = getResources().getString(R.string.rest_url) + "/users";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add(FitmeApplication.TokenName, FitmeApplication.token);

                HttpEntity entity = new HttpEntity(user,headers);
                ResponseEntity<User> forNow = null;
                try {
                    forNow = restTemplate.exchange(
                            url, HttpMethod.PUT, entity, User.class);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Unable to change user.", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), "User is changed.", Toast.LENGTH_SHORT).show();
                return forNow.getBody();

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }
    }

    private class UpdateUserWeight extends AsyncTask<Void, Void, UserWeight> {

        @Override
        protected UserWeight doInBackground(Void... params) {
            try {
                final String url = getResources().getString(R.string.rest_url) + "/user-weight";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add(FitmeApplication.TokenName, FitmeApplication.token);

                UserWeight nw = new UserWeight();
                nw.setDate(new Date(Calendar.getInstance().getTime().getTime()));
                nw.setWeight(Float.valueOf(newWeight));

                HttpEntity<UserWeight> request = new HttpEntity<>(nw, headers);
                return restTemplate.exchange(url, HttpMethod.POST, request, UserWeight.class).getBody();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(UserWeight userWeight) {
            super.onPostExecute(userWeight);
            Toast.makeText(getContext(), "Weight succesfully added.", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetUserWeight extends AsyncTask<Void, Void, UserWeight[]> {

        @Override
        protected UserWeight[] doInBackground(Void... params) {
            try {
                final String url = getResources().getString(R.string.rest_url) + "/user-weight";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add(FitmeApplication.TokenName, FitmeApplication.token);

                HttpEntity entity = new HttpEntity(headers);

                ResponseEntity<UserWeight[]> forNow = restTemplate.exchange(
                        url, HttpMethod.GET, entity, UserWeight[].class);

                return forNow.getBody();

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(UserWeight[] userWeight) {
            super.onPostExecute(userWeight);
            if (userWeight == null)
                return;
            userWeights.clear();
            userWeights.addAll(Arrays.asList(userWeight));
            weight.setText("WEIGHT " + String.valueOf(userWeights.get(userWeights.size()-1).getWeight()));
            currBmi.setText("BMI " + String.format("%.1f", (userWeights.get(userWeights.size()-1).getWeight() / (user.getHeight() * user.getHeight() / 10000))));
            calculatedCalorieIntake = user.calculateBMR(userWeights.get(userWeights.size()-1).getWeight());
            calorieIntakeDaily.setText("Calculated calorie intake: " + String.valueOf(calculatedCalorieIntake));
            ArrayList<Entry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();
            for (UserWeight weight : userWeights) {
                entries.add(new Entry(weight.getWeight(), userWeights.indexOf(weight)));
                labels.add(weight.getDate().toString());
            }
            LineDataSet dataset = new LineDataSet(entries, "Weight");
            LineData data = new LineData(labels, dataset);
            dataset.setDrawFilled(true);
            dataset.setFillColor(getResources().getColor(R.color.colorAccent));
            barChart.setData(data);
            dataset.setColor(getResources().getColor(R.color.colorAccent));
            dataset.setCircleColor(getResources().getColor(R.color.colorAccent));
            dataset.setValueTextColor(Color.WHITE);
            barChart.invalidate();
        }
    }

    public class DeleteUserWeight extends AsyncTask<Void, Void, UserWeight> {

        private UserWeight weight;

        public DeleteUserWeight(UserWeight weight) {
            this.weight = weight;
        }

        @Override
        protected UserWeight doInBackground(Void... params) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                final String url = getResources().getString(R.string.rest_url) + "/user-weight";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add(FitmeApplication.TokenName, FitmeApplication.token);

                HttpEntity entity = new HttpEntity(headers);
                restTemplate.exchange(url+"/delete/"+String.valueOf(weight.getId()), HttpMethod.DELETE, entity, Void.class);
                return null;
            } catch (Exception e) {
                Log.e("ProfileTab", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(UserWeight weight) {
            super.onPostExecute(weight);
            userWeights.remove(weight);
            barChart.notifyDataSetChanged();
        }
    }
}
