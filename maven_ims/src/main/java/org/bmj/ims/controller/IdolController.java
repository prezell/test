package org.bmj.ims.controller;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bmj.ims.service.IdolsService;
import org.bmj.ims.util.FileRenameUtil;
import org.bmj.ims.util.ResizeImageUtil;
import org.bmj.ims.vo.Idol;
import org.bmj.ims.vo.Like;
import org.bmj.ims.vo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class IdolController {

	@Autowired
	private IdolsService service;

	
	@RequestMapping(value = "/idol/{idolId}/modify",
			method = RequestMethod.GET)
	private String updateIdolForm(Model model,
			@PathVariable int idolId) {
		
		model.addAllAttributes(service.getIdolUpdateForm(idolId));
		
		return "idolForm";
	}
	

	@RequestMapping(value="/idol",
			method=RequestMethod.PUT)
	private String updateIdol(Idol idol) {
		service.updateIdol(idol);
		return "redirect:/idol/"+idol.getIdolId();
	}
	
	@RequestMapping(value = "/idol/register",
			method = RequestMethod.GET)
	private String registerIdol(Model model) {

		model.addAttribute("groups", service.getGroups());

		return "idolForm";
	}
	

	@RequestMapping(value = "/idol",
			method = RequestMethod.POST)
	private String registerIdol(
			HttpServletRequest request,
			Idol idol, MultipartFile profile) 
	throws Exception{

		// 서버
		ServletContext application = request.getServletContext();

		// 기본경로
		String rootPath = application.getRealPath("/");

		// 업로드 폴더 경로
		String uploadPath = rootPath + "img" + File.separator + "upload" + File.separator;

		// 파일의 실제 이름
		String fileName = profile.getOriginalFilename();

		// 파일 객체 생성
		File file = new File(uploadPath + fileName);

		// 파일이름이 같다면 숫자가 붙음
		file = FileRenameUtil.rename(file);

		System.out.println(file);

		// 임시폴더에 우리 업로드폴더로 이동
		profile.transferTo(file);

		// 리사이즈가 필요한 경우 하면 됨

		String resizePath = rootPath + "img" + File.separator + "idols" + File.separator;

		// 리사이즈
		ResizeImageUtil.resize(file.toString(), resizePath + file.getName(), 200);
		
		//Idol에 profileImage를 세팅(파일의 이름으로)
		idol.setProfileImage(file.getName());
		
		service.registerIdol(idol);

		return "redirect:/idol/"+idol.getIdolId();
	}

	@RequestMapping(value = "/idol/{idolId}",
			method = RequestMethod.DELETE)
	public String deleteIdol(@PathVariable int idolId) {

		service.deleteIdol(idolId);

		return "redirect:/idol/page/1";
	}

	@RequestMapping(value = "/idol/page/{page}", method = RequestMethod.GET)
	private String idols(Model model,
			@PathVariable int page) {
		model.addAllAttributes(service.getIdols(page));
		return "idols";
	}

	@RequestMapping(value = "/idol/{idolId}", method = RequestMethod.GET)
	private String idol(
			@PathVariable int idolId, Model model, HttpSession session) {
		
		Member member = (Member)session.getAttribute("loginMember");
		
		System.out.println(member.getNo());

		model.addAttribute("idol", service.getIdol(idolId,member.getNo()));
		
		return "idol";
	}
	
	
	@RequestMapping(value="/idol/like", method = RequestMethod.POST)
	private String idolLike(Like like) {
		
		try {
			service.likeIdol(like);
		} catch (Exception e) {
			
		}
		
		return "redirect:/idol/"+like.getTypeNo();
	}

}
