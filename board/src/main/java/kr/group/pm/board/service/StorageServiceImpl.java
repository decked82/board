package kr.group.pm.board.service;


import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.group.pm.board.mapper.FileDBMapper;
import kr.group.pm.board.model.FileDB;


@Service
public class StorageServiceImpl implements StorageService {

	@Autowired
	private FileDBMapper fileDBMapper;
	
	private final Path root = Paths.get("./src/main/webapp/download");
	
	@Transactional
	@Override
	public int store(MultipartFile file, String uuid) {
		FileDB fileDB = new FileDB();
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		
		try {
			Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
		    fileDB = new FileDB(uuid, fileName, file.getContentType(), file.getSize(), file.getBytes());
		} catch (Exception e) {
			
		}

	    return fileDBMapper.save(fileDB);	
	}

	@Override
	public FileDB getFile(int fno) {
		// TODO Auto-generated method stub
		return fileDBMapper.get(fno);
	}

	@Override
	public Stream<FileDB> getAllFiles() {
		// TODO Auto-generated method stub
		return fileDBMapper.getList().stream();
	}

	@Override
	public int deleteFile(int fno) {
		return fileDBMapper.delete(fno);    
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		try {
		      Files.createDirectories(root);
		    } catch (IOException e) {
		      throw new RuntimeException("Could not initialize folder for upload!");
		}
	}

	@Override
	public List<FileDB> getFiles(String uuid) {
		// TODO Auto-generated method stub
		return fileDBMapper.getListFiles(uuid);
	}

}
