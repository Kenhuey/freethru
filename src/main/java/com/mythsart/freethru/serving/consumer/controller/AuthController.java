package com.mythsart.freethru.serving.consumer.controller;

import com.mythsart.freethru.framework.config.CommonConfig;
import com.mythsart.freethru.framework.common.CommonController;
import com.mythsart.freethru.framework.common.exception.custom.NullParameterException;
import com.mythsart.freethru.framework.common.exception.custom.NullTokenException;
import com.mythsart.freethru.framework.common.exception.custom.UserLoginFailException;
import com.mythsart.freethru.framework.common.exception.custom.UserNotExistException;
import com.mythsart.freethru.framework.common.response.ResponseBuilder;
import com.mythsart.freethru.framework.common.response.ResponseResult;
import com.mythsart.freethru.framework.common.response.data.WeChatUserLoginData;
import com.mythsart.freethru.framework.common.response.data.WeChatUserTokenInfoData;
import com.mythsart.freethru.framework.common.util.jwt.JwtTokenUtil;
import com.mythsart.freethru.framework.jwt.annotation.WeChatUser;
import com.mythsart.freethru.framework.config.SwaggerConfig;
import com.mythsart.freethru.serving.consumer.service.WeChatUserAuthService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(SwaggerConfig.PATH_CONSUMER + "/auth")
@Api(tags = SwaggerConfig.TAG_NAME_CONSUMER_AUTH)
public class AuthController extends CommonController {

    private WeChatUserAuthService weChatUserAuthService;

    @Autowired
    private void setWeChatUserAuthService(final WeChatUserAuthService weChatUserAuthService) {
        this.weChatUserAuthService = weChatUserAuthService;
    }

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private void setJwtTokenUtil(final JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * WeChat user login
     **/
    @PostMapping("/wechat/login")
    public ResponseEntity<ResponseResult<WeChatUserLoginData>> login(
            @RequestParam(name = "wxCode") final String wxCode) throws UserLoginFailException, NullParameterException {
        return ResponseBuilder.makeOkResponse(weChatUserAuthService.login(wxCode));
    }

    /**
     * Get WeChat user and token info by token
     **/
    @GetMapping("/token/check")
    @WeChatUser
    public ResponseEntity<ResponseResult<WeChatUserTokenInfoData>> getTokenInfo(
            @RequestHeader(name = CommonConfig.HEADER_TOKEN_NAME, required = false) final String token) throws NullParameterException, UserNotExistException, NullTokenException {
        final String weChatUserUuid = this.jwtTokenUtil.getPayloadByFrontToken(token);
        final WeChatUserTokenInfoData weChatUserTokenInfoData = new WeChatUserTokenInfoData();
        weChatUserTokenInfoData.setWeChatUserInfoData(this.weChatUserAuthService.getWeChatUserInfoDataByWeChatUserUuid(weChatUserUuid));
        weChatUserTokenInfoData.setTokenExpireTime(this.jwtTokenUtil.getFrontTokenExpireTime(token));
        return ResponseBuilder.makeOkResponse(weChatUserTokenInfoData);
    }

}
