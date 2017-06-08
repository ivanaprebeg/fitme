package hr.fitme.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import hr.fitme.AddMealActivity;
import hr.fitme.R;
import hr.fitme.models.FoodIntake;


public class FoodIntakeActivityRecyclerAdapter extends RecyclerView.Adapter<FoodIntakeActivityRecyclerAdapter.ViewHolder> {

    private final List<FoodIntake> mValues;
    private final AddMealActivity.OnListFragmentInteractionListener mListener;

    public FoodIntakeActivityRecyclerAdapter(List<FoodIntake> items, AddMealActivity.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_meal_food_intake_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.foodIntakeName.setText(holder.mItem.getFood().getName());
        holder.foodIntakeAmount.setText(String.valueOf(holder.mItem.getAmount()) + " g");
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValues.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView foodIntakeName;
        public final TextView foodIntakeAmount;
        public final Button delete;
        public FoodIntake mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            foodIntakeAmount = (TextView) view.findViewById(R.id.text_amount_food_intake);
            foodIntakeName = (TextView) view.findViewById(R.id.text_name_food_intake);
            delete = (Button) view.findViewById(R.id.button_delete_food_intake);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mItem.getFood() + "'";
        }
    }
}
