package hr.fitme;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

import hr.fitme.adapters.MyFoodIntakeRecyclerViewAdapter;
import hr.fitme.models.Meal;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MealTab extends Fragment {

    private List<Meal> meals;
    private OnListFragmentInteractionListener mListener;
    private MyFoodIntakeRecyclerViewAdapter adapter;

    private FloatingActionButton fab;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MealTab() {
        meals = new LinkedList<>();
    }

    @Override
    public void onResume() {
        meals.clear();
        new FetchMeals().execute();
        super.onResume();
    }

    public static MealTab newInstance(int columnCount) {
        MealTab fragment = new MealTab();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meal_tab, container, false);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), AddMealActivity.class));
            }
        });

        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        adapter = new MyFoodIntakeRecyclerViewAdapter(meals, mListener,  getResources().getString(R.string.rest_url)+"/meal");
        recyclerView.setAdapter(adapter);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Meal item);
    }

    private class FetchMeals extends AsyncTask<Void, Void, Meal[]> {
        @Override
        protected Meal[] doInBackground(Void... params) {
            try {
                final String url = getResources().getString(R.string.rest_url) + "/meal/today";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add(FitmeApplication.TokenName,FitmeApplication.token);

                HttpEntity entity = new HttpEntity(headers);

                ResponseEntity<Meal[]> forNow = restTemplate.exchange(
                        url, HttpMethod.GET, entity, Meal[].class);
                return forNow.getBody();

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Meal[] searchList) {
            if(searchList != null) {
                Log.i("MainActivity", searchList.toString());
                meals.clear();
                for (Meal m : searchList) {
                    meals.add(m);
                }
                adapter.notifyDataSetChanged();
            }

        }

    }
}
