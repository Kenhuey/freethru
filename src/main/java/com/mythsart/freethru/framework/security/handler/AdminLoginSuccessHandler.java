package com.mythsart.freethru.framework.security.handler;

import com.alibaba.fastjson.JSONObject;
import com.mythsart.freethru.framework.common.response.ResponseBuilder;
import com.mythsart.freethru.framework.common.response.ResponseResult;
import com.mythsart.freethru.framework.common.util.jwt.JwtTokenUtil;
import com.mythsart.freethru.framework.config.CommonConfig;
import com.mythsart.freethru.serving.administrator.repository.AdminUserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class AdminLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private void setJwtTokenUtil(final JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    private AdminUserRepository adminUserRepository;

    @Autowired
    private void setAdminUserRepository(final AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    /**
     * Login success handler
     **/
    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication authentication) {
        final Map<String, String> tokenData = new HashMap<>();
        final String token = jwtTokenUtil.createAdminAuthTokenByPayload(adminUserRepository.findByUserName(authentication.getName()).get(0).getUserUuid());
        tokenData.put("token", token);
        final ResponseResult<Object> responseResult = new ResponseResult<>()
                .setData(tokenData)
                .setCode(HttpStatus.OK)
                .setMessage("Login success.");
        response.addHeader(CommonConfig.HEADER_TOKEN_NAME, token);
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.OK.value());
        final PrintWriter printWriter = response.getWriter();
        printWriter.write(JSONObject.toJSONString(responseResult));
        printWriter.flush();
        printWriter.close();
    }

}
