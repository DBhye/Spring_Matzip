package com.koreait.matzip.user;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koreait.matzip.Const;
import com.koreait.matzip.ViewRef;
import com.koreait.matzip.user.model.UserPARAM;
import com.koreait.matzip.user.model.UserVO;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logout(HttpSession hs) {
		hs.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping(value="/login", method = RequestMethod.GET)
	public String login(Model model) {

		model.addAttribute(Const.TITLE, "로그인");
		model.addAttribute(Const.VIEW, "user/login");
		
		return ViewRef.TEMP_DEFAULT;
	} 
		@RequestMapping(value="/login", method = RequestMethod.POST)
		public String login(UserPARAM param, HttpSession hs, RedirectAttributes ra) {
			
			int result = service.login(param);
			
			if(result == Const.SUCCESS) {
				hs.setAttribute(Const.LOGIN_USER, param);
				return "redirect:/";
			}
			
			String msg = null;
			if(result == Const.NO_ID) {
				msg = "아이디를 확인해주세요.";
			}else if(result == Const.NO_PW) {
				msg= "비밀번호를 확인해주세요.";
			}
			param.setMsg(msg);
			ra.addFlashAttribute("data", param); //객체로 넘어간다.세션에 박고 세션을 닫아준다. (응답할때 지운다)마치 POST처럼 쓸 수 있다.
			return "redirect:/user/login";
		}//msg를 여기서보낸다.
		
		@RequestMapping(value="/join", method = RequestMethod.GET)
		public String join(Model model, @RequestParam(defaultValue="0") int err) {
			System.out.println("err : " + err);
			if(err > 0) {
				model.addAttribute("msg","에러가 발생하였습니다.");
			}
			model.addAttribute(Const.TITLE, "회원가입");
			model.addAttribute(Const.VIEW, "user/join");
			return ViewRef.TEMP_DEFAULT;//jsp파일로 보냄
		}//get에서 모든 처리를 한다.
		
		@RequestMapping(value="/join", method=RequestMethod.POST)
		public String join(UserVO param, RedirectAttributes ra) {
			int result = service.join(param);
			if(result == 1) {
				return "redirect:/user/login";
			}
			ra.addAttribute("err", result);
			return "redirect:/user/join";//주소로이동
		}
		
		@RequestMapping(value="/ajaxIdChk",method=RequestMethod.POST)
		@ResponseBody
		public String ajaxIdChk(@RequestBody UserPARAM param) {
			System.out.println("user_id "+ param.getUser_id());
			int result = service.login(param);
			return String.valueOf(result); //이것 자체를 리턴해준다. //responsebody를 안붙이면 JSP 파일이 열렸다.
		}											//이 자체로 응답이다. 서버한테 첫페이지 빼고 데이터만 요구하여 이런식으로 개발을 많이한다.
}