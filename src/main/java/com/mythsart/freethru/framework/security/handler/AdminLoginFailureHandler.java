package com.mythsart.freethru.framework.security.handler;

import com.alibaba.fastjson.JSONObject;
import com.mythsart.freethru.framework.common.response.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class AdminLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler implements AccessDeniedHandler {

    /**
     * Login failure handler
     **/
    @Override
    public void onAuthenticationFailure(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException exception) throws IOException {
        final Map<String, String> causeData = new HashMap<>();
        causeData.put("causeMessage", exception.getMessage());
        final ResponseResult<Object> responseResult = new ResponseResult<>()
                .setData(causeData)
                .setCode(HttpStatus.UNAUTHORIZED)
                .setMessage("Login failure.");
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        final PrintWriter printWriter = response.getWriter();
        printWriter.write(JSONObject.toJSONString(responseResult));
        printWriter.flush();
        printWriter.close();
    }

    @Override
    public void handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AccessDeniedException exception) throws IOException {
        final Map<String, String> causeData = new HashMap<>();
        causeData.put("causeMessage", exception.getMessage());
        final ResponseResult<Object> responseResult = new ResponseResult<>()
                .setData(causeData)
                .setCode(HttpStatus.FORBIDDEN)
                .setMessage("Access forbidden.");
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        final PrintWriter printWriter = response.getWriter();
        printWriter.write(JSONObject.toJSONString(responseResult));
        printWriter.flush();
        printWriter.close();
    }
}
