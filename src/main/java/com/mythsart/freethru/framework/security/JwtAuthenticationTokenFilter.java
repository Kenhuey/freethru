package com.mythsart.freethru.framework.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.mythsart.freethru.framework.common.util.jwt.JwtTokenUtil;
import com.mythsart.freethru.framework.config.CommonConfig;
import com.mythsart.freethru.serving.administrator.repository.AdminUserRepository;
import com.mythsart.freethru.serving.administrator.repository.entity.AdminUserDo;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Resource
    private AdminUserRepository adminUserRepository;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest httpServletRequest,
            final HttpServletResponse httpServletResponse,
            final FilterChain filterChain) throws ServletException, IOException {
        // get token
        final String token = httpServletRequest.getHeader(CommonConfig.HEADER_TOKEN_NAME);
        String userUuid;
        try {
            userUuid = jwtTokenUtil.getPayloadByAdminTokenNonException(token);
        } catch (final TokenExpiredException | JWTDecodeException exception) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        // if token success
        final List<AdminUserDo> adminUserDoList = this.adminUserRepository.findByUserUuid(userUuid);
        if (userUuid != null && !adminUserDoList.isEmpty()) {
            final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    adminUserDoList.get(0), null, adminUserDoList.get(0).getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            String messageFlag = null;
            // check
            if (!adminUserDoList.get(0).isEnabled()) {
                messageFlag = "This user has been disable.";
            } else if (!adminUserDoList.get(0).isAccountNonLocked()) {
                messageFlag = "This user has been lock.";
            } else if (!adminUserDoList.get(0).isAccountNonExpired()) {
                messageFlag = "This user has been expired.";
            } else if (!adminUserDoList.get(0).isCredentialsNonExpired()) {
                messageFlag = "This user session has been expired.";
            }
            if (messageFlag != null) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            // set user login status
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
