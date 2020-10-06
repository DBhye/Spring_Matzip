package com.koreait.matzip.user;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.koreait.matzip.user.model.UserDMI;
import com.koreait.matzip.user.model.UserPARAM;
import com.koreait.matzip.user.model.UserVO;

@Mapper //mybatis를 통해서 UserMapper.xml과 한쌍으로 동작한다. , 알아서 DAO클래스를 만들어서 빈등록까지 해준다.
public interface UserMapper {
	public int insUser(UserVO p);
	int insFavorite(UserPARAM param);
	public UserDMI selUser(UserPARAM p);
	List<UserDMI> selFavoriteList(UserPARAM param);
	
	int delFavorite(UserPARAM param);
}
