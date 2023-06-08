package kr.group.pm.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.group.pm.board.model.ReplyVO;

@Mapper
public interface ReplyMapper {

	@Select("SELECT * FROM reply WHERE board_bno=#{bno}")
	@Results(id = "ReplyMap", value = {
			@Result(property = "bno", column = "board_bno"),
			@Result(property = "rno", column = "reply_rno"),
			@Result(property = "writer", column = "reply_writer"),
			@Result(property = "content", column = "reply_content"),
			@Result(property = "regDate", column = "reply_regDate"),
	})
	List<ReplyVO> getList(@Param("bno") int bno);
	
	@Select("SELECT * FROM reply WHERE board_bno=#{reply.bno} AND reply_rno=#{reply.rno}")
	@ResultMap("ReplyMap")
	ReplyVO getReply(@Param("reply") ReplyVO reply);
	
	@Insert("INSERT INTO reply(board_bno, reply_writer, reply_content) "
			+ "VALUES(#{reply.bno}, #{reply.writer}, #{reply.content})")
	int insert(@Param("reply") ReplyVO reply);
	
	@Update("UPDATE reply SET reply_writer=#{reply.writer}, reply_content=#{reply.content} "
			+ "WHERE reply_rno=#{reply.rno} AND board_bno=#{reply.bno}")
	int update(@Param("reply") ReplyVO reply);
	
	@Delete("DELETE FROM reply WHERE reply_rno=#{rno} AND board_bno=#{bno}")
	int delete(@Param("bno") int bno, @Param("rno") int rno);
}
