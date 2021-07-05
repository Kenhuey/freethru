package com.mythsart.freethru.framework.config;

import com.mythsart.freethru.framework.security.JwtAuthenticationEntryPoint;
import com.mythsart.freethru.framework.security.JwtAuthenticationTokenFilter;
import com.mythsart.freethru.framework.security.LoginValidateAuthenticationProvider;
import com.mythsart.freethru.framework.security.handler.AdminLoginFailureHandler;
import com.mythsart.freethru.framework.security.handler.AdminLoginSuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    /**
     * custom authentication bean
     **/
    @Resource
    private LoginValidateAuthenticationProvider loginValidateAuthenticationProvider;

    /**
     * custom login success handler
     **/
    @Resource
    private AdminLoginSuccessHandler loginSuccessHandler;

    /**
     * custom login failure handler
     **/
    @Resource
    private AdminLoginFailureHandler loginFailureHandler;

    /**
     * jwt verify entry point
     **/
    @Resource
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * custom authentication
     **/
    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(this.loginValidateAuthenticationProvider);
        if (auth.isConfigured()) {
            this.logger.info("Added authentication provider.");
        } else {
            this.logger.error("Authentication provider empty.");
        }
    }

    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() {
        return new JwtAuthenticationTokenFilter();
    }

    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private void setAccessDeniedHandler(final AccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }

    /**
     * security config
     **/
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // configs
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(SwaggerConfig.PATH_CONSUMER + "/**").permitAll()
                .antMatchers(SwaggerConfig.PATH_COMMON + "/**").permitAll()
                .antMatchers("/swagger-resources" + "/**").permitAll()
                .antMatchers("/v2" + "/*").permitAll()
                .antMatchers("/csrf").permitAll()
                .antMatchers("/webjars" + "/**").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers(SwaggerConfig.PATH_ADMIN + "/auth/register").permitAll()
                .antMatchers(SwaggerConfig.PATH_ADMIN + "/auth/login").permitAll()
                .antMatchers(SwaggerConfig.PATH_ADMIN + "/merchandise/public" + "/**").permitAll()
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(this.accessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .formLogin()
                .loginProcessingUrl(SwaggerConfig.PATH_ADMIN + "/auth/login")
                .successHandler(this.loginSuccessHandler)
                .failureHandler(this.loginFailureHandler)
                .permitAll();
        // add jwt filter
        http.addFilterBefore(this.authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
        // header and cors
        http.headers().cacheControl();
        http.csrf().disable();
    }

}
