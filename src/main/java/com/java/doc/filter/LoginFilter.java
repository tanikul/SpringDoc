package com.java.doc.filter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.java.doc.util.Validators;

public class LoginFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(LoginFilter.class);
    private List<String> skipUrls;
    
    @Override
    public void init(FilterConfig conf) throws ServletException {
        logger.info("LoginFilter starting...");
        
        skipUrls = new ArrayList<String>();
        String skipUrlPattern = conf.getInitParameter("skipUrlPattern");
        if (Validators.isNotEmpty(skipUrlPattern)) {
            String[] params = skipUrlPattern.split(",");
            for (String str : params) {
                skipUrls.add(str);
            }
        }
        
        logger.debug("Skip all url : ", skipUrls);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;
        
        String pathInfo = getPath(httpReq);
        logger.debug("Request path : {}", pathInfo);
        
        if (isSkipUrl(pathInfo)) {
            logger.debug("This is login page. Skip filter validate...");
            chain.doFilter(request, response);
            return;            
        }
        
        if (isAjaxRequest(httpReq)) {
            logger.debug("This is ajax request. Skip filter validate...");
            chain.doFilter(request, response);
            return;
        }
        
        // Get UserSession from HttpSession.
        //HttpSession httpSession = httpReq.getSession();
//        UserInfoBean userSession = getUserInfo(httpSession);
//        if (Validators.isNull(userSession) || Validators.isNull(userSession.getUser())) {
//            httpSession.invalidate();
            String path = httpReq.getContextPath() + "/";
//            logger.error("Uses not login, redirect to login page : {}", path);
            httpRes.sendRedirect(path);
            return;
//        }
        
//        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info("LoginFilter destroy...");
    }
    
    private String getPath(HttpServletRequest httpReq) {
        return httpReq.getRequestURI().substring(httpReq.getContextPath().length());
    }
    
    private boolean isAjaxRequest(HttpServletRequest request) {
        String facesRequest = request.getHeader("Faces-Request");
        if (facesRequest != null && facesRequest.equals("partial/ajax")) {
            return true;
        }
        return false;
    }
    
    private boolean isSkipUrl(String url) {
        if (Validators.isEmpty(skipUrls) || Validators.isEmpty(url)) {
            return false;
        }
        
        for (String str : skipUrls) {
            if (Validators.isEmpty(str)) {
                continue;
            }
            
            if (url.startsWith(str)) {
                return true;
            }
        }
        
        return false;
    }
}
