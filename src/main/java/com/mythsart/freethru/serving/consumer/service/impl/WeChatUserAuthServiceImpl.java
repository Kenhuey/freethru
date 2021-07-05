package com.mythsart.freethru.serving.consumer.service.impl;

import com.mythsart.freethru.framework.common.exception.custom.NullParameterException;
import com.mythsart.freethru.framework.common.exception.custom.UserLoginFailException;
import com.mythsart.freethru.framework.common.exception.custom.UserNotExistException;
import com.mythsart.freethru.framework.common.response.data.WeChatUserInfoData;
import com.mythsart.freethru.framework.common.response.data.WeChatUserLoginData;
import com.mythsart.freethru.framework.common.util.jwt.JwtTokenUtil;
import com.mythsart.freethru.framework.common.util.wechat.WeChatUtil;
import com.mythsart.freethru.framework.common.util.wechat.data.WechatUserLoginRecvData;
import com.mythsart.freethru.serving.consumer.repository.WeChatUserRepository;
import com.mythsart.freethru.serving.consumer.repository.entity.WechatUserDo;
import com.mythsart.freethru.serving.consumer.service.WeChatUserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeChatUserAuthServiceImpl implements WeChatUserAuthService {

    private WeChatUtil weChatUtil;

    @Autowired
    private void setWeChatUtil(final WeChatUtil weChatUtil) {
        this.weChatUtil = weChatUtil;
    }

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private void setJwtTokenUtil(final JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    private WeChatUserRepository weChatUserRepository;

    @Autowired
    private void setWeChatUserRepository(WeChatUserRepository weChatUserRepository) {
        this.weChatUserRepository = weChatUserRepository;
    }

    @Override
    public WeChatUserLoginData login(final String wxCode) throws UserLoginFailException, NullParameterException {
        // get user info from wechat server
        final WechatUserLoginRecvData wechatUserLoginRecvData = this.weChatUtil.wechatUserLogin(wxCode);
        if (wechatUserLoginRecvData.getOpenId() == null) {
            throw new UserLoginFailException(String.format("WeChat user login fail. (wxCode = %s)", wxCode));
        }
        // find user in database, if not exist then call register
        final List<WechatUserDo> wechatUserDoList = this.weChatUserRepository.findByWeChatUserOpenId(wechatUserLoginRecvData.getOpenId());
        if (wechatUserDoList.isEmpty()) {
            return this.register(wechatUserLoginRecvData.getOpenId());
        }
        // make token
        final WeChatUserLoginData weChatUserLoginData = new WeChatUserLoginData();
        weChatUserLoginData.setToken(this.jwtTokenUtil.createConsumerAuthTokenByPayload(wechatUserDoList.get(0).getWeChatUserUuid()));
        return weChatUserLoginData;
    }

    @Override
    public WeChatUserLoginData register(final String wechatUserOpenId) throws NullParameterException {
        final WeChatUserLoginData weChatUserLoginData = new WeChatUserLoginData();
        // register
        final WechatUserDo wechatUserDo = new WechatUserDo();
        wechatUserDo.setWeChatUserOpenId(wechatUserOpenId);
        this.weChatUserRepository.save(wechatUserDo);
        // make token
        weChatUserLoginData.setToken(this.jwtTokenUtil.createConsumerAuthTokenByPayload(wechatUserDo.getWeChatUserUuid()));
        return weChatUserLoginData;
    }

    @Override
    public WeChatUserInfoData getWeChatUserInfoDataByWeChatUserUuid(String weChatUserUuid) throws UserNotExistException {
        final List<WechatUserDo> wechatUserDoList = this.weChatUserRepository.findByWeChatUserUuid(weChatUserUuid);
        if (wechatUserDoList.isEmpty()) {
            throw new UserNotExistException();
        }
        final WeChatUserInfoData weChatUserInfoData = new WeChatUserInfoData();
        weChatUserInfoData.setWeChatUserRegisterTime(wechatUserDoList.get(0).getWeChatUserRegisterTime());
        weChatUserInfoData.setWeChatUserUuid(wechatUserDoList.get(0).getWeChatUserUuid());
        return weChatUserInfoData;
    }

}
