package kr.group.pm.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.group.pm.board.mapper.ReplyMapper;
import kr.group.pm.board.model.ReplyVO;

@Service
public class ReplyServiceImpl implements ReplyService {

	@Autowired
	private ReplyMapper replyMapper;
	
	@Override
	public List<ReplyVO> getList(int bno) {
		// TODO Auto-generated method stub
		return replyMapper.getList(bno);
	}

	@Transactional
	@Override
	public int add(ReplyVO reply) {
		// TODO Auto-generated method stub
		return replyMapper.insert(reply);
	}

	@Transactional
	@Override
	public int update(ReplyVO reply) {
		// TODO Auto-generated method stub
		return replyMapper.update(reply);
	}

	@Transactional
	@Override
	public int delete(int bno, int rno) {
		// TODO Auto-generated method stub
		return replyMapper.delete(bno, rno);
	}

	@Override
	public ReplyVO getReply(ReplyVO reply) {
		// TODO Auto-generated method stub
		return replyMapper.getReply(reply);
	}

}
