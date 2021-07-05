package com.mythsart.freethru.framework.common.util.wechat.data;

import lombok.Data;

@Data
public class WechatUserLoginRecvData {

    private String errMsg;

    private String errCode;

    private String openId;

    private String sessionKey;

    private String unionId;

}
