package kr.group.pm.board.model;

import lombok.ToString;

@ToString
public class FileDB {

	
	private int fno;
	
	private String uuid;
	
	private String name;

	private String contenttype;

	private byte[] data;
	
	private long size;

	public FileDB() {
		super();
	}

	public FileDB(String name, String contenttype, byte[] data) {
		super();
		this.name = name;
		this.contenttype = contenttype;
		this.data = data;
	}

	public FileDB(String uuid, String name, String contenttype, long size, byte[] data) {
		super();
		this.uuid = uuid;
		this.name = name;
		this.contenttype = contenttype;
		this.size = size;
		this.data = data;
	}
	
	public FileDB(String name, String contenttype, long size) {
		super();
		this.name = name;
		this.contenttype = contenttype;
		this.size = size;
	}

	public int getFno() {
		return fno;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContenttype() {
		return contenttype;
	}

	public void setContenttype(String contenttype) {
		this.contenttype = contenttype;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	
		
}
