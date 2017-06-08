package hr.fitme.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "food_category")
public class FoodCategory implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int categoryId;
	
//	@OneToMany(fetch = FetchType.EAGER, mappedBy = "category")
//	private Set<Food> foods;
	
	@Column(name="name")
	private String name;
	
	public FoodCategory() {
	}
	
	public FoodCategory(int id) {
		this.categoryId = id;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

//	public Set<Food> getFoods() {
//		return foods;
//	}
//
//	public void setFoods(Set<Food> id) {
//		this.foods = id;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
