package hr.fitme.controllers;

import java.security.Principal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.fitme.domain.FoodIntake;
import hr.fitme.domain.Meal;
import hr.fitme.domain.User;
import hr.fitme.dto.FoodIntakeDto;
import hr.fitme.dto.MealDto;
import hr.fitme.services.IFoodIntakeService;
import hr.fitme.services.IMealService;
import hr.fitme.services.IUserService;

@Transactional
@RestController
@RequestMapping("/food-intake")
public class FoodIntakeController {

	private final Logger LOG = LoggerFactory.getLogger(FoodIntakeController.class);
	
	@Autowired
	IFoodIntakeService foodIntakeService;
	
	@Autowired
	IMealService mealService;
	
	@Autowired
	IUserService userService;
	
	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<FoodIntakeDto>> getFoodIntakes(Principal principal ) {

        String name = principal.getName(); //get logged in username

		
		LOG.info("getting all intakes");
        List<FoodIntake> foodIntakes = foodIntakeService.getAll();

        if (foodIntakes == null || foodIntakes.isEmpty()){
            LOG.info("no food intakes found");
            return new ResponseEntity<List<FoodIntakeDto>>(HttpStatus.NO_CONTENT);
        }
        
        List<FoodIntakeDto> result = new ArrayList<FoodIntakeDto>();
        
        for (FoodIntake foodIntake : foodIntakes) {
        	result.add(new FoodIntakeDto(foodIntake));
		}
        
        return new ResponseEntity<List<FoodIntakeDto>>(result, HttpStatus.OK);
    } 
	
	@RequestMapping(value="/meals", method = RequestMethod.GET)
    public ResponseEntity<List<MealDto>> getMeals(Principal principal ) {
		LOG.info("getting all meals");
        String name = principal.getName(); //get logged in username
        User user = userService.findByUsername(name);
		
        List<Meal> meals = mealService.getForUser(user);
        
        if (meals == null || meals.isEmpty()){
            LOG.info("no meals intakes found");
            return new ResponseEntity<List<MealDto>>(HttpStatus.NO_CONTENT);
        }
        
        List<MealDto> result = new ArrayList<MealDto>();
        
        for (Meal meal : meals) {
        	result.add(new MealDto(meal));
		}
        
        return new ResponseEntity<List<MealDto>>(result, HttpStatus.OK);
    } 
	
	
	@RequestMapping(value="/meals-today", method = RequestMethod.GET)
    public ResponseEntity<List<MealDto>> getMealsToday(Principal principal ) {
		LOG.info("getting all meals today");
        String name = principal.getName(); //get logged in username
        User user = userService.findByUsername(name);
        Date today = new Date(Calendar.getInstance().getTime().getTime());
		
        List<Meal> meals = mealService.getForUser(user);
        
        if (meals == null || meals.isEmpty()){
            LOG.info("no meals intakes found");
            return new ResponseEntity<List<MealDto>>(HttpStatus.NO_CONTENT);
        }
        
        List<MealDto> result = new ArrayList<MealDto>();
        
        for (Meal meal : meals) {
        	if (meal.getDate() == today)
        		result.add(new MealDto(meal));
		}
        
        return new ResponseEntity<List<MealDto>>(result, HttpStatus.OK);
    } 
	
}
