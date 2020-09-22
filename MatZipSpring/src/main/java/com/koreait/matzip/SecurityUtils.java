package com.koreait.matzip;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;

import com.koreait.matzip.user.model.UserVO;


public class SecurityUtils {

		public static int getLoginUserPk(HttpServletRequest request) {
			return getLoginUser(request).getI_user(); 
		}
		
		public static int getLoginUserPk(HttpSession hs) {
			return ((UserVO)hs.getAttribute(Const.LOGIN_USER)).getI_user();
		}
		public static UserVO getLoginUser(HttpServletRequest request) {
			HttpSession hs = request.getSession();
			return (UserVO)hs.getAttribute(Const.LOGIN_USER);
		}
		
		public static boolean isLogout(HttpServletRequest request) {				
			return getLoginUser(request) == null;
		}

		public static String generateSalt() {
			return BCrypt.gensalt();
		}

		public static String getEncrypt(String pw, String salt) {
			return BCrypt.hashpw(pw, salt); 
		}
	}

//1. 회원가입할때 고객이 입력한 (비밀번호) + 생성된 salt(난수) = user_pw로 db에 저장된다. 
//2. db에서 salt(난수)도 함께 저장된다. 고객이 로그인할때는 저장된 난수+ 고객이 입력한 비밀번호가 db에 있는 user_pw와 동일할때 로그인이 성공된다!
