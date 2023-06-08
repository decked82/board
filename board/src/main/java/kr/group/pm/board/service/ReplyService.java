package kr.group.pm.board.service;

import java.util.List;

import kr.group.pm.board.model.ReplyVO;

public interface ReplyService {
	
	List<ReplyVO> getList(int bno);
	
	int add(ReplyVO reply);
	
	int update(ReplyVO reply);
	
	int delete(int bno, int rno);
	
	ReplyVO getReply(ReplyVO reply);
}
