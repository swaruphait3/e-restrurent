package com.swarup.e_restaurants.config;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;



@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = null;
        for (GrantedAuthority grantedAuthority : authorities) {
            System.out.println(grantedAuthority);
            if (grantedAuthority.getAuthority().equals("ADMIN")) {
                System.out.println("*******************************");
                redirectUrl = "/admin";
                break;
            } else if (grantedAuthority.getAuthority().equals("SHOP")) {
                redirectUrl = "/shop";
                break;
            } else {
                redirectUrl = "/403";
                break;
            }
        }
        if (redirectUrl == null) {
            throw new IllegalStateException();
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
    
}
