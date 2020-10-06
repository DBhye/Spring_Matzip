package com.koreait.matzip.user.model;

import java.util.List;

import com.koreait.matzip.rest.model.RestRecMenuVO;

public class UserDMI extends UserVO{
	private int i_rest;
	private String rest_nm;
	private String rest_addr;
	private List<RestRecMenuVO> menuList;
	
	public int getI_rest() {
		return i_rest;
	}
	public void setI_rest(int i_rest) {
		this.i_rest = i_rest;
	}
	public List<RestRecMenuVO> getMenuList() {
		return menuList;
	}
	public void setMenuList(List<RestRecMenuVO> menuList) {
		this.menuList = menuList;
	}
	
}
