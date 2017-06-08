package hr.fitme.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.fitme.domain.User;
import hr.fitme.domain.Walk;
import hr.fitme.dto.WalkDto;
import hr.fitme.services.IUserService;
import hr.fitme.services.IWalkService;

@RestController
@RequestMapping("/walk")
public class WalkController {

	private final Logger LOG = LoggerFactory.getLogger(WalkController.class);
	
	@Autowired
	IWalkService walkService;
	
	@Autowired
	IUserService userService;
	
	@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<WalkDto>> getWalks( Principal principal ) {

        String name = principal.getName(); //get logged in username
		LOG.info("getting all walks");
		User user = userService.findByUsername(name);
        List<Walk> walks = walkService.walksByUser(user);

        if (walks == null || walks.isEmpty()){
            LOG.info("no food walks found");
            return new ResponseEntity<List<WalkDto>>(HttpStatus.NO_CONTENT);
        }
        
        List<WalkDto> result = new ArrayList<WalkDto>();
        
        for (Walk wlk : walks) {
        	result.add(new WalkDto(wlk));
		}
        
        return new ResponseEntity<List<WalkDto>>(result, HttpStatus.OK);
    }
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{distance}")
	public ResponseEntity<WalkDto> updateWalk(Principal principal, @PathVariable("distance") float distance) {
		User user = userService.findByUsername(principal.getName());
		Walk todayWalk = walkService.todayWalk(user);
		float newDistance = todayWalk.getDistance() +  distance;
		todayWalk.setDistance(newDistance);
		walkService.update(todayWalk);
		return new ResponseEntity<WalkDto>(new WalkDto(todayWalk), HttpStatus.OK);
	}
	
}
