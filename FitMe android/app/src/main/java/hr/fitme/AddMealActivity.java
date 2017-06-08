package hr.fitme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import hr.fitme.adapters.FoodIntakeActivityRecyclerAdapter;
import hr.fitme.models.Food;
import hr.fitme.models.FoodCategory;
import hr.fitme.models.FoodIntake;
import hr.fitme.models.Meal;

public class AddMealActivity extends AppCompatActivity {

    private ArrayAdapter<FoodCategory> adapterCategory;
    private ArrayAdapter<Food> adapterFood;

    private Spinner categories;
    private Spinner foods;
    private EditText amount;
    private EditText mealName;

    private Button submitIntake;
    private FloatingActionButton submitMeal;

    private Meal meal;
    private RecyclerView addedIntakes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        meal = new Meal();

        new FetchCategories().execute();
        categories = (Spinner) findViewById(R.id.category_spinner);
        adapterCategory = new ArrayAdapter<FoodCategory>(AddMealActivity.this,
                android.R.layout.simple_spinner_item, new LinkedList<FoodCategory>());
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterFood = new ArrayAdapter<Food>(AddMealActivity.this, android.R.layout.simple_spinner_item,
                new LinkedList<Food>());
        adapterFood.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapterCategory);
        foods = (Spinner) findViewById(R.id.food_spinner);
        foods.setAdapter(adapterFood);
        foods.setVisibility(View.INVISIBLE);

        amount = (EditText) findViewById(R.id.amount);
        mealName = (EditText) findViewById(R.id.meal_name);

        submitIntake = (Button) findViewById(R.id.add_to_meal_button);
        submitMeal = (FloatingActionButton) findViewById(R.id.submit_meal_button);
        submitIntake.setEnabled(false);
        addedIntakes = (RecyclerView) findViewById(R.id.list);
        addedIntakes.setLayoutManager(new LinearLayoutManager(this));
        final FoodIntakeActivityRecyclerAdapter recAd = new FoodIntakeActivityRecyclerAdapter(meal.getIntakes(), new OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(FoodIntake item) {

            }
        });
        addedIntakes.setAdapter(recAd);
        submitIntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitMeal.setVisibility(View.VISIBLE);
                meal.addIntake(new FoodIntake((Food)foods.getSelectedItem(),
                        Float.valueOf(amount.getText().toString())));
                recAd.notifyDataSetChanged();

            }
        });
        submitMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meal.setName(mealName.getText().toString());
                new AddMeal().execute();
                startActivity(new Intent(AddMealActivity.this, MainActivity.class));
            }
        });

    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(FoodIntake item);
    }

    public class FetchCategories extends AsyncTask<Void, Void, FoodCategory[]> {

        @Override
        protected FoodCategory[] doInBackground(Void... params) {
            try {
                final String url = getResources().getString(R.string.rest_url) + "/food-category";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add(FitmeApplication.TokenName, FitmeApplication.token);

                HttpEntity entity = new HttpEntity(headers);

                ResponseEntity<FoodCategory[]> forNow = restTemplate.exchange(
                        url, HttpMethod.GET, entity, FoodCategory[].class);

                return forNow.getBody();

            } catch (Exception e) {
                Log.e("AddMealActivity", e.getMessage(), e);
                Toast.makeText(AddMealActivity.this, "Unable to fetch food categories.", Toast.LENGTH_SHORT).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(FoodCategory[] foodCategories) {
            super.onPostExecute(foodCategories);
            adapterCategory.clear();
            adapterCategory.addAll(Arrays.asList(foodCategories));
            categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    FoodCategory selected = adapterCategory.getItem(position);
                    adapterFood.clear();
                    new FetchFoodByCategory(selected.getCategoryId()).execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public class FetchFoodByCategory extends AsyncTask<Void, Void, Food[]> {
        int categoryId;
        public FetchFoodByCategory(int categoryId) {
            super();
            this.categoryId = categoryId;
        }

        @Override
        protected Food[] doInBackground(Void... params) {

            try {
                final String url = getResources().getString(R.string.rest_url) + "/food";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add(FitmeApplication.TokenName, FitmeApplication.token);

                HttpEntity entity = new HttpEntity(headers);


                HttpEntity<Integer> request = new HttpEntity<>(categoryId, headers);
                Food[] responseEntity = restTemplate.postForObject(url, request, Food[].class);

                return responseEntity;

            } catch (Exception e) {
                Log.e("AddMealActivity", e.getMessage(), e);
                Toast.makeText(AddMealActivity.this, "Unable to fetch food categories.", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Food[] foodsList) {
            super.onPostExecute(foodsList);
            adapterFood.clear();
            if (foodsList!=null)
                adapterFood.addAll(Arrays.asList(foodsList));
            foods.setVisibility(View.VISIBLE);
            foods.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    submitIntake.setEnabled(true);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }
    }

    public class AddMeal extends AsyncTask<Void, Void, Meal> {

        @Override
        protected Meal doInBackground(Void... params) {
            try {
                final String url = getResources().getString(R.string.rest_url) + "/meal";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add(FitmeApplication.TokenName, FitmeApplication.token);

                HttpEntity<Meal> request = new HttpEntity<>(meal, headers);
                Meal responseEntity = restTemplate.postForObject(url, request, Meal.class);

                return responseEntity;

            } catch (Exception e) {
                Log.e("AddMealActivity", e.getMessage(), e);
                Toast.makeText(AddMealActivity.this, "Unable to add meal.", Toast.LENGTH_SHORT).show();
                return new Meal();
            }
        }
    }
}
