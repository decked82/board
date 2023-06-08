package kr.group.pm.board.config;

import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.group.pm.board.model.BoardVO;
import kr.group.pm.board.model.User;
import kr.group.pm.board.service.BoardService;
import kr.group.pm.board.service.UserService;

@Component
public class DataInitializer {

	@Autowired
	BoardService boardService;
	@Autowired
	UserService userService;
	
	//@PostConstruct
	public void init() {
		for (int i=0; i<10; ++i) {
			User user = new User("작성자 " + i);
			BoardVO board = new BoardVO();
			board.setTitle("테목 "+i);
//			board.setWriter("작성자 " + i);
			board.setContent("콘텐츠"+i);
			board.setUuid(UUID.randomUUID().toString());
			
			boardService.add(board);
		}
	}
	
	public void initUsers() {
		for (int i=0;i<3; ++i) {
			User user = new User();
			user.setUsername("홍길동 " + i);
			user.setPassword("1234");
			
			userService.save(user);
		}
	}
}
