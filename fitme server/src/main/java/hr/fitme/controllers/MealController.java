package hr.fitme.controllers;

import java.security.Principal;
import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.fitme.domain.FoodIntake;
import hr.fitme.domain.Meal;
import hr.fitme.domain.User;
import hr.fitme.dto.FoodIntakeDto;
import hr.fitme.dto.MealDto;
import hr.fitme.services.IFoodCategoryService;
import hr.fitme.services.IFoodIntakeService;
import hr.fitme.services.IFoodService;
import hr.fitme.services.IMealService;
import hr.fitme.services.IUserService;

@Transactional
@RestController
@RequestMapping("/meal")
public class MealController {
	
	private final Logger LOG = LoggerFactory.getLogger(FoodIntakeController.class);

	@Autowired
	IFoodIntakeService foodIntakeService;
	
	@Autowired
	IMealService mealService;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	IFoodService foodService;
	
	@Autowired
	IFoodCategoryService foodCategoryService;
	
	@RequestMapping(value="/today", method = RequestMethod.GET)
    public ResponseEntity<List<MealDto>> getMealsToday(Principal principal ) {
		LOG.info("getting all meals today");
        String name = principal.getName(); //get logged in username
        User user = userService.findByUsername(name);
		
        List<Meal> meals = mealService.getMealsToday(user);
        
        if (meals == null || meals.isEmpty()){
            LOG.info("no meals intakes found");
            return new ResponseEntity<List<MealDto>>(HttpStatus.NO_CONTENT);
        }
        
        List<MealDto> result = new ArrayList<MealDto>();
        
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
        
        for (Meal meal : meals) {
        	if (dtf.format(meal.getDate().toInstant().atZone(ZoneId.systemDefault())
        			.toLocalDate()).equals(dtf.format(localDate)))
        		result.add(new MealDto(meal));
		}
        
        return new ResponseEntity<List<MealDto>>(result, HttpStatus.OK);
    }
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<MealDto> addMeal(Principal principal, @RequestBody MealDto meal) {
		User user = userService.findByUsername(principal.getName());
		Date today = new Date(Calendar.getInstance().getTime().getTime());
		Meal newMeal = new Meal(meal,user);
		newMeal.setDate(today);
		HashSet<FoodIntake> intakes = new HashSet<FoodIntake>();
		for (FoodIntakeDto intake : meal.getIntakes()) {
			intakes.add(new FoodIntake(intake, user));
		}
		newMeal.setIntakes(intakes);
		return new ResponseEntity<MealDto>(new MealDto(mealService.add(newMeal)), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/delete/{id}")
	public ResponseEntity<MealDto> deleteMeal (@PathVariable("id") int id) {
		Meal meal = mealService.getById(id);
		mealService.delete(meal);
		return new ResponseEntity<MealDto>(new MealDto(meal), HttpStatus.OK);
	}
	
}
