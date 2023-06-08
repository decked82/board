package kr.group.pm.board.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.group.pm.board.mapper.BoardMapper;
import kr.group.pm.board.mapper.FileDBMapper;
import kr.group.pm.board.model.BoardVO;


@Service
public class BoardServiceImpl implements BoardService {

	private BoardMapper boardMapper;
	private FileDBMapper fileDBMapper;

	public BoardServiceImpl(BoardMapper boardMapper, FileDBMapper fileDBMapper) {
		super();
		this.boardMapper = boardMapper;
		this.fileDBMapper = fileDBMapper;
	}

	@Transactional
	@Override
	public int add(BoardVO board) {
		// TODO Auto-generated method stub
		return boardMapper.insert(board);
	}

	@Transactional
	@Override
	public int update(BoardVO board) {
		// TODO Auto-generated method stub
		return boardMapper.update(board);
	}

	@Transactional
	@Override
	public int delete(int bno) {
		// TODO Auto-generated method stub
		return boardMapper.delete(bno);
	}

	@Override
	public BoardVO getBoard(int bno) {
		// TODO Auto-generated method stub
		BoardVO board = boardMapper.getBoard(bno);
		if (board != null) {
			board.setFiles(fileDBMapper.getListFiles(board.getUuid()));
		}

		return board;
	}

	@Override
	public int getCounterBySearch(String searchType, String keyword) {
		// TODO Auto-generated method stub
		return boardMapper.getCounterBySearch(searchType, keyword);
	}

	@Override
	public List<BoardVO> getListPageSearch(int displayPost, int postNum, String searchType, String keyword) {
		// TODO Auto-generated method stub
		List<BoardVO> boardList = boardMapper.getListPageSearch(displayPost, postNum, searchType, keyword);
		if (boardList != null && boardList.size() > 0) {
			for (BoardVO board : boardList) {
				board.setFiles(fileDBMapper.getListFiles(board.getUuid()));
			}
		}
		return boardList;
	}

	@Override
	public int updateViewCount(int bno) {		
		return boardMapper.incrementViews(bno);
	}

}
