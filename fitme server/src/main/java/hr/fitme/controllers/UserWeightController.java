package hr.fitme.controllers;

import java.security.Principal;
import java.util.ArrayList;
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

import hr.fitme.domain.Meal;
import hr.fitme.domain.User;
import hr.fitme.domain.UserWeight;
import hr.fitme.dto.MealDto;
import hr.fitme.dto.UserWeightDto;
import hr.fitme.services.IUserService;
import hr.fitme.services.IUserWeightService;

@Transactional
@RestController
@RequestMapping("/user-weight")
public class UserWeightController {
	
	private final Logger LOG = LoggerFactory.getLogger(FoodIntakeController.class);
	
	@Autowired
	IUserService userService;
	
	@Autowired
	IUserWeightService userWeightService;
	
	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserWeightDto>> getMeals(Principal principal ) {
		LOG.info("getting all user weights");
        String name = principal.getName(); 
        User user = userService.findByUsername(name);
		
        List<UserWeight> weights = userWeightService.getForUser(user);
        
        if (weights == null || weights.isEmpty()){
            LOG.info("no weights for user found");
            return new ResponseEntity<List<UserWeightDto>>(HttpStatus.NO_CONTENT);
        }
        
        List<UserWeightDto> result = new ArrayList<UserWeightDto>();
        
        for (UserWeight weight : weights) {
        	result.add(new UserWeightDto(weight));
		}
        
        return new ResponseEntity<List<UserWeightDto>>(result, HttpStatus.OK);
    } 
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<UserWeightDto> addWeight(@RequestBody UserWeightDto userWeight, Principal principal) {

		UserWeight newWeight = new UserWeight();
		newWeight.setDate(userWeight.getDate());
		newWeight.setUser(userService.findByUsername(principal.getName()));
		newWeight.setWeight(userWeight.getWeight());
		
		userWeightService.add(newWeight);

        return new ResponseEntity<UserWeightDto>(userWeight, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/delete/{id}")
	public ResponseEntity<UserWeightDto> deleteUserWeight (@PathVariable("id") int id) {
		UserWeight weight = userWeightService.getById(id);
		userWeightService.delete(weight);
		return new ResponseEntity<UserWeightDto>(new UserWeightDto(weight), HttpStatus.OK);
	}
	

}
