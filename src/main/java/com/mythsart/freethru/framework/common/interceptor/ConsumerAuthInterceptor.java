package com.mythsart.freethru.framework.common.interceptor;

import com.mythsart.freethru.framework.config.CommonConfig;
import com.mythsart.freethru.framework.common.exception.custom.NullTokenException;
import com.mythsart.freethru.framework.common.exception.custom.UserNotExistException;
import com.mythsart.freethru.framework.common.util.jwt.JwtTokenUtil;
import com.mythsart.freethru.framework.jwt.annotation.WeChatUser;
import com.mythsart.freethru.serving.consumer.repository.WeChatUserRepository;
import com.mythsart.freethru.serving.consumer.repository.entity.WechatUserDo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

public class ConsumerAuthInterceptor implements HandlerInterceptor {

    private WeChatUserRepository weChatUserRepository;

    @Autowired
    private void setWeChatUserRepository(final WeChatUserRepository weChatUserRepository) {
        this.weChatUserRepository = weChatUserRepository;
    }

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private void setJwtTokenUtil(final JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public boolean preHandle(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse, final Object object)
            throws NullTokenException, UserNotExistException {
        // check annotation
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        final HandlerMethod handlerMethod = (HandlerMethod) object;
        final Method method = handlerMethod.getMethod();
        // request
        if (method.isAnnotationPresent(WeChatUser.class)) {
            final WeChatUser weChatUser = method.getAnnotation(WeChatUser.class);
            if (!weChatUser.required()) {
                return true;
            }
            // get token
            final String token = httpServletRequest.getHeader(CommonConfig.HEADER_TOKEN_NAME);
            if (token == null || token.replace(" ", "").equals("")) {
                throw new NullTokenException("Null token.");
            }
            // verify user exist in database
            final String weChatUserUuid = this.jwtTokenUtil.getPayloadByFrontToken(token);
            final List<WechatUserDo> wechatUserDoList = this.weChatUserRepository.findByWeChatUserUuid(weChatUserUuid);
            if (wechatUserDoList.isEmpty()) {
                throw new UserNotExistException();
            }
        }
        return true;
    }

}
