package com.mythsart.freethru.framework.security;

import com.mythsart.freethru.framework.config.CommonConfig;
import com.mythsart.freethru.serving.administrator.repository.entity.AdminUserDo;
import com.mythsart.freethru.serving.administrator.service.AuthService;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Configuration
public class LoginValidateAuthenticationProvider extends CommonConfig implements AuthenticationProvider {

    @Resource
    private AuthService authService;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String userName = authentication.getName();
        final String rawPassWord = (String) authentication.getCredentials();
        final AdminUserDo adminUserDo = (AdminUserDo) this.authService.getUserDetailsByUserName(userName);
        if (!adminUserDo.isEnabled()) {
            throw new DisabledException("This user has been disable.");
        } else if (!adminUserDo.isAccountNonLocked()) {
            throw new LockedException("This user has been lock.");
        } else if (!adminUserDo.isAccountNonExpired()) {
            throw new AccountExpiredException("This user has been expired.");

        } else if (!adminUserDo.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("This user session has been expired.");
        }
        // custom password verify
        if (!adminUserDo.getPassword().equals(this.encryptSHA256(rawPassWord))) {
            throw new BadCredentialsException("Invalid username or password.");
        }
        return new UsernamePasswordAuthenticationToken(adminUserDo, rawPassWord, adminUserDo.getAuthorities());
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
