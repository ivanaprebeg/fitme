package hr.fitme.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.fitme.domain.Food;
import hr.fitme.domain.FoodCategory;
import hr.fitme.dto.FoodDto;
import hr.fitme.services.IFoodCategoryService;
import hr.fitme.services.IFoodService;

@RestController
@RequestMapping("/food")
public class FoodController {

	private final Logger LOG = LoggerFactory.getLogger(FoodController.class);
	
	@Autowired
	IFoodService foodService;
	
	@Autowired
	IFoodCategoryService foodCategoryService;
	
	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<FoodDto>> getFoods() {
        List<Food> foods = foodService.getAll();

        if (foods == null || foods.isEmpty()){
            LOG.info("no foods found");
            return new ResponseEntity<List<FoodDto>>(HttpStatus.NO_CONTENT);
        }
        
        List<FoodDto> result = new ArrayList<FoodDto>();
        
        for (Food food : foods) {
        	result.add(new FoodDto(food));
		}
        
        return new ResponseEntity<List<FoodDto>>(result, HttpStatus.OK);
    }
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<List<FoodDto>> getFoodsByCategory(@RequestBody int categoryId) {
		FoodCategory category = foodCategoryService.getById(categoryId);
		
		List<Food> foods = foodService.findByCategory(category);

        if (foods == null || foods.isEmpty()){
            LOG.info("no foods found");
            return new ResponseEntity<List<FoodDto>>(HttpStatus.NO_CONTENT);
        }
        
        List<FoodDto> result = new ArrayList<FoodDto>();
        
        for (Food food : foods) {
        	result.add(new FoodDto(food));
		}
        
        return new ResponseEntity<List<FoodDto>>(result, HttpStatus.OK);
	}
	
}