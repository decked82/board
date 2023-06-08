package kr.group.pm.board.model;

import java.time.LocalDate;
import java.util.List;


public class BoardVO {
	private int bno;
	private String uuid;
	private String title;
	private String content;
	private String writer;
	private LocalDate regDate;
	private int viewCnt;
	private List<ReplyVO> replies;
	private List<FileDB> files;
	private User user;
	
	public int getBno() {
		return bno;
	}
		
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUuid() {
		return uuid;
	}

	public void setBno(int bno) {
		this.bno = bno;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public LocalDate getRegDate() {
		return regDate;
	}
	public void setRegDate(LocalDate regDate) {
		this.regDate = regDate;
	}
	public int getViewCnt() {
		return viewCnt;
	}
	public void setViewCnt(int viewCnt) {
		this.viewCnt = viewCnt;
	}

	public List<FileDB> getFiles() {
		return files;
	}
	public void setFiles(List<FileDB> files) {
		this.files = files;
	}

	public List<ReplyVO> getReplies() {
		return replies;
	}

	public void setReplies(List<ReplyVO> replies) {
		this.replies = replies;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "BoardVO [bno=" + bno + ", uuid=" + uuid + ", title=" + title + ", content=" + content + ", writer="
				+ writer + ", regDate=" + regDate + ", viewCnt=" + viewCnt + ", replies=" + replies + ", files=" + files
				+ "]";
	}	
	
}
