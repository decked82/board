package kr.group.pm.board.service;

import kr.group.pm.board.model.User;

public interface UserService {
	
	User getUser(Long id);
	
	int save(User user);
	
	User getUserByUsername(String username);
	
}
