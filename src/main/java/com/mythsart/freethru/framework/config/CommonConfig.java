package com.mythsart.freethru.framework.config;

import lombok.Data;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Public config
 **/

@Data
@Component
@ConfigurationProperties(prefix = "app")
@Configuration
public class CommonConfig {

    public final static String HEADER_TOKEN_NAME = "Authorization";

    private String wechatUserSessionRequestUrl = "https://api.weixin.qq.com/sns/jscode2session";

    private String wechatAppId;

    private String wechatAppSecret;

    private String wechatGrantType = "authorization_code";

    private String frontJwtTokenSecret;

    private int frontJwtTokenExpiresMinute;

    private String adminJwtTokenSecret;

    private int adminJwtTokenExpiresMinute;

    private String adminUserPassWordSalt;

    private String adminDefaultRegisterCode;

    protected BasicTextEncryptor basicTextEncryptor() {
        final BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(this.getAdminUserPassWordSalt());
        return basicTextEncryptor;
    }

    protected String encryptSHA256(final String data) {
        return new SimpleHash("SHA-256", data, this.getAdminUserPassWordSalt(), 512).toString();
    }

}
