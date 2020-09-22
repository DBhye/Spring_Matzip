package com.koreait.matzip.rest;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestPARAM;
public interface RestMapper {
	int insRest(RestPARAM param);
	List<RestDMI> selRestList(RestPARAM param);
}

