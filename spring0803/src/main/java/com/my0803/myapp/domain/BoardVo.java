package com.my0803.myapp.domain;

import org.springframework.web.multipart.MultipartFile;

public class BoardVo {

	private int bidx;
	private int originbidx;
	private int depth;
	private int level_;
	private String subject;
	private String contents;
	private String writer;
	private int viewcnt;
	private String writeday;
	private String ip;
	private String delyn;
	private int midx;
	private String pwd;
	private String modifyday;
	private String filename2;
	//private MultipartFile filename;
	
	
//	public MultipartFile getFilename() {
//		return filename;
//	}
//	public void setFilename(MultipartFile filename) {
//		this.filename = filename;
//	}
	public String getFilename2() {
		return filename2;
	}
	public void setFilename2(String filename) {
		this.filename2 = filename;
	}
	public int getBidx() {
		return bidx;
	}
	public void setBidx(int bidx) {
		this.bidx = bidx;
	}
	public int getOriginbidx() {
		return originbidx;
	}
	public void setOriginbidx(int originbidx) {
		this.originbidx = originbidx;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public int getLevel_() {
		return level_;
	}
	public void setLevel_(int level_) {
		this.level_ = level_;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public int getViewcnt() {
		return viewcnt;
	}
	public void setViewcnt(int viewcnt) {
		this.viewcnt = viewcnt;
	}
	public String getWriteday() {
		return writeday;
	}
	public void setWriteday(String writeday) {
		this.writeday = writeday;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDelyn() {
		return delyn;
	}
	public void setDelyn(String delyn) {
		this.delyn = delyn;
	}
	public int getMidx() {
		return midx;
	}
	public void setMidx(int midx) {
		this.midx = midx;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getModifyday() {
		return modifyday;
	}
	public void setModifyday(String modifyday) {
		this.modifyday = modifyday;
	}	
	
}
