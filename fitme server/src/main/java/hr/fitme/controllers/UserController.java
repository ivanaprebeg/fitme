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
import hr.fitme.dto.MealDto;
import hr.fitme.dto.UserDto;
import hr.fitme.services.IUserService;

@RestController
@RequestMapping("/users")
@Transactional
public class UserController {
	private final Logger LOG = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> getUsers() {
		LOG.info("getting all users");
        List<User> users = userService.getAll();

        if (users == null || users.isEmpty()){
            LOG.info("no users found");
            return new ResponseEntity<List<UserDto>>(HttpStatus.NO_CONTENT);
        }
        
        List<UserDto> result = new ArrayList<UserDto>();
        
        for (User user : users) {
        	result.add(new UserDto(user));
		}
        
        return new ResponseEntity<List<UserDto>>(result, HttpStatus.OK);
    } 
	
	@RequestMapping(value="/current", method = RequestMethod.GET)
    public ResponseEntity<UserDto> getCurrentUser(Principal principal ) {
		LOG.info("getting current user");
        String name = principal.getName(); //get logged in username
        User user = userService.findByUsername(name);
       
        if (user == null){
            LOG.info("user not found");
            return new ResponseEntity<UserDto>(HttpStatus.NO_CONTENT);
        }     
        return new ResponseEntity<UserDto>(new UserDto(user), HttpStatus.OK);
    } 
	
	
	
//	@RequestMapping(value = "{id}", method = RequestMethod.GET)
//	public ResponseEntity<UserDto> getById(int id) {
//		LOG.info("searching for user by id: " + String.valueOf(id));
//		User user = userService.getById(id);
//		if (user == null) {
//			LOG.info("user not found. ");
//			return new ResponseEntity<UserDto>(HttpStatus.NOT_FOUND);
//		}	
//		else
//			return new ResponseEntity<UserDto>(new UserDto(user), HttpStatus.OK);
//	}
//    
//	@RequestMapping(method = RequestMethod.POST)
//    public ResponseEntity<UserDto> addUser(@RequestBody User user){
//		LOG.info("adding user: " + user.getUsername());
//		if (userService.findByUsername(user.getUsername()) != null)
//			return new ResponseEntity<UserDto>(HttpStatus.CONFLICT);
//		UserDto addUser = new UserDto(userService.add(user));
//		if (userService.findByUsername(user.getUsername()) != null)
//			return new ResponseEntity<UserDto>(addUser, HttpStatus.CREATED);
//		return new ResponseEntity<UserDto>(HttpStatus.PRECONDITION_FAILED);
//    }
	
	@RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<UserDto> updateCurrentUser(Principal principal, @RequestBody UserDto user) throws Exception {
		LOG.info("changing current user");
        userService.updateUser(new User(user));
        return new ResponseEntity<UserDto>(user, HttpStatus.OK);
    } 
	
//    
//    @RequestMapping(method = RequestMethod.DELETE)
//    public ResponseEntity<Void> deleteUser(@RequestBody User user){
//    	LOG.info("deleting user: " + user.getUsername());
//    	userService.delete(user);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
