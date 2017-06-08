package hr.fitme.models;

/**
 * Created by ivanc on 12/05/2017.
 */

public class FoodCategory {
    private int categoryId;

    private String name;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public FoodCategory() {
    }

    public FoodCategory(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }
}
