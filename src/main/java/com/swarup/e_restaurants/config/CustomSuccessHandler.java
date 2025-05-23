package com.swarup.e_restaurants.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        String redirectUrl = null;

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority : authorities) {
            System.out.println(grantedAuthority);
            if (grantedAuthority.getAuthority().equals("ADMIN")) {
                redirectUrl = "/admin";
                break;
            } else if (grantedAuthority.getAuthority().equals("RESTAURENT")) {
                redirectUrl = "/restrurent";
                break;
            } else if (grantedAuthority.getAuthority().equals("BRANCH")) {
                redirectUrl = "/branch";
                break;
            }
            else if (grantedAuthority.getAuthority().equals("DBOY")) {
                redirectUrl = "/dboy";
                break;
            }
            else if (grantedAuthority.getAuthority().equals("USER")) {
                redirectUrl = "/home";
                break;
            } else {
                redirectUrl = "/403";
                break;
            }
        }
        if (redirectUrl == null) {
            redirectUrl = "/error"; 
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }

}
