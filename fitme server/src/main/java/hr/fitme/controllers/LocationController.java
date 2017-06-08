package hr.fitme.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.fitme.domain.Location;
import hr.fitme.domain.User;
import hr.fitme.dto.LocationDto;
import hr.fitme.services.ILocationService;
import hr.fitme.services.IUserService;

@Transactional
@RestController
@RequestMapping("/location")
public class LocationController {
	
	private final Logger LOG = LoggerFactory.getLogger(FoodIntakeController.class);
	
	@Autowired
	ILocationService locationService;
	
	@Autowired
	IUserService userService;
	
	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<LocationDto>> getLocations(Principal principal ) {

        User user = userService.findByUsername(principal.getName());

		LOG.info("getting locations");
        List<Location> locations = locationService.getLocations(user);

        if (locations == null || locations.isEmpty()){
            LOG.info("no locations found");
            return new ResponseEntity<List<LocationDto>>(HttpStatus.NO_CONTENT);
        }
        
        List<LocationDto> result = new ArrayList<LocationDto>();
        
        for (Location location : locations) {
        	result.add(new LocationDto(location));
		}
        
        return new ResponseEntity<List<LocationDto>>(result, HttpStatus.OK);
    }
	
	@RequestMapping(method= RequestMethod.POST)
	public ResponseEntity<LocationDto> addLocation(@RequestBody Location location, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		location.setUser(user);
		location.setDate(new Date());
		LOG.info("saving location");
		locationService.add(location);
		return new ResponseEntity<LocationDto>(new LocationDto(location), HttpStatus.OK);
		
	}

}
