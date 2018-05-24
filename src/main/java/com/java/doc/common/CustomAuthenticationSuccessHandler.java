package com.java.doc.common;

import java.io.IOException;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	 
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Value("${lms.sys.expireTime}") 
	private String sessionTimeout;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
		HttpServletResponse response, Authentication authentication) throws IOException,
		ServletException {
		/*GenerateMenu menu = (GenerateMenu) ApplicationContextHolder.getContext().getBean("generateMenu");
		menu.init();
		UserLogin user = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getDetails();
		request.getSession().setMaxInactiveInterval(60 * Integer.parseInt(sessionTimeout));
		request.getSession().setAttribute("generateMenu", menu.getMenu());
		request.getSession().setAttribute("userInfo", user);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		if(user!=null && user.getLastLogin()!=null) {
			String lastLogin = dateFormat.format(user.getLastLogin());
			request.getSession().setAttribute("lastLogin", lastLogin);
		}
		*/
		String targetUrl = determineTargetUrl(authentication); 
		redirectStrategy.sendRedirect(request, response, targetUrl);
	}
	
	protected String determineTargetUrl(Authentication authentication) {
		return "/Welcome";
	}
	
	public RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}
	
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}
}

