package kr.group.pm.board.service;

import java.util.List;

import kr.group.pm.board.model.BoardVO;


public interface BoardService {
	
	int add(BoardVO boardVO);
	
	int update(BoardVO boardVO);
	
	int delete(int bno);
	
	int getCounterBySearch(String searchType, String keyword);
	
	BoardVO getBoard(int bno);
	
	List<BoardVO> getListPageSearch(int displayPost, int postNum, String searchType, String keyword);
	
	int updateViewCount(int bno);
}
