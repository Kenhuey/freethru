package com.mythsart.freethru.serving.administrator.service;

import com.mythsart.freethru.framework.common.exception.custom.NullTokenException;
import com.mythsart.freethru.framework.common.exception.custom.UserNotExistException;
import com.mythsart.freethru.serving.administrator.repository.entity.AdminUserDo;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthService {

    UserDetails getUserDetailsByUserName(final String userName);

    AdminUserDo registerNewDefaultUser(final String userName, final String passWord);

    boolean isPermission(final long permission) throws NullTokenException, UserNotExistException;

    boolean ignore();

}
