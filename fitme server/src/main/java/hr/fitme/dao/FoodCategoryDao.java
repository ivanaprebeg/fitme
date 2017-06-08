package hr.fitme.dao;

import org.springframework.stereotype.Repository;

import hr.fitme.domain.FoodCategory;

@Repository
public class FoodCategoryDao extends CommonDao<FoodCategory, Integer> implements IFoodCategoryDao {
}
