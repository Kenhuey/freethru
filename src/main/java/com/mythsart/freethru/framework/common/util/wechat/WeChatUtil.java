package com.mythsart.freethru.framework.common.util.wechat;

import com.alibaba.fastjson.JSONObject;
import com.mythsart.freethru.framework.config.CommonConfig;
import com.mythsart.freethru.framework.common.util.wechat.data.WechatUserLoginRecvData;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WeChatUtil extends CommonConfig {

    private String parseWechatUserLoginRequestUrl(final String wxCode) {
        final String requestUrl = this.getWechatUserSessionRequestUrl();
        final String requestParams = String.format(
                "?appid=%s&secret=%s&js_code=%s&grant_type=%s",
                this.getWechatAppId(),
                this.getWechatAppSecret(),
                wxCode,
                this.getWechatGrantType()
        );
        return requestUrl + requestParams;
    }

    private void jsonParseToWechatUserLoginRecvData(final String json, final WechatUserLoginRecvData wechatUserLoginRecvData) {
        final JSONObject userRecvData = JSONObject.parseObject(json);
        wechatUserLoginRecvData.setErrMsg(userRecvData.getString("errmsg"));
        wechatUserLoginRecvData.setErrCode(userRecvData.getString("errcode"));
        wechatUserLoginRecvData.setOpenId(userRecvData.getString("openid"));
        wechatUserLoginRecvData.setSessionKey(userRecvData.getString("session_key"));
        wechatUserLoginRecvData.setUnionId(userRecvData.getString("unionid"));
    }

    public WechatUserLoginRecvData wechatUserLogin(final String wxCode) {
        final WechatUserLoginRecvData wechatLoginData = new WechatUserLoginRecvData();
        final RestTemplate restTemplate = new RestTemplate();
        final String json = restTemplate.getForObject(this.parseWechatUserLoginRequestUrl(wxCode), String.class);
        this.jsonParseToWechatUserLoginRecvData(json, wechatLoginData);
        return wechatLoginData;
    }

}
