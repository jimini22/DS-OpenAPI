package com.mini.portal.comm.model;

/**
 * com.mini.portal.comm.model
 *		>> PageInfoVO.java
 * @author	: 지민희
 * @since	: 2021. 4. 29
 * @version :
 * 	----------------------------------
 * 		수정일			수정내용
 * 	----------------------------------
 * 		2021. 4. 29		최초 생성
 */
public class PageInfoVO {
	
	private static final long serialVersionUID = 4996663737643690932L; /* 이거 왜 쓰는거임??? */
	
	/* 현재 페이지 */
	private int currentPage = 1;
	
	/* 한 페이지당 게시글 수 */
	private int pageSize = 10;
	
	/* 총 데이터 수 */
	private int totalCnt;
	
	/*총 페이지 수*/
	private int pageCnt;
	
	public PageInfoVO() {
		
	}
	
	/**
	 * 페이징 정보 생성자
	 * @param currentPage
	 * @param pageSize
	 * @param totalCnt
	 */
	public PageInfoVO(int currentPage, int pageSize, int totalCnt) {
		
		setCurrentPage(currentPage);
		setPageSize(pageSize);
		setTotalCnt(totalCnt);
		setPageCnt(pageCnt);
		
	}
	
	/**
	 * @description : 현재 조회 페이지 번호를 반환한다
	 * @return
	 */
	public int getCurrentPage() {
		return currentPage;
	}
	
	/**
	 * @description : 현재 조회 페이지 번호를 지정한다
	 * @param currentPage
	 */
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	/**
	 * @description : 페이지 당 데이터 수를 반환한다
	 * @return
	 */
	public int getPageSize() {
		return pageSize;
	}
	
	/**
	 * @description : 페이지 당 데이터 수를 지정한다
	 * @param pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	/**
	 * @description : 전체 데이터 수를 반환한다
	 * @return
	 */
	public int getTotalCnt() {
		return totalCnt;
	}
	
	/**
	 * @description : 전체 데이터 수를 지정한다
	 * @param totalCnt
	 */
	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}
	
	/**
	 * @description : 전체 페이지 수를 반환한다
	 * @return
	 */
	public int getPageCnt() {
		return pageCnt;
	}
	
	/**
	 * @description : 전체 페이지 수를 계산하여 지정한다
	 * @param totalCnt
	 */
	public void setPageCnt(int totalCnt) {
		this.pageCnt = (int) Math.ceil(totalCnt * 1.0 / pageSize);
	}
	
}