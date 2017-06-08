package hr.fitme.dao;

import org.springframework.stereotype.Repository;

import hr.fitme.domain.FoodIntake;

@Repository
public class FoodIntakeDao extends CommonDao<FoodIntake, Integer> implements IFoodIntakeDao {

}
