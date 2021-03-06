package com.koreait.matzip.rest;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;

@Mapper
public interface RestMapper { //인터페이스 친구는 기본으로 public 들어가있음.
	int insRest(RestPARAM param);
	int insRestRecMenu(RestRecMenuVO param);
	int insRestMenu(RestRecMenuVO param);
	
	int selRestChkUser(int param);
	List<RestDMI> selRestList(RestPARAM param);
	RestDMI selRest(RestPARAM param);
	List<RestRecMenuVO> selRestMenus(RestPARAM param);
	List<RestRecMenuVO> selRestRecMenus(RestPARAM param);
	
	int updAddHits(RestPARAM param);
	
	int delRestRecMenu(RestPARAM param);
	int delRestMenu(RestPARAM param);
	int delRest(RestPARAM param);
}

