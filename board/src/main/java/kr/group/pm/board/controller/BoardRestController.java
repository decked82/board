package kr.group.pm.board.controller;

import java.net.URLDecoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.multipart.MultipartFile;

import kr.group.pm.board.model.BoardVO;
import kr.group.pm.board.model.Page;
import kr.group.pm.board.model.User;
import kr.group.pm.board.service.BoardService;
import kr.group.pm.board.service.ReplyService;
import kr.group.pm.board.service.StorageService;
import kr.group.pm.board.service.UserService;

@RestController
@RequestMapping("/api/board")
public class BoardRestController {

	private BoardService boardService;
	private ReplyService replyService;
	private StorageService service;
	private UserService userService;

	public BoardRestController(BoardService boardService, ReplyService replyService, StorageService service, UserService userService) {
		super();
		this.boardService = boardService;
		this.replyService = replyService;
		this.service = service;
		this.userService = userService;
	}

	
	@GetMapping("/list") 
	public ResponseEntity<Map<String, Object>> showList(HttpServletRequest request) {

		String num = Arrays.stream(request.getCookies())
			.filter(cookie->"page".equals(cookie.getName()))
			.map(cookie-> URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8))
			.findAny().get();
		
		String searchType = Arrays.stream(request.getCookies())
				.filter(cookie->"searchType".equals(cookie.getName()))
				.map(cookie-> URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8))
				.findAny().get();
		
		String keyword = Arrays.stream(request.getCookies())
				.filter(cookie->"keyword".equals(cookie.getName()))
				.map(cookie-> URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8))
				.findAny().get();
		
		HttpSession session = request.getSession();
		Boolean logged = (Boolean) session.getAttribute("isLogged");
		Long userId = (Long)request.getSession().getAttribute("userId");		
		Map<String, Object> result = new HashMap<>();
		
		if (logged==null || !logged) {
			result.put("isLogged", false);
		} else {
			result.put("isLogged", true);
			result.put("userId", userId);
		}
		
		Page page = new Page();
		page.setNum(Integer.parseInt(num));
		page.setCount(boardService.getCounterBySearch(searchType, keyword));
		page.setSearchTypeKeyword(searchType, keyword);
		page.setKeyword(keyword);
		page.setSearchType(searchType);
		List<BoardVO> boardList = boardService.getListPageSearch(page.getDisplayPost(), page.getPostNum(), 
				searchType, keyword);
		result.put("boardList", boardList);
		result.put("page", page);
		result.put("searchType", searchType);
		result.put("keyword", keyword);
		
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	
	@SuppressWarnings("unused")
	@GetMapping
	public ResponseEntity<Map<String, Object>> showView(HttpServletRequest request) {
		
		String bno = Arrays.stream(request.getCookies())
				.filter(cookie->"bno".equals(cookie.getName()))
				.map(cookie-> URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8))
				.findAny().get();
		HttpSession session = request.getSession();
		String sessionKey = "viewed_post_" + bno;
		Boolean viewed = (Boolean) session.getAttribute(sessionKey);
		Boolean logged = (Boolean) session.getAttribute("isLogged");
		Long userId = (Long) session.getAttribute("userId"); 
		Map<String, Object> result = new HashMap<>();
		result.put("board", boardService.getBoard(Integer.parseInt(bno)));

		if (viewed == null || !viewed) {
			boardService.updateViewCount(Integer.parseInt(bno));
			session.setAttribute(sessionKey, true);
		}
		
		if (logged==null || !logged) {
			result.put("isLogged", false);
		} else {
			result.put("isLogged", true);
			result.put("userId", userId);
		}
		
		return new ResponseEntity<>(result, HttpStatus.OK);		
		
	}
	
	@PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<BoardVO> create(@RequestPart("board") BoardVO board, 
			@RequestPart(value = "files", required = false) MultipartFile[] files, HttpServletRequest request) {

		Long id = (Long)request.getSession().getAttribute("userId");
		User user = userService.getUser((Long)request.getSession().getAttribute("userId"));
		System.out.println("id= "+id);
		String uuid = UUID.randomUUID().toString();
		
		if (files != null) {
			if (files.length > 0) {
				for (MultipartFile file : files) {			
						service.store(file, uuid);
				}
			}
		}
		board.setUuid(uuid);
		boardService.add(board);
		return new ResponseEntity<>(board, HttpStatus.OK);
	}
	
	@PostMapping(value = "/edit", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<BoardVO> update(@RequestPart("board") BoardVO board,
			@RequestPart(value = "files", required = false) MultipartFile[] files, @RequestParam String uuid) {
		
		
		if (files != null) {
			if (files.length > 0) {
				for (MultipartFile file : files) {
					service.store(file, uuid);					
				}
			}	
		}
		board.setUuid(uuid);
		boardService.update(board);
		return new ResponseEntity<>(board, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<BoardVO> delete(HttpServletRequest request) {
		String bno = Arrays.stream(request.getCookies())
				.filter(cookie->"bno".equals(cookie.getName()))
				.map(cookie-> URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8))
				.findAny().get();
		
		boardService.delete(Integer.parseInt(bno));
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
}
