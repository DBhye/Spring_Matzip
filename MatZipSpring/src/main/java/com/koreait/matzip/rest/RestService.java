package com.koreait.matzip.rest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.koreait.matzip.CommonUtils;
import com.koreait.matzip.Const;
import com.koreait.matzip.FileUtils;
import com.koreait.matzip.SecurityUtils;
import com.koreait.matzip.model.CodeVO;
import com.koreait.matzip.model.CommonMapper;
import com.koreait.matzip.rest.model.RestDMI;
import com.koreait.matzip.rest.model.RestFile;
import com.koreait.matzip.rest.model.RestPARAM;
import com.koreait.matzip.rest.model.RestRecMenuVO;


@Service
public class RestService {
   
   @Autowired
   private RestMapper mapper;
   
   @Autowired
   private CommonMapper cMapper;
   
   public List<RestDMI> selRestList(RestPARAM param) {
     return mapper.selRestList(param);
   }
   
   public List<RestRecMenuVO> selRestMenus(RestPARAM param) {
	   return mapper.selRestMenus(param);
   }
   
   
   public List<CodeVO> selCategoryList() {
	   CodeVO p = new CodeVO();
	   p.setI_m(1); //음식점 카테고리 코드 = 1
	   
	   return cMapper.selCodeList(p);
   }
   
   public int insRest(RestPARAM param) {
	   return mapper.insRest(param);
   }
   
   public void updAddHits(RestPARAM param, HttpServletRequest req) {
	   //글에 접속한 사람(요청을 보낸사람)의 ip값을 알아내야한다.
	   String myIp = req.getRemoteAddr();
	   ServletContext ctx = req.getServletContext();
	   //페이지 컨텍스트, 리퀘스트, 세션 , 어플리케이션 역할 구별해서 생각하기	   
	   String currentReadIp = (String)ctx.getAttribute(Const.CURRENT_REST_READ_IP + param.getI_rest());
	   if(currentReadIp == null || !currentReadIp.contentEquals(myIp)) {
		   
		   int i_user = SecurityUtils.getLoginUserPk(req);
		   
		   param.setI_user(i_user);
		   //내가 쓴 글이면 조회수 안올라가게 쿼리문으로 막는다.
		   //조회수 올림 처리 할것임
		   mapper.updAddHits(param);
		   ctx.setAttribute(Const.CURRENT_REST_READ_IP + param.getI_rest(), myIp);
	   }
   }
   public RestDMI selRest(RestPARAM param) {
	   return mapper.selRest(param);
   }
   @Transactional
   public void delRestTran(RestPARAM param) {
	   mapper.delRestRecMenu(param);
	   mapper.delRestMenu(param);
	   mapper.delRest(param);
   } 
   public int delRestRecMenu (RestPARAM param) {
	   return mapper.delRestRecMenu(param);
   }
//   public int delRestMenu(RestPARAM param) {
//	   return mapper.delRestMenu(param);
//   }
   
  
   
