package com.koreait.matzip.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.koreait.matzip.Const;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.ViewRef;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestFile;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;

@Controller
@RequestMapping("/rest")
public class RestController {
//HandlerMapping 역할
//("/rest") 는 	handlermapper에서 case문으로 두번째 주소를 넣어준것과 같다.
	@Autowired
	private RestService service;
	
	@RequestMapping("/map")
	public String restMap(Model model) {
		model.addAttribute(Const.TITLE, "지도보기");
		model.addAttribute(Const.VIEW,"rest/restMap");
		
		return ViewRef.TEMP_MENU_TEMP;
	}
	
	@RequestMapping(value="/ajaxGetList", produces = {"application/json; charset=UTF-8"})
	@ResponseBody public List<RestDMI> ajaxGetList(RestPARAM param) {
		System.out.println("sw_lat : " + param.getSw_lat());
		System.out.println("sw_lng : " + param.getSw_lng());
		System.out.println("ne_lat : " + param.getNe_lat());
		System.out.println("ne_lng : " + param.getNe_lng());
		return service.selRestList(param);
	}
	
	@RequestMapping("/reg")
	public String restReg(Model model) {
		model.addAttribute("categoryList", service.selCategoryList());
		model.addAttribute(Const.TITLE, "가게등록");
		model.addAttribute(Const.VIEW, "rest/restReg");
		return ViewRef.TEMP_MENU_TEMP;
	}
	
	@RequestMapping(value="/reg" , method = RequestMethod.POST)
	public String restReg(RestPARAM param, HttpSession hs) {
			param.setI_user(SecurityUtils.getLoginUserPk(hs));
			int result =service.insRest(param);
			return "redirect:/rest/map";
		}
	@RequestMapping("/detail")
	public String detail(RestPARAM param, Model model, HttpServletRequest req) {
		
			service.updAddHits(param, req);
			RestDMI data = service.selRest(param);
			
			//model.addAttribute("menuList",service.selRestMenus(param));
			int i_user = SecurityUtils.getLoginUserPk(req);
			param.setI_user(i_user);
			model.addAttribute("recMenuList", service.selRestRecMenus(param));
			model.addAttribute("css",new String[] {"restDetail", "swiper-bundle.min"});
			model.addAttribute("data", data);
			model.addAttribute(Const.TITLE, data.getNm());
			model.addAttribute(Const.VIEW, "rest/restDetail"); //파일명 적는곳
			return ViewRef.TEMP_MENU_TEMP;
		}
	
	@RequestMapping("/ajaxSelMenuList")
	@ResponseBody
	public List<RestRecMenuVO> ajaxSerMenuList(RestPARAM param) {
			return service.selRestMenus(param);
			
	}
	@RequestMapping("/del")
	public String del(RestPARAM param, HttpSession hs) {
		int loginI_user = SecurityUtils.getLoginUserPk(hs);
		param.setI_user(loginI_user);
		int result = 1;
		try {
			service.delRestTran(param);
		} catch(Exception e) {
			result = 0;
		}
		System.out.println("result : "+result);
		return "redirect:/";
		}
	@RequestMapping(value="/recMenus", method=RequestMethod.POST)
	public String recMenus(MultipartHttpServletRequest mReq, RedirectAttributes ra) {
		
		int i_rest = service.insRecMenus(mReq);
		
		ra.addAttribute("i_rest",i_rest);
		return "redirect:/rest/detail"; 
		
	}
	
	@RequestMapping("/ajaxDelRecMenu")
	@ResponseBody public int ajaxDelRecMenu (RestPARAM param, HttpSession hs) {
		System.out.println("삭제하기위해 auth result 넘어왔는가?");
		String path = "/resources/img/rest/" + param.getI_rest() + "/rec_menu/";
		String realPath = hs.getServletContext().getRealPath(path);
		hs.getServletContext().getRealPath("");
		param.setI_user(SecurityUtils.getLoginUserPk(hs)); // 로그인유저 pk담기
		return service.delRecMenu(param, realPath);
	}
	
	@RequestMapping("/ajaxDelMenu")
	@ResponseBody public int ajaxDelMenu(RestPARAM param) { //i_rest, seq, menu_pic
		return service.delRestMenu(param);
	}
	
	
	@RequestMapping("/menus")
	public String menus(@ModelAttribute RestFile param, 
			HttpSession hs, 
			RedirectAttributes ra) {
		int i_user = SecurityUtils.getLoginUserPk(hs);
		int result = service.insRestMenu(param, i_user);
		
		ra.addAttribute("i_rest", param.getI_rest());
		return "redirect:/rest/detail";
	}
	

}

