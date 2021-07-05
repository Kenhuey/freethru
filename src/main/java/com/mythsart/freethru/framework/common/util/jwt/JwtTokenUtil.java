package com.mythsart.freethru.framework.common.util.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mythsart.freethru.framework.config.CommonConfig;
import com.mythsart.freethru.framework.common.exception.custom.NullParameterException;
import com.mythsart.freethru.framework.common.exception.custom.NullTokenException;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
public class JwtTokenUtil extends CommonConfig {

    public String createAdminAuthTokenByPayload(final String payload) throws NullParameterException {
        if (payload == null || payload.replace(" ", "").equals("")) {
            throw new NullParameterException();
        }
        return JWT.create()
                .withAudience(payload)
                .withExpiresAt(new Date(System.currentTimeMillis() + this.getAdminJwtTokenExpiresMinute() * 60 * 1000))
                .sign(Algorithm.HMAC256(this.getAdminJwtTokenSecret()));
    }

    public String getPayloadByAdminToken(final String token) throws NullTokenException {
        if (token == null || token.replace(" ", "").equals("")) {
            throw new NullTokenException();
        }
        final JWTVerifier jwtVerifier = JWT.require(
                Algorithm.HMAC256(this.getAdminJwtTokenSecret())
        ).build();
        final DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getAudience().get(0);
    }

    public String getPayloadByAdminTokenNonException(final String token) {
        if (token == null || token.replace(" ", "").equals("")) {
            return null;
        }
        final JWTVerifier jwtVerifier = JWT.require(
                Algorithm.HMAC256(this.getAdminJwtTokenSecret())
        ).build();
        final DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getAudience().get(0);
    }

    public long getAdminTokenExpireTime(final String token) throws NullParameterException {
        if (token == null || token.replace(" ", "").equals("")) {
            throw new NullParameterException();
        }
        final JWTVerifier jwtVerifier = JWT.require(
                Algorithm.HMAC256(this.getAdminJwtTokenSecret())
        ).build();
        final DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getExpiresAt().getTime();
    }

    public String createConsumerAuthTokenByPayload(final String payload) throws NullParameterException {
        if (payload == null || payload.replace(" ", "").equals("")) {
            throw new NullParameterException();
        }
        return JWT.create()
                .withAudience(payload)
                .withExpiresAt(new Date(System.currentTimeMillis() + this.getFrontJwtTokenExpiresMinute() * 60 * 1000))
                .sign(Algorithm.HMAC256(this.getFrontJwtTokenSecret()));
    }

    public String getPayloadByFrontToken(final String token) throws NullTokenException {
        if (token == null || token.replace(" ", "").equals("")) {
            throw new NullTokenException();
        }
        final JWTVerifier jwtVerifier = JWT.require(
                Algorithm.HMAC256(this.getFrontJwtTokenSecret())
        ).build();
        final DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getAudience().get(0);
    }

    public long getFrontTokenExpireTime(final String token) throws NullParameterException {
        if (token == null || token.replace(" ", "").equals("")) {
            throw new NullParameterException();
        }
        final JWTVerifier jwtVerifier = JWT.require(
                Algorithm.HMAC256(this.getFrontJwtTokenSecret())
        ).build();
        final DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT.getExpiresAt().getTime();
    }

}
