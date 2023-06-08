package kr.group.pm.board.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import kr.group.pm.board.model.FileDB;

public interface StorageService {

	void init();
	
	int deleteFile(int fno);
	
	int store(MultipartFile file, String uuid);
	
	FileDB getFile(int fno);
	
	Stream<FileDB> getAllFiles();
	
	List<FileDB> getFiles(String uuid);
}
