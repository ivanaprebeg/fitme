package hr.fitme.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.fitme.domain.FoodCategory;
import hr.fitme.dto.FoodCategoryDto;
import hr.fitme.services.IFoodCategoryService;

@RestController
@RequestMapping("/food-category")
public class FoodCategoryController {

	private final Logger LOG = LoggerFactory.getLogger(FoodCategoryController.class);
	
	@Autowired
	IFoodCategoryService foodCategoryService;
	
	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<FoodCategoryDto>> getFoodCategories( Principal principal ) {

        String name = principal.getName(); //get logged in username
		LOG.info("getting all users");
        List<FoodCategory> foodCategories = foodCategoryService.getAll();

        if (foodCategories == null || foodCategories.isEmpty()){
            LOG.info("no food categories found");
            return new ResponseEntity<List<FoodCategoryDto>>(HttpStatus.NO_CONTENT);
        }
        
        List<FoodCategoryDto> result = new ArrayList<FoodCategoryDto>();
        
        for (FoodCategory foodCategory : foodCategories) {
        	result.add(new FoodCategoryDto(foodCategory));
		}
        
        return new ResponseEntity<List<FoodCategoryDto>>(result, HttpStatus.OK);
    } 
	
}
