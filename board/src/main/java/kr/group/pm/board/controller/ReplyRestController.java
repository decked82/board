package kr.group.pm.board.controller;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.group.pm.board.model.ReplyVO;
import kr.group.pm.board.service.ReplyService;

@RestController
@RequestMapping("/api/reply")
public class ReplyRestController {

	private ReplyService replyService;

	public ReplyRestController(ReplyService replyService) {
		super();
		this.replyService = replyService;
	}
	
	@GetMapping("/list")
	public ResponseEntity<List<ReplyVO>> showList(HttpServletRequest request) {
		String bno = Arrays.stream(request.getCookies())
				.filter(cookie->"bno".equals(cookie.getName()))
				.map(cookie-> URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8))
				.findAny().get();
		return new ResponseEntity<>(replyService.getList(Integer.parseInt(bno)), HttpStatus.OK);
	}
	
	@PostMapping("/add")
	public ResponseEntity<ReplyVO> create(@RequestBody ReplyVO reply) {
		replyService.add(reply);
		return new ResponseEntity<>(reply, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<ReplyVO> update(@RequestBody ReplyVO reply) {
		replyService.update(reply);
		return new ResponseEntity<>(reply, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Void> delete(HttpServletRequest request) {
		String bno = Arrays.stream(request.getCookies())
				.filter(cookie->"bno".equals(cookie.getName()))
				.map(cookie-> URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8))
				.findAny().get();
		String rno = Arrays.stream(request.getCookies())
				.filter(cookie->"rno".equals(cookie.getName()))
				.map(cookie-> URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8))
				.findAny().get();
		
		replyService.delete(Integer.parseInt(bno), Integer.parseInt(rno));
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
