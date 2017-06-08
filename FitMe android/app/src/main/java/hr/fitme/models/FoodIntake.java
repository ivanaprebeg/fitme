package hr.fitme.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ivanc on 06/05/2017.
 */

public class FoodIntake implements Parcelable{

    private Food food;

    private float amount;

    private int id;

    public FoodIntake() {
    }

    public FoodIntake(Food food, float amount) {
        this.amount = amount;
        this.food = food;
    }

    protected FoodIntake(Parcel in) {
        amount = in.readFloat();
        id = in.readInt();
    }

    public static final Creator<FoodIntake> CREATOR = new Creator<FoodIntake>() {
        @Override
        public FoodIntake createFromParcel(Parcel in) {
            return new FoodIntake(in);
        }

        @Override
        public FoodIntake[] newArray(int size) {
            return new FoodIntake[size];
        }
    };

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(amount);
        dest.writeInt(id);
    }
}
