package com.mythsart.freethru.serving.consumer.service;

import com.mythsart.freethru.framework.common.exception.custom.NullParameterException;
import com.mythsart.freethru.framework.common.exception.custom.UserLoginFailException;
import com.mythsart.freethru.framework.common.exception.custom.UserNotExistException;
import com.mythsart.freethru.framework.common.response.data.WeChatUserInfoData;
import com.mythsart.freethru.framework.common.response.data.WeChatUserLoginData;

public interface WeChatUserAuthService {

    WeChatUserLoginData login(final String wxCode) throws UserLoginFailException, NullParameterException;

    WeChatUserLoginData register(final String wechatUserOpenId) throws NullParameterException;

    WeChatUserInfoData getWeChatUserInfoDataByWeChatUserUuid(final String weChatUserUuid) throws UserNotExistException;

}
