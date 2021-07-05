package com.mythsart.freethru.framework.common.response.data;

import lombok.Data;

@Data
public class WeChatUserTokenInfoData {

    private WeChatUserInfoData weChatUserInfoData;

    private long tokenExpireTime;

}
