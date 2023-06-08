package kr.group.pm.board.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.group.pm.board.model.User;
import kr.group.pm.board.service.UserService;

@RestController
@RequestMapping("/api")
public class UserRestController {

	private UserService userService;

	public UserRestController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> login(HttpServletRequest request, @RequestBody Map<String, Object> params) {
		HttpSession session = request.getSession(false);
		String username = (String) params.get("username");
		String password = (String) params.get("password");

		User user = userService.getUserByUsername(username);
		String username_db = user.getUsername();
		String password_db =  user.getPassword();
		System.out.println(username);
		System.out.println(password);
		if ((username_db).equals(username) && password_db.equals(password)) {
			session.setAttribute("isLogged", true);
			session.setAttribute("userId", user.getId());
			return ResponseEntity.status(HttpStatus.OK).build();
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
	}

	@GetMapping("/logout")
	public ResponseEntity<Void> logoutEntity(HttpServletRequest request) {
		HttpSession session =request.getSession();
		
		if (session != null) {
			session.invalidate();
			session = null;
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("user")
	public ResponseEntity<User> showUser(@RequestParam Long id) {
		return new ResponseEntity<>(userService.getUser(id), HttpStatus.OK);
	}
	
}
