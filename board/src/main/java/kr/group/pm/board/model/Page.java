package kr.group.pm.board.model;

public class Page {

	private int num;
	private int count;
	private int postNum = 10;
	private int pageNum;
	private int displayPost;
	private int pageNumCnt = 10;
	private int endPageNum;
	private int startPageNum;
	
	private boolean prev;
	private boolean next;

	private String searchTypeKeyword;
	private String searchType;
	private String keyword;
	
	public void setNum(int num) {
		this.num = num;
	}
	public void setCount(int count) {
		this.count = count;
		dataCalc();
	}
	public int getNum() {
		return num;
	}
	public int getCount() {
		return count;
	}
	public int getPostNum() {
		return postNum;
	}
	public int getPageNum() {
		return pageNum;
	}
	public int getDisplayPost() {
		return displayPost;
	}
	public int getPageNumCnt() {
		return pageNumCnt;
	}
	public int getEndPageNum() {
		return endPageNum;
	}
	public int getStartPageNum() {
		return startPageNum;
	}
	public boolean isPrev() {
		return prev;
	}
	public boolean isNext() {
		return next;
	}
	
	
	private void dataCalc() {
		endPageNum = startPageNum+pageNumCnt-1;
		startPageNum = ((num-1)/pageNumCnt)*pageNumCnt+1;
		int endPageNum_tmp = (int) (Math.ceil((double)count / (double)pageNumCnt));
					
		 if(endPageNum > endPageNum_tmp) {
			 endPageNum = endPageNum_tmp;
		 }
		 
		prev = startPageNum == 1 ? false : true;
		next = endPageNum * pageNumCnt >= count ? false : true;
		displayPost = (num - 1) * postNum;
	}
	
	
	
	private void dataCalc111() {
		endPageNum = (int)(Math.ceil((double)num / (double)pageNumCnt) * pageNumCnt);
		startPageNum = endPageNum - (pageNumCnt-1);
		int endPageNum_tmp = (int) (Math.ceil((double)count / (double)postNum));
//		System.out.println(startPageNum + " " + endPageNum + " " + endPageNum_tmp);
					
		 if(endPageNum > endPageNum_tmp) {
			 endPageNum = endPageNum_tmp;
		 }
		 
		prev = startPageNum == 1 ? false : true;
		next = endPageNum * postNum >= count ? false : true;
		displayPost = (num - 1) * postNum;
	}
	
	@Override
	public String toString() {
		return "Page [num=" + num + ", count=" + count + ", postNum=" + postNum + ", pageNum=" + pageNum
				+ ", displayPost=" + displayPost + ", pageNumCnt=" + pageNumCnt + ", endPageNum=" + endPageNum
				+ ", startPageNum=" + startPageNum + ", prev=" + prev + ", next=" + next + ", searchTypeKeyword="
				+ searchTypeKeyword + ", searchType=" + searchType + ", keyword=" + keyword + "]";
	}
	
	public void setSearchTypeKeyword(String searchType, String keyword) {
		 
		 if(searchType.equals("") || keyword.equals("")) {
		  searchTypeKeyword = ""; 
		 } else {
		  searchTypeKeyword = "&searchType=" + searchType + "&keyword=" + keyword; 
		 }  
	}

	public String getSearchTypeKeyword() {
		return searchTypeKeyword;
	}
	
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
}
