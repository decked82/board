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

import kr.group.pm.board.model.FileDB;

@Mapper
public interface FileDBMapper {

	@Insert("INSERT INTO store(board_randomid, store_name, store_contenttype, store_size) "
			+ "VALUES(#{store.uuid}, #{store.name}, #{store.contenttype}, #{store.size})")
	int save(@Param("store") FileDB fileDB);
	
	@Update("UPDATE store SET store_fno=#{store.fno}, board_randomid=#{store.uuid} WHERE board_randomid=#{store.uuid}")
	int update(@Param("store") FileDB fileDB);
	
	@Delete("DELETE FROM store WHERE store_fno=#{fno}")
	int delete(int fno);
	
	@Select("SELECT * FROM store WHERE store_fno=#{fno}")
	@Results(id = "FileMap", value = {
			@Result(property = "fno", column = "store_fno"),
			@Result(property = "uuid", column = "board_randomid"),
			@Result(property = "name", column = "store_name"),
			@Result(property = "contenttype", column = "store_contenttype"),
			@Result(property = "size", column = "store_size"),
	})
	FileDB get(@Param("fno") int fno);
	
	@Select("SELECT * FROM store WHERE board_randomid=#{uuid}")
	@ResultMap("FileMap")
	List<FileDB> getListByBoard(@Param("uuid") String uuid);
	
	@Select("SELECT * FROM store WHERE board_randomid=#{uuid}")
	@ResultMap("FileMap")
	List<FileDB> getListFiles(@Param("uuid") String uuid);
	
	@Select("SELECT * FROM store")
	@ResultMap("FileMap")
	List<FileDB> getList();
	
}
