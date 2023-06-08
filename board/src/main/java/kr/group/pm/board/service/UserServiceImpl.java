package kr.group.pm.board.service;

import org.springframework.stereotype.Service;

import kr.group.pm.board.mapper.UserMapper;
import kr.group.pm.board.model.User;

@Service
public class UserServiceImpl implements UserService {

	private UserMapper userMapper;
	
	public UserServiceImpl(UserMapper userMapper) {
		super();
		this.userMapper = userMapper;
	}


	@Override
	public User getUser(Long id) {
		return userMapper.getUserById(id);
		//return null;
	}


	@Override
	public int save(User user) {
		
		return userMapper.save(user);
	}


	@Override
	public User getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return userMapper.getUserByUsername(username);
	}

}
