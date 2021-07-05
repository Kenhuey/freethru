package com.mythsart.freethru.framework.security;

import com.mythsart.freethru.framework.security.handler.AdminLoginFailureHandler;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Resource
    private AdminLoginFailureHandler adminLoginFailureHandler;
    @Override
    public void commence(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            AuthenticationException exception) throws IOException {
        // send
        this.adminLoginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, exception);
    }

}
