package kr.group.pm.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.group.pm.board.model.BoardVO;

@Mapper
public interface BoardMapper {
	
	@Select("<script>"
			+ "SELECT COUNT(board_bno) FROM board"
			+ "<if test='searchType.equals(\"title\")'> WHERE board_title LIKE concat('%', #{keyword}, '%')</if>"
			+ "<if test='searchType.equals(\"content\")'> WHERE board_content LIKE concat('%', #{keyword}, '%')</if>"
			+ "<if test='searchType.equals(\"title_content\")'> WHERE board_title LIKE concat('%', #{keyword}, '%') "
			+ "OR board_content LIKE concat('%', #{keyword}, '%')</if>"
			+ "<if test='searchType.equals(\"writer\")'> WHERE board_writer LIKE concat('%', #{keyword}, '%')</if>"
			+ "</script>")
	int getCounterBySearch(@Param("searchType") String searchType, @Param("keyword") String keyword);
	
	@Select("SELECT COUNT(board_bno) FROM board WHERE board_title LIKE concat('%', #{keyword}, '%')")
	int getSearchCount();
	
	@Select("SELECT * FROM board WHERE board_bno=#{bno}")
	@Results(id = "BoardMap", value = {
			@Result(property = "bno", column = "board_bno"),
			@Result(property = "uuid", column = "board_randomid"),
			@Result(property = "title", column = "board_title"),
			@Result(property = "writer", column = "board_writer"),
			@Result(property = "content", column = "board_content"),
			@Result(property = "regDate", column = "board_regDate"),
			@Result(property = "viewCnt", column = "board_viewCnt"),
			@Result(property = "replies", column = "board_bno", many = @Many(select = "kr.group.pm.board.mapper.ReplyMapper.getList")),
			@Result(property = "user", column = "user_id", one = @One(select = "kr.group.pm.board.mapper.UserMapper.getUserById"))
	})
	BoardVO getBoard(@Param("bno") int bno);
	
	@Select("<script>"
			+ "SELECT * FROM board"
			+ "<if test='searchType.equals(\"title\")'> WHERE board_title LIKE concat('%', #{keyword}, '%')</if>"
			+ "<if test='searchType.equals(\"content\")'> WHERE board_content LIKE concat('%', #{keyword}, '%')</if>"
			+ "<if test='searchType.equals(\"title_content\")'> WHERE board_title LIKE concat('%', #{keyword}, '%') "
			+ "OR board_content LIKE concat('%', #{keyword}, '%')</if>"
			+ "<if test='searchType.equals(\"writer\")'> WHERE user_id IN (SELECT user_id FROM user WHERE user_username LIKE concat('%', #{keyword}, '%'))</if>"
//			+ "<if test='searchType.equals(\"writer\")'> WHERE board_writer LIKE concat('%', #{keyword}, '%')</if>"
			+ "ORDER BY board_bno DESC LIMIT #{displayPost}, #{postNum}"
			+ "</script>")
	@ResultMap("BoardMap")
	List<BoardVO> getListPageSearch(@Param("displayPost") int displayPost, @Param("postNum") int postNum, 
									@Param("searchType") String searchType, @Param("keyword") String keyword);

	@Insert("INSERT INTO board(user_id, board_randomid, board_title, board_content, board_writer) "
			+ "VALUES(#{board.user}, #{board.uuid}, #{board.title}, #{board.content}, #{board.writer})")
	int insert(@Param("board") BoardVO board);
	
	@Update("UPDATE board SET board_title=#{board.title}, board_content=#{board.content}, "
			+ "board_writer=#{board.writer} WHERE board_bno=#{board.bno}")
	int update(@Param("board") BoardVO board);

	@Delete("DELETE FROM board WHERE board_bno=#{bno}")
	int delete(@Param("bno") int bno);

	@Update("UPDATE board SET board_viewCnt=board_viewCnt+1 WHERE board_bno=#{bno}")
	int incrementViews(@Param("bno") int bno);

}
