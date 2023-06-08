package kr.group.pm.board.controller;


import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import kr.group.pm.board.message.ResponseFile;
import kr.group.pm.board.model.FileDB;
import kr.group.pm.board.service.StorageService;


@RestController
@RequestMapping("/api/storage")
public class FileRestController {

	@Autowired
	private StorageService storageService;
	
	@GetMapping("/download")
	public ResponseFile downloadFile() {
		return null;
	}
	
	@PostMapping("/upload")
	public ResponseFile uploadFile(@RequestParam("file") MultipartFile file) {
	    String message = "";
	
	      //storageService.store(file);

	      String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
	              .path("/download/")
	              .path(file.getOriginalFilename())
	              .toUriString();

	          return new ResponseFile(file.getOriginalFilename(), fileDownloadUri,
	              file.getContentType(), file.getSize());	    
	  }
	
	@GetMapping("/files")
	public ResponseEntity<Map<String, Object>> getListFiles(@RequestParam String uuid) {
		Map<String, Object> filesMap = new HashMap<>();
		filesMap.put("listFiles", storageService.getFiles(uuid));
		return new ResponseEntity<>(filesMap, HttpStatus.OK);
	}
	
	@GetMapping("/file")
	public ResponseEntity<Object> getFile(@RequestParam int fno) {
		FileDB fileDB = storageService.getFile(fno);
		
		if (fileDB==null) {
			return ResponseEntity.notFound().build();
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDisposition(ContentDisposition.attachment().filename(fileDB.getName()).build());
			
		return new ResponseEntity<>(fileDB, HttpStatus.OK);
	}
	
//	@GetMapping("/files/{id}")
//	public ResponseEntity<byte[]> getFile1(@PathVariable int id) {
//	    FileDB fileDB = storageService.getFile(id);
//
//	    return ResponseEntity.ok()
//	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
//	        .body(fileDB.getData());
//	}
	
	@DeleteMapping
	public ResponseEntity<Void> deleteFile(HttpServletRequest request) {
		String fno = Arrays.stream(request.getCookies())
				.filter(cookie->"fno".equals(cookie.getName()))
				.map(cookie-> URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8))
				.findAny().get();
		storageService.deleteFile(Integer.parseInt(fno));
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
