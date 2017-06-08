package hr.fitme.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by ivanc on 21/05/2017.
 */

public class Meal implements Parcelable{
    private LinkedList<FoodIntake> intakes;
    private String name;
    private int id;

    public Meal() {
        intakes = new LinkedList<>();
    }

    protected Meal(Parcel in) {
        name = in.readString();
        id = in.readInt();
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    public LinkedList<FoodIntake> getIntakes() {
        return intakes;
    }

    public void setIntakes(LinkedList<FoodIntake> intakes) {
        this.intakes = intakes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addIntake(FoodIntake intake) {
        intakes.add(intake);
    }

    public Map<String, Integer> calculateNutrition() {
        int protein = 0;
        int sugar = 0;
        int fiber = 0;
        int kcal = 0;
        int carbs = 0;
        int fat = 0;
        for (FoodIntake intake : intakes) {
            float amount = intake.getAmount() / 100;
            Food food = intake.getFood();
            protein += food.getProtein() * amount;
            kcal += food.getCalories() * amount;
            carbs += food.getCarbs() * amount;
            fat += food.getFat() * amount;
        }

        Map<String, Integer> meal = new HashMap<>();
        meal.put("protein", protein);
        meal.put("kcal", kcal);
        meal.put("carbs", carbs);
        meal.put("fat", fat);

        return meal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(id);
    }
}
