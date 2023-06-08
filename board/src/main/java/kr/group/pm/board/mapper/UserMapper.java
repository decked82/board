package kr.group.pm.board.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import kr.group.pm.board.model.BoardVO;
import kr.group.pm.board.model.User;

@Mapper
public interface UserMapper {

	@Select("SELECT * FROM user WHERE user_id=#{id}")
	@Results(id = "UserMap", value = {
			@Result(property = "id", column = "user_id"),
			@Result(property = "username", column = "user_username"),
			@Result(property = "password", column = "user_password"),
	})
	User getUserById(@Param("id") Long id);
	
	@Insert("INSERT INTO user(user_username, user_password) VALUES(#{user.username}, #{user.password}")
	int save(@Param("user") User user);
	
	@Select("SELECT * FROM user WHERE board_bno=#{bno}")
	@ResultMap("UserMap")
	User getUserByBno(@Param("bno") int bno);
	
	@Select("SELECT * FROM user WHERE user_username=#{username}")
	@ResultMap("UserMap")
	User getUserByUsername(@Param("username") String username);
	
}
