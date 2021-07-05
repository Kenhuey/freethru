package com.mythsart.freethru.serving.administrator.service.impl;

import com.mythsart.freethru.framework.common.exception.custom.NullTokenException;
import com.mythsart.freethru.framework.common.exception.custom.UserNotExistException;
import com.mythsart.freethru.framework.common.util.jwt.JwtTokenUtil;
import com.mythsart.freethru.framework.config.CommonConfig;
import com.mythsart.freethru.serving.administrator.repository.AdminUserRepository;
import com.mythsart.freethru.serving.administrator.repository.entity.AdminUserDo;
import com.mythsart.freethru.serving.administrator.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Service("authService")
@Configuration
public class AuthServiceImpl extends CommonConfig implements AuthService {

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    private AdminUserRepository adminUserRepository;

    @Autowired
    private void setAdminUserRepository(final AdminUserRepository adminUserRepository) {
        this.adminUserRepository = adminUserRepository;
    }

    @Override
    public UserDetails getUserDetailsByUserName(final String userName) {
        List<AdminUserDo> adminUserDoList = this.adminUserRepository.findByUserName(userName);
        if (adminUserDoList.isEmpty()) {
            throw new UsernameNotFoundException(new UserNotExistException().getMessage() + "(user=" + userName + ")");
        }
        return adminUserDoList.get(0);
    }

    @Override
    public AdminUserDo registerNewDefaultUser(final String userName, final String passWord) {
        final AdminUserDo adminUserDo = new AdminUserDo();
        adminUserDo.setUserActive(true);
        adminUserDo.setUserName(userName);
        adminUserDo.setUserPassWord(this.encryptSHA256(passWord));
        this.adminUserRepository.save(adminUserDo);
        return adminUserDo;
    }

    @Override
    public boolean isPermission(final long permission) throws NullTokenException, UserNotExistException {
        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        final String token = request.getHeader(CommonConfig.HEADER_TOKEN_NAME);
        final String userUuid = this.jwtTokenUtil.getPayloadByAdminToken(token);
        final List<AdminUserDo> adminUserDoList = this.adminUserRepository.findByUserUuid(userUuid);
        if (adminUserDoList.isEmpty()) {
            throw new UserNotExistException();
        }
        final AdminUserDo adminUserDo = adminUserDoList.get(0);
        final long userMostPermission = adminUserDo.getPermission();
        return permission <= userMostPermission;
    }

    @Override
    public boolean ignore() {
        return true;
    }

}
