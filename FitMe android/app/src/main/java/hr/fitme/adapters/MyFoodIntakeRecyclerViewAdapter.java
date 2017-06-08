package hr.fitme.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import hr.fitme.AddMealActivity;
import hr.fitme.FitmeApplication;
import hr.fitme.MealTab.OnListFragmentInteractionListener;
import hr.fitme.R;
import hr.fitme.models.Meal;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyFoodIntakeRecyclerViewAdapter extends RecyclerView.Adapter<MyFoodIntakeRecyclerViewAdapter.ViewHolder> {

    private final List<Meal> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final String url;

    public static final int[] MYCOLORS = {
            Color.parseColor("#8BC34A"), Color.parseColor("#BDBDBD"), Color.parseColor("#795548")
    };

    public MyFoodIntakeRecyclerViewAdapter(List<Meal> items, OnListFragmentInteractionListener listener, String url) {
        mValues = items;
        mListener = listener;
        this.url = url;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.meal_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        HashMap<String, Integer> map = new HashMap<>();
        map = (HashMap<String, Integer>) mValues.get(position).calculateNutrition();
        holder.mealName.setText(mValues.get(position).getName());
        holder.carbs.setText("Carbs: " + map.get("carbs"));
        holder.protein.setText("Protein: " + map.get("protein"));
        holder.calories.setText("Kcal: " + map.get("kcal"));
        holder.fat.setText("Fat: " + map.get("fat"));
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteMeal(holder.mItem).execute();
            }
        });

        float carbs = Float.valueOf(map.get("carbs"));
        float protein = Float.valueOf(map.get("protein"));
        float fat = Float.valueOf(map.get("fat"));
        float sum = carbs + protein + fat;
        carbs /= sum;
        protein /= sum;
        fat /= sum;

        List<Entry> yvalues = new ArrayList<>();

        if (carbs>0)
            yvalues.add(new Entry(carbs, 0));

        if (protein>0)
            yvalues.add(new Entry(protein, 1));

        if (fat>0)
            yvalues.add(new Entry(fat, 2));


        PieDataSet dataSet = new PieDataSet(yvalues, "");
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        List<String> xVals = new ArrayList<>();

        xVals.add("Carbs");
        xVals.add("Protein");
        xVals.add("Fat");



        holder.pieChart.setDrawHoleEnabled(true);
        holder.pieChart.setTransparentCircleRadius(30f);
        holder.pieChart.setHoleRadius(50f);
        holder.pieChart.setDescription("");
        holder.pieChart.getLegend().setEnabled(false);

        PieData data = new PieData(xVals,dataSet);

        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);


        data.setValueFormatter(new PercentFormatter());
        holder.pieChart.setData(data);

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 06/06/2017 fix this
                Bundle bundle = new Bundle();
                bundle.putParcelable("meal", holder.mItem);
                bundle.putParcelableArrayList("intakes", new ArrayList<Parcelable>(holder.mItem.getIntakes()));
                Intent intent = new Intent(v.getContext(), AddMealActivity.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
            }
        });
        holder.mView.setDrawingCacheEnabled(true);
        holder.mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        holder.mView.layout(0, 0, holder.mView.getMeasuredWidth(), holder.mView.getMeasuredHeight());
        holder.mView.buildDrawingCache(true);
        Bitmap image = Bitmap.createBitmap(holder.mView.getDrawingCache());
        holder.mView.setDrawingCacheEnabled(false);

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        final SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        holder.shareButton.setShareContent(content);

        holder.shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog dialog = new ShareDialog((Activity) v.getContext());
                dialog.show(content, ShareDialog.Mode.AUTOMATIC);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mealName;
        public final TextView calories;
        public final TextView protein;
        public final TextView carbs;
        public final TextView fat;
        public final Button deleteButton;
        public final Button addButton;
        public Meal mItem;
        public PieChart pieChart;
        public ShareButton shareButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mealName = (TextView) view.findViewById(R.id.meal_name_view);
            calories = (TextView) view.findViewById(R.id.calories_text_view);
            protein = (TextView) view.findViewById(R.id.protein_text_view);
            carbs = (TextView) view.findViewById(R.id.carbs_text_view);
            fat = (TextView) view.findViewById(R.id.fat_text_view);
            deleteButton = (Button) view.findViewById(R.id.delete_meal_button);
            addButton = (Button) view.findViewById(R.id.add_intake_button);
            pieChart = (PieChart) view.findViewById(R.id.pie_chart);
            shareButton = (ShareButton) view.findViewById(R.id.share_on_facebook_button);
            pieChart.setUsePercentValues(true);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mealName.getText() + "'";
        }
    }

    public class DeleteMeal extends AsyncTask<Void, Void, Meal> {

        private Meal meal;

        public DeleteMeal(Meal meal) {
            this.meal = meal;
        }

        @Override
        protected Meal doInBackground(Void... params) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add(FitmeApplication.TokenName, FitmeApplication.token);

                HttpEntity entity = new HttpEntity(headers);
                restTemplate.exchange(url+"/delete/"+String.valueOf(meal.getId()), HttpMethod.DELETE, entity, Void.class);
                return null;
            } catch (Exception e) {
                Log.e("AddMealActivity", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(Meal meal) {
            super.onPostExecute(meal);
            int position = mValues.indexOf(meal);
            mValues.remove(meal);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mValues.size());
        }
    }

}
