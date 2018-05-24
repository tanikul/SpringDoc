package com.java.doc.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {
	
	public Authentication assignRole(Authentication authenticate, List<GrantedAuthority> grantedAuths){
        User user = (User) authenticate.getDetails();
        return new CustomUsernamePasswordAuthenticationToken(authenticate.getName(), user.getPassword(), grantedAuths, (User) authenticate.getDetails());
    }
	
    @Override
    public Authentication authenticate(Authentication authentication) 
            throws AuthenticationException {
    	try {
    		String username = authentication.getName();
            String password = authentication.getCredentials().toString();
            Map<String, String> obj = new HashMap<String, String>();
            obj.put("userId", username);
            obj.put("password", password);
            List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
            User user = new User(password, password, false, false, false, false, grantedAuths);
            grantedAuths.add(new SimpleGrantedAuthority(user.getUsername()));
            return new CustomUsernamePasswordAuthenticationToken(username, user, grantedAuths, user);
    	}catch(Exception ex){
    		throw new BadCredentialsException(ex.getMessage());
    	}
    }
    
    /*@Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }*/
    
    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

	
}