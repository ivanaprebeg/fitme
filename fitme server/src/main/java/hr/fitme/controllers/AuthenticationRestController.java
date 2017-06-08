package hr.fitme.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import hr.fitme.auth.CustomAuthManager;
import hr.fitme.auth.JwtAuthenticationRequest;
import hr.fitme.auth.JwtAuthenticationResponse;
import hr.fitme.auth.JwtTokenUtil;
import hr.fitme.auth.JwtUser;
import hr.fitme.domain.User;
import hr.fitme.dto.UserDto;
import hr.fitme.services.IUserService;
import hr.fitme.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthenticationRestController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private CustomAuthManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        String refreshedToken = jwtTokenUtil.refreshToken(token);
        return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
    }
    
    
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationTokenRegister(@RequestBody UserDto user) throws AuthenticationException {
    	
    	if (userService.findByUsername(user.getUsername()) != null) {
            return (ResponseEntity<?>) ResponseEntity.badRequest();
    	}
    	userService.add(new User(user));
    	JwtAuthenticationRequest authenticationRequest = new JwtAuthenticationRequest();
    	authenticationRequest.setUsername(user.getUsername());
    	authenticationRequest.setPassword(user.getPassword());
    	
    	
        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }
    
//	@RequestMapping(value = "/register", method = RequestMethod.POST)
//    public ResponseEntity<UserDto> addUser(@RequestBody User user){
//		if (userService.findByUsername(user.getUsername()) != null)
//			return new ResponseEntity<UserDto>(HttpStatus.CONFLICT);
//		UserDto addUser = new UserDto(userService.add(user));
//		if (userService.findByUsername(user.getUsername()) != null)
//			return new ResponseEntity<UserDto>(addUser, HttpStatus.CREATED);
//		return new ResponseEntity<UserDto>(HttpStatus.PRECONDITION_FAILED);
//    }

}
