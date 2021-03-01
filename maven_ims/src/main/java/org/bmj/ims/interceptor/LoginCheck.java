package org.bmj.ims.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bmj.ims.vo.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheck implements HandlerInterceptor{
	
	private final  Logger logger = 
			LoggerFactory.getLogger(this.getClass());
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		Member login = (Member)request.getSession().getAttribute("loginMember");
		
		if(login==null) {
			
			response.sendRedirect("/");
			
			logger.info("로그인 안되었으므로 컨트롤러로 안가요");
			return false;
		}else {
			logger.info("로그인 되어있으므로 컨트롤러로 넘김");
			return true;
		}
		
	}
	

}