   public int insRestMenu(RestFile param, int i_user) {
	   //객체로 받기 때문에 값을 알 수 없다.
	   if(_authFail(param.getI_rest(), i_user)) {
		   return Const.FAIL;
	   }
	   System.out.println(Const.realPath);
	   
	   String path = Const.realPath + "/resources/img/rest/" + param.getI_rest() + "/menu/"; 
	   
		List<RestRecMenuVO> list = new ArrayList();
		
		for(MultipartFile mf : param.getMenu_pic()) {
			RestRecMenuVO vo = new RestRecMenuVO();
			list.add(vo);
			
			String saveFileNm = FileUtils.saveFile(path, mf);
			//파일없으면 null 넘어간다 (saveFile)
			vo.setMenu_pic(saveFileNm);
			vo.setI_rest(param.getI_rest());
		}
		
		for(RestRecMenuVO vo : list) {
			mapper.insRestMenu(vo);
		}
		return Const.SUCCESS;
   }
   public int insRecMenus(MultipartHttpServletRequest mReq) {
	   	int i_user = SecurityUtils.getLoginUserPk(mReq.getSession());
		int i_rest = Integer.parseInt(mReq.getParameter("i_rest"));
		if(_authFail(i_rest, i_user)) {
			return Const.FAIL;
		} //내가 쓴글 아닌데도 메뉴등록되는 것을 막기 위해. 통과되었다? 자기가 쓴글이다.
		
		List<MultipartFile> fileList = mReq.getFiles("menu_pic");
		String[] menuNmArr = mReq.getParameterValues("menu_nm");
		String[] menuPriceArr = mReq.getParameterValues("menu_price");
		String path = Const.realPath + "/resources/img/rest/" + i_rest + "/rec_menu/";
		
	   List<RestRecMenuVO> list = new ArrayList();
	  
	   for(int i=0; i<menuNmArr.length; i++) {
		//파일 각 저장 
		   RestRecMenuVO vo = new RestRecMenuVO();
		   list.add(vo);
		   
		   String menu_nm = menuNmArr[i];
		   int menu_price = CommonUtils.parseStringToInt(menuPriceArr[i]);
		   vo.setI_rest(i_rest);
		   vo.setMenu_nm(menu_nm);
		   vo.setMenu_price(menu_price);
		   
		   MultipartFile mf = fileList.get(i);
		   
		   if(mf.isEmpty()) {
			   continue;
		   }
		   String originFilNm = mf.getOriginalFilename();
		   String ext = FileUtils.getExt(originFilNm);
		   String saveFileNm = UUID.randomUUID() + ext;
		   
		   try {
			   mf.transferTo(new File(path + saveFileNm));
			   vo.setMenu_pic(saveFileNm);
		   } catch (Exception e){
			   e.printStackTrace();
		   }
	   }
	   for(RestRecMenuVO vo : list) {
		   mapper.insRestRecMenu(vo);
	   }
	   return i_rest;
	   
   }
   public List<RestRecMenuVO> selRestRecMenus (RestPARAM param) {
	   List<RestRecMenuVO> list = mapper.selRestRecMenus(param);
	   return mapper.selRestRecMenus(param);
   }
   public int delRecMenu(RestPARAM param, String realPath) {
	   //파일삭제 
	   List<RestRecMenuVO> list = mapper.selRestRecMenus(param);
	   if(list.size() == 1) {
		   RestRecMenuVO item = list.get(0);
		   
		   if(item.getMenu_pic() !=null && !item.getMenu_pic().equals("")) {
			   //이미지있음 > 삭제 !!
			   File file = new File(realPath + item.getMenu_pic());
			   if(file.exists()) { //이미지가 지워지지 않았을 경우에 삭제해주는 역할.
				   if(file.delete()) {
					   return mapper.delRestRecMenu(param);
				   } else {
					   return 0;
				   }
			   }
		   }
	   }
	   return mapper.delRestRecMenu(param);
   }
   public int delRestMenu(RestPARAM param) {
	   if(param.getMenu_pic() != null && !"".equals(param.getMenu_pic())) {
		   String path = Const.realPath + "/resources/img/rest/" + param.getI_rest() + "/menu/";
		   
		   if(FileUtils.delFile(path + param.getMenu_pic())) {
			   return mapper.delRestMenu(param);
		   } else {
			   return Const.FAIL;
		   }
	   }
	   return mapper.delRestMenu(param);
   }
   
   private boolean _authFail(int i_rest, int i_user) {
	   RestPARAM param = new RestPARAM();
	   param.setI_rest(i_rest);
	   //모든 가게는 누가쓴글인지 정보가 담겨있다.(i_user)
	   //sel로 0줄 or 1줄로 값을 가져온다.(객체하나로만 받으면 된다.)
	   RestDMI dbResult = mapper.selRest(param); 
	   //i_rest, i_user로만 sel 문을 받는다 
	   if(dbResult == null || dbResult.getI_user() != i_user) {
		   return true;
	   }
	   
	   return false; //인증이 완료
   }
}